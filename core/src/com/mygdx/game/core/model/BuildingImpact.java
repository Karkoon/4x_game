package com.mygdx.game.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BuildingImpact {

  private BuildingType buildingType;
  private List<BuildingImpactValue> buildingImpactValues;
}
