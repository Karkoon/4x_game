package com.mygdx.game.client.input;

import com.badlogic.gdx.graphics.Camera;
import com.mygdx.game.client.Updatable;
import lombok.NonNull;

class CameraControl implements Updatable {

    private final static float SPEED = 0.5f;
    private final Camera camera;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean movingUp = false;
    private boolean movingDown = false;

    CameraControl(@NonNull Camera camera) {
        this.camera = camera;
    }

    void stopMoving(@NonNull Direction direction) {
        switch (direction) {
            case RIGHT -> movingRight = false;
            case LEFT -> movingLeft = false;
            case UP -> movingUp = false;
            case DOWN -> movingDown = false;
            case ANY -> {
                movingLeft = false;
                movingRight = false;
                movingUp = false;
                movingDown = false;
            }
        }
    }

    void startMoving(@NonNull Direction direction) {
        switch (direction) {
            case RIGHT -> movingRight = true;
            case LEFT -> movingLeft = true;
            case UP -> movingUp = true;
            case DOWN -> movingDown = true;
        }
    }

    boolean isMoving() {
        return movingDown || movingUp || movingRight || movingLeft;
    }

    enum Direction {
        RIGHT, LEFT, UP, ANY, DOWN
    }

    @Override
    public void update() {
        float xSpeed = 0f;
        float ySpeed = 0f;

        if (movingLeft) {
            xSpeed -= SPEED;
        }

        if (movingRight) {
            xSpeed += SPEED;
        }

        if (movingDown) {
            ySpeed -= SPEED;
        }

        if (movingUp) {
            ySpeed += SPEED;
        }

        camera.position.add(xSpeed, ySpeed, 0);
    }
}

