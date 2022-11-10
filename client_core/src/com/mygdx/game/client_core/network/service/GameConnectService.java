package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.network.MessageSender;
import com.mygdx.game.core.model.MapSize;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class GameConnectService {

  private final MessageSender sender;
  private final PlayerInfo playerInfo;

  @Inject
  public GameConnectService(
      MessageSender sender,
      PlayerInfo playerInfo
  ) {
    this.sender = sender;
    this.playerInfo = playerInfo;
  }

  public void connect(String gameRoomName, String userName) {
    log.info("connect request sent");
    playerInfo.setUserName(userName);
    sender.send("connect:" + playerInfo.getUserName() + ":" + playerInfo.getToken() + ":" + gameRoomName + ":" + playerInfo.getCivilization());
  }

  public void changeUser() {
    log.info("change user request sent");
    sender.send("change_user:"  + playerInfo.getCivilization());
  }

  public void changeLobby(MapSize selectedMapSize, int mapType) {
    var mapSize = selectedMapSize.name();
    log.info("change lobby request sent");
    sender.send("change_lobby:"  + mapSize + ":" + mapType);
  }

}
