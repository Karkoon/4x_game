package com.mygdx.game.server.network;

import com.badlogic.gdx.utils.Json;
import com.mygdx.game.server.model.Client;
import dagger.Reusable;
import io.vertx.core.buffer.Buffer;

import javax.inject.Inject;
import java.util.Collection;

@Reusable
public class MessageSender {

  private final Json json = new Json();

  @Inject
  MessageSender() {
    super();
  }

  public void send(Object message, Client client) {
    var buffer = Buffer.buffer(json.toJson(message, (Class<?>) null));
    client.getSocket().write(buffer);
  }

  public void sendToAll(Object message, Collection<Client> clients) {
    var buffer = Buffer.buffer(json.toJson(message, (Class<?>) null));
    for (var client : clients) {
      client.getSocket().write(buffer);
    }
  }
}
