package com.mygdx.game.client_core.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.SubField;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class SubFieldFactory {

  private ComponentMapper<Name> nameMapper;
  private ComponentMapper<Position> positionMapper;
  private ComponentMapper<SubField> subFieldMapper;

  @Inject
  public SubFieldFactory(
      @NonNull World world
  ) {
    world.inject(this);
  }

  public @NonNull void createEntity(SubFieldConfig config, int entity) {
    setUpName(config, entity);
    subFieldMapper.create(entity);
    positionMapper.create(entity);
  }

  private void setUpName(@NonNull SubFieldConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }
}
