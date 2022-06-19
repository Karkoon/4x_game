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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Singleton
@Log
public class BotClient {
  private final PlayerScore playerScore;
  private final GameState gameState;
  private final MoveEntityBotInputAdapter inputAdapter;
  private final ComponentMapper<Movable> movableMapper;
  private final Random random;
  private final Map<Integer, Map<Integer, Double>> qMap = new HashMap<>();
  private Coordinates currentCoordinates;

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
    this.movableMapper = world.getMapper(Movable.class);

    this.currentCoordinates = new Coordinates(0, 0);
    this.random = new Random();
  }

  public void run() {
    log.info("Run bot");
    int movableEntity = getMovableEntity(currentCoordinates);

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        HashMap<Integer, Double> aMap = new HashMap<>();
        for (int k = 0; k < 6; k++) {
          aMap.put(k, 0.0);
        }
        qMap.put((int) (Math.pow(2, i) * Math.pow(3, j)), aMap);
      }
    }

    while (true) {
//      List<Integer> availableCoordinates = CoordinateUtil.getAvailableCoordinates(currentCoordinates);
//      int nextMove = random.nextInt(0, availableCoordinates.size());
//      Integer newMove = availableCoordinates.get(nextMove);
//      Coordinates newCoordinates = CoordinateUtil.mapMoveToCoordinate(currentCoordinates, newMove);
      log.info("Old coordinates: " + currentCoordinates);

      int nextMove = getAction(currentCoordinates);
      Coordinates newCoordinates = CoordinateUtil.mapMoveToCoordinate(currentCoordinates, nextMove);
      inputAdapter.moveUnit(movableEntity, newCoordinates);

      int reward = getReward(currentCoordinates, newCoordinates);
      updateQ(currentCoordinates, newCoordinates, nextMove, reward);
      log.info("New coordinates: " + newCoordinates);
      log.info("Bot score: " + playerScore.getScoreValue());
      currentCoordinates = newCoordinates;

      if (currentCoordinates.getX() == 8 && currentCoordinates.getY() == 8) {
        log.info("RESET");
        log.info("NEW ROUND... CURRENT QMAP: " + qMap);
        currentCoordinates = new Coordinates(0, 0);
        inputAdapter.moveUnit(movableEntity, currentCoordinates);
      }

      log.info("CURRENT QMAP: " + qMap);

      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }

  private int getMovableEntity(Coordinates at) {
    log.info("wait for map");
    var entities = gameState.getEntitiesAtCoordinate(at);
    while (entities == null) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      entities = gameState.getEntitiesAtCoordinate(currentCoordinates);
    }
    for (int i = 0; i < entities.size; i++) {
      if (movableMapper.has(entities.get(i))) {
        return entities.get(i);
      }
    }
    throw new IllegalArgumentException("No movable entity at coordinates " + at);
  }

  private int getReward(Coordinates currentCoordinates, Coordinates newCoordinates) {
    var ax = currentCoordinates.getX() - (currentCoordinates.getY() - (currentCoordinates.getY() % 2)) / 2;
    var ay = currentCoordinates.getY();
    var az = -ax - ay;

    var bx = newCoordinates.getX() - (newCoordinates.getY() - (newCoordinates.getY() % 2)) / 2;
    var by = newCoordinates.getY();
    var bz = -ax - ay;

    if (newCoordinates.getX() == 8 && newCoordinates.getY() == 8) {
      return 25;
    }

    var goalx = 4;
    var goaly = 8;
    var goalz = -12;

    return Math.max(Math.max(Math.abs(ax - goalx), Math.abs(ay - goaly)), Math.abs(az - goalz))
        - Math.max(Math.max(Math.abs(bx - goalx), Math.abs(by - goaly)), Math.abs(bz - goalz));
  }

  private int getAction(Coordinates currentCoordinates) {
    List<Integer> availableCoordinates = CoordinateUtil.getAvailableCoordinates(currentCoordinates);
    int coordsHash = (int) (Math.pow(2, currentCoordinates.getX()) * Math.pow(3, currentCoordinates.getY()));
    Map<Integer, Double> actions = qMap.get(coordsHash);

    Map.Entry<Integer, Double> maxEntry = null;

    var epsilon = random.nextInt(0, 1000);

    if (epsilon < 100) {
      var nextMove = random.nextInt(0, availableCoordinates.size());
      var newMove = availableCoordinates.get(nextMove);

      return newMove;
    } else {
      for (Map.Entry<Integer, Double> entry : actions.entrySet()) {
        if (availableCoordinates.contains(entry.getKey())) {
          if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
            maxEntry = entry;
          }
        }
      }
    }

    return maxEntry.getKey();
  }

  private void updateQ(Coordinates currentCoordinates, Coordinates newCoordinates, int action, int reward) {
    int coordsHash = (int) (Math.pow(2, currentCoordinates.getX()) * Math.pow(3, currentCoordinates.getY()));
    int newCoordsHash = (int) (Math.pow(2, newCoordinates.getX()) * Math.pow(3, newCoordinates.getY()));

    double newQ = qMap.get(coordsHash).get(action) + 0.1 * (reward + 0.2 * qMap.get(newCoordsHash).get(getAction(newCoordinates)) - qMap.get(coordsHash).get(action));
    qMap.get(coordsHash).put(action, newQ);
  }
}
