package com.mygdx.game.client.entityfactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.assets.Assets;
import com.mygdx.config.FieldConfig;
import com.mygdx.config.UnitConfig;
import com.mygdx.game.client.component.ModelInstanceComponent;
import com.mygdx.game.client.component.PositionComponent;
import com.mygdx.game.client.model.Field;
import com.mygdx.game.client.model.Unit;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Level;

@Singleton
@Log
public class FieldFactory extends EntityFactory<FieldConfig>{

    @Inject
    public FieldFactory(@NonNull Engine engine, @NonNull Assets assets) {
        super(engine, assets);
    }

    @Override
    public @NonNull Field createEntity(@NonNull FieldConfig config, int x, int y) {
        var entity = engine.createEntity();
        var positionComponent = engine.createComponent(PositionComponent.class);
        positionComponent.setPosition(new Vector3(x * 115 + (y%2) * 55, 0, y * 105));
        entity.add(positionComponent);
        var modelInstanceComponent = engine.createComponent(ModelInstanceComponent.class);
        modelInstanceComponent.setModelInstanceFromModel(assets.getModel(config));
        entity.add(modelInstanceComponent);
        engine.addEntity(entity);
        log.log(Level.INFO, "Added a field.");
        return new Field(0,0, config);
    }

}
