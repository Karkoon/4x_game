package com.mygdx.game.bot.input;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.client_core.ecs.component.Score;
import com.mygdx.game.client_core.model.ActiveToken;
import com.mygdx.game.client_core.model.GameState;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.model.PlayerScore;
import com.mygdx.game.client_core.network.service.MoveEntityService;
import com.mygdx.game.core.ecs.component.Coordinates;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class MoveEntityBotInputAdapter extends InputAdapter {

  private final GameState gameState;
  private final MoveEntityService moveEntityService;
  private final ComponentMapper<Score> scoreMapper;
  private final PlayerScore playerScore;
  private final PlayerInfo playerInfo;
  private final ActiveToken activeToken;

  @Inject
  public MoveEntityBotInputAdapter(
      @NonNull GameState gameState,
      @NonNull World world,
      @NonNull MoveEntityService moveEntityService,
      @NonNull PlayerScore playerScore,
      @NonNull PlayerInfo playerInfo,
      @NonNull ActiveToken activeToken
  ) {
    this.gameState = gameState;
    this.moveEntityService = moveEntityService;
    this.scoreMapper = world.getMapper(Score.class);
    this.playerScore = playerScore;
    this.playerInfo = playerInfo;
    this.activeToken = activeToken;
  }

  public void moveUnit(int entityId, Coordinates clickedCoords) {
    if (canPlayerMove()) {
      processScore(clickedCoords);
      moveEntityService.moveEntity(entityId, clickedCoords);
    } else {
      log.info("Not your turn");
    }
  }

  private boolean canPlayerMove() {
    return activeToken.isActiveToken(playerInfo.getToken());
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
