package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.mygdx.game.client.ecs.component.Position;
import com.mygdx.game.core.ecs.component.Technology;
import lombok.extern.java.Log;

import javax.inject.Singleton;

@Singleton
@All({Technology.class, Position.class})
@Log
public class TechnologyRenderSystem {

  private ComponentMapper<Technology> technologyMapper;
  private ComponentMapper<Position> positionMapper;

}
