package com.mygdx.game.client.screen;

public interface Navigator {
  void changeTo(Direction direction);

  void changeToGameScreen();

  void changeToFieldScreen();

  void changeToLoadingScreen();

  void changeToMenuScreen();

  void changeToAboutScreen();

  void exit();

  enum Direction {
    GAME_SCREEN,
    FIELD_SCREEN,
    TECHNOLOGY_SCREEN,
    LOADING_SCREEN,
    MENU_SCREEN,
    ABOUT_SCREEN,
    EXIT
  }
}
