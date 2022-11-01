package com.mygdx.game.bot.ecs.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.mygdx.game.bot.ecs.component.Highlighted;
import com.mygdx.game.bot.model.ChosenEntity;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.core.ecs.component.SubField;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class SetHighlightSystem extends BaseSystem {

  private final ChosenEntity chosenEntity;
  private ComponentMapper<Highlighted> highlightedMapper;
  private ComponentMapper<Movable> movableMapper;
  private ComponentMapper<SubField> subFieldMapper;

  @Inject
  public SetHighlightSystem(
      ChosenEntity chosenEntity
  ) {
    this.chosenEntity = chosenEntity;
  }

  @Override
  protected void processSystem() {
    if (chosenEntity.isAnyChosen() && movableMapper.has(chosenEntity.peek())) {
      var entityId = chosenEntity.pop();
      highlightedMapper.set(entityId, true);
    } else if (chosenEntity.isAnyChosen() && subFieldMapper.has(chosenEntity.peek())) {
      var entityId = chosenEntity.pop();
      highlightedMapper.set(entityId, true);
    }
  }
}
