package com.mygdx.game.client.ui;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.ecs.component.Highlighted;
import com.mygdx.game.core.ecs.component.Name;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Log
@Singleton
public final class ChooseEntityDialogFactory {

  private final GameScreenAssets assets;
  private final Stage stage;

  private ComponentMapper<Highlighted> highlightedComponentMapper;
  private ComponentMapper<Name> nameComponentMapper;

  @Inject
  public ChooseEntityDialogFactory(
      GameScreenAssets assets,
      @Named(StageModule.GAME_SCREEN) Stage stage,
      World world
  ) {
    this.assets = assets;
    this.stage = stage;
    world.inject(this);
  }

  public void createAndShow(IntBag entities, @NonNull EntityHandler handler) {
    var dialog = createEntityDialog(handler);
    dialog.text("Choose on of the following:");
    addEntityButtons(dialog, entities);
    dialog.show(stage);
    log.info("shown FieldClickedDialog");
  }

  private void addEntityButtons(Dialog dialog, IntBag entities) {
    for (int i = 0; i < entities.size(); i++) {
      var entity = entities.get(i);
      var name = nameComponentMapper.get(entity).getName();
      var highlighted = highlightedComponentMapper.has(entity) ? "Selected: " : "";
      dialog.button(highlighted + name, entity);
    }
  }

  private Dialog createEntityDialog(EntityHandler handler) {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    return new Dialog("Choose", skin) {
      @Override
      protected void result(Object object) {
        if (!(object instanceof Integer entity)) {
          throw new IllegalArgumentException("Passed object must be of Entity type but is " + object.getClass() + ".");
        }
        handler.handle(entity);
      }
    };
  }

  public interface EntityHandler {
    void handle(Integer entity);
  }
}
