package com.mygdx.game.client.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.client.screen.Navigator;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;

import javax.inject.Inject;

@GameInstanceScope
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
    if (keycode == Input.Keys.ESCAPE) {
      backToGameScreen();
    }
    return false;
  }

  private void backToGameScreen() {
    navigator.changeToGameScreen();
  }

}
