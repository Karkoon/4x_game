package com.mygdx.game.client.ecs.system;

import com.artemis.BaseSystem;
import com.mygdx.game.client.model.ClickInput;
import com.mygdx.game.client.ui.NotPlayerTurnDialogFactory;
import com.mygdx.game.client_core.model.PlayerInfo;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class BlockInputSystem extends BaseSystem {

  private final PlayerInfo playerInfo;
  private final ClickInput clickInput;
  private final NotPlayerTurnDialogFactory notPlayerTurnDialogFactory;

  @Inject
  public BlockInputSystem(
      @NonNull PlayerInfo playerInfo,
      @NonNull ClickInput clickInput,
      @NonNull NotPlayerTurnDialogFactory notPlayerTurnDialogFactory
  ) {
    this.playerInfo = playerInfo;
    this.clickInput = clickInput;
    this.notPlayerTurnDialogFactory = notPlayerTurnDialogFactory;
  }

  @Override
  protected void processSystem() {
    if (!clickInput.isHandled()) {
      log.info("HANDLED");
      if (!playerInfo.isPlayerTurn()) {
        log.info("Blocked click");
        clickInput.clear();
        notPlayerTurnDialogFactory.createAndShow();
      }
    }
  }
}