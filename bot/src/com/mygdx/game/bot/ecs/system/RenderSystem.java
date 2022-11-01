package com.mygdx.game.bot.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.bot.ecs.component.Visible;
import com.mygdx.game.bot.ModelInstanceRenderer;
import com.mygdx.game.bot.ecs.component.ModelInstanceComp;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Position;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@All({ModelInstanceComp.class, Position.class, Visible.class})
@GameInstanceScope
public class RenderSystem extends IteratingSystem {

  private final ModelInstanceRenderer renderer;
  private ComponentMapper<ModelInstanceComp> modelInstanceMapper;
  private ComponentMapper<Position> positionMapper;

  @Inject
  public RenderSystem(
      ModelInstanceRenderer renderer
  ) {
    this.renderer = renderer;
  }

  @Override
  protected void process(int entityId) {
    var yIndex = 0;
    var modelInstancesIter = modelInstanceMapper.get(entityId).getModelInstances().values();
    var position = positionMapper.get(entityId).getValue();
    while (modelInstancesIter.hasNext()) {
      var instance = modelInstancesIter.next();
      instance.transform.setTranslation(position.x, position.y + yIndex, position.z);
      yIndex += 5f;
      renderer.addModelToCache(instance);
    }
  }

  @Override
  protected void end() {
    renderer.render();
  }
}
