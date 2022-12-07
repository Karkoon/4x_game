package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.bot.di.DaggerGameFactory;
import com.mygdx.game.bot.model.InitialBotRoom;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class BotLauncher {
  public static void main(String[] arg) {
    if (arg.length == 3) {
      InitialBotRoom.roomName = arg[0];
      InitialBotRoom.botType = arg[1];
      InitialBotRoom.botCiv = Integer.valueOf(arg[2]);
    }
    var config = new Lwjgl3ApplicationConfiguration();
    config.setForegroundFPS(60);
    new Lwjgl3Application(DaggerGameFactory.create().providesGame(), config);
  }
}
