package com.mygdx.game.client.network;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.websocket.AbstractWebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Position;
import com.mygdx.game.core.network.ComponentMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class ComponentMessageListener extends AbstractWebSocketListener {

  /**
   * Used as default value when invoking {@link ObjectMap#get(Object, Object)} on {@link #handlers} to prevent
   * NPE.
   */
  private static final Handler<Component> unknown = (webSocket, packet) -> NOT_HANDLED;
  private final ObjectMap<Class<?>, Handler<Component>> handlers = new ObjectMap<>();
  private final Json json = new Json();
  private final ComponentMapper<Position> positionMapper;

  @Inject
  public ComponentMessageListener(
      NetworkEntityManager networkEntityManager,
      World world
  ) {
    this.positionMapper = world.getMapper(Position.class);

    registerHandler(Name.class, (webSocket, packet) -> {
      var entity = networkEntityManager.getWorldEntity(packet.getEntityId());
      if (entity == -1) {
        throw new RuntimeException("entity needs to be created locally before use");
      }
      return true;
    });
    registerHandler(Position.class, (webSocket, packet) -> {
      var entity = networkEntityManager.getWorldEntity(packet.getEntityId());
      if (entity == -1) {
        throw new RuntimeException("entity needs to be created locally before use");
      }
      log.info("Read position component");
      var newPosition = (Position) packet.getComponent();
      var unitPosition = positionMapper.get(entity);
      unitPosition.setPosition(newPosition.getPosition());
      return true;
    });
  }

  /**
   * @param packetClass class of the packet that should be passed to the selected handler.
   * @param handler     will be notified when the chosen type of packet is received. Should be prepared to handle the
   *                    specific packet class, otherwise {@link ClassCastException} might be thrown.
   */
  @SuppressWarnings("unchecked")
  public void registerHandler(final Class<?> packetClass, final Handler<? extends Component> handler) {
    handlers.put(packetClass, (Handler<Component>) handler);
  }

  @Override
  protected boolean onMessage(final WebSocket webSocket, final Object packet) throws WebSocketException {
    try {
      if (!(packet instanceof JsonValue jsonValue) || jsonValue.isArray()) {
        return NOT_HANDLED;
      }


      var message = json.fromJson(ComponentMessage.class, jsonValue.asString());
      return handlers.get(message.getComponent().getClass(), unknown).handle(webSocket, message);
    } catch (final Exception exception) {
      return onError(webSocket,
          new WebSocketException("Unable to handle the received packet: " + packet, exception));
    }
  }

  /**
   * Common interface for handlers that consume a specific type of components.
   *
   * @param <C> type of handled components.
   * @author MJ, Kacper Jankowski (lol)
   */
  public interface Handler<C extends Component> {
    /**
     * Should perform the logic using the received packet.
     *
     * @param webSocket this socket received the packet.
     * @param packet    the deserialized packet instance.
     * @return true if message was fully handled and other web socket listeners should not be notified.
     * @see WebSocketListener#FULLY_HANDLED
     * @see WebSocketListener#NOT_HANDLED
     */
    boolean handle(WebSocket webSocket, ComponentMessage<C> packet);
  }
}
