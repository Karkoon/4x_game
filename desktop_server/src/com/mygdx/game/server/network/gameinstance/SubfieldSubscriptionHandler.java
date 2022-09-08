package com.mygdx.game.server.network.gameinstance;

import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.ShowSubfieldService;

import javax.inject.Inject;

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
