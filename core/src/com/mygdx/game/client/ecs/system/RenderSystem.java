package com.mygdx.game.client.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.ecs.component.Position;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.badlogic.ashley.core.ComponentMapper.getFor;

@Singleton
public class RenderSystem extends IteratingSystem {

  private static final Family RENDER_SYSTEM_FAMILY = Family.all(
      ModelInstanceComp.class, Position.class
  ).get();

  private final ComponentMapper<ModelInstanceComp> modelInstanceMapper = getFor(ModelInstanceComp.class);
  private final ComponentMapper<Position> positionMapper = getFor(Position.class);
  private final ModelInstanceRenderer renderer;

  @Inject
  public RenderSystem(@NonNull ModelInstanceRenderer renderer) {
    super(RENDER_SYSTEM_FAMILY);
    this.renderer = renderer;
  }


  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    var modelInstance = modelInstanceMapper.get(entity).getModelInstance();
    var position = positionMapper.get(entity).getPosition();
    modelInstance.transform.setTranslation(position);
    renderer.addToCache(modelInstance);
  }
}
