package com.mygdx.game.client.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SlotComponent implements Component {

  private Entity unitEntity;
}
