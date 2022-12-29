package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.MessageSender;

import javax.inject.Inject;

@GameInstanceScope
public class GameInterruptedService {

  private final MessageSender messageSender;

  @Inject
  public GameInterruptedService(
      MessageSender messageSender
  ) {
    this.messageSender = messageSender;
  }

  public void sendInterruptNotification() {
    messageSender.send("interrupt");
  }

}
