package com.mygdx.game.client.input;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.UnitMovement;
import com.mygdx.game.client.model.ActiveEntity;
import com.mygdx.game.client.model.Coordinates;
import com.mygdx.game.client.model.GameState;
import com.mygdx.game.client.ui.ChooseUnitFieldDialog;
import com.mygdx.game.client.util.PositionUtil;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.Map;

@Log
public class GameScreenInputAdapter extends InputAdapter {

  private static class GameObject {
    public final Vector3 position;
    public final Coordinates coords;
    public final float radius;

    private GameObject(Coordinates coords) {
      this.coords = coords;
      this.position = PositionUtil.generateWorldPositionForCoords(coords);
      radius = 90f;
    }
  }

  private static final ComponentMapper<UnitMovement> unitMovementCompMapper = ComponentMapper.getFor(UnitMovement.class);

  private final Viewport viewport;
  private final Array<GameObject> gameObjects = new Array<>();
  private final GameState gameState;
  private final Stage parentStage;
  private final GameScreenAssets assets;
  private final ActiveEntity activeEntity;

  public GameScreenInputAdapter(@NonNull Viewport viewport,
                                @NonNull GameState gameState,
                                @NonNull Stage stage,
                                @NonNull GameScreenAssets assets,
                                @NonNull ActiveEntity activeEntity) {
    this.viewport = viewport;
    this.gameState = gameState;
    loadGameObjects(gameState.getFieldList());
    this.parentStage = stage;
    this.assets = assets;
    this.activeEntity = activeEntity;
  }

  private void loadGameObjects(Map<Coordinates, Entity> coordinatesEntityMap) {
    for (var entry : coordinatesEntityMap.entrySet()) {
      var cords = entry.getKey();
      gameObjects.add(new GameObject(cords));
    }
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    GameObject selecting = getObject(screenX, screenY);
    if (selecting != null) {
      if (activeEntity.getEntity() != null && unitMovementCompMapper.has(activeEntity.getEntity())) {
        var unitMovementComp = unitMovementCompMapper.get(activeEntity.getEntity());
        unitMovementComp.setToEntity(gameState.getFieldList().get(selecting.coords));
        activeEntity.clear();
      } else {
        ChooseUnitFieldDialog.createCustomDialog(parentStage, gameState.getFieldList().get(selecting.coords), assets, activeEntity);
      }
    }
    return selecting != null;
  }

  public GameObject getObject(int screenX, int screenY) {
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
