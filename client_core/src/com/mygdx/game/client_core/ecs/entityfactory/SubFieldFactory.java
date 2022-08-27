package com.mygdx.game.client_core.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Name;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.core.ecs.component.SubField;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@GameInstanceScope
@Log
public class SubFieldFactory extends EntityFactory<SubFieldConfig> {

  private final ComponentMapper<Name> nameMapper;
  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<SubField> subFieldMapper;

  @Inject
  public SubFieldFactory(
      @NonNull World world,
      @NonNull GameScreenAssets assets
  ) {
    super(world, assets);
    this.nameMapper = world.getMapper(Name.class);
    this.positionMapper = world.getMapper(Position.class);
    this.subFieldMapper = world.getMapper(SubField.class);
  }

  @Override
  public @NonNull void createEntity(SubFieldConfig config, int entity) {
    setUpName(config, entity);
    subFieldMapper.create(entity);
    positionMapper.create(entity);
  }

  private void setUpName(@NonNull SubFieldConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }
}
