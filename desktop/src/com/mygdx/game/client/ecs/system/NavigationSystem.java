package com.mygdx.game.client.ecs.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.mygdx.game.client.ecs.component.NavigationDirection;
import com.mygdx.game.client.model.ChosenEntity;
import com.mygdx.game.client.screen.Navigator;
import com.mygdx.game.client.ui.WarningDialogFactory;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.core.ecs.component.Owner;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class NavigationSystem extends BaseSystem {

  private final ChosenEntity chosenEntity;
  private final Lazy<Navigator> navigator;
  private final PlayerInfo playerInfo;
  private final WarningDialogFactory warningDialog;

  private ComponentMapper<NavigationDirection> directionMapper;
  private ComponentMapper<Owner> ownerMapper;

  @Inject
  public NavigationSystem(
      final ChosenEntity chosenEntity,
      final Lazy<Navigator> navigator,
      PlayerInfo playerInfo,
      WarningDialogFactory warningDialog
  ) {
    this.chosenEntity = chosenEntity;
    this.navigator = navigator;
    this.playerInfo = playerInfo;
    this.warningDialog = warningDialog;
  }

  @Override
  protected void processSystem() {
    if (!chosenEntity.isAnyChosen() || !directionMapper.has(chosenEntity.peek())) {
      return;
    }
    var chosenEntityId = chosenEntity.peek();
    var owner = ownerMapper.get(chosenEntityId);
    if (owner == null || !playerInfo.getToken().equals(owner.getToken())){
      warningDialog.createAndShow("Subfield restricted!",
              "You cannot access this subfield! You are not an owner!");
      chosenEntity.pop();
    }
    else {
      var direction = directionMapper.get(chosenEntityId).direction;
      this.navigator.get().changeTo(direction);
    }
  }
}
