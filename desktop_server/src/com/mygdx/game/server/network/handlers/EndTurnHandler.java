package com.mygdx.game.server.network.handlers;

import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.EndTurnService;

import javax.inject.Inject;

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
