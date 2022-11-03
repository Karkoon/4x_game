package com.mygdx.game.core.model;

import java.util.function.BiFunction;

public enum TechnologyImpactOperation {

  PLUS(Float::sum),
  MINUS((x,y) -> x - y),
  MULTIPLE((x,y) -> x * y),
  DIVIDE((x,y) -> x / y),
  NONE((x,y) -> null);

  public final BiFunction<Float, Float, Float> function;

  TechnologyImpactOperation(BiFunction<Float, Float, Float> function) {
    this.function = function;
  }

}
