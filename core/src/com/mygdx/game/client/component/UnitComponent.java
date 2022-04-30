package com.mygdx.game.client.component;

import com.badlogic.ashley.core.Component;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
public class UnitComponent implements Component {

  @NonNull
  private String name;
}
