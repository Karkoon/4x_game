package com.mygdx.game.bot.model;

import com.mygdx.game.core.model.BotType;
import lombok.Data;

import javax.inject.Inject;
import javax.inject.Singleton;

@Data
@Singleton
public class ChosenBotType {

  private BotType botType;

  @Inject
  public ChosenBotType(
  ) {
    this.botType = BotType.RANDOM_FIRST;
  }
}
