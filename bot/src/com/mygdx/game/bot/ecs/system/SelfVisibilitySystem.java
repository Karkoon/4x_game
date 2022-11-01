package com.mygdx.game.bot.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.bot.ecs.component.Visible;
import com.mygdx.game.bot.model.InField;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.Owner;
import lombok.extern.java.Log;

import javax.inject.Inject;

@All({Coordinates.class, Owner.class, Field.class})
@Log
@GameInstanceScope
public class SelfVisibilitySystem extends IteratingSystem {

  private final InField inField;
  private final PlayerInfo playerInfo;
  private ComponentMapper<Visible> visibleMapper;
  private ComponentMapper<Owner> ownerMapper;

  @Inject
  public SelfVisibilitySystem(
      InField inField,
      PlayerInfo playerInfo
  ) {
    super();
    this.inField = inField;
    this.playerInfo = playerInfo;
  }

  @Override
  protected void process(int perceivable) {
    if (inField.isInField()) return;
    visibleMapper.set(perceivable, true);
  }
}
