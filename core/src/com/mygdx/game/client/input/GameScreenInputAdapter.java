package com.mygdx.game.client.input;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.ecs.component.Slot;
import com.mygdx.game.client.ecs.component.UnitMovement;
import com.mygdx.game.client.model.Coordinates;
import com.mygdx.game.client.model.GameState;
import com.mygdx.game.client.ui.FieldClickedDialogFactory;
import com.mygdx.game.client.util.PositionUtil;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.logging.Level;

@Log
public class GameScreenInputAdapter extends InputAdapter {

  public static final float CLICK_RADIUS = 90f;

  private final Viewport viewport;
  private final GameState gameState;
  private final Engine engine;
  private final FieldClickedDialogFactory fieldClickedDialogFactory;

  private Entity selectedUnit;
  private Entity selectedUnitsField;

  @Inject
  public GameScreenInputAdapter(@NonNull Viewport viewport,
                                @NonNull GameState gameState,
                                @NonNull Engine engine,
                                @NonNull FieldClickedDialogFactory dialogFactory) {
    this.viewport = viewport;
    this.gameState = gameState;
    this.engine = engine;
    this.fieldClickedDialogFactory = dialogFactory;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    var selectedCoords = getCoordinatesFromClickPosition(screenX, screenY);
    if (selectedCoords == null) {
      return false;
    }
    var field = gameState.getFields().get(selectedCoords);
    if (selectedUnit == null) {
      handleFieldClicked(field);
    } else {
      handleMovement(field);
    }
    return true;
  }

  private void handleMovement(Entity toField) {
    var movement = engine.createComponent(UnitMovement.class);
    movement.setFromEntity(selectedUnitsField);
    movement.setToEntity(toField);
    selectedUnit.add(movement);
    selectedUnit = null;
    selectedUnitsField = null;
  }

  private void handleFieldClicked(Entity field) {
    var units = field.getComponent(Slot.class).getEntities();
    fieldClickedDialogFactory.createAndShow(field, chosenEntity -> {
      if (field.equals(chosenEntity)) {
        log.log(Level.INFO, "Selected field.");
      } else if (units.contains(chosenEntity, true)) {
        selectedUnitsField = field;
        selectedUnit = chosenEntity;
        log.log(Level.INFO, "Selected unit.");
      }
    });
  }

  private Coordinates getCoordinatesFromClickPosition(int screenX, int screenY) {
    var ray = viewport.getPickRay(screenX, screenY);
    var minDistance = Float.MAX_VALUE;
    var coordinates = gameState.getFields().keySet().stream().toList();

    Coordinates result = null;
    for (var i = 0; i < coordinates.size(); i++) {
      var coordinate = coordinates.get(i);
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
