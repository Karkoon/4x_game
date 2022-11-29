package com.mygdx.game.client.input;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.mygdx.game.client.model.ChosenMapSize;
import com.mygdx.game.core.util.Updatable;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.HashSet;
import java.util.Set;

@Log
class CameraControl implements Updatable {

  private static final float SPEED = 5f;
  private BoundingBox validCameraSpace;
  private final Camera camera;
  private final Set<Direction> activeDirections = new HashSet<>();

  CameraControl(
      @NonNull Camera camera
  ) {
    this.camera = camera;
    this.validCameraSpace = new BoundingBox(
            new Vector3(0, 0, 0),
            new Vector3(195f * ChosenMapSize.mapSize.width, 650f, 165f * ChosenMapSize.mapSize.height)
    );
  }

  void stopMoving(@NonNull Direction direction) {
    if (direction == Direction.ANY) {
      activeDirections.clear();
    } else {
      activeDirections.remove(direction);
    }
  }

  void startMoving(@NonNull Direction direction) {
    if (direction == Direction.ANY) {
      throw new IllegalArgumentException("Unexpected value: " + direction);
    } else {
      activeDirections.add(direction);
    }
  }

  boolean isMoving() {
    return !activeDirections.isEmpty();
  }

  @Override
  public void update(float delta) {
    var translationVector = new Vector3(0, 0, 0);
    activeDirections.forEach(direction -> translationVector.add(direction.velocity));
    var cameraTranslationResult = camera.position.add(translationVector);
    if (!validCameraSpace.contains(cameraTranslationResult)) {
      camera.position.sub(translationVector);
    }
  }


  enum Direction {
    RIGHT(new Vector3(SPEED, 0, 0)),
    LEFT(new Vector3(-SPEED, 0, 0)),
    UP(new Vector3(0, 0, -SPEED)),
    ANY(Vector3.Zero),
    DOWN(new Vector3(0, 0, SPEED));

    final Vector3 velocity;

    Direction(Vector3 velocity) {
      this.velocity = velocity;
    }
  }
}
