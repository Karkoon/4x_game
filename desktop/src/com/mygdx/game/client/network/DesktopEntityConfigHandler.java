package com.mygdx.game.client.network;

import com.artemis.Component;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.client.ecs.entityfactory.DesktopCoordinateSetter;
import com.mygdx.game.client.ecs.entityfactory.ModelInstanceCompSetter;
import com.mygdx.game.client.ecs.entityfactory.TextureCompSetter;
import com.mygdx.game.client_core.ecs.entityfactory.Setter;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.config.ModelConfig;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.config.TextureConfig;
import com.mygdx.game.core.ecs.component.EntityConfigId;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;
import static com.github.czyzby.websocket.WebSocketListener.NOT_HANDLED;
import static com.mygdx.game.client_core.ecs.entityfactory.Setter.Result.HANDLED;
import static com.mygdx.game.client_core.ecs.entityfactory.Setter.Result.HANDLING_ERROR;

public class DesktopEntityConfigHandler implements ComponentMessageListener.Handler {

  private final GameConfigAssets assets;
  private final List<Setter> setterList = new ArrayList<>();
  private final ModelInstanceCompSetter modelInstanceCompSetter;
  private final TextureCompSetter textureCompSetter;
  private final DesktopCoordinateSetter desktopCoordinateSetter;

  @Inject
  public DesktopEntityConfigHandler(
      GameConfigAssets assets,
      ModelInstanceCompSetter modelInstanceCompSetter,
      TextureCompSetter textureCompSetter,
      DesktopCoordinateSetter desktopCoordinateSetter
  ) {
    this.assets = assets;
    this.setterList.add(modelInstanceCompSetter);
    this.modelInstanceCompSetter = modelInstanceCompSetter;
    this.textureCompSetter = textureCompSetter;
    this.desktopCoordinateSetter = desktopCoordinateSetter;
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Component component) {
    var entityConfigId = ((EntityConfigId) component).getId();
    var config = assets.getGameConfigs().get(entityConfigId);
    boolean handledValue = NOT_HANDLED;
    if (config instanceof TechnologyConfig technologyConfig) {
      desktopCoordinateSetter.set(technologyConfig, worldEntity);
      handledValue = FULLY_HANDLED;
    }
    if (config instanceof ModelConfig modelConfig) {
      modelInstanceCompSetter.set(modelConfig, worldEntity);
      handledValue = FULLY_HANDLED;
    }
    if (config instanceof TextureConfig textureConfig) {
      textureCompSetter.set(textureConfig, worldEntity);
      handledValue = FULLY_HANDLED;
    }
    return handledValue;
    return NOT_HANDLED;
    var setterResults = setterList.stream()
        .map(setter -> setter.set(config, worldEntity))
        .toList();
    var anyErrors = setterResults.stream().anyMatch(HANDLING_ERROR::equals);
    var wasHandled = setterResults.stream().anyMatch(HANDLED::equals);
    if (anyErrors || !wasHandled) {
      return NOT_HANDLED;
    }
    return FULLY_HANDLED;
  }
}
