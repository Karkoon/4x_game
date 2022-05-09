package com.mygdx.game.client.screen;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.component.NameComponent;
import com.mygdx.game.client.component.SlotComponent;
import com.mygdx.game.client.component.UnitMovementComp;
import com.mygdx.game.client.model.ActiveEntity;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class ChooseUnitFieldDialog {

  private static final ComponentMapper<SlotComponent> slotComponentMapper = ComponentMapper.getFor(SlotComponent.class);
  private static final ComponentMapper<NameComponent> nameComponentMapper = ComponentMapper.getFor(NameComponent.class);
  private static final ComponentMapper<UnitMovementComp> unitMovementCompMapper = ComponentMapper.getFor(UnitMovementComp.class);

  public static Dialog customDialog = null;

  public static void createCustomDialog(Stage parentStage, Entity entity, GameScreenAssets assets, ActiveEntity activeEntity) {
    if (customDialog != null) {
      customDialog.hide();
      customDialog = null;
    }
    Skin skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    var fieldSlotComponent = slotComponentMapper.get(entity);

    customDialog = new Dialog("Choose", skin) {
      @Override
      protected void result(Object object) {
        if (object.equals("field")) {
          activeEntity.setEntity(entity);
          log.log(Level.INFO, "Selected field.");
        } else if (object.equals("unit")) {
          var unitEntity = slotComponentMapper.get(entity).getUnitEntity();
          activeEntity.setEntity(unitEntity);
          var unitMovementComp = unitMovementCompMapper.get(unitEntity);
          unitMovementComp.setFromEntity(entity);
          log.log(Level.INFO, "Selected unit.");
        }
      }
    };

    var fieldNameComponent = nameComponentMapper.get(entity);
    customDialog.text("Choose on of the following:");
    customDialog.button("Field: " + fieldNameComponent.getName(), "field");

    if (fieldHasUnit(fieldSlotComponent)) {
      var unitNameComponent = nameComponentMapper.get(fieldSlotComponent.getUnitEntity());
      customDialog.button("Unit: " + unitNameComponent.getName(), "unit");
    }
    customDialog.show(parentStage);
  }

  private static boolean fieldHasUnit(SlotComponent slotComponent) {
    return slotComponent.getUnitEntity() != null;
  }

}