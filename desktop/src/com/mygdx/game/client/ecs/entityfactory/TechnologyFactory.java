package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.Name;
import com.mygdx.game.client.ecs.component.Position;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.config.TechnologyConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class TechnologyFactory extends EntityFactory<TechnologyConfig> {

  private final ComponentMapper<Name> nameMapper;
  private final ComponentMapper<Position> positionMapper;


  @Inject
  public TechnologyFactory(@NonNull World world,
                         @NonNull GameScreenAssets assets) {
    super(world, assets);
    this.nameMapper = world.getMapper(Name.class);
    this.positionMapper = world.getMapper(Position.class);
  }

  @Override
  public @NonNull void createEntity(TechnologyConfig config, int entity) {
    setUpName(config, entity);
    positionMapper.create(entity);
  }

  private void setUpName(@NonNull TechnologyConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }
}
