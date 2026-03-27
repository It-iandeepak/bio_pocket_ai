package com.nutrition.optimizer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeficiencyResponse {
    private List<String> symptoms;
    private List<String> detectedNutrientDeficiencies;
    private Map<String, List<String>> nutrientToFoods;   // nutrient → food suggestions
    private List<String> healthTips;
}
