package com.mygdx.game.server.network;

import com.artemis.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.game.core.network.messages.ComponentMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;
import io.vertx.core.buffer.Buffer;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class GameRoomSyncer {
  private final GameRoom room;
  private final Json json = new Json(JsonWriter.OutputType.minimal);

  @Inject
  public GameRoomSyncer(GameRoom room) {
    this.room = room;
  }

  public void sendComponent(Component component, int entityId) {
    room.getClients().forEach(client -> {
      log.info("sending " + component.hashCode() + " to " + client.getId());
      sendComponentTo(component, entityId, client);
    });
  }

  public void sendComponentTo(Component component, int entityId, Client client) {
    log.info("Sending component " + component + " to " + entityId);
    var message = new ComponentMessage<>(component, entityId);
    var jsonString = json.toJson(message, (Class<?>) null); // required to save type information inside json
    client.getSocket().write(Buffer.buffer(jsonString));
  }

}
