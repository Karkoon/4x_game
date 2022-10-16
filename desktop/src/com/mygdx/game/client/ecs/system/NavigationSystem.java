package com.mygdx.game.client.ecs.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.mygdx.game.client.ecs.component.NavigationDirection;
import com.mygdx.game.client.model.ChosenEntity;
import com.mygdx.game.client.screen.Navigator;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class NavigationSystem extends BaseSystem {

  private final ChosenEntity chosenEntity;
  private final Lazy<Navigator> navigator;
  private ComponentMapper<NavigationDirection> directionMapper;

  @Inject
  public NavigationSystem(
      final Lazy<Navigator> navigator,
      final ChosenEntity chosenEntity
  ) {
    this.navigator = navigator;
    this.chosenEntity = chosenEntity;
  }

  @Override
  protected void processSystem() {
    if (!chosenEntity.isAnyChosen() || !directionMapper.has(chosenEntity.peek())) {
      return;
    }
    var direction = directionMapper.get(chosenEntity.peek()).direction;
    this.navigator.get().changeTo(direction);
  }
}
