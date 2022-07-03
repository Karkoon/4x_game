package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.ecs.component.Name;
import com.mygdx.game.client.ecs.component.Position;
import com.mygdx.game.client.util.ModelInstanceUtil;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.config.SubFieldConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class SubFieldFactory extends EntityFactory<SubFieldConfig> {

  private final ComponentMapper<Name> nameMapper;
  private final ComponentMapper<ModelInstanceComp> modelMapper;
  private final ComponentMapper<Position> positionMapper;

  @Inject
  public SubFieldFactory(@NonNull World world,
                      @NonNull GameScreenAssets assets) {
    super(world, assets);
    this.nameMapper = world.getMapper(Name.class);
    this.modelMapper = world.getMapper(ModelInstanceComp.class);
    this.positionMapper = world.getMapper(Position.class);
  }

  @Override
  public @NonNull void createEntity(SubFieldConfig config, int entity) {
    setUpModelInstanceComp(config, entity);
    setUpName(config, entity);
    positionMapper.create(entity);
  }

  private void setUpModelInstanceComp(@NonNull SubFieldConfig config, int entityId) {
    var modelInstanceComp = modelMapper.create(entityId);
    modelInstanceComp.setModelInstanceFromModel(assets.getModel(config.getModelPath()));
    var texture = assets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstanceComp.getModelInstance(), texture);
  }

  private void setUpName(@NonNull SubFieldConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }
}
