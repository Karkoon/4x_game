package com.mygdx.game.server.network.gameinstance.handlers;

import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.gameinstance.services.EndTurnService;

import javax.inject.Inject;

@GameInstanceScope
public class EndTurnHandler {

  private final EndTurnService endTurnService;

  @Inject
  public EndTurnHandler(
      EndTurnService endTurnService
  ) {
    this.endTurnService = endTurnService;
  }

  public void handle(Client client) {
    endTurnService.nextTurn(client);
  }
}
