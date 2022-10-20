package com.mygdx.game.config;

import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.core.model.MaterialUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingConfig implements Config, ModelConfig {

  private long id;
  @NonNull
  private String name;
  @NonNull
  private String polishName;
  @NonNull
  private String modelPath;
  @NonNull
  private String textureName;
  @NonNull
  private String iconName;
  private int turnAmount;
  @NonNull
  private List<MaterialUnit> materials;

  public Map<MaterialBase, MaterialUnit> getMaterials() {
    var parsedEntries = new EnumMap<MaterialBase, MaterialUnit>(MaterialBase.class);
    materials.forEach(entry -> parsedEntries.put(entry.getBase(), entry));
    return parsedEntries;
  }
}
