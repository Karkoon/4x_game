package com.mygdx.game.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TechnologyImpactValue {

  private TechnologyImpactParameter parameter;
  private ImpactOperation operation;
  private int value;
}
