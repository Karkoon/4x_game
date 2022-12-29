package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.MessageSender;
import com.mygdx.game.core.model.BotType;
import com.mygdx.game.core.model.MapSize;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class GameConnectService {

  private final MessageSender messageSender;
  private final PlayerInfo playerInfo;

  @Inject
  public GameConnectService(
      MessageSender messageSender,
      PlayerInfo playerInfo
  ) {
    this.messageSender = messageSender;
    this.playerInfo = playerInfo;
  }

  public void connect(String gameRoomName, String userName, String isBot, Integer civId) {
    log.info("connect request sent");
    playerInfo.setUserName(userName);
    playerInfo.setCivilization(civId);
    messageSender.send("connect:" + playerInfo.getUserName() + ":" + playerInfo.getToken() + ":" + gameRoomName + ":" + civId + ":" + isBot);
  }

  public void changeUser() {
    log.info("change user request sent");
    messageSender.send("change_user:"  + playerInfo.getUserName() + ":" + playerInfo.getCivilization() + ":" + BotType.NOT_BOT.name());
  }

  public void changeUser(String userName, long civId, String botType) {
    log.info("change user request sent");
    messageSender.send("change_user:"  + userName + ":" + civId + ":" + botType);
  }

  public void changeLobby(MapSize selectedMapSize, int mapType) {
    var mapSize = selectedMapSize.name();
    log.info("change lobby request sent");
    messageSender.send("change_lobby:"  + mapSize + ":" + mapType);
  }

  public void removeUser(String userName) {
    log.info("remove user request sent");
    messageSender.send("remove_user:"  + userName);
  }

  public void addBot() {
    log.info("add bot");
    messageSender.send("add_bot");
  }
}
