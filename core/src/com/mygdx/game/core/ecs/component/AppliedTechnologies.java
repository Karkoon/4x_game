package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class AppliedTechnologies extends PooledComponent {

  private List<Integer> technologies = new ArrayList<>();

  @Override
  protected void reset() {
    technologies = new ArrayList<>();
  }

  public void add(int technologyConfigId) {
    this.technologies.add(technologyConfigId);
  }
}
