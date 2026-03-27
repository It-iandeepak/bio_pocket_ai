package com.nutrition.optimizer.service;

import com.nutrition.optimizer.entity.FoodItem;
import com.nutrition.optimizer.entity.MealItem;
import com.nutrition.optimizer.entity.Nutrient;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * OptimizationService — STEP 2.
 * Implements a budget-constrained, multi-objective greedy Knapsack optimizer.
 *
 * Algorithm:
 * 1. Filter foods by diet type (veg/non-veg) and optionally by deficiency priorities.
 * 2. Score each food by: (nutritionDensity × goalWeight) / cost.
 * 3. Sort by score descending.
 * 4. Greedily pick foods until budget is exhausted.
 * 5. Each food is assigned a standard serving of 100g.
 */
@Service
public class OptimizationService {

    private final CookingLossService cookingLossService;

    public OptimizationService(CookingLossService cookingLossService) {
        this.cookingLossService = cookingLossService;
    }

    public record OptimizationRequest(
            List<FoodItem> candidateFoods,
            double budget,
            String goal,
            String role,
            String cookingMethod,
            List<String> priorityNutrients,   // from deficiency or nutrientQuery
            GoalService.GoalTarget goalTarget
    ) {}

    public record OptimizationResult(
            List<MealItem> selectedItems,
            double totalCost,
            Map<String, Double> totalNutrients
    ) {}

    public OptimizationResult optimize(OptimizationRequest req) {
        List<FoodItem> foods = req.candidateFoods();
        double budget = req.budget();
        String cookingMethod = req.cookingMethod() != null ? req.cookingMethod() : "STEAM";

        // 1. Score all candidate foods
        List<ScoredFood> scored = new ArrayList<>();
        for (FoodItem food : foods) {
            double score = computeScore(food, req.goal(), req.priorityNutrients());
            scored.add(new ScoredFood(food, score));
        }

        // 2. Sort by score descending (greedy knapsack)
        scored.sort((a, b) -> Double.compare(b.score(), a.score()));

        // 3. Greedy selection — 100g standard serving per food item
        List<MealItem> selected = new ArrayList<>();
        double spent = 0.0;
        Map<String, Double> accumulated = initNutrientMap();

        for (ScoredFood sf : scored) {
            FoodItem food = sf.food();
            double costFor100g = food.getCostPer100g();
            if (spent + costFor100g > budget) continue;

            // Apply cooking loss
            List<Nutrient> nutrients = cookingLossService.applyRetention(food, cookingMethod);
            String rawVsCookedReason = cookingLossService.recommendCookingMethod(food);

            MealItem item = MealItem.builder()
                    .foodItem(food)
                    .quantityG(100.0)
                    .actualCost(costFor100g)
                    .nutrients(nutrients)
                    .cookingMethod(cookingMethod)
                    .reason(buildReason(food, req.goal(), req.priorityNutrients(), rawVsCookedReason))
                    .build();

            selected.add(item);
            spent += costFor100g;
            accumulateNutrients(accumulated, nutrients);

            if (spent >= budget * 0.95) break; // stop when 95% budget used
        }

        return new OptimizationResult(selected, spent, accumulated);
    }

    // ─── SCORING LOGIC ─────────────────────────────────────────────────────────
    private double computeScore(FoodItem food, String goal, List<String> priorityNutrients) {
        double cost = food.getCostPer100g();
        if (cost <= 0) cost = 0.1; // prevent division by zero for free items

        // Base nutrition density = calories + protein*4 + fiber*2 (rough nutrient score)
        double nutritionDensity = food.getCaloriesKcal() * 0.01
                + food.getProteinG() * 3.0
                + food.getFiberG()   * 2.0
                + food.getIronMg()   * 5.0
                + food.getCalciumMg()* 0.05
                + food.getVitaminCMg()* 0.5
                + food.getOmega3G() * 10.0
                + food.getVitaminAMcg() * 0.01;

        // Goal-specific bonus
        switch (goal.toUpperCase()) {
            case "WEIGHT_LOSS" -> {
                nutritionDensity += food.getFiberG() * 3.0;
                if (food.getCaloriesKcal() < 100) nutritionDensity += 15;
                if (food.getCaloriesKcal() > 300) nutritionDensity -= 10;
            }
            case "MUSCLE_GAIN" -> {
                nutritionDensity += food.getProteinG() * 5.0;
                nutritionDensity += food.getZincMg()   * 2.0;
                nutritionDensity += food.getMagnesiumMg() * 0.1;
            }
            case "RECOVERY" -> {
                nutritionDensity += food.getVitaminCMg() * 1.0;
                nutritionDensity += food.getZincMg()     * 3.0;
                nutritionDensity += food.getOmega3G()    * 15.0;
            }
            case "MAINTENANCE" -> {
                // balanced — no extra bonus
            }
        }

        // Priority nutrient bonus (from deficiency detection)
        if (priorityNutrients != null) {
            for (String nutrient : priorityNutrients) {
                nutritionDensity += getPriorityBonus(food, nutrient);
            }
        }

        // Absorption-adjusted score
        nutritionDensity *= food.getAbsorptionRate();

        return nutritionDensity / cost;
    }

