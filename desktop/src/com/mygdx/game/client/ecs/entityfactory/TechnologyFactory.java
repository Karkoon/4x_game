package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.TextureComp;
import com.mygdx.game.client_core.ecs.component.Name;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.client_core.ecs.entityfactory.EntityFactory;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.core.ecs.component.Technology;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class TechnologyFactory extends EntityFactory<TechnologyConfig> {

  private final ComponentMapper<Name> nameMapper;
  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<TextureComp> textureMapper;
  private final ComponentMapper<Technology> technologyMapper;

  @Inject
  public TechnologyFactory(@NonNull World world,
                         @NonNull GameScreenAssets assets) {
    super(world, assets);
    this.nameMapper = world.getMapper(Name.class);
    this.positionMapper = world.getMapper(Position.class);
    this.textureMapper = world.getMapper(TextureComp.class);
    this.technologyMapper = world.getMapper(Technology.class);
  }

  @Override
  public @NonNull void createEntity(TechnologyConfig config, int entity) {
    setUpName(config, entity);
    setUpPosition(config, entity);
    setUpTexture(config, entity);
    setUpTechnology(config, entity);
  }

  private void setUpName(@NonNull TechnologyConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }

  private void setUpPosition(TechnologyConfig config, int entity) {
    var position = positionMapper.create(entity);
    Vector3 vector = new Vector3(config.getX()*100, config.getY()*100, config.getY()*100);
    position.setPosition(vector);
  }

  private void setUpTechnology(TechnologyConfig config, int entity) {
    technologyMapper.create(entity);
  }

  private void setUpTexture(TechnologyConfig config, int entity) {
    var texture = textureMapper.create(entity);
    texture.setTexture(assets.getTexture(config.getTextureName()));
  }
}
