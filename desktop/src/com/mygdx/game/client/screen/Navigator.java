package com.mygdx.game.client.screen;

public interface Navigator {
  void changeTo(Direction direction);

  void changeToGameScreen();

  void changeToFieldScreen();

  void changeToLoadingScreen();

  void changeToMenuScreen();

  void changeToAboutScreen();

  enum Direction {
    GAME_SCREEN,
    FIELD_SCREEN,
    LOADING_SCREEN,
    MENU_SCREEN,
    ABOUT_SCREEN
  }
}
