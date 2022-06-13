package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.util.ModelInstanceUtil;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Position;
import com.mygdx.game.core.model.Coordinates;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class UnitFactory extends EntityFactory<UnitConfig> {

  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<Name> nameMapper;
  private final ComponentMapper<ModelInstanceComp> modelInstanceCompMapper;

  @Inject
  public UnitFactory(@NonNull World world,
                     @NonNull GameScreenAssets assets) {
    super(world, assets);
    this.positionMapper = world.getMapper(Position.class);
    this.nameMapper = world.getMapper(Name.class);
    this.modelInstanceCompMapper = world.getMapper(ModelInstanceComp.class);
  }

  @Override
  public int createEntity(@NonNull UnitConfig config, @NonNull Coordinates coordinates) {
    var entity = world.create();
    positionMapper.create(entity);
    /* add model instance here again */
    setUpNameComponent(config, entity);
    setUpModelInstanceComp(config, entity);
    return entity;
  }

  private void setUpNameComponent(@NonNull UnitConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }

  private void setUpModelInstanceComp(@NonNull UnitConfig config, int entityId) {
    var modelInstanceComp = modelInstanceCompMapper.create(entityId);
    modelInstanceComp.setModelInstanceFromModel(assets.getModel(config.getModelPath()));
    var texture = assets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstanceComp.getModelInstance(), texture);
  }
}
