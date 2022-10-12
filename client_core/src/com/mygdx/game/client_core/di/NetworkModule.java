package com.mygdx.game.client_core.di;

import com.github.czyzby.websocket.CommonWebSockets;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSockets;
import com.mygdx.game.client_core.model.ServerConnectionConfig;
import com.mygdx.game.client_core.network.NetworkJobRegisterHandler;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.client_core.network.ServerConnection;
import com.mygdx.game.client_core.network.message_handlers.ChangeTurnMessageHandler;
import com.mygdx.game.client_core.network.message_handlers.RemoveEntityMessageHandler;
import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import com.mygdx.game.core.network.messages.RemoveEntityMessage;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Singleton;

@Module
@Log
public class NetworkModule {

  private static final String HOST = "127.0.0.1"; // todo put configs in a runtime-provided object
  private static final int PORT = 10666;

  @Provides
  @Singleton
  public WebSocket providesWebsocket(
      ServerConnectionConfig config,
      ServerConnection serverConnection, // todo move it to something that can restore connections
      NetworkJobRegisterHandler messageListener
  ) {
    serverConnection.connect(config);
    serverConnection.setPersistentListener(messageListener);
    log.info("provided socket: " + socket);
    return ;
  }

  @Provides
  @Singleton
  public QueueMessageListener providesWebSocketHandler(
      @NonNull ChangeTurnMessageHandler changeTurnMessageHandler,
      @NonNull RemoveEntityMessageHandler removeEntityMessageHandler
  ) {
    var listener = new QueueMessageListener();
    listener.registerHandler(ChangeTurnMessage.class, changeTurnMessageHandler);
    listener.registerHandler(RemoveEntityMessage.class, removeEntityMessageHandler);
    return listener;
  }
}