    private double getPriorityBonus(FoodItem food, String nutrient) {
        return switch (nutrient.toLowerCase()) {
            case "iron"       -> food.getIronMg()       * 8.0;
            case "protein"    -> food.getProteinG()      * 5.0;
            case "calcium"    -> food.getCalciumMg()     * 0.1;
            case "vitamin b12"-> food.getVitaminB12Mcg() * 50.0;
            case "vitamin c"  -> food.getVitaminCMg()    * 1.5;
            case "vitamin a"  -> food.getVitaminAMcg()   * 0.05;
            case "omega-3"    -> food.getOmega3G()        * 20.0;
            case "folate"     -> food.getFolateMcg()     * 0.1;
            case "zinc"       -> food.getZincMg()         * 6.0;
            case "magnesium"  -> food.getMagnesiumMg()    * 0.2;
            case "fiber"      -> food.getFiberG()         * 4.0;
            case "potassium"  -> food.getPotassiumMg()    * 0.02;
            default           -> 0.0;
        };
    }

    // ─── NUTRIENT ACCUMULATION ─────────────────────────────────────────────────
    private Map<String, Double> initNutrientMap() {
        Map<String, Double> m = new LinkedHashMap<>();
        m.put("calories", 0.0); m.put("protein", 0.0); m.put("carbs", 0.0);
        m.put("fiber", 0.0); m.put("fat", 0.0); m.put("calcium", 0.0);
        m.put("iron", 0.0); m.put("vitaminC", 0.0); m.put("vitaminA", 0.0);
        m.put("vitaminB12", 0.0); m.put("magnesium", 0.0); m.put("potassium", 0.0);
        m.put("zinc", 0.0); m.put("folate", 0.0); m.put("omega3", 0.0);
        return m;
    }

    private void accumulateNutrients(Map<String, Double> acc, List<Nutrient> nutrients) {
        Map<String, String> nameToKey = Map.of(
                "Calories (kcal)", "calories", "Protein", "protein", "Carbs", "carbs",
                "Fiber", "fiber", "Fat", "fat", "Calcium", "calcium",
                "Iron", "iron", "Vitamin C", "vitaminC", "Vitamin A", "vitaminA",
                "Vitamin B12", "vitaminB12"
        );
        Map<String, String> nameToKey2 = Map.of(
                "Magnesium", "magnesium", "Potassium", "potassium",
                "Zinc", "zinc", "Folate", "folate", "Omega-3", "omega3"
        );
        for (Nutrient n : nutrients) {
            String key = nameToKey.getOrDefault(n.getName(), nameToKey2.get(n.getName()));
            if (key != null) acc.merge(key, n.getCookedAmount(), Double::sum);
        }
    }

    private String buildReason(FoodItem food, String goal, List<String> priority, String rawCooked) {
        StringBuilder sb = new StringBuilder();
        sb.append("Selected for ").append(goal).append(" goal: ");
        if (food.getProteinG() > 15) sb.append("High protein (").append(food.getProteinG()).append("g). ");
        if (food.getFiberG() > 5)    sb.append("High fiber (").append(food.getFiberG()).append("g). ");
        if (food.getIronMg() > 3)    sb.append("Iron-rich (").append(food.getIronMg()).append("mg). ");
        if (food.getCaloriesKcal() < 60) sb.append("Low calorie (").append(food.getCaloriesKcal()).append(" kcal). ");
        sb.append(rawCooked);
        return sb.toString();
    }

    private record ScoredFood(FoodItem food, double score) {}
}
