package com.mygdx.game.server.network.gameinstance.handlers;

import com.artemis.World;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.gameinstance.services.CreateUnitService;

import javax.inject.Inject;

@GameInstanceScope
public class UnitHandler extends EntityCommandHandler {

  private final CreateUnitService createUnitService;
  private final World world;

  @Inject
  public UnitHandler(
      CreateUnitService createUnitService,
      World world
  ) {
    this.createUnitService = createUnitService;
    this.world = world;
  }

  public void handle(String[] commands, Client client) {
    var unitConfigId = Integer.parseInt(commands[1]);
    var fieldEntityId = Integer.parseInt(commands[2]);
    createUnitService.createUnit(unitConfigId, fieldEntityId, client, false);
    world.process();
  }
}
