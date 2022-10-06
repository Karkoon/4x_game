package com.mygdx.game.client.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.client.screen.Navigator;

import javax.inject.Inject;

public class SubFieldUiInputProcessor extends InputAdapter {

  private final Navigator navigator;

  @Inject
  public SubFieldUiInputProcessor(
      Navigator navigator
  ) {
    this.navigator = navigator;
  }

  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Input.Keys.ESCAPE -> backToGameScreen();
      default -> {
        return false;
      }
    }
    return true;
  }

  private void backToGameScreen() {
    navigator.changeToGameScreen();
  }
}
