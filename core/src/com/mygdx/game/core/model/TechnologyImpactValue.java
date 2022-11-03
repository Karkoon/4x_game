package com.mygdx.game.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TechnologyImpactValue {

  private TechnologyImpactParameter parameter;
  private TechnologyImpactOperation operation;
  private int value;
}
