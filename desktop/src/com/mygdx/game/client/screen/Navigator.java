package com.mygdx.game.client.screen;

public interface Navigator {

  void changeTo(Direction direction);

  void changeToGameScreen();

  void changeToFieldScreen();

  void changeToTechnologyScreen();

  void exit();

  enum Direction {
    GAME_SCREEN,
    FIELD_SCREEN,
    TECHNOLOGY_SCREEN,
    EXIT
  }
}
