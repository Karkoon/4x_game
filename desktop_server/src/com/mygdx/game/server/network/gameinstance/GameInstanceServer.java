package com.mygdx.game.server.network.gameinstance;

import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameInstance;
import com.mygdx.game.server.network.gameinstance.handlers.AttackHandler;
import com.mygdx.game.server.network.gameinstance.handlers.BuildBotHandler;
import com.mygdx.game.server.network.gameinstance.handlers.BuildHandler;
import com.mygdx.game.server.network.gameinstance.handlers.EndTurnHandler;
import com.mygdx.game.server.network.gameinstance.handlers.InterruptHandler;
import com.mygdx.game.server.network.gameinstance.handlers.MoveHandler;
import com.mygdx.game.server.network.gameinstance.handlers.ResearchHandler;
import com.mygdx.game.server.network.gameinstance.handlers.SubfieldSubscriptionHandler;
import com.mygdx.game.server.network.gameinstance.handlers.UnitHandler;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class GameInstanceServer {

  private final AttackHandler attackHandler;
  private final BuildHandler buildHandler;
  private final BuildBotHandler buildBotHandler;
  private final EndTurnHandler endTurnHandler;
  private final MoveHandler moveHandler;
  private final ResearchHandler researchHandler;
  private final SubfieldSubscriptionHandler subfieldSubscriptionHandler;
  private final UnitHandler unitHandler;
  private final InterruptHandler interruptHandler;
  private final GameInstance gameInstance;

  @Inject
  public GameInstanceServer(
      AttackHandler attackHandler,
      BuildHandler buildHandler,
      BuildBotHandler buildBotHandler,
      EndTurnHandler endTurnHandler,
      MoveHandler moveHandler,
      ResearchHandler researchHandler,
      SubfieldSubscriptionHandler subfieldSubscriptionHandler,
      UnitHandler unitHandler,
      InterruptHandler interruptHandler,
      GameInstance gameInstance
  ) {
    this.attackHandler = attackHandler;
    this.buildHandler = buildHandler;
    this.buildBotHandler = buildBotHandler;
    this.endTurnHandler = endTurnHandler;
    this.moveHandler = moveHandler;
    this.researchHandler = researchHandler;
    this.subfieldSubscriptionHandler = subfieldSubscriptionHandler;
    this.unitHandler = unitHandler;
    this.interruptHandler = interruptHandler;
    this.gameInstance = gameInstance;
  }

  public void handle(String[] commands, Client client) {
    var handled = handleUnauthorized(commands, client);
    if (!handled && isAuthorized(client)) {
      if (!handleAuthorized(commands, client)) {
        throw new RuntimeException("wtf");
      }
    } else {
      log.info("player " + client.getPlayerUsername() + " tried to send an unauthorized message");
    }
  }

  private boolean isAuthorized(Client client) {
    return gameInstance.getActivePlayer().getPlayerToken().equals(client.getPlayerToken());
  }

  private boolean handleUnauthorized(String[] commands, Client client) {
    switch (commands[0]) {
      case "interrupt" -> interruptHandler.handle(client);
      default -> {
        return false;
      }
    }
    return true;
  }

  private boolean handleAuthorized(String[] commands, Client client) {
    switch (commands[0]) {
      case "attack" -> attackHandler.handle(commands, client);
      case "build" -> buildHandler.handle(commands, client);
      case "build_bot" -> buildBotHandler.handle(commands, client);
      case "create_unit" -> unitHandler.handle(commands, client);
      case "end_turn" -> endTurnHandler.handle();
      case "field" -> subfieldSubscriptionHandler.handle(commands, client);
      case "research" -> researchHandler.handle(commands, client);
      case "move" -> moveHandler.handle(commands, client);
      default -> {
        return false;
      }
    }
    return true;
  }
}
