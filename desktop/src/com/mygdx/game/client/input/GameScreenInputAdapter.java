package com.mygdx.game.client.input;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.ui.FieldClickedDialogFactory;
import com.mygdx.game.core.ecs.component.Slot;
import com.mygdx.game.core.model.Coordinates;
import com.mygdx.game.core.model.GameState;
import com.mygdx.game.core.util.PositionUtil;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.logging.Level;

@Log
public class GameScreenInputAdapter extends InputAdapter {

  public static final float CLICK_RADIUS = 90f;

  private final Viewport viewport;
  private final GameState gameState;
  private final World world;
  private final FieldClickedDialogFactory fieldClickedDialogFactory;
  private final ComponentMapper<Slot> slotMapper;

  private int selectedUnit;
  private int selectedUnitsField;

  @Inject
  public GameScreenInputAdapter(@NonNull Viewport viewport,
                                @NonNull GameState gameState,
                                @NonNull World world,
                                @NonNull FieldClickedDialogFactory dialogFactory,
                                @NonNull ComponentMapper<Slot> slotMapper) {
    this.viewport = viewport;
    this.gameState = gameState;
    this.world = world;
    this.fieldClickedDialogFactory = dialogFactory;
    this.slotMapper = slotMapper;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    var selectedCoords = getCoordinatesFromClickPosition(screenX, screenY);
    if (selectedCoords == null) {
      return false;
    }
    var field = gameState.getFields().get(selectedCoords);

    if (selectedUnit == 0) {
      handleFieldClicked(field);
    } else {
      handleMovement(field);
    }
    return true;
  }

  private void handleMovement(int toField) {
    // unit movement przez zapytanie do serwera, przez jakąś usługę, która byłaby wstrzykiwana
    /*
    movement.setFromEntity(selectedUnitsField);
    movement.setToEntity(toField);
    selectedUnit.add(movement);

    syncer.sync(movement);

    selectedUnit = null;
    selectedUnitsField = null;*/
  }

  private void handleFieldClicked(int field) {
    var units = slotMapper.get(field).getEntities();
    fieldClickedDialogFactory.createAndShow(field, chosenEntity -> {
      if (chosenEntity.equals(field)) {
        log.log(Level.INFO, "Selected field.");
      } else if (units.contains(chosenEntity)) {
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
