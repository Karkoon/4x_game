package com.mygdx.game.server.network;

import com.artemis.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.core.network.messages.ComponentMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;
import io.vertx.core.buffer.Buffer;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class GameRoomSyncer {
  private final Json json = new Json(JsonWriter.OutputType.minimal);
  private final ObjectMap<GameRoom, Transaction> transactionMap = new ObjectMap<>();

  @Inject
  public GameRoomSyncer() {
    super();
  }

  public synchronized void beginTransaction(GameRoom room) {
    var transaction = transactionMap.get(room);
    if (transaction == null) {
      transactionMap.put(room, new Transaction());
    } else {
      if (transaction.isPending()) {
        throw new IllegalStateException("Previous transaction wasn't ended");
      }
    }
  }

  public synchronized void endTransactionSingleClient(Client client) {
    var room = client.getGameRoom();
    var transaction = transactionMap.remove(room);
    if (!transaction.isPending()) {
      throw new IllegalStateException("Transaction not pending");
    }
    log.info("sending " + transaction + " to " + client.getPlayerUsername());
    sendSavingClassInJson(transaction, client);
  }

  public synchronized void endTransaction(GameRoom room) {
    var transaction = transactionMap.remove(room);
    if (!transaction.isPending()) {
      throw new IllegalStateException("Transaction not pending");
    }
    var messageBuffer = transaction.getMessageBuffer();
    room.getClients().forEach(client -> {
      log.info("sending " + messageBuffer + " of " + messageBuffer + " to " + client.getPlayerUsername());
      sendSavingClassInJson(messageBuffer, client);
    });
  }

  public synchronized void sendComponent(Component component, int entityId, GameRoom room) {
    if (!transactionMap.containsKey(room)) {
      room.getClients().forEach(client -> sendComponentTo(component, entityId, client));
    } else {
      saveToBuffer(component, entityId, room);
    }
  }

  public synchronized void sendComponentTo(Component component, int entityId, Client client) {
    if (!transactionMap.containsKey(client.getGameRoom())) {
      log.info("Sending component " + component + " to " + client);
      var message = new ComponentMessage<>(component, entityId);
      sendSavingClassInJson(message, client);
    } else {
      saveToBuffer(component, entityId, client.getGameRoom());
    }
  }

  private void saveToBuffer(Component component, int entityId, GameRoom room) {
    var message = new ComponentMessage<>(component, entityId);
    transactionMap.get(room).addToBuffer(message);
  }

  private void sendSavingClassInJson(Object message, Client client) {
    var jsonString = json.toJson(message, (Class<?>) null); // required to save type information inside json
    client.getSocket().write(Buffer.buffer(jsonString));
  }
}
