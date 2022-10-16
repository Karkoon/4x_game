package com.mygdx.game.client_core.di.gameinstance;

import com.github.czyzby.websocket.WebSocketListener;
import com.mygdx.game.client_core.model.NetworkJobsQueueJobJobberManager;
import com.mygdx.game.client_core.network.NetworkJobRegisterListener;
import com.mygdx.game.client_core.network.QueueMessageListener;
import dagger.Module;
import dagger.Provides;
import lombok.extern.java.Log;

import javax.inject.Named;
import java.util.Map;
import java.util.Set;

@Module
@Log
public class GameInstanceNetworkModule {

  public static final String GAME_INSTANCE = "game_instance";

  @Provides
  @GameInstanceScope
  @Named(GAME_INSTANCE)
  public QueueMessageListener providesWebSocketHandler(
      @Named(GameInstanceNetworkModule.GAME_INSTANCE) Map<Class<?>, QueueMessageListener.Handler> handlerMap
  ) {
    var listener = new QueueMessageListener();
    log.info("debugTestText " + handlerMap.entrySet().size());
    for (var entry : handlerMap.entrySet()) {
      listener.registerHandler(entry.getKey(), entry.getValue());
    }
    return listener;
  }

  @Provides
  @GameInstanceScope
  @Named(GAME_INSTANCE)
  public NetworkJobsQueueJobJobberManager providesNetworkJobsQueueJobJobberManager(
      NetworkJobRegisterListener listener,
      @Named(GameInstanceNetworkModule.GAME_INSTANCE) Set<WebSocketListener> listenerSet
  ) {
    var obj = new NetworkJobsQueueJobJobberManager(listener, listenerSet);
    log.info("debugTestText2 " + listenerSet.size() + " " + listener.hashCode());
    return obj;
  }
}
