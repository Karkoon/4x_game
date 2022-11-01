package com.mygdx.game.bot.di;

import com.github.czyzby.websocket.WebSocketListener;
import com.mygdx.game.client_core.network.QueueMessageListener;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

import javax.inject.Singleton;

@Module
public interface WebSocketListenerBindingsModule {

  @Binds
  @IntoSet
  @Singleton
  WebSocketListener queueMessageListener(QueueMessageListener queueMessageListener);
}
