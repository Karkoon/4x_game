package com.mygdx.game.client_core.model;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.QueueMessageListener;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ConcurrentLinkedQueue;

@Log
@Singleton
public class NetworkJobsQueueJobJobberManager {

  private final ComponentMessageListener componentMessageListener;
  private final QueueMessageListener queueMessageListener;
  private final ConcurrentLinkedQueue<OnMessageArgs> dataToBeHandled = new ConcurrentLinkedQueue<>();

  @Inject
  NetworkJobsQueueJobJobberManager(
      ComponentMessageListener componentMessageListener, // todo use multibindings to remove the dependency
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
      QueueMessageListener queueMessageListener
  ) {
    this.componentMessageListener = componentMessageListener;
    this.queueMessageListener = queueMessageListener;
  }

  public void add(WebSocket webSocket, byte[] data) {
    log.info("data added");
    dataToBeHandled.add(new OnMessageArgs(webSocket, data));
  }

  public void doAllJobs() {
    var datum = dataToBeHandled.poll();
    while (datum != null) {
      log.info("data tried to be handled content: " + datum);
      if (!queueMessageListener.onMessage(datum.socket(), datum.data())) {
        if (!componentMessageListener.onMessage(datum.socket(), datum.data())) {
          throw new RuntimeException("data " + datum + "can't be handled");
        }
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
