package com.mygdx.game.client_core.di;

import com.mygdx.game.client_core.model.ServerConnectionConfig;
import com.mygdx.game.client_core.network.MessageSender;
import com.mygdx.game.client_core.network.NetworkJobRegisterHandler;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.client_core.network.ServerConnection;
import dagger.Module;
import dagger.Provides;
import lombok.extern.java.Log;

import javax.inject.Singleton;
import java.util.Map;

@Module
@Log
public class NetworkModule {

  private static final String HOST = "127.0.0.1"; // todo load this from file
  private static final int PORT = 10666;

  @Provides
  @Singleton
  public MessageSender providesMessageSender(
      ServerConnection serverConnection,
      NetworkJobRegisterHandler messageListener
  ) {
    var config = new ServerConnectionConfig(HOST, PORT);
    serverConnection.connect(config);
    serverConnection.setPersistentListener(messageListener);
    log.info("provided message sender: " + serverConnection);
    return serverConnection;
  }

  @Provides
  @Singleton // todo remove handlers from gameinstance that aren't usable anymore but still exist
  public QueueMessageListener providesWebSocketHandler(
      Map<Class<?>, QueueMessageListener.Handler> handlerMap
  ) {
    var listener = new QueueMessageListener();
    for (var entry : handlerMap.entrySet()) {
      listener.registerHandler(entry.getKey(), entry.getValue());
    }
    return listener;
  }
}
