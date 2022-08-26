package com.mygdx.game.client.screen;

public interface Navigator {
  void changeTo(Direction direction);

  void changeToGameScreen();

  void changeToFieldScreen();

  void changeToTechnologyScreen();

  void changeToLoadingScreen();

  void changeToMenuScreen();

  void changeToAboutScreen();

  void exit();

  void changeToGameRoomScreen();

  void changeToGameRoomListScreen();

  enum Direction {
    GAME_SCREEN,
    GAME_ROOM_SCREEN,
    GAME_ROOM_LIST_SCREEN,
    FIELD_SCREEN,
    TECHNOLOGY_SCREEN,
    LOADING_SCREEN,
    MENU_SCREEN,
    ABOUT_SCREEN,
    EXIT
  }
}
