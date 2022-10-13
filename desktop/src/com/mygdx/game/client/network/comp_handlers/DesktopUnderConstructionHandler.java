package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.client.ecs.entityfactory.ModelInstanceCompSetter;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class DesktopUnderConstructionHandler implements ComponentMessageListener.Handler<UnderConstruction> {

  private final GameConfigAssets assets;
  private final ModelInstanceCompSetter modelInstanceCompSetter;

  @Inject
  public DesktopUnderConstructionHandler(
      GameConfigAssets assets,
      ModelInstanceCompSetter modelInstanceCompSetter,
      World world
  ) {
    this.assets = assets;
    this.modelInstanceCompSetter = modelInstanceCompSetter;
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, UnderConstruction component) {
    log.info("Read desktop construction component " + worldEntity);

    var config = assets.getGameConfigs().get(GameConfigs.BUILDING_MIN);
    modelInstanceCompSetter.setModel(config, worldEntity, UnderConstruction.constructionTexture);

    return FULLY_HANDLED;
  }

}
