package com.mygdx.game.server.network.gameinstance.handlers;

import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.gameinstance.services.AttackEntityService;

import javax.inject.Inject;

@GameInstanceScope
public class AttackHandler extends EntityCommandHandler {

  private final AttackEntityService attackEntityService;

  @Inject
  public AttackHandler(
      AttackEntityService attackEntityService
  ) {
    this.attackEntityService = attackEntityService;
  }

  public void handle(String[] commands, Client client) {
    if (!checkValidity(commands, client)) return;
    var attacker = Integer.parseInt(commands[1]);
    var attacked = Integer.parseInt(commands[2]);
    attackEntityService.attackEntity(attacker, attacked);
  }
}
