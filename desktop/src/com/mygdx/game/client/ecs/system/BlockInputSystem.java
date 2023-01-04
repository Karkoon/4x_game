package com.mygdx.game.client.ecs.system;

import com.artemis.BaseSystem;
import com.mygdx.game.client.model.ClickInput;
import com.mygdx.game.client.ui.NotPlayerTurnDialogFactory;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.ActiveToken;
import com.mygdx.game.client_core.model.PlayerInfo;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class BlockInputSystem extends BaseSystem {

  private final ActiveToken activeToken;
  private final ClickInput clickInput;
  private final NotPlayerTurnDialogFactory notPlayerTurnDialogFactory;
  private final PlayerInfo playerInfo;

  @Inject
  public BlockInputSystem(
      ActiveToken activeToken,
      ClickInput clickInput,
      NotPlayerTurnDialogFactory notPlayerTurnDialogFactory,
      PlayerInfo playerInfo
  ) {
    this.activeToken = activeToken;
    this.clickInput = clickInput;
    this.notPlayerTurnDialogFactory = notPlayerTurnDialogFactory;
    this.playerInfo = playerInfo;
  }

  @Override
  protected void processSystem() {
    if (!clickInput.isHandled()) {
      log.info("HANDLED");
      if (!playerIsActive()) {
        log.info("Blocked click");
        clickInput.clear();
        notPlayerTurnDialogFactory.createAndShow();
      }
    }
  }

  private boolean playerIsActive() {
    return activeToken.isActiveToken(playerInfo.getToken());
  }
}
