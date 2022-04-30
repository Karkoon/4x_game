package com.mygdx.game.client.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.initialize.PositionUtil;
import com.mygdx.game.client.model.Coordinates;
import com.mygdx.game.client.model.GameState;
import com.mygdx.game.client.screen.ChooseUnitFieldDialog;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.Map;
import java.util.logging.Level;

@Log
public class GameScreenInputAdapter extends InputAdapter {

  private static class GameObject {

    public final Vector3 position;
    public final Coordinates coords;
    public final float radius;

    private GameObject (Coordinates coords) {
      this.coords = coords;
      this.position = PositionUtil.generateWorldPositionForCoords(coords);
      radius = 90f;
    }

  }

  private final Viewport viewport;
  private final Array<GameObject> gameObjects = new Array<>();
  private final GameState gameState;
  private final Stage parentStage;

  public GameScreenInputAdapter(@NonNull Viewport viewport,
                                @NonNull GameState gameState, Stage stage) {
    this.viewport = viewport;
    this.gameState = gameState;
    loadGameObjects(gameState.getFieldList());
    this.parentStage = stage;
  }

  public void loadGameObjects(Map<Coordinates, Entity> coordinatesEntityMap) {
    for (var entry : coordinatesEntityMap.entrySet()) {
      var cords = entry.getKey();
      gameObjects.add(new GameObject(cords));
    }
  }

  @Override
  public boolean touchDown (int screenX, int screenY, int pointer, int button) {
    GameObject selecting = getObject(screenX, screenY);
    if (selecting != null) {
      ChooseUnitFieldDialog.createCustomDialog(parentStage, gameState.getFieldList().get(selecting.coords), s -> {
        if (s.equals("field")) {
          log.log(Level.INFO, "Selected field.");
        } else if (s.equals("unit")) {
          log.log(Level.INFO, "Selected unit.");
        }
      });
    }
    return selecting != null;
  }

  public GameObject getObject (int screenX, int screenY) {
    var ray = viewport.getPickRay(screenX, screenY);
    GameObject result = null;
    float distance = -1;
    for (var i = 0; i < gameObjects.size; i++) {
      var gameObject = gameObjects.get(i);
      float dist2 = ray.origin.dst2(gameObject.position);
      if (distance >= 0f && dist2 > distance) {
        continue;
      }
      if (Intersector.intersectRaySphere(ray, gameObject.position, gameObject.radius, null)) {
        result = gameObject;
        distance = dist2;
      }
    }
    return result;
  }
}
