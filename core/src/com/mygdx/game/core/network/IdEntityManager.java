package com.mygdx.game.core.network;

import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.utils.Bag;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Helps manage entities in a listening-world (i.e. a client-side world).
 * todo use it for networking
 */
public class IdEntityManager extends Manager {
  private final Map<Integer, Entity> idToEntity = new HashMap<>();
  private final Bag<Integer> entityToId = new Bag<>();

  @Override
  public void deleted(@NonNull Entity e) {
    var id = entityToId.safeGet(e.getId());
    if (id == null) {
      return;
    }

    var oldEntity = idToEntity.get(id);
    if (oldEntity != null && oldEntity.equals(e)) {
      idToEntity.remove(id);
    }

    entityToId.set(e.getId(), null);
  }

  public Entity getEntity(int id) {
    return idToEntity.get(id);
  }

  /**
   * @param e Entity to get ID for
   * @return MAY RETURN NULL
   */
  public int getId(Entity e) {
    return entityToId.safeGet(e.getId());
  }

  public void setId(Entity e, int newId) {
    var oldId = entityToId.safeGet(e.getId());
    if (oldId != null) {
      idToEntity.remove(oldId);
    }

    idToEntity.put(newId, e);
    entityToId.set(e.getId(), newId);
  }
}
