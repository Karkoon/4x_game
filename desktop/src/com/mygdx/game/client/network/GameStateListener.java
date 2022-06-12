package com.mygdx.game.client.network;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.websocket.AbstractWebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.mygdx.game.client.model.GameState;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.network.ComponentMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
/* this class will only be used once, for getting the initial game map, because it's a list and
* a bulk data change, it could probably be done using the normal componentmessagelistener but who has the time
*  */
public class GameStateListener extends AbstractWebSocketListener {

  /**
   * Used as default value when invoking {@link ObjectMap#get(Object, Object)} on {@link #handlers} to prevent
   * NPE.
   */
  private static final Handler<Component> unknown = (webSocket, packet) -> NOT_HANDLED;
  private final ObjectMap<Class<?>, Handler<Component>> handlers = new ObjectMap<>();

  @Inject
  public GameStateListener(
      ComponentMapper<Name> nameMapper,
      NetworkEntityManager networkEntityManager
  ) {
    registerHandler(GameState.class, (webSocket, packet) -> {
      /* this */
      var entity = networkEntityManager.getWorldEntity(packet.getEntityId());
      var name = nameMapper.get(entity);
      System.out.println("lets see " + name);
      return true;
    });
  }

  /**
   * @param packetClass class of the packet that should be passed to the selected handler.
   * @param handler     will be notified when the chosen type of packet is received. Should be prepared to handle the
   *                    specific packet class, otherwise {@link ClassCastException} might be thrown.
   */
  @SuppressWarnings("unchecked")
  public void registerHandler(final Class<?> packetClass, final Handler<? extends Component> handler) {
    handlers.put(packetClass, (Handler<Component>) handler);
  }

  @Override
  protected boolean onMessage(final WebSocket webSocket, final Object packet) throws WebSocketException {
    try {
      if (!(packet instanceof JsonValue jsonValue && jsonValue.isArray())) {
        return false;
      }

      var message = (ComponentMessage) packet;
      return handlers.get(GameState.class, unknown).handle(webSocket, message);
    } catch (final Exception exception) {
      return onError(webSocket,
          new WebSocketException("Unable to handle the received packet: " + packet, exception));
    }
  }

  @Override
  public boolean onOpen(WebSocket webSocket) {
    log.info("openeded" + webSocket.isOpen());
    webSocket.send("whatattaatta");
    return super.onOpen(webSocket);
  }

  @Override
  public boolean onError(WebSocket webSocket, Throwable error) {
    log.info("on close" + webSocket.isOpen() + " " + error.getMessage());
    return super.onError(webSocket, error);
  }

  @Override
  public boolean onClose(WebSocket webSocket, int closecCode, String reason) {
    log.info("on close" + webSocket.isOpen() + " " + reason);
    return super.onClose(webSocket, closecCode, reason);
  }

  /**
   * Common interface for handlers that consume a specific type of components.
   *
   * @param <C> type of handled components.
   * @author MJ, Kacper Jankowski (lol)
   */
  public interface Handler<C extends Component> {
    /**
     * Should perform the logic using the received packet.
     *
     * @param webSocket this socket received the packet.
     * @param packet    the deserialized packet instance.
     * @return true if message was fully handled and other web socket listeners should not be notified.
     * @see WebSocketListener#FULLY_HANDLED
     * @see WebSocketListener#NOT_HANDLED
     */
    boolean handle(WebSocket webSocket, ComponentMessage<C> packet);
  }
}
