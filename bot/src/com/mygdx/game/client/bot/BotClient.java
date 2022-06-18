package com.mygdx.game.client.bot;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.client.ecs.component.Movable;
import com.mygdx.game.client.input.MoveEntityBotInputAdapter;
import com.mygdx.game.client.model.GameState;
import com.mygdx.game.client.model.PlayerScore;
import com.mygdx.game.client.util.CoordinateUtil;
import com.mygdx.game.core.ecs.component.Coordinates;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Singleton
@Log
public class BotClient {


  private final PlayerScore playerScore;
  private final GameState gameState;
  private final MoveEntityBotInputAdapter inputAdapter;
  private final Map<Coordinates, Integer> scoreMap;
  private final ComponentMapper<Coordinates> coordinatesMapper;
  private final ComponentMapper<Movable> movableMapper;
  private int unitEntityId;
  private Coordinates currentCoordinates;
  private final Random random;

  @Inject
  BotClient(
          @NonNull PlayerScore playerScore,
          @NonNull GameState gameState,
          @NonNull MoveEntityBotInputAdapter inputAdapter,
          @NonNull World world
  ) {
    this.playerScore = playerScore;
    this.gameState = gameState;
    this.inputAdapter = inputAdapter;
    this.scoreMap = gameState.getScoreMap();
    this.coordinatesMapper = world.getMapper(Coordinates.class);
    this.movableMapper = world.getMapper(Movable.class);
    this.currentCoordinates = new Coordinates(0, 0);
    this.random = new Random();
  }

  public void run() {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    getInitialUnit();

    while (true) {
      List<Integer> availableCoordinates = CoordinateUtil.getAvailableCoordinates(currentCoordinates);
      int nextMove = random.nextInt(0, availableCoordinates.size());
      Integer newMove = availableCoordinates.get(nextMove);
      Coordinates newCoordinates = CoordinateUtil.mapMoveToCoordinate(currentCoordinates, newMove);


      log.info("Old coordinates: " + currentCoordinates);
      currentCoordinates = newCoordinates;
      inputAdapter.moveUnit(unitEntityId, currentCoordinates);
      log.info("New coordinates: " + currentCoordinates);
      log.info("Bot score: " + playerScore.getScoreValue());

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }

  private void getInitialUnit() {
    var entities = gameState.getEntitiesAtCoordinate(currentCoordinates);
    for (int i = 0; i < entities.size; i++) {
      if (movableMapper.has(entities.get(i)))
        unitEntityId = entities.get(i);
    }
  }

}
