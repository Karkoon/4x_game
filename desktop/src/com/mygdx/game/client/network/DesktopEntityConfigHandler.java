package com.mygdx.game.client.network;

import com.artemis.Component;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.client.ecs.entityfactory.ModelInstanceCompSetter;
import com.mygdx.game.client_core.ecs.entityfactory.Setter;
import com.mygdx.game.client_core.network.ComponentMessageListener;
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

  @Inject
  public DesktopEntityConfigHandler(
      GameConfigAssets assets,
      ModelInstanceCompSetter modelInstanceCompSetter
  ) {
    this.assets = assets;
    this.setterList.add(modelInstanceCompSetter);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Component component) {
    var entityConfigId = ((EntityConfigId) component).getId();
    var config = assets.getGameConfigs().get(entityConfigId);
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