package com.mygdx.game.client.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.client.model.Lifecycle;

import javax.inject.Inject;

import static com.mygdx.game.client.model.Lifecycle.State.END;

public class RestartGameInput extends InputAdapter {

  private final Lifecycle lifecycle;

  @Inject
  RestartGameInput(Lifecycle lifecycle) {
    this.lifecycle = lifecycle;
  }

  @Override
  public boolean keyDown(int keycode) {
    if (keycode == Input.Keys.SPACE) {
      lifecycle.changeState(END);
      return true;
    }
    return false;
  }
}
