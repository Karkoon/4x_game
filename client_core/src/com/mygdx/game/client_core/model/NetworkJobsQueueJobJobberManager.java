package com.mygdx.game.client_core.model;

import com.badlogic.gdx.utils.Array;
import com.github.czyzby.websocket.serialization.Serializer;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.OnMessageListener;
import com.mygdx.game.client_core.network.AddJobToQueueListener;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Log
@GameInstanceScope
public class NetworkJobsQueueJobJobberManager {

  private final AddJobToQueueListener queueContainer;
  private final Set<OnMessageListener> onMessageListeners;
  private final Serializer serializer;

  public NetworkJobsQueueJobJobberManager(
      AddJobToQueueListener queueContainer,
      Set<OnMessageListener> onMessageListeners,
      Serializer serializer
  ) {
    this.queueContainer = queueContainer;
    this.onMessageListeners = onMessageListeners;
    this.serializer = serializer;
  }

  private List<Object> unpack(final Object object) {
    var messageList = new ArrayList<>();
    if (object instanceof Array<?> array) {
      for (var i = 0; i < array.size; i++) {
        messageList.add(array.get(i));
      }
    } else {
      messageList.add(object);
    }
    return messageList;
  }

  public boolean doAllJobs() {
    var datum = queueContainer.peek();
    var anyHandled = false;
    while (datum != null) {
      log.info("data tried to be handled content: " + hashCode() + " " + datum);
      var handled = false;
      var dataArray = unpack(serializer.deserialize(datum.data()));
      for (var listener : onMessageListeners) {
        for (int i = 0; i < dataArray.size(); i++) {
          if (listener.onMessage(datum.socket(), dataArray.get(i))) {
            log.info("handling with " + listener.getClass().getName());
            handled = true;
            anyHandled = true;
          }
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
