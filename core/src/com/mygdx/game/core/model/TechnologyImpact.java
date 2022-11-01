package com.mygdx.game.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TechnologyImpact {

  private TechnologyImpactType technologyImpactType;
  private List<TechnologyImpactValue> technologyImpactValues;

}
