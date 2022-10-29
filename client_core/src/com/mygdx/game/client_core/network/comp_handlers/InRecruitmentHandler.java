package com.mygdx.game.client_core.network.comp_handlers;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.ComponentMessageListener;
import com.mygdx.game.core.ecs.component.InRecruitment;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@GameInstanceScope
public class InRecruitmentHandler implements ComponentMessageListener.Handler<InRecruitment> {

  private ComponentMapper<InRecruitment> inRecruitmentMapper;

  @Inject
  public InRecruitmentHandler(
      World world
  ) {
    world.inject(this);
  }

  @Override
  public boolean handle(WebSocket webSocket, int worldEntity, InRecruitment component) {
    log.info("Read inRecruitment component " + worldEntity);
    var inRecruitment = inRecruitmentMapper.create(worldEntity);
    inRecruitment.setTurnLeft(component.getTurnLeft());
    inRecruitment.setUnitConfigId(component.getUnitConfigId());
    if (inRecruitment.getTurnLeft() == 0)
      inRecruitmentMapper.remove(worldEntity);
    return FULLY_HANDLED;
  }
}
