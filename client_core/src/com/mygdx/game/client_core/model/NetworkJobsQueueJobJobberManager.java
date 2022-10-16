package com.mygdx.game.client_core.model;

import com.github.czyzby.websocket.WebSocketListener;
import com.mygdx.game.client_core.network.NetworkJobRegisterListener;
import lombok.extern.java.Log;

import java.util.Set;

@Log
public class NetworkJobsQueueJobJobberManager {

  private final NetworkJobRegisterListener queueContainer;
  private final Set<WebSocketListener> webSocketListenerSet; // quickfix would be to have a set keyed by class to have
  // the websocket listenres be replaced but it's horrible

  public NetworkJobsQueueJobJobberManager(
      NetworkJobRegisterListener queueContainer,
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
    this.queueContainer = queueContainer;
    this.webSocketListenerSet = webSocketListenerSet;
  }

  public void doAllJobs() {
    var datum = queueContainer.poll();
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
      datum = queueContainer.poll();
    }
  }


}
