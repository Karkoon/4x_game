package com.mygdx.game.client_core.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.client_core.ecs.component.Score;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.Name;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class FieldFactory {

  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<Name> nameMapper;
  private ComponentMapper<Position> positionMapper;
  private ComponentMapper<Score> scoreMapper;

  @Inject
  public FieldFactory(
      World world
  ) {
    world.inject(this);
  }

  public void createEntity(@NonNull FieldConfig config, int entity) {
    setUpName(config, entity);
    setUpScore(config, entity);
    fieldMapper.create(entity);
    positionMapper.create(entity);
  }

  private void setUpName(@NonNull FieldConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }

  private void setUpScore(FieldConfig config, int entityId) {
    var score = scoreMapper.create(entityId);
    score.setScoreValue(config.getScoreValue());
  }
}
