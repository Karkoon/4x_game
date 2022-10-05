package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import com.mygdx.game.core.model.MaterialUnit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class MaterialIncomeComponent extends PooledComponent {

  private List<MaterialUnit> materialIncomes = new ArrayList<>();

  @Override
  protected void reset() {
    materialIncomes = new ArrayList<>();
  }
}