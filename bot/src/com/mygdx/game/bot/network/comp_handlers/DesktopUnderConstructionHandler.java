package com.mygdx.game.bot.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.bot.ecs.component.ModelInstanceComp;
import com.mygdx.game.bot.util.ModelInstanceUtil;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.config.Config;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.ModelConfig;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class DesktopUnderConstructionHandler implements ComponentMessageListener.Handler<UnderConstruction> {

  private final GameConfigAssets gameConfigAssets;
  private final GameScreenAssets gameScreenAssets;

  private ComponentMapper<ModelInstanceComp> modelMapper;


  @Inject
  public DesktopUnderConstructionHandler(
      GameConfigAssets gameConfigAssets,
      GameScreenAssets gameScreenAssets,
      World world
  ) {
    this.gameConfigAssets = gameConfigAssets;
    this.gameScreenAssets = gameScreenAssets;
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, UnderConstruction component) {
    log.info("Read desktop construction component " + worldEntity);

    var config = gameConfigAssets.getGameConfigs().get(GameConfigs.BUILDING_MIN);
    setModelWithConstruction(config, worldEntity);

    return FULLY_HANDLED;
  }

  private void setModelWithConstruction(Config config, int entityId) {
    if (config instanceof ModelConfig modelConfig) {
      var modelInstance = prepareModelInstance(modelConfig);
      var modelInstanceComp = modelMapper.create(entityId);
      modelInstanceComp.setMainModel(modelInstance);
    }
  }

  private ModelInstance prepareModelInstance(ModelConfig config) {
    var modelInstance = new ModelInstance(gameScreenAssets.getModel(config.getModelPath()), new Vector3());
    var texture = gameScreenAssets.getTexture(GameScreenAssetPaths.CONSTRUCTION_TEXTURE);
    ModelInstanceUtil.setTexture(modelInstance, texture);
    return modelInstance;
  }

}
