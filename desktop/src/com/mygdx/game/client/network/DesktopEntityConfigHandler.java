package com.mygdx.game.client.network;

import com.artemis.Component;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.client.ecs.entityfactory.ModelInstanceCompSetter;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.config.ModelConfig;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;
import static com.github.czyzby.websocket.WebSocketListener.NOT_HANDLED;

@Log
public class DesktopEntityConfigHandler implements ComponentMessageListener.Handler {

  private final GameConfigAssets assets;
  private final ModelInstanceCompSetter modelInstanceCompSetter;

  @Inject
  public DesktopEntityConfigHandler(
      GameConfigAssets assets,
      ModelInstanceCompSetter modelInstanceCompSetter
  ) {
    this.assets = assets;
    this.modelInstanceCompSetter = modelInstanceCompSetter;
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Component component) {
    var entityConfigId = ((EntityConfigId) component).getId();
    var config = assets.getGameConfigs().get(entityConfigId);
    if (config instanceof ModelConfig modelConfig) {
      modelInstanceCompSetter.set(modelConfig, worldEntity);
      return FULLY_HANDLED;
    }
    return NOT_HANDLED;
  }
}
