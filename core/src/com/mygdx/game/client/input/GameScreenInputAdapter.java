package com.mygdx.game.client.input;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.component.ModelInstanceComponent;
import com.mygdx.game.client.component.PositionComponent;
import com.mygdx.game.client.model.Coordinates;
import lombok.extern.java.Log;

import java.util.Map;

@Log
public class GameScreenInputAdapter extends InputAdapter {

  public static class GameObject extends ModelInstance {
    public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();
    public final float radius;

    private final static BoundingBox bounds = new BoundingBox();

    public GameObject (Model model, Vector3 position) {
      super(model, position);
      calculateBoundingBox(bounds);
      bounds.getCenter(center);
      bounds.getDimensions(dimensions);
      radius = dimensions.len() / 3f;
    }
  }

  private final ComponentMapper<ModelInstanceComponent> modelInstanceMapper = ComponentMapper.getFor(ModelInstanceComponent.class);
  private final ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);

  private final Viewport viewport;
  private final Array<GameObject> fieldList = new Array<>();
  private final Array<Coordinates> fieldCords = new Array<>();

  private final Vector3 position = new Vector3();

  public GameScreenInputAdapter(Viewport viewport, Map<Coordinates, Entity> fieldList) {
    this.viewport = viewport;
    loadFields(fieldList);
  }

  private void loadFields(Map<Coordinates, Entity> fields) {
    for (Map.Entry<Coordinates, Entity> field : fields.entrySet()) {
      Entity fieldEntity = field.getValue();
      Coordinates cords = field.getKey();
      ModelInstance modelInstance = modelInstanceMapper.get(fieldEntity).getModelInstance();
      Vector3 position = positionMapper.get(fieldEntity).getPosition();
      fieldList.add(new GameObject(modelInstance.model, position));
      fieldCords.add(cords);
    }
  }

  @Override
  public boolean touchDown (int screenX, int screenY, int pointer, int button) {
    int selecting = getObject(screenX, screenY);
    if (selecting >= 0)
      System.out.println(selecting + " - " + fieldCords.get(selecting));
    return selecting >= 0;
  }

  public int getObject (int screenX, int screenY) {
    Ray ray = viewport.getPickRay(screenX, screenY);
    int result = -1;
    float distance = -1;
    for (int i = 0; i < fieldList.size; ++i) {
      final GameObject instance = fieldList.get(i);
      instance.transform.getTranslation(position);
      position.add(instance.center);
      float dist2 = ray.origin.dst2(position);
      if (distance >= 0f && dist2 > distance) continue;
      if (Intersector.intersectRaySphere(ray, position, instance.radius, null)) {
        result = i;
        distance = dist2;
      }
    }
    return result;
  }
}
