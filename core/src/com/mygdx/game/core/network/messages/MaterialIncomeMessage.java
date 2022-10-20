package com.mygdx.game.core.network.messages;

import com.mygdx.game.core.model.MaterialBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialIncomeMessage {

  Map<MaterialBase, Integer> incomes;

}
