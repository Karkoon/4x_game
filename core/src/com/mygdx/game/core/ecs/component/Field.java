package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.IntArray;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Field extends PooledComponent {

  private @NonNull IntArray subFields = new IntArray();

  @Override
  protected void reset() {
    subFields = new IntArray();
  }

  public void add(int subFieldId) {
    this.subFields.add(subFieldId);
  }

}
