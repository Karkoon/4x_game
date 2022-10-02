package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.UnitFactory;
import com.mygdx.game.server.model.Client;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class CreateUnitService extends WorldService {

  private ComponentMapper<Coordinates> coordinatesMapper;
  private UnitFactory unitFactory;
  private GameConfigAssets assets;
  private World world;

  @Inject
  public CreateUnitService(
      UnitFactory unitFactory,
      GameConfigAssets assets,
      World world
  ) {
    this.unitFactory = unitFactory;
    this.assets = assets;
    world.inject(this);
  }

  public void createUnit(int unitConfigId, int fieldEntityId, Client client) {
    var config = assets.getGameConfigs().get(UnitConfig.class, unitConfigId);
    var coordinates = coordinatesMapper.get(fieldEntityId);
    unitFactory.createEntity(config, coordinates, client);
    world.process();
  }
}
