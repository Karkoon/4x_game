package com.mygdx.game.bot;

import com.badlogic.gdx.Game;
import com.github.czyzby.websocket.WebSocketHandler;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.bot.di.bot.BotClient;
import com.mygdx.game.client_core.di.CoreNames;
import com.mygdx.game.client_core.model.ActiveToken;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.client_core.network.service.GameConnectService;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@Singleton
public class GdxGame extends Game {

  private final GameConfigAssets assets;
  private final GameConnectService gameConnectService;
  private final BotClient botClient;
  private final QueueMessageListener handler;
  private final ActiveToken activeToken;
  private final Scheduler main;

  @Inject
  GdxGame(
      @NonNull GameConfigAssets assets,
      @NonNull GameConnectService gameConnectService,
      @NonNull BotClient botClient,
      @NonNull QueueMessageListener listener,
      @NonNull ActiveToken activeToken,
      @NonNull @Named(CoreNames.MAIN_THREAD) Scheduler main
  ) {
    this.assets = assets;
    this.gameConnectService = gameConnectService;
    this.botClient = botClient;
    this.handler = listener;
    this.activeToken = activeToken;
    this.main = main;
  }

  @Override
  public void create() {
    log.info("Loading assets...");
    assets.loadAssetsSync();
    log.info("Assets loaded.");

    log.info("Waiting for game start.");

    handler.registerHandler(GameStartedMessage.class, ((webSocket, o) -> {
      Completable.fromAction(() -> {
            log.info("Starting bot.");
            var message = (GameStartedMessage) o;
            activeToken.setActiveToken(message.getPlayerToken());
            botClient.run();
          })
          .observeOn(Schedulers.io())
          .subscribeOn(main)
          .subscribe();
      return FULLY_HANDLED;
    }));

    gameConnectService.connect();
    main.start();
  }
}
