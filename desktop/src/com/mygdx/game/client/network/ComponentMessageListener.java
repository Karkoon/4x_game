package com.mygdx.game.client.network;

import com.artemis.Component;
import com.artemis.World;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.websocket.AbstractWebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.Position;
import com.mygdx.game.client.ecs.entityfactory.FieldFactory;
import com.mygdx.game.client.ecs.entityfactory.UnitFactory;
import com.mygdx.game.client.model.GameState;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.network.ComponentMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class ComponentMessageListener extends AbstractWebSocketListener {

  private static final Handler NO_HANDLER = (webSocket, worldEntity, packet) -> {
    throw new RuntimeException("Every ComponentMessage needs to be handled: id=" + worldEntity
        + " content=" + packet);
  };
  private final ObjectMap<Class<? extends Component>, Handler> handlers = new ObjectMap<>();
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  @Inject
  public ComponentMessageListener(
      NetworkWorldEntityMapper networkWorldEntityMapper,
      World world,
      GameScreenAssets assets,
      FieldFactory fieldFactory,
      UnitFactory unitFactory,
      GameState gameState
  ) {
    this.networkWorldEntityMapper = networkWorldEntityMapper;
    final var positionMapper = world.getMapper(Position.class);
    final var coordinatesMapper = world.getMapper(Coordinates.class);

    registerHandler(EntityConfigId.class, (webSocket, worldEntity, packet) -> { // todo zmienić znowu na serwisy i fabryki,
      // todo bo to jednak nie jest komponent tylko pojedyncza wiadomość xD
      // albo coś
      var entityConfigId = ((EntityConfigId) packet).getId();
      if (entityConfigId >= 1  && entityConfigId <= GameConfigs.FIELD_AMOUNT) { // 1, 2 są z plików jsona EntityConfigów
        log.info("field id " + worldEntity);
        var config = assets.getGameConfigs().get(FieldConfig.class, entityConfigId);
        fieldFactory.createEntity(config, worldEntity);
        return FULLY_HANDLED;
      } else if (entityConfigId > GameConfigs.FIELD_AMOUNT && entityConfigId <= GameConfigs.UNIT_AMOUNT + GameConfigs.FIELD_AMOUNT) {
        log.info("unit id " + worldEntity);
        var config = assets.getGameConfigs().get(UnitConfig.class, entityConfigId);
        unitFactory.createEntity(config, worldEntity);
        return FULLY_HANDLED;
      }
      return NOT_HANDLED;
    });

    registerHandler(Position.class, (webSocket, worldEntity, component) -> { // Position should be changed to CoordinatesComponent
      log.info("Read position component " + worldEntity);
      var newPosition = (Position) component;
      var entityPosition = positionMapper.create(worldEntity);
      entityPosition.setPosition(newPosition.getPosition());
      return FULLY_HANDLED;
    });


    registerHandler(Coordinates.class, (webSocket, worldEntity, component) -> {
      log.info("Read coordinates component " + worldEntity);
      var newCoordinates = (Coordinates) component;
      var entityCoordinates = coordinatesMapper.create(worldEntity);
      gameState.removeEntity(worldEntity);
      entityCoordinates.setCoordinates(newCoordinates);
      gameState.saveEntity(worldEntity);
      return FULLY_HANDLED;
    });
  }

  /**
   * @param packetClass class of the packet that should be passed to the selected handler.
   * @param handler     will be notified when the chosen type of packet is received. Should be prepared to handle the
   *                    specific packet class, otherwise {@link ClassCastException} might be thrown.
   */
  public void registerHandler(final Class<? extends Component> packetClass, final Handler handler) {
    handlers.put(packetClass, handler);
  }

  @Override
  protected boolean onMessage(final WebSocket webSocket, final Object packet) throws WebSocketException {
    try {
      if (!(packet instanceof ComponentMessage<? extends Component> message)) {
        return NOT_HANDLED;
      }
      var worldEntityId = networkWorldEntityMapper.getWorldEntity(message.getEntityId());
      var component = message.getComponent();
      var handler = handlers.get(message.getComponent().getClass(), NO_HANDLER);
      var result = handler.handle(webSocket, worldEntityId, component);
      if (result != FULLY_HANDLED) {
        throw new RuntimeException("Every ComponentMessage needs to be handled: id=" + worldEntityId
            + " content=" + packet);
      }
      return FULLY_HANDLED;
    } catch (final Exception exception) {
      return onError(webSocket,
          new WebSocketException("Unable to handle the received packet: " + packet, exception));
    }
  }

  /**
   * Common interface for handlers that consume a specific type of components.
   *
   * @author MJ, Kacper Jankowski (lol)
   */
  public interface Handler {
    /**
     * Should perform the logic using the received packet.
     *
     * @param webSocket this socket received the packet.
     * @return true if message was fully handled and other web socket listeners should not be notified.
     * @see WebSocketListener#FULLY_HANDLED
     * @see WebSocketListener#NOT_HANDLED
     */
    boolean handle(WebSocket webSocket, int worldEntity, Component component);
  }
}
