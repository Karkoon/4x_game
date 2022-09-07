package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.client_core.ecs.component.Score;
import com.mygdx.game.core.ecs.component.SubField;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@All({ModelInstanceComp.class, Position.class})
@Log
public class RenderSystem extends IteratingSystem {

  private ComponentMapper<ModelInstanceComp> modelInstanceMapper;
  private ComponentMapper<Position> positionMapper;
  private ComponentMapper<Score> scoreMapper;
  private ComponentMapper<Movable> movableMapper;
  private ComponentMapper<SubField> subFieldMapper;

  private final ModelInstanceRenderer renderer;

  @Inject
  public RenderSystem(@NonNull ModelInstanceRenderer renderer) {
    this.renderer = renderer;
  }

  @Override
  protected void process(int entityId) {
    try {
      var modelInstance = modelInstanceMapper.get(entityId).getModelInstance();
      var position = positionMapper.get(entityId).getValue();
      modelInstance.transform.setTranslation(position);
      if (scoreMapper.has(entityId) || movableMapper.has(entityId)) {
        renderer.addModelToCache(modelInstance);
      } else if (subFieldMapper.has(entityId)) {
        int parent = subFieldMapper.get(entityId).getParent();
        renderer.addSubModelToCache(parent, modelInstance);
      }
    } catch (Exception exception) {
      // TODO #94 fix problem with errors when model instance is created but not set
      exception.printStackTrace();
    }
  }
}
