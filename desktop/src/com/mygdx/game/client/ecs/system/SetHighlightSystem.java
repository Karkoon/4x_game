package com.mygdx.game.client.ecs.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.mygdx.game.client.ecs.component.Highlighted;
import com.mygdx.game.client.model.ChosenEntity;
import com.mygdx.game.client_core.ecs.component.Movable;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class SetHighlightSystem extends BaseSystem {

  private final ChosenEntity chosenEntity;
  private ComponentMapper<Highlighted> highlightedMapper;
  private ComponentMapper<Movable> movableMapper;

  @Inject
  public SetHighlightSystem(ChosenEntity chosenEntity) {
    this.chosenEntity = chosenEntity;
  }

  @Override
  protected void processSystem() {
    if (chosenEntity.isAnyChosen() && movableMapper.has(chosenEntity.peek())) {
      var entityId = chosenEntity.pop();
      highlightedMapper.set(entityId, true);
    }
  }
}
