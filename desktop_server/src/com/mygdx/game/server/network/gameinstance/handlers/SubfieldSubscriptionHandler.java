package com.mygdx.game.server.network.gameinstance.handlers;

import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.gameinstance.services.ShowSubfieldService;

import javax.inject.Inject;

@GameInstanceScope
public class SubfieldSubscriptionHandler {

  private final ShowSubfieldService showSubfieldService;

  @Inject
  public SubfieldSubscriptionHandler(
      ShowSubfieldService showSubfieldService
  ) {
    this.showSubfieldService = showSubfieldService;
  }

  public void handle(String[] commands, Client client) {
    var entityId = Integer.parseInt(commands[1]);
    showSubfieldService.flipSubscriptionState(entityId, client);
  }
}
