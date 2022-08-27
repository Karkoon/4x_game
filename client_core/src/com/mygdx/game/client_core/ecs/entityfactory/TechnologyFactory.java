package com.mygdx.game.client_core.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Name;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.client_core.model.Technologies;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.core.ecs.component.Technology;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@GameInstanceScope
@Log
public class TechnologyFactory extends EntityFactory<TechnologyConfig> {

  private final ComponentMapper<Name> nameMapper;
  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<Technology> technologyMapper;
  private final Technologies technologies;

  @Inject
  public TechnologyFactory(
          @NonNull World world,
          @NonNull GameScreenAssets assets,
          @NonNull Technologies technologies) {
    super(world, assets);
    this.nameMapper = world.getMapper(Name.class);
    this.positionMapper = world.getMapper(Position.class);
    this.technologyMapper = world.getMapper(Technology.class);
    this.technologies = technologies;
  }

  @Override
  public @NonNull void createEntity(TechnologyConfig config, int entity) {
    setUpName(config, entity);
    setUpTechnology(config, entity);
    positionMapper.create(entity);
    technologies.saveTechnology(entity);
  }

  private void setUpName(@NonNull TechnologyConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }

  private void setUpTechnology(TechnologyConfig config, int entity) {
    technologyMapper.create(entity);
  }

}
