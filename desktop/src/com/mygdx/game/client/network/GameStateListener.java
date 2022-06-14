package com.mygdx.game.client.network;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.czyzby.websocket.AbstractWebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.data.WebSocketException;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.entityfactory.FieldFactory;
import com.mygdx.game.client.ecs.entityfactory.UnitFactory;
import com.mygdx.game.client.model.GameState;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Slot;
import com.mygdx.game.core.model.Coordinates;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.HashMap;

@Log
/* this class will only be used once, for getting the initial game map, because it's a list and
 * a bulk data change, it could probably be done using the normal componentmessagelistener but who has the time
 *  */
public class GameStateListener extends AbstractWebSocketListener {

  private final NetworkEntityManager networkEntityManager;
  private final FieldFactory fieldFactory;
  private final UnitFactory unitFactory;
  private final GameState gameState;
  private final GameScreenAssets assets;
  private final Json json = new Json();
  private final ComponentMapper<Slot> slotMapper;


  @Inject
  public GameStateListener(
      NetworkEntityManager networkEntityManager,
      FieldFactory fieldFactory,
      UnitFactory unitFactory,
      GameState gameState,
      GameScreenAssets assets,// it's suspicious how a network listener has access to factories and assets
      World world
  ) {
    this.networkEntityManager = networkEntityManager;
    this.fieldFactory = fieldFactory;
    this.unitFactory = unitFactory;
    this.gameState = gameState;
    this.assets = assets;
    this.slotMapper = world.getMapper(Slot.class);
  }

  @Override
  protected boolean onMessage(final WebSocket webSocket, final Object packet) throws WebSocketException {
    try {
      if (!(packet instanceof Array<?> values)) {
        return NOT_HANDLED;
      }

      var message = ((Array<Array<Object>>) values);
      var fields = new HashMap<Coordinates, Integer>();

      String initType = (String) message.get(0).get(0);

      if (initType.equals("map")) {
        log.info("Receiving map");
        for (int i = 1; i < message.size; i++) {
          var fieldConfig = assets.getGameConfigs().getAny(FieldConfig.class);
          var coordinate = json.fromJson(Coordinates.class, ((JsonValue) message.get(i).get(0)).toJson(JsonWriter.OutputType.json));
          var networkEntity = ((Float) message.get(i).get(1)).intValue();
          var worldEntity = fieldFactory.createEntity(fieldConfig, coordinate);
          networkEntityManager.putEntity(networkEntity, worldEntity);
          fields.put(coordinate, worldEntity);
        }
        gameState.setFields(fields);
      } else if (initType.equals("unit")) {
        log.info("Receiving units");
        for (int i = 1; i < message.size; i++) {
          var unitConfig = assets.getGameConfigs().getAny(UnitConfig.class);
          var coordinate = json.fromJson(Coordinates.class, ((JsonValue) message.get(i).get(0)).toJson(JsonWriter.OutputType.json));
          var networkEntity = ((Float) message.get(i).get(1)).intValue();
          var worldEntity = unitFactory.createEntity(unitConfig, coordinate);
          networkEntityManager.putEntity(networkEntity, worldEntity);
          var field = gameState.getFields().get(coordinate);
          var slot = slotMapper.get(field);
          slot.getEntities().add(worldEntity);
        }
      }

      log.info("lets see " + message);

      return true;
    } catch (final Exception exception) {
      return onError(webSocket,
          new WebSocketException("Unable to handle the received packet: " + packet, exception));
    }
  }
}
