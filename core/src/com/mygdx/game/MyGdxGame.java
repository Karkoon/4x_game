package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.CompositeUpdatable;
import com.mygdx.game.client.input.CameraMoverInputProcessor;
import lombok.NonNull;

public class MyGdxGame extends ApplicationAdapter {

    CompositeUpdatable compositeUpdatable = new CompositeUpdatable();
    Viewport viewport;
    ModelBatch batch;
    Texture img;
    ModelInstance modelInstance;
    ModelInstance fieldInstance;
    AssetManager assets;
    Model field;

    private @NonNull Viewport createViewport() {
        Camera camera = new OrthographicCamera(300, 300);
        camera.near = 1f;
        camera.far = 300f;
        return new ExtendViewport(300, 300, camera);
    }

    private @NonNull ModelInstance createModelInstance() {
        Model model = new ModelBuilder().createBox(25, 25, 25,
                new Material(TextureAttribute.createDiffuse(img)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
        return new ModelInstance(model, new Vector3(0, 0, 0));
    }

    @Override
    public void create() {
        viewport = createViewport();

        viewport.getCamera().position.set(0, 100, 100);
        viewport.getCamera().lookAt(-100, 0, 0);

        CameraMoverInputProcessor inputProcessor = new CameraMoverInputProcessor(viewport);
        compositeUpdatable.addUpdatable(inputProcessor.getCameraControl());
        Gdx.input.setInputProcessor(inputProcessor);

        img = new Texture("badlogic.jpg");
        assets = new AssetManager();
        assets.load("field.g3db", Model.class);
        assets.finishLoading();
        field = assets.get("field.g3db", Model.class);
        modelInstance = createModelInstance();
        fieldInstance = new ModelInstance(field, new Vector3(0, 0, 0));

        batch = new ModelBatch();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1, true);
        compositeUpdatable.update();
        viewport.getCamera().update();
        batch.begin(viewport.getCamera());
        batch.render(modelInstance);
        batch.render(fieldInstance);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        field.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
