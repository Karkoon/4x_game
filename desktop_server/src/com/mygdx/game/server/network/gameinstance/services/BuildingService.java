package com.mygdx.game.server.network.gameinstance.services;

import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.ecs.entityfactory.BuildingFactory;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.model.GameRoom;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class BuildingService extends WorldService {

  private final BuildingFactory buildingFactory;
  private final ComponentFactory componentFactory;
  private final GameConfigAssets assets;

  @Inject
  BuildingService(
      @NonNull BuildingFactory buildingFactory,
      @NonNull ComponentFactory componentFactory,
      @NonNull GameConfigAssets assets
  ) {
    this.buildingFactory = buildingFactory;
    this.componentFactory = componentFactory;
    this.assets = assets;
  }

  public void createBuilding(int entityConfig, int parentField, int x, int y, GameRoom room) {
    var world = room.getGameInstance().getWorld();
    var buildingConfig = assets.getGameConfigs().get(BuildingConfig.class, entityConfig);
    buildingFactory.createEntity(
      buildingConfig,
      new Coordinates(x, y),
      parentField
    );
    world.process();
  }
}
