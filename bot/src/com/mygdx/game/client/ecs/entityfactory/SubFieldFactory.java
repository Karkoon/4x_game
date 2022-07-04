package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.SubField;
import com.mygdx.game.config.SubFieldConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class SubFieldFactory extends EntityFactory<SubFieldConfig> {

  private final ComponentMapper<SubField> subFieldMapper;

  @Inject
  public SubFieldFactory(@NonNull World world,
                         @NonNull GameScreenAssets assets) {
    super(world, assets);
    this.subFieldMapper = world.getMapper(SubField.class);
  }

  @Override
  public @NonNull void createEntity(SubFieldConfig config, int entity) {
    setUpSubField(config, entity);
  }

  private void setUpSubField(SubFieldConfig config, int entityId) {
    var field = subFieldMapper.create(entityId);
  }

}
