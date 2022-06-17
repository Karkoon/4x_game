package com.mygdx.game.server.network;

import com.artemis.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.game.core.network.ComponentMessage;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class ComponentSyncer {
  private final ClientManager clientManager;
  private final Json json = new Json(JsonWriter.OutputType.minimal);

  @Inject
  public ComponentSyncer(ClientManager clientManager) {
    this.clientManager = clientManager;
  }

  public void sendComponent(Component component, int entityId) {
    clientManager.getClients().forEach(websocket -> sendComponentToWebsocket(component, entityId, websocket));
  }

  public void sendComponentTo(Component component, int entityId, int clientId) {
    var websocket = clientManager.getClient(clientId);
    sendComponentToWebsocket(component, entityId, websocket);
  }

  private void sendComponentToWebsocket(Component component, int entityId, ServerWebSocket webSocket) {
    log.info("Sending component " + component + " to " + entityId);
    var message = new ComponentMessage<>(component, entityId);
    var jsonString = json.toJson(message, (Class<?>) null); // required to save type information inside json
    webSocket.write(Buffer.buffer(jsonString));
  }

}
