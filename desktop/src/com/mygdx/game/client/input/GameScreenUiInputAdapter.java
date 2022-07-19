package com.mygdx.game.client.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.client.GdxGame;
import lombok.NonNull;
import lombok.extern.java.Log;

@Log
public class GameScreenUiInputAdapter extends InputAdapter {

  private final GdxGame game;

  public GameScreenUiInputAdapter(@NonNull GdxGame game) {
    this.game = game;
  }

  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Input.Keys.T -> openTechnologyScreen();
    }
    return true;
  }

  private void openTechnologyScreen() {
    game.changeToTechnologyScreen();
  }
}
