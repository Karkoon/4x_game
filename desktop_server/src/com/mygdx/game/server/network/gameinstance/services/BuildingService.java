package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.server.util.MaterialUtilServer;
import com.mygdx.game.server.ecs.entityfactory.BuildingFactory;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.model.GameRoom;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class BuildingService extends WorldService {

  private final BuildingFactory buildingFactory;
  private final ComponentFactory componentFactory;
  private final GameConfigAssets assets;
  private final MaterialUtilServer materialUtilServer;
  private ComponentMapper<SubField> subfieldMapper;

  @Inject
  BuildingService(
      BuildingFactory buildingFactory,
      ComponentFactory componentFactory,
      GameConfigAssets assets,
      MaterialUtilServer materialUtilServer,
      World world
  ) {
    this.buildingFactory = buildingFactory;
    this.componentFactory = componentFactory;
    this.assets = assets;
    this.materialUtilServer = materialUtilServer;
    world.inject(this);
  }

  public void createBuilding(int entityConfig, int parentSubfield, int x, int y, GameRoom room, int clientIndex) {
    log.info("Create building");
    var world = room.getGameInstance().getWorld();
    var buildingConfig = assets.getGameConfigs().get(BuildingConfig.class, entityConfig);
    String playerToken = room.getClients().get(clientIndex).getPlayerToken();

    if (subfieldMapper.get(parentSubfield).getBuilding() != -0xC0FEE) {
      log.info("THERE IS BUILDING WITH PARENT: " + parentSubfield);
    } else if(!materialUtilServer.checkIfCanBuy(playerToken, buildingConfig.getMaterials())) {
      log.info("NOT ENOUGH RESOURCES");
    } else {
      materialUtilServer.removeMaterials(playerToken, buildingConfig.getMaterials());
      buildingFactory.createBeforeEntity(
              buildingConfig,
              new Coordinates(x, y),
              parentSubfield,
              clientIndex
      );
      world.process();
    }
  }
}
