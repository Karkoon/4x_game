package com.mygdx.game.client;

public interface Navigator {
  void changeToGameScreen();
  void changeToAboutScreen();
  void changeToLoadingScreen();
  void changeToMenuScreen();
  void exit();
}
