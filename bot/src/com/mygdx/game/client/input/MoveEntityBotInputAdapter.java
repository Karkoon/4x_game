package com.mygdx.game.client.input;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.client.ecs.component.Score;
import com.mygdx.game.client.model.GameState;
import com.mygdx.game.client.model.PlayerScore;
import com.mygdx.game.client.network.MoveEntityService;
import com.mygdx.game.core.ecs.component.Coordinates;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class MoveEntityBotInputAdapter extends InputAdapter {

  public static final float CLICK_RADIUS = 90f;
  private static final int NO_ENTITY = -0xC0FFEE;

  private final GameState gameState;
  private final MoveEntityService moveEntityService;
  private final ComponentMapper<Score> scoreMapper;
  private final PlayerScore playerScore;

  @Inject
  public MoveEntityBotInputAdapter(
      @NonNull GameState gameState,
      @NonNull World world,
      @NonNull MoveEntityService moveEntityService,
      @NonNull PlayerScore playerScore
  ) {
    this.gameState = gameState;
    this.moveEntityService = moveEntityService;
    this.scoreMapper = world.getMapper(Score.class);
    this.playerScore = playerScore;
  }

  public void moveUnit(int entityId, Coordinates clickedCoords) {
    processScore(clickedCoords);
    moveEntityService.moveEntity(entityId, clickedCoords);
  }

  private void processScore(Coordinates clickedCoords) {
    var entities = gameState.getEntitiesAtCoordinate(clickedCoords);
    for (int i = 0; i < entities.size; i++) {
      var entity = entities.get(i);
      log.info(String.valueOf(entity));
      log.info(String.valueOf(i));
      if (scoreMapper.has(entity)) {
        var scoreValue = scoreMapper.get(entity).getScoreValue();
        playerScore.setScoreValue(playerScore.getScoreValue() + scoreValue);
        log.info("Current score: " + playerScore.getScoreValue());
      }
    }
  }
}
