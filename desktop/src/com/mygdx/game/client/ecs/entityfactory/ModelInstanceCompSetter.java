package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.util.ModelInstanceUtil;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.entityfactory.Setter;
import com.mygdx.game.config.Config;
import com.mygdx.game.config.ModelConfig;
import lombok.NonNull;

import javax.inject.Inject;

@GameInstanceScope
public class ModelInstanceCompSetter implements Setter {

  private final GameScreenAssets gameScreenAssets;

  private ComponentMapper<ModelInstanceComp> modelMapper;

  @Inject
  public ModelInstanceCompSetter(World world,
      GameScreenAssets gameScreenAssets
  ) {
    world.inject(this);
    this.gameScreenAssets = gameScreenAssets;
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
    var modelInstance = new ModelInstance(gameScreenAssets.getModel(config.getModelPath()), new Vector3());
    var texture = gameScreenAssets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstance, texture);
    return modelInstance;
  }

}
