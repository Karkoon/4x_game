package com.mygdx.game.client.ui;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.Name;
import com.mygdx.game.client.ecs.component.Slot;
import com.mygdx.game.client.ecs.component.UnitMovement;
import com.mygdx.game.client.model.ActiveEntity;
import lombok.extern.java.Log;

import java.util.logging.Level;

import static com.badlogic.ashley.core.ComponentMapper.getFor;

@Log
public final class ChooseUnitFieldDialog {

  private ChooseUnitFieldDialog() {
  }

  private static final ComponentMapper<Slot> slotMapper = getFor(Slot.class);
  private static final ComponentMapper<Name> nameMapper = getFor(Name.class);
  private static final ComponentMapper<UnitMovement> unitMovementCompMapper = getFor(UnitMovement.class);

  public static Dialog customDialog = null;

  public static void createCustomDialog(Stage parentStage, Entity entity, GameScreenAssets assets, ActiveEntity activeEntity) {
    if (customDialog != null) {
      customDialog.hide();
      customDialog = null;
    }
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    var fieldSlotComponent = slotMapper.get(entity);

    customDialog = new Dialog("Choose", skin) {
      @Override
      protected void result(Object object) {
        if (object.equals("field")) {
          activeEntity.setEntity(entity);
          log.log(Level.INFO, "Selected field.");
        } else if (object.equals("unit")) {
          var unitEntity = slotMapper.get(entity).getUnitEntity();
          activeEntity.setEntity(unitEntity);
          var unitMovementComp = unitMovementCompMapper.get(unitEntity);
          unitMovementComp.setFromEntity(entity);
          log.log(Level.INFO, "Selected unit.");
        }
      }
    };

    var fieldNameComponent = nameMapper.get(entity);
    customDialog.text("Choose on of the following:");
    customDialog.button("Field: " + fieldNameComponent.getName(), "field");

    if (fieldHasUnit(fieldSlotComponent)) {
      var unitNameComponent = nameMapper.get(fieldSlotComponent.getUnitEntity());
      customDialog.button("Unit: " + unitNameComponent.getName(), "unit");
    }
    customDialog.show(parentStage);
  }

  private static boolean fieldHasUnit(Slot slot) {
    return slot.getUnitEntity() != null;
  }

}
