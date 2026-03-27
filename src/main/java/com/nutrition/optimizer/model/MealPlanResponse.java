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
public class MealPlanResponse {
    private boolean success;
    private String goal;
    private String role;
    private double budgetInr;
    private double totalCostUsed;
    private double budgetLeftover;
    private String nutritionScore;                // e.g. "86/100"

    private List<MealItemDTO> mealItems;
    private Map<String, Double> totalNutrients;   // after cooking
    private Map<String, Double> goalTargets;      // calorie/macro targets for goal
    private List<String> deficienciesDetected;
    private List<String> healthSuggestions;
    private List<String> foodCombinations;        // synergy combos tip

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealItemDTO {
        private String foodName;
        private boolean vegetarian;
        private double quantityG;
        private double cost;
        private String cookingMethod;
        private String recommendation;     // RAW or COOKED reason
        private Map<String, Double> nutrientsAfterCooking;
        private String reason;
    }
}
