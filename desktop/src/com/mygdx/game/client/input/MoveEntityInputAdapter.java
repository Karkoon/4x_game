package com.mygdx.game.client.input;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.screen.FieldScreen;
import com.mygdx.game.client.screen.Navigator;
import com.mygdx.game.client.ui.CoordinateClickedDialogFactory;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.ecs.component.Score;
import com.mygdx.game.client_core.model.GameState;
import com.mygdx.game.client_core.model.PlayerScore;
import com.mygdx.game.client_core.network.MoveEntityService;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.util.PositionUtil;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class MoveEntityInputAdapter extends InputAdapter {

  public static final float CLICK_RADIUS = 90f;
  private static final int NO_ENTITY = -0xC0FFEE;

  private final Navigator navigator;
  private final Viewport viewport;
  private final GameState gameState;
  private final CoordinateClickedDialogFactory coordinateClickedDialogFactory;
  private final MoveEntityService moveEntityService;
  private final ComponentMapper<Movable> movableMapper;
  private final ComponentMapper<Score> scoreMapper;
  private final PlayerScore playerScore;

  private int selectedUnit = NO_ENTITY;

  @Inject
  public MoveEntityInputAdapter(
      @NonNull Navigator navigator,
      @NonNull Viewport viewport,
      @NonNull GameState gameState,
      @NonNull CoordinateClickedDialogFactory dialogFactory,
      @NonNull World world,
      @NonNull MoveEntityService moveEntityService,
      @NonNull PlayerScore playerScore
  ) {
    this.navigator = navigator;
    this.viewport = viewport;
    this.gameState = gameState;
    this.coordinateClickedDialogFactory = dialogFactory;
    this.moveEntityService = moveEntityService;
    this.movableMapper = world.getMapper(Movable.class);
    this.scoreMapper = world.getMapper(Score.class);
    this.playerScore = playerScore;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    var clickedCoordinates = getCoordinatesFromClickPosition(screenX, screenY);
    if (clickedCoordinates == null) {
      return false;
    }

    if (selectedUnit == NO_ENTITY) {
      handleNoSelectedUnit(clickedCoordinates);
    } else {
      handleWithSelectedUnit(clickedCoordinates);
    }
    return true;
  }

  private void handleWithSelectedUnit(Coordinates clickedCoords) {
    processScore(clickedCoords);

    moveEntityService.moveEntity(selectedUnit, clickedCoords);
    selectedUnit = NO_ENTITY;
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

  private void handleNoSelectedUnit(Coordinates clickedCoords) {
    var entities = gameState.getSpecifiedEntitiesAtCoordinate(clickedCoords, new ComponentMapper[]{movableMapper, scoreMapper});
    coordinateClickedDialogFactory.createAndShow(entities, chosenEntity -> {
      if (movableMapper.has(chosenEntity)) {
        selectedUnit = chosenEntity;
        log.info("Selected a movable.");
      } else if (scoreMapper.has(chosenEntity)) {
        log.info("Selected a score with id " + chosenEntity);
        FieldScreen.choosenField = chosenEntity;
        navigator.changeToFieldScreen();
      }
    });
  }

  private Coordinates getCoordinatesFromClickPosition(int screenX, int screenY) {
    var ray = viewport.getPickRay(screenX, screenY);
    var minDistance = Float.MAX_VALUE;
    var coordinates = gameState.getSavedCoordinates();

    Coordinates result = null;
    for (var coordinate : coordinates) {
      var worldPosition = PositionUtil.generateWorldPositionForCoords(coordinate);
      var dist2 = ray.origin.dst2(worldPosition);
      if (dist2 > minDistance) {
        continue;
      }
      if (Intersector.intersectRaySphere(ray, worldPosition, CLICK_RADIUS, null)) {
        result = coordinate;
        minDistance = dist2;
      }
    }
    return result;
  }
}
