package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.assets.AssetPaths;
import com.mygdx.assets.Assets;
import com.mygdx.game.client.CompositeUpdatable;
import com.mygdx.game.client.input.CameraMoverInputProcessor;
import lombok.NonNull;
import lombok.extern.java.Log;

@Log
public class MyGdxGame extends ApplicationAdapter {

    private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();
    private Viewport viewport;
    private ModelBatch batch;
    private Texture img;
    private ModelInstance modelInstance;
    private ModelInstance fieldModelInstance;
    private ModelInstance unitModelInstance;
    private Assets assets;

    private @NonNull Viewport createViewport() {
        Camera camera = new PerspectiveCamera(66, 300, 300);
        camera.near = 1f;
        camera.far = 300f;
        return new ExtendViewport(300, 300, camera);
    }

    private @NonNull ModelInstance createDebugBoxModelInstance() {
        var model = new ModelBuilder().createBox(25, 25, 25,
                new Material(TextureAttribute.createDiffuse(img)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
        return new ModelInstance(model, new Vector3(0, 0, 0));
    }

    private @NonNull ModelInstance createModelInstance(Model model) {
        return new ModelInstance(model, new Vector3(0, 0, 0));
    }

    @Override
    public void create() {
        viewport = createViewport();
        assets = new Assets();
        viewport.getCamera().position.set(0, 100, 100);
        viewport.getCamera().lookAt(0, 0, 0);

        var inputProcessor = new CameraMoverInputProcessor(viewport);
        compositeUpdatable.addUpdatable(inputProcessor.getCameraControl());
        Gdx.input.setInputProcessor(inputProcessor);


        assets.loadConfig();
        assets.loadAssets();

        img = assets.getTexture(AssetPaths.DEMO_TEXTURE_PATH);
        modelInstance = createDebugBoxModelInstance();

        var fieldConfig = assets.getGameContentService().getAnyField();
        fieldModelInstance = createModelInstance(assets.getModel(fieldConfig));
        fieldModelInstance.transform.set(new Vector3(100, 0, 0), new Quaternion());

        var unitConfig = assets.getGameContentService().getAnyUnit();
        unitModelInstance = createModelInstance(assets.getModel(unitConfig));
        unitModelInstance.transform.set(new Vector3(-100, 0, 0),
                new Quaternion(),
                new Vector3(0.5f, 0.5f, 0.5f));

        batch = new ModelBatch();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1, true);
        compositeUpdatable.update();
        viewport.getCamera().update();
        batch.begin(viewport.getCamera());
        batch.render(modelInstance);
        batch.render(fieldModelInstance);
        batch.render(unitModelInstance);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        assets.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
