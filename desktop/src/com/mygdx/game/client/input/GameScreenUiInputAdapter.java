package com.mygdx.game.client.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.client.screen.Navigator;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class GameScreenUiInputAdapter extends InputAdapter {

  private final Navigator navigator;

  @Inject
  public GameScreenUiInputAdapter(@NonNull Navigator navigator) {
    this.navigator = navigator;
  }

  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Input.Keys.T -> openTechnologyScreen();
    }
    return false;
  }

  private void openTechnologyScreen() {
    navigator.changeToTechnologyScreen();
  }
}
