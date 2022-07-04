package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.Score;
import com.mygdx.game.config.FieldConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class FieldFactory extends EntityFactory<FieldConfig> {

  private final ComponentMapper<Score> scoreMapper;

  @Inject
  public FieldFactory(@NonNull World world,
                      @NonNull GameScreenAssets assets) {
    super(world, assets);
    this.scoreMapper = world.getMapper(Score.class);
  }

  @Override
  public void createEntity(@NonNull FieldConfig config, int entity) {
    setUpScore(config, entity);
  }


  private void setUpScore(FieldConfig config, int entityId) {
    var score = scoreMapper.create(entityId);
    score.setScoreValue(config.getScoreValue());
  }
}
