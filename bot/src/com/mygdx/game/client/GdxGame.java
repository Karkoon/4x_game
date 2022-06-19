package com.mygdx.game.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.mygdx.game.client.bot.BotClient;
import com.mygdx.game.client.network.GameStartService;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@Singleton
public class GdxGame extends Game {

  private final AllAssets assets;
  private final WebSocket webSocket;
  private final GameStartService gameStartService;
  private final BotClient botClient;
  private final WebSocketHandler handler;

  @Inject
  GdxGame(
      @NonNull AllAssets assets,
      @NonNull WebSocket webSocket,
      @NonNull GameStartService gameStartService,
      @NonNull BotClient botClient,
      @NonNull WebSocketHandler handler
      ) {
    this.assets = assets;
    this.webSocket = webSocket;
    this.gameStartService = gameStartService;
    this.botClient = botClient;
    this.handler = handler;
  }

  @Override
  public void create() {
    log.info("Loading assets...");
    assets.loadAssetsSync();
    log.info("Assets loaded.");

    log.info("Waitinig for game start.");

    handler.registerHandler(GameStartedMessage.class, ((webSocket, o) -> {
      log.info("Starting bot.");
      botClient.run();
      return FULLY_HANDLED;
    }));

    webSocket.send("connect");
  }

  @Override
  public void render() {
    ScreenUtils.clear(0f, 0f, 0f, 1, true);
    super.render();
  }
}
