package com.mygdx.game.client_core.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Technology;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class TechnologyFactory {

  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<Name> nameMapper;
  private ComponentMapper<Position> positionMapper;
  private ComponentMapper<Technology> technologyMapper;

  @Inject
  public TechnologyFactory(
      World world
  ) {
    world.inject(this);
  }

  public void createEntity(@NonNull TechnologyConfig config, int entity) {
    setUpName(config, entity);
    entityConfigIdMapper.create(entity).setId(config.getId());
    setUpTechnology(entity);
    positionMapper.create(entity);
  }

  private void setUpName(@NonNull TechnologyConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }

  private void setUpTechnology(int entity) {
    technologyMapper.create(entity);
  }

}
