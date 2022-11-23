package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.mygdx.game.client.di.DaggerGameFactory;
import com.mygdx.game.client.model.WindowConfig;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
  public static void main(String[] arg) {
    var config = new Lwjgl3ApplicationConfiguration();
    config.setWindowListener(new Lwjgl3WindowAdapter() {
       @Override
       public void created(final Lwjgl3Window window) {
          WindowConfig.windowHandle = window.getWindowHandle();
       }
     });
    config.setForegroundFPS(60);
    new Lwjgl3Application(DaggerGameFactory.create().providesGame(), config);
  }
}
