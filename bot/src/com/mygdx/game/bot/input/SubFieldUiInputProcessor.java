package com.mygdx.game.bot.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.bot.screen.Navigator;

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
    if (keycode == Input.Keys.ESCAPE) {
      backToGameScreen();
    } else {
      return false;
    }
    return true;
  }

  private void backToGameScreen() {
    navigator.changeToGameScreen();
  }
}
