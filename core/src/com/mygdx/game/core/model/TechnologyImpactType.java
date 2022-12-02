package com.mygdx.game.core.model;

public enum TechnologyImpactType {

  UNIT_IMPACT("Unit technology"),
  BUILDING_IMPACT("Building technology"),
  MATERIAL_IMPACT("Material technology");

  public final String name;

  TechnologyImpactType(String name) {
    this.name = name;
  }
}
