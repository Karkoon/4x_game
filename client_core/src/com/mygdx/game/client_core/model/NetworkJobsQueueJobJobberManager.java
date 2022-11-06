package com.mygdx.game.client_core.model;

import com.github.czyzby.websocket.WebSocketListener;
import com.mygdx.game.client_core.network.AddJobToQueueListener;
import lombok.extern.java.Log;

import java.util.Set;

@Log
public class NetworkJobsQueueJobJobberManager {

  private final AddJobToQueueListener queueContainer;
  private final Set<WebSocketListener> webSocketListenerSet; // quickfix would be to have a set keyed by class to have
  // the websocket listenres be replaced but it's horrible

  public NetworkJobsQueueJobJobberManager(
      AddJobToQueueListener queueContainer,
      Set<WebSocketListener> webSocketListenerSet
  ) {
    this.queueContainer = queueContainer;
    this.webSocketListenerSet = webSocketListenerSet;
  }

  public boolean doAllJobs() {
    var datum = queueContainer.peek();
    var anyHandled = false;
    while (datum != null) {
      log.info("data tried to be handled content: " + hashCode() + " " + datum);
      var handled = false;
      for (var listener : webSocketListenerSet) {
        if (listener.onMessage(datum.socket(), datum.data())) {
          handled = true;
          anyHandled = true;
        }
      }
      if (!handled) {
        log.info("can't handle, stopping execution " + datum);
        break;
      }
      queueContainer.remove();
      datum = queueContainer.peek();
    }
    return anyHandled;
  }
}
