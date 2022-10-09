package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.util.ModelInstanceUtil;
import com.mygdx.game.client_core.ecs.entityfactory.Setter;
import com.mygdx.game.config.Config;
import com.mygdx.game.config.ModelConfig;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ModelInstanceCompSetter implements Setter {

  private ComponentMapper<ModelInstanceComp> modelMapper;
  private final GameScreenAssets assets;

  @Inject
  public ModelInstanceCompSetter(World world,
      GameScreenAssets assets
  ) {
    world.inject(this);
    this.assets = assets;
  }

  @Override
  public Result set(Config config, int entityId) {
    if (config instanceof ModelConfig modelConfig) {
      setUpModelInstanceComp(modelConfig, entityId);
      return Result.HANDLED;
    } else {
      return Result.REJECTED;
    }
  }

  private void setUpModelInstanceComp(@NonNull ModelConfig config, int entityId) {
    var modelInstance = prepareModelInstance(config);
    var modelInstanceComp = modelMapper.create(entityId);
    modelInstanceComp.setMainModel(modelInstance);
  }

  private ModelInstance prepareModelInstance(ModelConfig config) {
    var modelInstance = new ModelInstance(assets.getModel(config.getModelPath()), new Vector3());
    var texture = assets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstance, texture);
    return modelInstance;
  }

}
