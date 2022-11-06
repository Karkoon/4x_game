package com.mygdx.game.bot.di.gameinstance;

import com.github.czyzby.websocket.WebSocketListener;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceNetworkModule;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.QueueMessageListener;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import dagger.multibindings.Multibinds;

import javax.inject.Named;
import java.util.Set;

@Module
public interface GameInstanceWebSocketListenerBindingsModule {

  @Multibinds
  @Named(GameInstanceNetworkModule.GAME_INSTANCE)
  Set<WebSocketListener> webListenerSet();

  @Binds
  @IntoSet
  @GameInstanceScope
  @Named(GameInstanceNetworkModule.GAME_INSTANCE)
  WebSocketListener componentMessageListener(ComponentMessageListener listener);

  @Binds
  @IntoSet
  @GameInstanceScope
  @Named(GameInstanceNetworkModule.GAME_INSTANCE)
  WebSocketListener queueMessageListener(
      @Named(GameInstanceNetworkModule.GAME_INSTANCE) QueueMessageListener queueMessageListener
  );
}
