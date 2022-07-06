package com.mygdx.game.server.initialize;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.server.ecs.entityfactory.SubFieldFactory;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.GameRoomSyncer;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@Singleton
public class SubMapInitializer {

  private final World world;
  private final SubFieldFactory subFieldFactory;
  private final GameScreenAssets assets;
  private final Random random = new Random();
  private final GameRoomSyncer syncer;
  private final ComponentMapper<SubField> subFieldMapper;

  private final ArrayList<Coordinates> coordinatesList = new ArrayList<>(Arrays.asList(
          new Coordinates(2,0),
          new Coordinates(1,1),
          new Coordinates(3,1),
          new Coordinates(0,2),
          new Coordinates(2,2),
          new Coordinates(4,2),
          new Coordinates(1,3),
          new Coordinates(3,3),
          new Coordinates(0,4),
          new Coordinates(2,4),
          new Coordinates(4,4),
          new Coordinates(1,5),
          new Coordinates(3,5),
          new Coordinates(0,6),
          new Coordinates(2,6),
          new Coordinates(4,6),
          new Coordinates(1,7),
          new Coordinates(3,7),
          new Coordinates(2,8)
  ));


  @Inject
  public SubMapInitializer(
          @NonNull World world,
          @NonNull SubFieldFactory subFieldFactory,
          @NonNull GameScreenAssets assets,
          @NonNull GameRoomSyncer gameRoomSyncer
  ) {
    this.world = world;
    this.subFieldFactory = subFieldFactory;
    this.assets = assets;
    this.syncer = gameRoomSyncer;
    this.subFieldMapper = world.getMapper(SubField.class);
  }

  public Array<Integer> initializeSubarea(int fieldId, Client owner) {
    Array<Integer> subFields = new Array<>();
    for (Coordinates coordinates : coordinatesList) {
      Integer entityId = subFieldFactory.createEntity(assets
                      .getGameConfigs()
                      .get(SubFieldConfig.class, random.nextInt(GameConfigs.SUBFIELD_MAX - GameConfigs.SUBFIELD_MIN) + GameConfigs.SUBFIELD_MIN),
              coordinates,
              owner);
      subFields.add(entityId);
      var subField = subFieldMapper.create(entityId);
      subField.setParent(fieldId);
      syncer.sendComponent(subField, entityId);
    }
    return subFields;
  }

}
