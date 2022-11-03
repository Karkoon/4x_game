package com.mygdx.game.core.model;

public enum BuildingImpactParameter {

  // materials impact

  GOLD("Gold"),
  FOOD("Food"),
  SCIENCE("Science"),
  PRODUCTION("Production"),

  // recruitment impact
  RECRUIT("recruit");

  public final String name;

  BuildingImpactParameter(String name) {
    this.name = name;
  }
}
