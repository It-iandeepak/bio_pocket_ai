package com.nutrition.optimizer.entity;

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
public class MealPlan {
    private String goal;
    private String role;
    private double totalCost;
    private double budgetUsedPercent;
    private List<MealItem> mealItems;
    private Map<String, Double> totalNutrients;    // summed after cooking
    private List<String> deficienciesDetected;
    private List<String> healthSuggestions;
    private List<String> foodCombinations;         // synergy tips
    private Map<String, Double> goalTargets;       // calorie/macro targets
    private String nutritionScore;                 // e.g. "82/100"
}
