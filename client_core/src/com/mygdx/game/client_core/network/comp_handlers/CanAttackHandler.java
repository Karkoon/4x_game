package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.CanAttack;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@GameInstanceScope
public class CanAttackHandler implements ComponentMessageListener.Handler<CanAttack> {

  private ComponentMapper<CanAttack> canAttackComponentMapper;

  @Inject
  public CanAttackHandler(
      World world
  ) {
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, CanAttack component) {
    canAttackComponentMapper.create(worldEntity).setCanAttack(component.isCanAttack());
    return FULLY_HANDLED;
  }
}
