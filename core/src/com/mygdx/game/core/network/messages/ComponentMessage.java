package com.mygdx.game.core.network.messages;

import com.artemis.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentMessage<T extends Component> {
  private T component;
  private int entityId;
}
