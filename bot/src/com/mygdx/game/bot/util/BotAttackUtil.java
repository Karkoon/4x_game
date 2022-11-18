package com.mygdx.game.bot.util;

import com.artemis.World;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class BotAttackUtil {

  private final World world;

  @Inject
  public BotAttackUtil (
    World world
  ) {
    this.world = world;
    world.inject(this);
  }
}
