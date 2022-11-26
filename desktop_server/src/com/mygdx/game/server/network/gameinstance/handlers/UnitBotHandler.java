package com.mygdx.game.server.network.gameinstance.handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.core.ecs.component.InRecruitment;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.gameinstance.services.CreateUnitService;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class UnitBotHandler extends EntityCommandHandler {

  private final CreateUnitService createUnitService;
  private final GameConfigAssets gameConfigAssets;
  private final World world;

  private ComponentMapper<InRecruitment> inRecruitmentMapper;

  @Inject
  public UnitBotHandler(
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
    var fieldEntityId = Integer.parseInt(commands[1]);
    if (inRecruitmentMapper.has(fieldEntityId)) {
      log.info("In field " + fieldEntityId + " a unit is already in recruitment");
    }
    else {
      createUnitService.createUnit(fieldEntityId, client);
      world.process();
    }
  }
}
