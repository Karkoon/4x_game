package com.mygdx.game.client.initialize;

import com.mygdx.game.assets.Assets;
import com.mygdx.game.client.entityfactory.FieldFactory;
import com.mygdx.game.client.model.Field;
import com.mygdx.game.config.FieldConfig;

import java.util.ArrayList;
import java.util.List;

public class MapInitializer {

    public static List<Field> initializeMap(FieldFactory fieldFactory, Assets assets) {
        return initializeMap(InitializeParameters.INITIAL_WIDTH, InitializeParameters.INITIAL_HEIGHT, fieldFactory, assets);
    }

    public static List<Field> initializeMap(int width, int height, FieldFactory fieldFactory, Assets assets) {
        List<Field> fieldList = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Field field = fieldFactory.createEntity(assets.getGameConfigs().getAny(FieldConfig.class), i, j);
                fieldList.add(field);
            }
        }
        return fieldList;
    }
}
