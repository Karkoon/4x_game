package com.mygdx.game.bot.ecs.system;

import com.artemis.BaseSystem;
import com.mygdx.game.bot.model.ClickInput;
import com.mygdx.game.bot.ui.NotPlayerTurnDialogFactory;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.ActiveToken;
import com.mygdx.game.client_core.model.PlayerInfo;
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
      PlayerInfo playerInfo,
      ActiveToken activeToken,
      ClickInput clickInput,
      NotPlayerTurnDialogFactory notPlayerTurnDialogFactory
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
