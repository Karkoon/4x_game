package com.mygdx.game.client.input;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.client.util.Updatable;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

class CameraControl implements Updatable {

  private static final float SPEED = 2.5f;
  private final Camera camera;
  private final Set<Direction> activeDirections = new HashSet<>();

  CameraControl(@NonNull Camera camera) {
    this.camera = camera;
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

  enum Direction {
    RIGHT, LEFT, UP, ANY, DOWN
  }

  @Override
  public void update(float delta) {
    var translationVector = new Vector3(0, 0, 0);
    activeDirections.forEach(direction -> {
      switch (direction) {
        case RIGHT -> translationVector.x += SPEED;
        case LEFT -> translationVector.x -= SPEED;
        case UP -> translationVector.z -= SPEED;
        case DOWN -> translationVector.z += SPEED;
        default -> throw new IllegalStateException("Unhandled direction: " + direction);
      }
    });

    camera.position.add(translationVector);
  }
}
