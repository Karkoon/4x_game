package com.mygdx.game.client_core.model;

import com.artemis.World;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.core.model.MaterialBase;
import lombok.Data;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@GameInstanceScope
@Data
public class PredictedIncome {

  private Map<MaterialBase, Integer> incomes;

  @Inject
  public PredictedIncome(
  ) {
    this.incomes = new HashMap<>();
    for (MaterialBase value : MaterialBase.values()) {
      incomes.put(value, 0);
    }
  }

}
