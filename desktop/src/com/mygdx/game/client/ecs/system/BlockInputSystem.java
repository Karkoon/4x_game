package com.mygdx.game.client.ecs.system;

import com.artemis.BaseSystem;
import com.mygdx.game.client.model.ClickInput;
import com.mygdx.game.client.ui.NotActivePlayerDialogFactory;
import com.mygdx.game.client_core.model.ActivePlayerInfo;
import com.mygdx.game.client_core.model.PlayerInfo;

import javax.inject.Inject;

public class BlockInputSystem extends BaseSystem {

  private final ActivePlayerInfo activePlayerInfo;
  private final PlayerInfo playerInfo;
  private final ClickInput clickInput;
  private final NotActivePlayerDialogFactory notActivePlayerDialogFactory;

  @Inject
  public BlockInputSystem(
      ActivePlayerInfo activePlayerInfo,
      PlayerInfo playerInfo,
      ClickInput clickInput,
      NotActivePlayerDialogFactory notActivePlayerDialogFactory
  ) {
    this.activePlayerInfo = activePlayerInfo;
    this.playerInfo = playerInfo;
    this.clickInput = clickInput;
    this.notActivePlayerDialogFactory = notActivePlayerDialogFactory;
  }

  @Override
  protected void processSystem() {
    if (!clickInput.isHandled()) {
      var activePlayerUsername = activePlayerInfo.getUsername();
      var playerUsername = playerInfo.getUserName();
      if (!activePlayerUsername.equals(playerUsername)) {
        clickInput.clear();
        notActivePlayerDialogFactory.createAndShow();
      }
    }
  }
}
