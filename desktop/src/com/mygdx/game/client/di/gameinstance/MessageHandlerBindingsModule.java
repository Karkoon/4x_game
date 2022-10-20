package com.mygdx.game.client.di.gameinstance;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceNetworkModule;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.QueueMessageListener.Handler;
import com.mygdx.game.client_core.network.message_handlers.ChangeTurnMessageHandler;
import com.mygdx.game.client.network.message_handlers.MaterialIncomeMessageHandler;
import com.mygdx.game.client_core.network.message_handlers.RemoveEntityMessageHandler;
import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import com.mygdx.game.core.network.messages.MaterialIncomeMessage;
import com.mygdx.game.core.network.messages.RemoveEntityMessage;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

import javax.inject.Named;

@Module
public interface MessageHandlerBindingsModule {

  @Binds
  @IntoMap
  @ClassKey(ChangeTurnMessage.class)
  @GameInstanceScope

  @Named(GameInstanceNetworkModule.GAME_INSTANCE)
  Handler changeTurnMessageHandler(ChangeTurnMessageHandler handler);

  @Binds
  @IntoMap
  @ClassKey(RemoveEntityMessage.class)
  @GameInstanceScope
  @Named(GameInstanceNetworkModule.GAME_INSTANCE)
  Handler removeEntityMessageHandler(RemoveEntityMessageHandler handler);

  @Binds
  @IntoMap
  @ClassKey(MaterialIncomeMessage.class)
  @GameInstanceScope
  @Named(GameInstanceNetworkModule.GAME_INSTANCE)
  Handler materialIncomeMessageHandler(MaterialIncomeMessageHandler handler);
}
