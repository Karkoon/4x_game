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
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Log
@Singleton
public class GameRoomSyncer {
  private final GameRoom room;
  private final Json json = new Json(JsonWriter.OutputType.minimal);

  @Inject
  public GameRoomSyncer(GameRoom room) {
    this.room = room;
  }

  private final List<ComponentMessage<?>> messageBuffer = new ArrayList<>();
  private boolean transaction = false;

  public synchronized void beginTransaction() {
    if (!messageBuffer.isEmpty() || transaction) {
      throw new IllegalStateException("Previous transaction wasn't commited");
    }
    transaction = true;
  }

  public synchronized void endTransactionSingleClient(Client client) {
    if (messageBuffer.isEmpty() || !transaction) {
      throw new IllegalStateException("No messages to send");
    }
    log.info("sending " + messageBuffer + " to " + client.getPlayerUsername());
    sendSavingClassInJson(messageBuffer, client);
    transaction = false;
    messageBuffer.clear();
  }

  public synchronized void endTransaction() {
    if (messageBuffer.isEmpty() || !transaction) {
      throw new IllegalStateException("No messages to send");
    }
    room.getClients().forEach(client -> {
      log.info("sending " + messageBuffer.size() + " of " + messageBuffer + " to " + client.getPlayerUsername());
      sendSavingClassInJson(messageBuffer, client);
    });
    transaction = false;
    messageBuffer.clear();
  }

  public synchronized void sendComponent(Component component, int entityId) {
    if (!transaction) {
      room.getClients().forEach(client -> sendComponentTo(component, entityId, client));
    } else {
      saveToBuffer(component, entityId);
    }
  }

  public synchronized void sendComponentTo(Component component, int entityId, Client client) {
    if (!transaction) {
      log.info("Sending component " + component + " to " + client);
      var message = new ComponentMessage<>(component, entityId);
      sendSavingClassInJson(message, client);
    } else {
      saveToBuffer(component, entityId);
    }
  }

  private void saveToBuffer(Component component, int entityId) {
    var message = new ComponentMessage<>(component, entityId);
    messageBuffer.add(message);
  }

  private void sendSavingClassInJson(Object message, Client client) {
    var jsonString = json.toJson(message, (Class<?>) null); // required to save type information inside json
    client.getSocket().write(Buffer.buffer(jsonString));
  }
}
