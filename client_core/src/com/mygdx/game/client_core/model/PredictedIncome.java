package com.mygdx.game.client_core.model;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.core.model.MaterialBase;
import lombok.Data;

import javax.inject.Inject;
import java.util.EnumMap;
import java.util.Map;

@GameInstanceScope
@Data
public class PredictedIncome {

  private Map<MaterialBase, Integer> incomes;

  @Inject
  public PredictedIncome() {
    this.incomes = new EnumMap<>(MaterialBase.class);
    for (var value : MaterialBase.values()) {
      incomes.put(value, 0);
    }
  }

  public void setIncome(MaterialBase base, Integer value) {
    incomes.put(base, value);
  }

}
