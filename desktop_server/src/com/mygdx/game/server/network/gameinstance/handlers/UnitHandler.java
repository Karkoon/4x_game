package com.mygdx.game.server.network.gameinstance.handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.core.ecs.component.InRecruitment;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.gameinstance.services.CreateUnitService;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class UnitHandler extends EntityCommandHandler {

  private final CreateUnitService createUnitService;
  private final World world;

  private ComponentMapper<InRecruitment> inRecruitmentMapper;

  @Inject
  public UnitHandler(
      CreateUnitService createUnitService,
      World world
  ) {
    this.createUnitService = createUnitService;
    this.world = world;
    world.inject(this);
  }

  public void handle(String[] commands, Client client) {
    var unitConfigId = Integer.parseInt(commands[1]);
    var fieldEntityId = Integer.parseInt(commands[2]);
    if (inRecruitmentMapper.has(fieldEntityId)) {
      log.info("In field " + fieldEntityId + " a unit is already in recruitment");
    } else {
      createUnitService.createUnit(unitConfigId, fieldEntityId, client, false);
      world.process();
    }
  }
}
