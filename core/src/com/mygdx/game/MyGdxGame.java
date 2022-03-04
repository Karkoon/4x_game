package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
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
import lombok.NonNull;

public class MyGdxGame extends ApplicationAdapter {

	Viewport viewport;
	ModelBatch batch;
	Texture img;
	ModelInstance modelInstance;

	private @NonNull Viewport createViewport() {
		PerspectiveCamera camera = new PerspectiveCamera(67, 300, 300);
		camera.near = 1f;
		camera.far = 300f;
		return new ExtendViewport(300, 300, camera);
	}

	private @NonNull ModelInstance createModelInstance() {
		Model model = new ModelBuilder().createBox(10, 10, 10,
				new Material(TextureAttribute.createDiffuse(img)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);
		return new ModelInstance(model, new Vector3(0, 0, 0));
	}

	@Override
	public void create () {
		viewport = createViewport();

		viewport.getCamera().position.set(25, 25, 25);
		viewport.getCamera().lookAt(0, 0, 0);

		img = new Texture("badlogic.jpg");
		modelInstance = createModelInstance();

		batch = new ModelBatch();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);
		batch.begin(viewport.getCamera());
		batch.render(modelInstance);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}
