package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.ecs.component.Position;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@All({ModelInstanceComp.class, Position.class})
public class RenderSystem extends IteratingSystem {

  private ComponentMapper<ModelInstanceComp> modelInstanceMapper;
  private ComponentMapper<Position> positionMapper;

  private final ModelInstanceRenderer renderer;

  @Inject
  public RenderSystem(@NonNull ModelInstanceRenderer renderer) {
    this.renderer = renderer;
  }

  @Override
  protected void process(int entityId) {
    var modelInstance = modelInstanceMapper.get(entityId).getModelInstance();
    var position = positionMapper.get(entityId).getPosition();
    modelInstance.transform.setTranslation(position);
    renderer.addToCache(modelInstance);
  }
}
