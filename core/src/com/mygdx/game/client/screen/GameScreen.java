package com.mygdx.game.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.assets.AssetPaths;
import com.mygdx.assets.Assets;
import com.mygdx.config.UnitConfig;
import com.mygdx.game.GameEngine;
import com.mygdx.game.client.CompositeUpdatable;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.entityfactory.UnitFactory;
import com.mygdx.game.client.input.CameraMoverInputProcessor;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameScreen extends ScreenAdapter {

    private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();

    private final Assets assets;
    private final GameEngine engine;

    private final Viewport viewport;
    private final UnitFactory unitFactory;

    private final ModelInstanceRenderer renderer;

    @Inject
    public GameScreen(@NonNull Assets assets,
                      @NonNull ModelInstanceRenderer renderer,
                      @NonNull GameEngine engine,
                      @NonNull Viewport viewport,
                      @NonNull UnitFactory unitFactory) {
        this.assets = assets;
        this.engine = engine;
        this.renderer = renderer;
        this.viewport = viewport;
        this.unitFactory = unitFactory;
    }

    private void positionCamera(@NonNull Camera camera) {
        camera.position.set(0, 100, 100);
        camera.lookAt(0, 0, 0);
    }

    @Override
    public void show() {
        assets.loadConfigs();
        assets.loadAssets();
        positionCamera(viewport.getCamera());

        compositeUpdatable.addUpdatable(engine);

        var inputProcessor = new CameraMoverInputProcessor(viewport);
        compositeUpdatable.addUpdatable(inputProcessor.getCameraControl());
        Gdx.input.setInputProcessor(inputProcessor);

        unitFactory.createUnit(assets.getGameConfigs().getAny(UnitConfig.class));
    }

    @Override
    public void render(float delta) {
        compositeUpdatable.update(delta);
        viewport.getCamera().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        assets.dispose();
    }

    private @NonNull ModelInstance createDebugBoxModelInstance() {
        var texture = assets.getTexture(AssetPaths.DEMO_TEXTURE_PATH);
        var model = new ModelBuilder().createBox(25, 25, 25,
                new Material(TextureAttribute.createDiffuse(texture)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
        return new ModelInstance(model, new Vector3(0, 0, 0));
    }
}
