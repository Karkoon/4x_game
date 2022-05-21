package com.mygdx.game.client.ui;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.Name;
import com.mygdx.game.client.ecs.component.Slot;
import com.mygdx.game.client.modules.StageModule;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.badlogic.ashley.core.ComponentMapper.getFor;

@Log
@Singleton
public final class FieldClickedDialogFactory {

  private final GameScreenAssets assets;
  private final Stage stage;
  private final ComponentMapper<Slot> slotMapper = getFor(Slot.class);
  private final ComponentMapper<Name> nameMapper = getFor(Name.class);

  @Inject
  public FieldClickedDialogFactory(@NonNull GameScreenAssets assets,
                                   @NonNull @Named(StageModule.GAME_SCREEN) Stage stage) {
    this.assets = assets;
    this.stage = stage;
  }

  public void createAndShow(@NonNull Entity field, @NonNull EntityHandler handler) {
    var dialog = createEntityDialog(handler);
    dialog.text("Choose on of the following:");
    addFieldButton(dialog, field);
    addUnitButtons(dialog, field);
    dialog.show(stage);
    log.info("shown FieldClickedDialog");
  }

  private void addFieldButton(Dialog dialog, Entity field) {
    var fieldName = nameMapper.get(field);
    dialog.button("Field: " + fieldName.getName(), field);
  }

  private void addUnitButtons(Dialog dialog, Entity field) {
    var fieldSlot = slotMapper.get(field);
    var units = fieldSlot.getEntities();
    for (int i = 0; i < units.size; i++) {
      var unit = units.get(i);
      var unitName = nameMapper.get(unit);
      dialog.button("Unit: " + unitName.getName(), unit);
    }
  }

  private Dialog createEntityDialog(EntityHandler handler) {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    return new Dialog("Choose", skin) {
      @Override
      protected void result(Object object) {
        if (!(object instanceof Entity entity)) {
          throw new IllegalArgumentException("Passed object must be of Entity type but is " + object.getClass() + ".");
        }
        handler.handle(entity);
      }
    };
  }

  public interface EntityHandler {
    void handle(Entity entity);
  }
}
