package com.mygdx.game.client_core.network;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;
import com.github.czyzby.websocket.AbstractWebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import lombok.extern.java.Log;

@Log
public class QueueMessageListener extends AbstractWebSocketListener {

  private final ObjectMap<Class<?>, Queue<Handler>> handlers = new ObjectMap<>();

  /**
   * @param packetClass class of the packet that should be passed to the selected handler.
   * @param handler     will be notified when the chosen type of packet is received. Should be prepared to handle the
   *                    specific packet class, otherwise {@link ClassCastException} might be thrown.
   */
  public <T> void registerHandler(final Class<T> packetClass, final Handler<T> handler) {
    var queue = handlers.get(packetClass);
    if (queue == null) {
      queue = new Queue<>();
      handlers.put(packetClass, queue);
    }
    queue.addLast(handler);
  }

  @Override
  protected boolean onMessage(final WebSocket webSocket, final Object packet) throws WebSocketException {
    try {
      return routeMessageToHandler(webSocket, packet);
    } catch (final Exception exception) {
      return onError(webSocket,
          new WebSocketException("Unable to handle the received packet: " + packet, exception));
    }
  }

  private boolean routeMessageToHandler(WebSocket webSocket, Object message) {
    var queue = handlers.get(message.getClass());
    if (queue == null) {
      log.info("queue is null for: " + message.getClass());
      return NOT_HANDLED;
    }
    for (var handler : queue) {
      var result = handler.handle(webSocket, message);
      if (result != FULLY_HANDLED) {
        throw new RuntimeException("Every Message needs to be handled; content=" + message);
      }
    }
    return FULLY_HANDLED;
  }

  /**
   * Common interface for handlers that consume a specific type of components.
   *
   * @author MJ, Kacper Jankowski (lol)
   */
  public interface Handler<T> {
    /**
     * Should perform the logic using the received packet.
     *
     * @param webSocket this socket received the packet.
     * @return true if message was fully handled and other web socket listeners should not be notified.
     * @see WebSocketListener#FULLY_HANDLED
     * @see WebSocketListener#NOT_HANDLED
     */
    boolean handle(WebSocket webSocket, T message);
  }
}
