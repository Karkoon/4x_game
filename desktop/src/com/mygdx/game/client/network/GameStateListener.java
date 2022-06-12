package com.mygdx.game.client.network;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.czyzby.websocket.AbstractWebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.data.WebSocketException;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.entityfactory.FieldFactory;
import com.mygdx.game.client.model.GameState;
import com.mygdx.game.config.FieldConfig;
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
  private final GameState gameState;
  private final GameScreenAssets assets;
  private final Json json = new Json();

  @Inject
  public GameStateListener(
      NetworkEntityManager networkEntityManager,
      FieldFactory fieldFactory,
      GameState gameState,
      GameScreenAssets assets // it's suspicious how a network listener has access to factories and assets
  ) {
    this.networkEntityManager = networkEntityManager;
    this.fieldFactory = fieldFactory;
    this.gameState = gameState;
    this.assets = assets;
  }

  @Override
  protected boolean onMessage(final WebSocket webSocket, final Object packet) throws WebSocketException {
    try {
      if (!(packet instanceof Array<?> values)) {
        return NOT_HANDLED;
      }

      var message = ((Array<Array<Object>>) values);
      var fields = new HashMap<Coordinates, Integer>();
      for (int i = 0; i < message.size; i++) {
        var fieldConfig = assets.getGameConfigs().getAny(FieldConfig.class);
        var coordinate = json.fromJson(Coordinates.class, ((JsonValue) message.get(i).get(0)).toJson(JsonWriter.OutputType.json));
        var networkEntity = ((Float) message.get(i).get(1)).intValue();
        var worldEntity = fieldFactory.createEntity(fieldConfig, coordinate);
        networkEntityManager.putEntity(networkEntity, worldEntity);
        fields.put(coordinate, worldEntity);
      }
      gameState.setFields(fields);

      System.out.println("lets see " + message);

      return true;
    } catch (final Exception exception) {
      return onError(webSocket,
          new WebSocketException("Unable to handle the received packet: " + packet, exception));
    }
  }
}
