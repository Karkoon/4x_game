package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.model.GameState;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.client_core.network.NetworkWorldEntityMapper;
import com.mygdx.game.core.ecs.component.SubField;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
public class SubFieldHandler implements ComponentMessageListener.Handler {

  private final NetworkWorldEntityMapper networkWorldEntityMapper;
  private final GameState gameState;
  private final ComponentMapper<SubField> subFieldMapper;

  @Inject
  public SubFieldHandler(
      NetworkWorldEntityMapper networkWorldEntityMapper,
      World world,
      GameState gameState
  ) {
    this.networkWorldEntityMapper = networkWorldEntityMapper;
    this.gameState = gameState;
    this.subFieldMapper = world.getMapper(SubField.class);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, Component component) {
    log.info("Read subfield component " + worldEntity);
    var newParent = ((SubField) component).getParent();
    newParent = networkWorldEntityMapper.getWorldEntity(newParent);
    var subField = subFieldMapper.create(worldEntity);
    gameState.removeEntity(worldEntity);
    subField.setParent(newParent);
    gameState.saveEntity(worldEntity);
    return FULLY_HANDLED;
  }
}
