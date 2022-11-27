package com.mygdx.game.server.network.gameinstance.handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.UnitConfig;
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
  private final GameConfigAssets gameConfigAssets;
  private final World world;

  private ComponentMapper<InRecruitment> inRecruitmentMapper;

  @Inject
  public UnitHandler(
      CreateUnitService createUnitService,
      GameConfigAssets gameConfigAssets,
      World world
  ) {
    this.createUnitService = createUnitService;
    this.gameConfigAssets = gameConfigAssets;
    this.world = world;
    world.inject(this);
  }

  public void handle(String[] commands, Client client) {
    var unitConfigId = Integer.parseInt(commands[1]);
    var fieldEntityId = Integer.parseInt(commands[2]);
    if (inRecruitmentMapper.has(fieldEntityId)) {
      log.info("In field " + fieldEntityId + " a unit is already in recruitment");
    } else if (gameConfigAssets.getGameConfigs().get(UnitConfig.class, unitConfigId).getCivilizationConfigId() != client.getCivId()) {
      log.info("Unit belongs to other civ than client");
    }
    else {
      createUnitService.createUnit(unitConfigId, fieldEntityId, client, false);
    }
  }
}
