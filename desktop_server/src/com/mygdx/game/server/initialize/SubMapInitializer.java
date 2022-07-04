package com.mygdx.game.server.initialize;

import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.ecs.entityfactory.SubFieldFactory;
import com.mygdx.game.server.model.Client;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SubMapInitializer {

  private final SubFieldFactory subFieldFactory;
  private final GameScreenAssets assets;
  private final Random random = new Random();

  private final ArrayList<Coordinates> coordinatesList = (ArrayList<Coordinates>) Arrays.asList(
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
  );


  @Inject
  public SubMapInitializer(
          @NonNull SubFieldFactory subFieldFactory,
          @NonNull GameScreenAssets assets
  ) {
    this.subFieldFactory = subFieldFactory;
    this.assets = assets;
  }

  public void initializeSubarea(Client owner) {

    for (Coordinates coordinates : coordinatesList) {
      subFieldFactory.createEntity(assets
                      .getGameConfigs()
                      .get(SubFieldConfig.class, random.nextInt(GameConfigs.SUBFIELD_MAX - GameConfigs.SUBFIELD_MIN) + GameConfigs.SUBFIELD_MIN + 1),
              coordinates,
              owner);
    }
  }
}
