package com.mygdx.game.client.di.gameinstance;

import com.github.czyzby.websocket.WebSocketListener;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
public interface GameInstanceWebSocketListenerBindingsModule {

  @Binds
  @IntoSet
  @GameInstanceScope
  WebSocketListener componentMessageListener(ComponentMessageListener listener);
}
