package com.mygdx.game.server.network.gameinstance;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
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

  private synchronized void beginTransaction(Client client) {
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
    send(transaction.getMessageBuffer(), client);
    transaction.clear();
  }

  public synchronized void sendObjectTo(@NonNull Object component, Client client) {
    if (!transactionMap.containsKey(client)) {
      beginTransaction(client);
      log.info("Sending component " + component + " to " + client);
    }
    saveToBuffer(component, client);
  }

  private void saveToBuffer(Object component, Client client) {
    transactionMap.get(client).addToBuffer(component);
  }

  private Buffer bufferize(Object object) {
    var jsonString = json.toJson(object, (Class<?>) null); // required to save type information inside json
    return Buffer.buffer(jsonString);
  }

  private void send(Object message, Client client) {
    client.getSocket().write(bufferize(message));
  }

  public void flush() {
    for (var client : transactionMap.keys()) {
      endTransaction(client);
    }
    transactionMap.clear();
  }
}
