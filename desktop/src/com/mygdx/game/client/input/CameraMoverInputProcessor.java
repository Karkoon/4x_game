package com.mygdx.game.client.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.input.CameraControl.Direction;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

public final class CameraMoverInputProcessor extends InputAdapter {

  private static final float MARGIN_X = 0.03f;
  private static final float MARGIN_Y = 0.03f;

  private final Set<Integer> handledKeys = Set.of(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT);
  @Getter
  private final CameraControl cameraControl;
  private final Viewport viewport;

  private boolean arrowKeysUsed = false;
  private boolean isNearVerticalEdge = false;
  private boolean isNearHorizontalEdge = false;

  public CameraMoverInputProcessor(
      @NonNull Viewport viewport
  ) {
    this.cameraControl = new CameraControl(viewport.getCamera());
    this.viewport = viewport;
  }

  @Override
  public boolean keyDown(int keycode) {
    if (!handledKeys.contains(keycode)) {
      return false;
    }

    if (cameraControl.isMoving() && !arrowKeysUsed) {
      cameraControl.stopMoving(Direction.ANY);
    }
    cameraControl.startMoving(keyToDirection(keycode));
    arrowKeysUsed = true;
    return true;
  }

  @Override
  public boolean keyUp(int keycode) {
    if (!handledKeys.contains(keycode)) {
      return false;
    }

    cameraControl.stopMoving(keyToDirection(keycode));
    if (!cameraControl.isMoving()) {
      arrowKeysUsed = false;
    }
    return true;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    if (arrowKeysUsed) {
      return false;
    }

    float xPercentage = screenX / (float) viewport.getScreenWidth();
    float yPercentage = screenY / (float) viewport.getScreenHeight();

    handleHorizontalMovement(xPercentage);
    handleVerticalMovement(yPercentage);

    return true;
  }

  private void handleHorizontalMovement(float xPercentage) {
    if (!isNearHorizontalEdge) {
      if (xPercentage < MARGIN_X) {
        cameraControl.startMoving(Direction.LEFT);
        isNearHorizontalEdge = true;
      }

      if (1.0f - MARGIN_X < xPercentage) {
        cameraControl.startMoving(Direction.RIGHT);
        isNearHorizontalEdge = true;
      }
    } else {
      if (MARGIN_X < xPercentage && xPercentage < 1.0f - MARGIN_X) {
        cameraControl.stopMoving(Direction.LEFT);
        cameraControl.stopMoving(Direction.RIGHT);
        isNearHorizontalEdge = false;
      }
    }
  }

  private void handleVerticalMovement(float yPercentage) {
    if (!isNearVerticalEdge) {
      if (yPercentage < MARGIN_Y) {
        cameraControl.startMoving(Direction.UP);
        isNearVerticalEdge = true;
      }

      if (1.0f - MARGIN_Y < yPercentage) {
        cameraControl.startMoving(Direction.DOWN);
        isNearVerticalEdge = true;
      }
    } else {
      if (MARGIN_Y < yPercentage && yPercentage < 1.0f - MARGIN_Y) {
        cameraControl.stopMoving(Direction.UP);
        cameraControl.stopMoving(Direction.DOWN);
        isNearVerticalEdge = false;
      }
    }
  }

  private @NonNull Direction keyToDirection(int keyCode) {
    return switch (keyCode) {
      case Input.Keys.LEFT -> Direction.LEFT;
      case Input.Keys.RIGHT -> Direction.RIGHT;
      case Input.Keys.UP -> Direction.UP;
      case Input.Keys.DOWN -> Direction.DOWN;
      default -> throw new IllegalArgumentException("Not an arrow-key");
    };
  }
}
