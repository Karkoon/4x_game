package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.util.ModelInstanceUtil;
import com.mygdx.game.client_core.ecs.entityfactory.Setter;
import com.mygdx.game.config.EntityConfig;
import com.mygdx.game.config.ModelConfig;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ModelInstanceCompSetter implements Setter {

  private final ComponentMapper<ModelInstanceComp> modelMapper;
  private final GameScreenAssets assets;

  @Inject
  public ModelInstanceCompSetter(@NonNull World world,
                                 @NonNull GameScreenAssets assets) {
    this.modelMapper = world.getMapper(ModelInstanceComp.class);
    this.assets = assets;
  }

  @Override
  public Result set(EntityConfig config, int entityId) {
    if (config instanceof ModelConfig modelConfig) {
      setUpModelInstanceComp(modelConfig, entityId);
      return Result.HANDLED;
    } else {
      return Result.REJECTED;
    }
  }

  private void setUpModelInstanceComp(@NonNull ModelConfig config, int entityId) {
    var modelInstanceComp = modelMapper.create(entityId);
    modelInstanceComp.setModelInstanceFromModel(assets.getModel(config.getModelPath()));
    var texture = assets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstanceComp.getModelInstance(), texture);
  }

}