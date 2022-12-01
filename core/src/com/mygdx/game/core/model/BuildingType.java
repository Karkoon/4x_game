package com.mygdx.game.core.model;

public enum BuildingType {

  MATERIALS_BUILDING("Material building"),
  RECRUITMENT_BUILDING("Recruit building");

  public final String name;

  BuildingType(String name) {
    this.name = name;
  }
}
