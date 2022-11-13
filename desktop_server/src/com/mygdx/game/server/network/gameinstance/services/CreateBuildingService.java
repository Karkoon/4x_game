package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.core.model.MaterialUnit;
import com.mygdx.game.core.model.TechnologyImpactType;
import com.mygdx.game.server.util.MaterialUtilServer;
import com.mygdx.game.server.ecs.entityfactory.BuildingFactory;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.util.TechnologyUtilServer;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.Map;

@Log
public class CreateBuildingService extends WorldService {

  private final BuildingFactory buildingFactory;
  private final ComponentFactory componentFactory;
  private final GameConfigAssets assets;
  private final MaterialUtilServer materialUtilServer;
  private final TechnologyUtilServer technologyUtilServer;
  private ComponentMapper<SubField> subfieldMapper;

  @Inject
  CreateBuildingService(
      BuildingFactory buildingFactory,
      ComponentFactory componentFactory,
      GameConfigAssets assets,
      MaterialUtilServer materialUtilServer,
      TechnologyUtilServer technologyUtilServer,
      World world
  ) {
    this.buildingFactory = buildingFactory;
    this.componentFactory = componentFactory;
    this.assets = assets;
    this.materialUtilServer = materialUtilServer;
    this.technologyUtilServer = technologyUtilServer;
    world.inject(this);
  }

  public void createBuilding(int entityConfig, int parentSubfield, int x, int y, GameRoom room, int clientIndex) {
    log.info("Create building");

    var buildingConfig = assets.getGameConfigs().get(BuildingConfig.class, entityConfig);
    String playerToken = room.getClients().get(clientIndex).getPlayerToken();

    Map<MaterialBase, MaterialUnit> reducedMaterials = technologyUtilServer.getReducedParametersForBuilding(buildingConfig.getMaterials(), playerToken);

    if (subfieldMapper.get(parentSubfield).getBuilding() != -0xC0FEE) {
      log.info("THERE IS BUILDING WITH PARENT: " + parentSubfield);
    } else if(!materialUtilServer.checkIfCanBuy(playerToken, reducedMaterials)) {
      log.info("NOT ENOUGH RESOURCES");
    } else {
      var world = room.getGameInstance().getWorld();
      materialUtilServer.removeMaterials(playerToken, reducedMaterials);
      int buildingEntityId = buildingFactory.createBeforeEntity(
              buildingConfig,
              new Coordinates(x, y),
              parentSubfield,
              clientIndex
      );
      technologyUtilServer.applyTechnologyToNewEntities(buildingEntityId, playerToken, TechnologyImpactType.BUILDING_IMPACT);
      world.process();
    }
  }
}
