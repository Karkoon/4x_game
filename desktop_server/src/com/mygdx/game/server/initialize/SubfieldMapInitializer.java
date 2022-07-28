package com.mygdx.game.server.initialize;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.ecs.entityfactory.SubFieldFactory;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.GameRoomSyncer;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Random;

@Singleton
public class SubfieldMapInitializer {

  private static final List<Coordinates> coordinatesList = List.of(
      new Coordinates(2, 0),
      new Coordinates(1, 1),
      new Coordinates(3, 1),
      new Coordinates(0, 2),
      new Coordinates(2, 2),
      new Coordinates(4, 2),
      new Coordinates(1, 3),
      new Coordinates(3, 3),
      new Coordinates(0, 4),
      new Coordinates(2, 4),
      new Coordinates(4, 4),
      new Coordinates(1, 5),
      new Coordinates(3, 5),
      new Coordinates(0, 6),
      new Coordinates(2, 6),
      new Coordinates(4, 6),
      new Coordinates(1, 7),
      new Coordinates(3, 7),
      new Coordinates(2, 8)
  );
  private final ComponentFactory componentFactory;
  private final SubFieldFactory subFieldFactory;
  private final GameConfigAssets assets;
  private final Random random = new Random();
  private final GameRoomSyncer syncer;
  private final ComponentMapper<SubField> subFieldMapper;

  @Inject
  public SubfieldMapInitializer(
          @NonNull World world,
          @NonNull ComponentFactory componentFactory,
          @NonNull SubFieldFactory subFieldFactory,
          @NonNull GameConfigAssets assets,
          @NonNull GameRoomSyncer gameRoomSyncer
  ) {
    this.componentFactory = componentFactory;
    this.subFieldFactory = subFieldFactory;
    this.assets = assets;
    this.syncer = gameRoomSyncer;
    this.subFieldMapper = world.getMapper(SubField.class);
  }

  public IntArray initializeSubarea(int fieldId, Client owner) {
    var subFields = new IntArray();
    for (Coordinates coordinates : coordinatesList) {
      int entityId = componentFactory.createEntityId();

      componentFactory.createCoordinateComponent(coordinates, entityId);
      subFieldFactory.createEntity(entityId, assets
              .getGameConfigs()
              .get(SubFieldConfig.class, random.nextInt(GameConfigs.SUBFIELD_MAX - GameConfigs.SUBFIELD_MIN) + GameConfigs.SUBFIELD_MIN),
          owner);
      componentFactory.createSubFieldComponent(fieldId, entityId);

      subFields.add(entityId);
    }
    return subFields;
  }

}
