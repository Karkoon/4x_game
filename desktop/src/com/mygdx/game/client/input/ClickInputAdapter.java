package com.mygdx.game.client.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.model.ClickInput;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class ClickInputAdapter extends InputAdapter {

  private final Viewport viewport;
  private final ClickInput clickInput;

  @Inject
  public ClickInputAdapter(
      @NonNull Viewport viewport,
      @NonNull ClickInput clickInput
  ) {
    this.viewport = viewport;
    this.clickInput = clickInput;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    var ray = viewport.getPickRay(screenX, screenY);
    clickInput.setValue(ray);
    return true;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    clickInput.clear();
    return true;
  }
}
