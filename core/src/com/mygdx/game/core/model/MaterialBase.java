package com.mygdx.game.core.model;

public enum MaterialBase {

  GOLD("Gold", "Żywność", "icons/gold_icon.png"),
  FOOD("Food", "Jedzenie", "icons/food_icon.png"),
  SCIENCE("Science", "Nauka", "icons/science_icon.png"),
  PRODUCTION("Production", "Produkcja", "icons/production_icon.png");

  public final String name;
  public final String polishName;
  public final String iconPath;

  private MaterialBase(String name, String polishName, String iconPath) {
    this.name = name;
    this.polishName = polishName;
    this.iconPath = iconPath;
  }
}
