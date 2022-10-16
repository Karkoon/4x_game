package com.mygdx.game.client.di.gameinstance;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.QueueMessageListener.Handler;
import com.mygdx.game.client_core.network.message_handlers.ChangeTurnMessageHandler;
import com.mygdx.game.client_core.network.message_handlers.GameStartedMessageHandler;
import com.mygdx.game.client_core.network.message_handlers.RemoveEntityMessageHandler;
import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import com.mygdx.game.core.network.messages.RemoveEntityMessage;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public interface MessageHandlerBindingsModule {

  @Binds
  @IntoMap
  @ClassKey(ChangeTurnMessage.class)
  @GameInstanceScope
  Handler<ChangeTurnMessage> changeTurnMessageHandler(ChangeTurnMessageHandler handler);

  @Binds
  @IntoMap
  @ClassKey(RemoveEntityMessage.class)
  @GameInstanceScope
  Handler<RemoveEntityMessage> removeEntityMessageHandler(RemoveEntityMessageHandler handler);

  @Binds
  @IntoMap
  @ClassKey(GameStartedMessage.class)
  @GameInstanceScope
  Handler<GameStartedMessage> gameStartedMessageHandler(GameStartedMessageHandler handler);

}
