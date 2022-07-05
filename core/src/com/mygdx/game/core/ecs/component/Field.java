package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.Array;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Field extends PooledComponent {
  private @NonNull Array<Integer> subFields = new Array<>();

  @Override
  protected void reset() {
    subFields = new Array<>();
  }

  public void add(Integer subFieldId) {
    this.subFields.add(subFieldId);
  }

}
