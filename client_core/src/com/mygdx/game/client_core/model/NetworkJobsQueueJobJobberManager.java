package com.mygdx.game.client_core.model;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.QueueMessageListener;
import lombok.SneakyThrows;
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
      ComponentMessageListener componentMessageListener,
      QueueMessageListener queueMessageListener
  ) {
    this.componentMessageListener = componentMessageListener;
    this.queueMessageListener = queueMessageListener;
  }

  @SneakyThrows
  public void add(WebSocket webSocket, byte[] data) {
    log.info("data added");
    dataToBeHandled.add(new OnMessageArgs(webSocket, data));
  }

  @SneakyThrows
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
