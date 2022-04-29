package com.mygdx.game.client.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.client.model.Coordinates;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class FieldComponent implements Component {

  private Entity unitEntity;
  @NonNull
  private Coordinates coordinates;
  @NonNull
  private String name;
}
