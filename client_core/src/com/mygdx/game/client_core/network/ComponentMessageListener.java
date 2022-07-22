package com.mygdx.game.client_core.network;

import com.artemis.Component;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;
import com.github.czyzby.websocket.AbstractWebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.mygdx.game.core.network.messages.ComponentMessage;
import lombok.extern.java.Log;

@Log
public class ComponentMessageListener extends AbstractWebSocketListener {

  private final ObjectMap<Class<? extends Component>, Queue<Handler>> handlers = new ObjectMap<>();
  private final NetworkWorldEntityMapper networkWorldEntityMapper;

  public ComponentMessageListener(
      NetworkWorldEntityMapper networkWorldEntityMapper
  ) {
    this.networkWorldEntityMapper = networkWorldEntityMapper;
  }

  /**
   * @param packetClass class of the packet that should be passed to the selected handler.
   * @param handler     will be notified when the chosen type of packet is received. Should be prepared to handle the
   *                    specific packet class, otherwise {@link ClassCastException} might be thrown.
   */
  public void registerHandler(final Class<? extends Component> packetClass, final Handler handler) {
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
      if (!(packet instanceof ComponentMessage<? extends Component> message)) {
        return NOT_HANDLED;
      }
      var worldEntityId = networkWorldEntityMapper.getWorldEntity(message.getEntityId());
      var component = message.getComponent();
      var queue = handlers.get(message.getComponent().getClass());
      for (var handler : queue) {
        var result = handler.handle(webSocket, worldEntityId, component);
        if (result != FULLY_HANDLED) {
          throw new RuntimeException("Every ComponentMessage needs to be handled: id=" + worldEntityId
              + " content=" + packet);
        }
      }
      return FULLY_HANDLED;
    } catch (final Exception exception) {
      return onError(webSocket,
          new WebSocketException("Unable to handle the received packet: " + packet, exception));
    }
  }

  /**
   * Common interface for handlers that consume a specific type of components.
   *
   * @author MJ, Kacper Jankowski (lol)
   */
  public interface Handler {
    /**
     * Should perform the logic using the received packet.
     *
     * @param webSocket this socket received the packet.
     * @return true if message was fully handled and other web socket listeners should not be notified.
     * @see WebSocketListener#FULLY_HANDLED
     * @see WebSocketListener#NOT_HANDLED
     */
    boolean handle(WebSocket webSocket, int worldEntity, Component component);
  }
}
