package com.mygdx.game.client_core.model;

import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

@Log
@Singleton
public class NetworkJobsQueueJobJobberManager {

  private final Set<WebSocketListener> webSocketListenerSet;
  private final ConcurrentLinkedQueue<OnMessageArgs> dataToBeHandled = new ConcurrentLinkedQueue<>();

  @Inject
  NetworkJobsQueueJobJobberManager(
      Set<WebSocketListener> webSocketListenerSet
      //ComponentMessageListener componentMessageListener, // todo use multibindings to remove the dependency
      /* in docs:
      * subComponent can add elements to multibound sets or maps that are bound in its parent.
      * When that happens, the set or map is different depending on where it is injected.
      * When it is injected into a binding defined on the subcomponent, then it has the values
      * or entries defined by the subcomponent’s multibindings as well as those
      * defined by the parent component’s multibindings. When it is injected into
      * a binding defined on the parent component, it has only the values or entries defined there.
      * */

      // this means that the "outer" part of the game won't have to think about the GameInstance world at all, which is good
      // because they will be exchanged
      // QueueMessageListener queueMessageListener
  ) {
    this.webSocketListenerSet = webSocketListenerSet;
  }

  public void add(WebSocket webSocket, byte[] data) {
    log.info("data added");
    dataToBeHandled.add(new OnMessageArgs(webSocket, data));
  }

  public void doAllJobs() {
    var datum = dataToBeHandled.poll();
    while (datum != null) {
      log.info("data tried to be handled content: " + datum);
      var handled = false;
      for (var listener : webSocketListenerSet) {
        if (listener.onMessage(datum.socket(), datum.data())) {
          handled = true;
        }
      }
      if (!handled) {
        throw new RuntimeException("data " + datum + "can't be handled");
      }
      datum = dataToBeHandled.poll();
    }
  }

  public record OnMessageArgs(WebSocket socket, byte[] data) {

    @Override
    public String toString() {
      return new String(data);
    }
  }
}
