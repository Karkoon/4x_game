package com.mygdx.game.client.model;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.core.ecs.component.Owner;

import javax.inject.Inject;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static com.badlogic.gdx.graphics.Color.*;

public class OwnerToColorMap {

  @Inject
  public OwnerToColorMap() {
    super();
  }

  private Map<String, Color> map = new HashMap<>();

  public Color get(Owner owner) {
    var color = map.get(owner.getToken());
    if (color == null) {
      color = colors.poll();
      map.put(owner.getToken(), color);
    }
    return color;
  }

  private final Queue<Color> colors = new ArrayDeque<>(
      List.of(YELLOW, ROYAL,
      SKY, CYAN, TEAL, GREEN,
      BLUE, ORANGE, BROWN, TAN,
      FIREBRICK, RED, PINK, PURPLE));
}
