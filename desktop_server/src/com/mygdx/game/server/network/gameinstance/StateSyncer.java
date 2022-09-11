package com.mygdx.game.server.network.gameinstance;

import com.artemis.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.core.network.messages.ComponentMessage;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.Transaction;
import io.vertx.core.buffer.Buffer;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class StateSyncer {
  private final Json json = new Json(JsonWriter.OutputType.minimal);
  private final ObjectMap<Client, Transaction> transactionMap = new ObjectMap<>();

  @Inject
  public StateSyncer() {
    super();
  }

  public synchronized void beginTransaction(Client client) {
    var transaction = transactionMap.get(client);
    if (transaction == null) {
      transactionMap.put(client, new Transaction());
    }
  }

  private synchronized void endTransaction(Client client) {
    var transaction = transactionMap.get(client, null);
    if (!transaction.isPending()) {
      return;
    }
    var buffer = transaction.getMessageBuffer();
    log.info("sending " + buffer.size + " " + buffer + "\nto " + client.getPlayerUsername());
    sendSavingClassInJson(transaction.getMessageBuffer(), client);
    transaction.clear();
  }

  public synchronized void sendComponentTo(@NonNull Component component, int entityId, Client client) {
    if (!transactionMap.containsKey(client)) {
      log.info("Sending component " + component + " to " + client);
      var message = new ComponentMessage<>(component, entityId);
      sendSavingClassInJson(message, client);
    } else {
      saveToBuffer(component, entityId, client);
    }
  }

  private void saveToBuffer(Component component, int entityId, Client client) {
    var message = new ComponentMessage<>(component, entityId);
    transactionMap.get(client).addToBuffer(message);
  }

  private void sendSavingClassInJson(Object message, Client client) {
    var jsonString = json.toJson(message, (Class<?>) null); // required to save type information inside json
    client.getSocket().write(Buffer.buffer(jsonString));
  }

  public void flush() {
    for (var client : transactionMap.keys()) {
      endTransaction(client);
    }
    transactionMap.clear();
  }
}
