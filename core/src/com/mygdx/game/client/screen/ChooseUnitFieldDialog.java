package com.mygdx.game.client.screen;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.client.component.FieldComponent;
import com.mygdx.game.client.component.UnitComponent;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class ChooseUnitFieldDialog {

  private static final ComponentMapper<FieldComponent> fieldComponentMapper = ComponentMapper.getFor(FieldComponent.class);
  private static final ComponentMapper<UnitComponent> unitComponentMapper = ComponentMapper.getFor(UnitComponent.class);
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
        super.result(object);
        if (object.equals("field")) {
          log.log(Level.INFO, "Selected field.");
        } else if (object.equals("unit")) {
          log.log(Level.INFO, "Selected unit.");
        }
      }
    };

    FieldComponent fieldComponent = fieldComponentMapper.get(entity);
    customDialog.text("Choose on of the following:");
    customDialog.button("Field: " + fieldComponent.getName(), "field");

    if (fieldHasUnit(fieldComponent)) {
      UnitComponent unitComponent = unitComponentMapper.get(fieldComponent.getUnitEntity());
      customDialog.button("Unit: " + unitComponent.getName(), "unit");
    }
    customDialog.show(parentStage);
  }

  private static boolean fieldHasUnit(FieldComponent fieldComponent) {
    return fieldComponent.getUnitEntity() != null;
  }

}
