package com.mygdx.game.client.actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.NonNull;

public class LoadingBar extends Actor {

  @NonNull
  private final Animation<TextureRegion> animation;
  @NonNull
  private TextureRegion currentFrame;

  private float time;

  public LoadingBar(@NonNull Animation<TextureRegion> animation) {
    this.animation = animation;
    currentFrame = animation.getKeyFrame(0);
  }

  @Override
  public void act(float delta) {
    time += delta;
    currentFrame = animation.getKeyFrame(time);
  }

  @Override
  public void draw(@NonNull Batch batch, float parentAlpha) {
    batch.draw(currentFrame, getX(), getY());
  }
}
