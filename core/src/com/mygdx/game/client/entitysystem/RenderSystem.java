package com.mygdx.game.client.entitysystem;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.component.ModelInstanceComponent;
import com.mygdx.game.client.component.PositionComponent;
import lombok.NonNull;

import javax.inject.Inject;

public class RenderSystem extends IteratingSystem {

    private static final Family RENDER_SYSTEM_FAMILY = Family.one(ModelInstanceComponent.class).get();

    private final ComponentMapper<ModelInstanceComponent> modelInstanceMapper = ComponentMapper.getFor(ModelInstanceComponent.class);
    private final ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private final ModelInstanceRenderer renderer;

    @Inject
    public RenderSystem(@NonNull ModelInstanceRenderer renderer) {
        super(RENDER_SYSTEM_FAMILY);
        this.renderer = renderer;
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        var modelInstance = modelInstanceMapper.get(entity).getModelInstance();
        var position = positionMapper.get(entity).getPosition();
        modelInstance.transform.setTranslation(position);
        renderer.addToCache(modelInstance);
    }
}
