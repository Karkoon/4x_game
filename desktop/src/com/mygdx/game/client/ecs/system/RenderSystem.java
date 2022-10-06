package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.ecs.component.Visible;
import com.mygdx.game.client_core.ecs.component.Position;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
@All({ModelInstanceComp.class, Position.class, Visible.class})
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
    var modelInstance = modelInstanceMapper.get(entityId).getModelInstance();
    var position = positionMapper.get(entityId).getValue();
    modelInstance.transform.setTranslation(position);
    renderer.addModelToCache(modelInstance);
    // TODO #94 fix problem with errors when model instance is created but not set
  }

  @Override
  protected void end() {
    renderer.render();
  }
}
