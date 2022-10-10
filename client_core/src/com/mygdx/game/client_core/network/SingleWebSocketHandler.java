package com.mygdx.game.client_core.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.websocket.AbstractWebSocketListener;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.data.WebSocketException;

/**
 * @author MJ
 */
public class SingleWebSocketHandler extends AbstractWebSocketListener {
  private final ObjectMap<Class<?>, Handler> handlers = new ObjectMap<>();

  public <T> Disposable registerHandler(
      final Class<T> packetClass,
      final Handler<T> handler
  ) {
    Disposable disposable = () -> dispose(packetClass);
    handlers.put(packetClass, handler);
    return disposable;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean onMessage(final WebSocket webSocket, final Object packet) throws WebSocketException {
    var handled = NOT_HANDLED;
    try {
      handled = handlers.get(packet.getClass(), quitter).handle(webSocket, packet);
    } catch (Exception any) {
      any.printStackTrace();
      Gdx.app.postRunnable(Gdx.app::exit);
    }
    if (handled) {
      dispose(packet.getClass());
    }
    return handled;
  }

  private void dispose(Class<?> aClass) {
    handlers.remove(aClass);
  }

  public interface Handler<P> {
    boolean handle(WebSocket webSocket, P p);
  }

  private final Handler<?> quitter = (webSocket, o) -> {
    throw new RuntimeException("Can't handle " + o.getClass());
  };
}
