package com.mygdx.game.server.ecs;

import com.artemis.Component;
import com.artemis.World;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.mygdx.game.server.di.GameInstanceScope;

import javax.inject.Inject;

@GameInstanceScope
public class ComponentClassToIndexCache {

  private final World world;
  private final ObjectIntMap<Class<? extends Component>> cache = new ObjectIntMap<>();

  @Inject
  public ComponentClassToIndexCache(
      World world
  ) {
    this.world = world;
  }

  public Bits getIndicesFor(Class<? extends Component>[] classes) {
    var bits = new Bits(world.getComponentManager().getComponentTypes().size());
    for (int i = 0; i < classes.length; i++) {
      bits.set(getIndexFor(classes[i]));
    }
    return bits;
  }

  private int getIndexFor(Class<? extends Component> aClass) {
    var index = cache.get(aClass, -1);
    if (index == -1) {
      index = world.getComponentManager().getTypeFactory().getTypeFor(aClass).getIndex();
      cache.put(aClass, index);
    }
    return index;
  }
}
