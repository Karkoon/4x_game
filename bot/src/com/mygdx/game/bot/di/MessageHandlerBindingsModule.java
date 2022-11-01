package com.mygdx.game.bot.di;

import com.mygdx.game.client_core.network.QueueMessageListener;
import dagger.Module;
import dagger.multibindings.Multibinds;

import java.util.Map;

@Module
public abstract class MessageHandlerBindingsModule {

  @Multibinds
  public abstract Map<Class<?>, QueueMessageListener.Handler> handlers();

}
