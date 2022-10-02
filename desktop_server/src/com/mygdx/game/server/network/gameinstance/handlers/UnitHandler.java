package com.mygdx.game.server.network.gameinstance.handlers;

import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.gameinstance.services.AttackEntityService;
import com.mygdx.game.server.network.gameinstance.services.CreateUnitService;

import javax.inject.Inject;

@GameInstanceScope
public class UnitHandler extends EntityCommandHandler {

  private final CreateUnitService createUnitService;

  @Inject
  public UnitHandler(
      CreateUnitService createUnitService
  ) {
    this.createUnitService = createUnitService;
  }

  public void handle(String[] commands, Client client) {
    var unitConfigId = Integer.parseInt(commands[1]);
    var fieldEntityId = Integer.parseInt(commands[2]);
    createUnitService.createUnit(unitConfigId, fieldEntityId, client);
  }}
