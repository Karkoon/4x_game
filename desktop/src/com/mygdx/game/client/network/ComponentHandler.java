package com.mygdx.game.client.network;

import com.artemis.Component;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.websocket.AbstractWebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.mygdx.game.core.ecs.component.Name;

import javax.inject.Inject;

public class ComponentHandler extends AbstractWebSocketListener {

  @Inject
  public ComponentHandler() {
    registerHandler(Name.class, (webSocket, packet) -> false);
  }

  private final ObjectMap<Class<?>, Handler<Component>> handlers = new ObjectMap<>();


  /**
   * Used as default value when invoking {@link ObjectMap#get(Object, Object)} on {@link #handlers} to prevent
   * NPE.
   */
  private final Handler<Component> unknown = (webSocket, packet) -> {
    onError(webSocket, new WebSocketException("Unknown packet type: " + packet.toString()));
    return NOT_HANDLED;
  };

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
      var message = (ComponentMessage) packet;
      return handlers.get(message.getComponent().getClass(), unknown).handle(webSocket, message);
    } catch (final Exception exception) {
      return onError(webSocket,
          new WebSocketException("Unable to handle the received packet: " + packet, exception));
    }
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
