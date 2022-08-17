package com.mygdx.game.bot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.czyzby.websocket.WebSocketHandler;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.bot.di.bot.BotClient;
import com.mygdx.game.client_core.network.GameConnectService;
import com.mygdx.game.client_core.network.PlayerInfo;
import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@Singleton
public class GdxGame extends Game {

  private final GameConfigAssets assets;
  private final GameConnectService gameConnectService;
  private final BotClient botClient;
  private final WebSocketHandler handler;
  private final PlayerInfo playerInfo;

  @Inject
  GdxGame(
      @NonNull GameConfigAssets assets,
      @NonNull GameConnectService gameConnectService,
      @NonNull BotClient botClient,
      @NonNull WebSocketHandler handler,
      @NonNull PlayerInfo playerInfo
  ) {
    this.assets = assets;
    this.gameConnectService = gameConnectService;
    this.botClient = botClient;
    this.handler = handler;
    this.playerInfo = playerInfo;
  }

  @Override
  public void create() {
    log.info("Loading assets...");
    assets.loadAssetsSync();
    log.info("Assets loaded.");

    log.info("Waiting for game start.");

    handler.setFailIfNoHandler(false);
    handler.registerHandler(GameStartedMessage.class, ((webSocket, o) -> {
      log.info("Starting bot.");
      var message = (GameStartedMessage) o;
      if (message.getPlayerToken().equals(playerInfo.getToken()))
        playerInfo.activatePlayer();
      botClient.run();
      return FULLY_HANDLED;
    }));

    handler.registerHandler(ChangeTurnMessage.class, ((webSocket, o) -> {
      var message = (ChangeTurnMessage) o;
      var playerToken = message.getPlayerToken();
      log.info("Receiver token = " + playerToken);

      if (playerInfo.getToken().equals(playerToken)) {
        playerInfo.activatePlayer();
      } else {
        playerInfo.deactivatePlayer();
      }

      return FULLY_HANDLED;
    }));


    gameConnectService.connect();
  }

  @Override
  public void render() {
    ScreenUtils.clear(0f, 0f, 0f, 1, true);
    super.render();
  }
}
