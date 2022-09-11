package com.mygdx.game.server.network.gameinstance;

import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameInstance;
import com.mygdx.game.server.network.gameinstance.handlers.EndTurnHandler;
import com.mygdx.game.server.network.gameinstance.handlers.MoveHandler;
import com.mygdx.game.server.network.gameinstance.handlers.SubfieldSubscriptionHandler;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class GameInstanceServer {
  private final MoveHandler moveHandler;
  private final EndTurnHandler endTurnHandler;
  private final SubfieldSubscriptionHandler subfieldSubscriptionHandler;
  private final GameInstance gameInstance;

  @Inject
  public GameInstanceServer(
      MoveHandler moveHandler,
      EndTurnHandler endTurnHandler,
      SubfieldSubscriptionHandler subfieldSubscriptionHandler,
      GameInstance gameInstance
  ) {
    this.moveHandler = moveHandler;
    this.endTurnHandler = endTurnHandler;
    this.subfieldSubscriptionHandler = subfieldSubscriptionHandler;
    this.gameInstance = gameInstance;
  }

  public void handle(String[] commands, Client client) {
    if (!gameInstance.getActivePlayer().getPlayerToken().equals(client.getPlayerToken())) {
      log.info("player " + client.getPlayerUsername() + " tried to send an unauthorized message");
      return;
    }
    switch (commands[0]) {
      case "move" -> moveHandler.handle(commands, client);
      case "field" -> subfieldSubscriptionHandler.handle(commands, client);
      case "end_turn" -> endTurnHandler.handle(client);
      default -> throw new RuntimeException("wtf");
    }
  }
}
