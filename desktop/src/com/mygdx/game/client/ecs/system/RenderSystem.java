package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.ecs.component.Position;
import com.mygdx.game.client.ecs.component.Score;
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
  private ComponentMapper<SubField> subFieldMapper;

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
    if (scoreMapper.has(entityId))
      renderer.addModelToCache(modelInstance);
    else if (subFieldMapper.has(entityId)) {
      Integer parent = subFieldMapper.get(entityId).getParent();
      renderer.addSubModelToCache(parent, modelInstance);
    }
  }
}
