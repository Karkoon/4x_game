package com.mygdx.game.client.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.client.GdxGame;
import lombok.NonNull;
import lombok.extern.java.Log;

@Log
public class SubFieldUiInputProcessor extends InputAdapter {

  private final GdxGame game;

  public SubFieldUiInputProcessor(@NonNull GdxGame game) {
    this.game = game;
  }

  @Override
  public boolean keyDown(int keycode) {
    if (keycode == Input.Keys.ESCAPE) {
      backToGameScreen();
    }
    return true;
  }

  private void backToGameScreen() {
    game.changeToGameScreen();
  }
}
