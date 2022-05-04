package com.mygdx.game.client.screen;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.client.component.NameComponent;
import com.mygdx.game.client.component.SlotComponent;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class ChooseUnitFieldDialog {

  private static final ComponentMapper<SlotComponent> slotComponentMapper = ComponentMapper.getFor(SlotComponent.class);
  private static final ComponentMapper<NameComponent> nameComponentMapper = ComponentMapper.getFor(NameComponent.class);
  private static final Skin uiSkin = new Skin(Gdx.files.internal("dialog/custom/uiskin.json"));

  public static Dialog customDialog = null;

  public static void createCustomDialog(Stage parentStage, Entity entity) {
    if (customDialog != null) {
      customDialog.hide();
      customDialog = null;
    }

    customDialog = new Dialog("Choose", uiSkin) {
      @Override
      protected void result(Object object) {
        if (object.equals("field")) {
          log.log(Level.INFO, "Selected field.");
        } else if (object.equals("unit")) {
          log.log(Level.INFO, "Selected unit.");
        }
      }
    };

    var fieldSlotComponent = slotComponentMapper.get(entity);
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
