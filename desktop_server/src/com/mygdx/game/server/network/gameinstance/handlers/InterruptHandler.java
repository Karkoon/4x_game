package com.mygdx.game.server.network.gameinstance.handlers;

import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.gameinstance.services.InterruptService;

import javax.inject.Inject;

public class InterruptHandler {
  private final InterruptService interruptService;

  @Inject
  public InterruptHandler(
      InterruptService interruptService
  ) {
    this.interruptService = interruptService;
  }

  public void handle(Client client) {
    var exceptPlayerToken = client.getPlayerToken();
    interruptService.notifyOfInterrupt(exceptPlayerToken);
  }
}
