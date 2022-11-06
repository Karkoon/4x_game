package com.mygdx.game.bot.screen;

import com.artemis.World;
import com.badlogic.gdx.ScreenAdapter;
import com.mygdx.game.bot.hud.InfieldHUD;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.service.ShowSubfieldService;
import com.mygdx.game.core.util.CompositeUpdatable;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class FieldScreen extends ScreenAdapter {

  private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();

  private final World world;

  private final InfieldHUD infieldHUD;
  private final ShowSubfieldService showSubfieldService;

  private int fieldParent = -1;

  @Inject
  public FieldScreen(
      World world,
      InfieldHUD infieldHUD,
      ShowSubfieldService showSubfieldService
  ) {
    this.world = world;
    world.inject(this);
    this.infieldHUD = infieldHUD;
    this.showSubfieldService = showSubfieldService;
  }

  @Override
  public void show() {
    log.info("SubArea shown");
    showSubfieldService.flipSubscriptionState(fieldParent);
    infieldHUD.prepareHudSceleton();
  }

  @Override
  public void render(float delta) {
    compositeUpdatable.update(delta);
    world.setDelta(delta);
    world.process();
  }

  @Override
  public void hide() {
    showSubfieldService.flipSubscriptionState(fieldParent);
    fieldParent = -1;
  }

}
