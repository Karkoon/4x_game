package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.util.ModelInstanceUtil;
import com.mygdx.game.config.ModelConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class ModelInstanceCompSetter {

  private final ComponentMapper<ModelInstanceComp> modelMapper;
  private final GameScreenAssets assets;

  @Inject
  public ModelInstanceCompSetter(@NonNull World world,
                                 @NonNull GameScreenAssets assets) {
    this.modelMapper = world.getMapper(ModelInstanceComp.class);
    this.assets = assets;
  }

  public void set(@NonNull ModelConfig config, int entity) {
    setUpModelInstanceComp(config, entity);
  }

  private void setUpModelInstanceComp(@NonNull ModelConfig config, int entityId) {
    var modelInstanceComp = modelMapper.create(entityId);
    modelInstanceComp.setModelInstanceFromModel(assets.getModel(config.getModelPath()));
    var texture = assets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstanceComp.getModelInstance(), texture);
  }
}
