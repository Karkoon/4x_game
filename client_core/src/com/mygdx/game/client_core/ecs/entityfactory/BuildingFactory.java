package com.mygdx.game.client_core.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.core.ecs.component.Building;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Name;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class BuildingFactory {

  private ComponentMapper<Building> buildingMapper;
  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<Name> nameMapper;
  private ComponentMapper<Position> positionMapper;

  @Inject
  public BuildingFactory(
      World world
  ) {
    world.inject(this);
  }

  public void createEntity(@NonNull BuildingConfig config, int entity) {
    setUpName(config, entity);
    entityConfigIdMapper.create(entity).setId(config.getId());
    buildingMapper.create(entity);
    positionMapper.create(entity).getValue().set(0, 10, 0);
  }

  private void setUpName(@NonNull BuildingConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }

}
