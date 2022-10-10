package com.mygdx.game.client.ecs.system;

import com.artemis.BaseSystem;
import com.mygdx.game.client.model.ClickInput;
import com.mygdx.game.client.ui.NotPlayerTurnDialogFactory;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.ActiveToken;
import com.mygdx.game.client_core.model.PlayerInfo;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class BlockInputSystem extends BaseSystem {

  private final PlayerInfo playerInfo;
  private final ActiveToken activeToken;
  private final ClickInput clickInput;
  private final NotPlayerTurnDialogFactory notPlayerTurnDialogFactory;

  @Inject
  public BlockInputSystem(
      @NonNull PlayerInfo playerInfo,
      @NonNull ActiveToken activeToken,
      @NonNull ClickInput clickInput,
      @NonNull NotPlayerTurnDialogFactory notPlayerTurnDialogFactory
  ) {
    this.playerInfo = playerInfo;
    this.activeToken = activeToken;
    this.clickInput = clickInput;
    this.notPlayerTurnDialogFactory = notPlayerTurnDialogFactory;
  }

  @Override
  protected void processSystem() {
    if (!clickInput.isHandled()) {
      log.info("HANDLED");
      if (activeToken.isActiveToken(playerInfo.getToken())) {
        log.info("Blocked click");
        clickInput.clear();
        notPlayerTurnDialogFactory.createAndShow();
      }
    }
  }
}
