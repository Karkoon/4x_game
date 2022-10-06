package com.mygdx.game.client.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.client.screen.Navigator;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TechnologyScreenUiInputAdapter extends InputAdapter {

  private final Navigator navigator;

  @Inject
  public TechnologyScreenUiInputAdapter(
      Navigator navigator
  ) {
    this.navigator = navigator;
  }

  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Input.Keys.ESCAPE -> backToGameScreen();
    }
    return false;
  }

  private void backToGameScreen() {
    navigator.changeToGameScreen();
  }

}
