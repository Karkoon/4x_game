package com.mygdx.game.client.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.client.screen.Navigator;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.EndTurnService;
import dagger.Lazy;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@GameInstanceScope
public class GameScreenUiInputAdapter extends InputAdapter {

  private final Lazy<Navigator> navigator;
  private final EndTurnService endTurnService;

  @Inject
  public GameScreenUiInputAdapter(
      @NonNull Lazy<Navigator> navigator,
      @NonNull EndTurnService endTurnService
  ) {
    this.navigator = navigator;
    this.endTurnService = endTurnService;
  }

  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Input.Keys.T -> openTechnologyScreen();
      case Input.Keys.R -> endTurnService.endTurn();
    }
    return false;
  }

  private void openTechnologyScreen() {
    navigator.get().changeToTechnologyScreen();
  }
}
