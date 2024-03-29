package com.mygdx.game.client.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.client.hud.GameScreenHUD;
import com.mygdx.game.client.screen.Navigator;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.service.EndTurnService;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class GameScreenUiInputAdapter extends InputAdapter {

  private final EndTurnService endTurnService;
  private final GameScreenHUD gameScreenHUD;
  private final Lazy<Navigator> navigator;

  @Inject
  public GameScreenUiInputAdapter(
      EndTurnService endTurnService,
      GameScreenHUD gameScreenHUD,
      Lazy<Navigator> navigator
  ) {
    this.endTurnService = endTurnService;
    this.gameScreenHUD = gameScreenHUD;
    this.navigator = navigator;
  }

  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Input.Keys.T -> openTechnologyScreen();
      case Input.Keys.R -> endTurnService.endTurn();
      case Input.Keys.N -> gameScreenHUD.selectNextUnit();
      case Input.Keys.ESCAPE -> exitGame();
    }
    return false;
  }

  private void openTechnologyScreen() {
    navigator.get().changeToTechnologyScreen();
  }

  private void exitGame() {
    navigator.get().exit();
  }
}
