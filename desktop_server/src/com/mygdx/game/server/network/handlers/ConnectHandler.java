package com.mygdx.game.server.network.handlers;

import com.mygdx.game.core.network.messages.PlayerJoinedRoomMessage;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;
import io.vertx.core.buffer.Buffer;

import javax.inject.Inject;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

public class ConnectHandler {

  private final GameRoom room;

  @Inject
  public ConnectHandler(
      GameRoom room
  ) {
    this.room = room;
  }

  public void handle(String[] commands, Client client) {
    var userName = commands[1];
    var userToken = commands[2];
    client.setPlayerUsername(userName);
    client.setPlayerToken(userToken);
    room.getClients().forEach(ws -> {
      var msg = new PlayerJoinedRoomMessage(room.getNumberOfClients());
      var buffer = Buffer.buffer(json.toJson(msg, (Class<?>) null));
      ws.getSocket().write(buffer);
    });
  }
}
