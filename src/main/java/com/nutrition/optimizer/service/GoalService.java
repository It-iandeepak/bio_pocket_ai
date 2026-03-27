package com.nutrition.optimizer.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * GoalService — defines calorie + macro targets per Goal Mode and Role (Age group).
 * STEP 5: Goal Engine Logic.
 */
@Service
public class GoalService {

    public record GoalTarget(
            double minCalories, double maxCalories,
            double minProteinG,  double minCarbsG,  double minFiberG,  double minFatG,
            double minCalciumMg, double minIronMg,  double minVitCMg,
            String description,  List<String> focusNutrients
    ) {}

    // Key format: "GOAL_ROLE"
    private static final Map<String, GoalTarget> TARGETS = Map.ofEntries(
            // ── WEIGHT LOSS ──────────────────────────────────────────────────────
            Map.entry("WEIGHT_LOSS_ADULT",    new GoalTarget(1200,1500, 80,  130, 30, 35,  800, 18, 75, "Low-calorie, high-protein, high-fiber diet to promote satiety and fat loss.", List.of("Fiber","Protein","Potassium"))),
            Map.entry("WEIGHT_LOSS_CHILD",    new GoalTarget(1200,1400, 40,  150, 20, 30,  800, 10, 50, "Moderate calorie reduction with adequate growth nutrients.", List.of("Calcium","Protein","Vitamin D"))),
            Map.entry("WEIGHT_LOSS_PREGNANT", new GoalTarget(1800,2000, 71, 175, 28, 50, 1000, 27, 85, "Balanced calorie management while meeting pregnancy needs.", List.of("Folate","Iron","Calcium","Omega-3"))),
            Map.entry("WEIGHT_LOSS_ELDERLY",  new GoalTarget(1200,1400, 70,  130, 25, 35, 1200, 8, 75,  "Lean diet with emphasis on bone and muscle maintenance.", List.of("Calcium","Vitamin D","Protein","Magnesium"))),

            // ── MUSCLE GAIN ──────────────────────────────────────────────────────
            Map.entry("MUSCLE_GAIN_ADULT",    new GoalTarget(2200,2800, 140, 280, 35, 70,  800, 18, 75, "High-protein, high-calorie diet to support muscle growth and recovery.", List.of("Protein","Zinc","Magnesium","Vitamin B12"))),
            Map.entry("MUSCLE_GAIN_CHILD",    new GoalTarget(1800,2200, 60,  230, 25, 60,  800, 10, 50, "Adequate protein for muscle development with high carbs for energy.", List.of("Protein","Calcium","Iron"))),
            Map.entry("MUSCLE_GAIN_PREGNANT", new GoalTarget(2300,2600, 80,  250, 30, 60, 1200, 27, 85, "Protein-rich diet to support fetal development and maternal muscle.", List.of("Protein","Folate","Iron","Omega-3"))),
            Map.entry("MUSCLE_GAIN_ELDERLY",  new GoalTarget(2000,2400, 100, 240, 30, 60, 1200, 8, 75, "Higher protein to counter sarcopenia (muscle loss with age).", List.of("Protein","Calcium","Vitamin D","Magnesium"))),

            // ── MAINTENANCE ──────────────────────────────────────────────────────
            Map.entry("MAINTENANCE_ADULT",    new GoalTarget(1800,2200, 90,  225, 28, 60,  800, 18, 65, "Balanced macros to maintain current weight and health.", List.of("Fiber","Protein","Healthy Fats"))),
            Map.entry("MAINTENANCE_CHILD",    new GoalTarget(1600,1800, 52,  200, 19, 55,  800, 10, 45, "Well-rounded balanced diet for healthy growth.", List.of("Calcium","Iron","Vitamin A","Vitamin C"))),
            Map.entry("MAINTENANCE_PREGNANT", new GoalTarget(2200,2400, 71,  220, 28, 60, 1200, 27, 85, "Optimal macros to sustain pregnancy health and fetal growth.", List.of("Folate","Iron","Calcium","Omega-3","Protein"))),
            Map.entry("MAINTENANCE_ELDERLY",  new GoalTarget(1600,1900, 70,  190, 25, 55, 1200, 8, 75, "Moderate calories with high micronutrient density.", List.of("Calcium","Vitamin D","Protein","Fiber","Omega-3"))),

            // ── RECOVERY ─────────────────────────────────────────────────────────
            Map.entry("RECOVERY_ADULT",       new GoalTarget(2000,2500, 120, 250, 30, 65,  900, 18, 90, "Health-focused diet emphasizing anti-inflammatory and immunity-boosting foods.", List.of("Vitamin C","Zinc","Omega-3","Protein","Iron"))),
            Map.entry("RECOVERY_CHILD",       new GoalTarget(1600,2000, 60,  210, 22, 55,  900, 12, 60, "Immune-supportive diet rich in vitamins and minerals for healing.", List.of("Vitamin C","Zinc","Iron","Calcium"))),
            Map.entry("RECOVERY_PREGNANT",    new GoalTarget(2400,2600, 80,  260, 30, 65, 1200, 27, 90, "Recovery-focused with extra folate and omega-3 for fetal repair.", List.of("Folate","Iron","Calcium","Omega-3","Vitamin C"))),
            Map.entry("RECOVERY_ELDERLY",     new GoalTarget(1800,2200, 80,  210, 25, 55, 1200, 10, 90, "Anti-inflammatory diet to speed recovery and maintain bone/muscle health.", List.of("Vitamin D","Calcium","Omega-3","Zinc","Protein")))
    );

    public GoalTarget getTarget(String goal, String role) {
        String key = goal.toUpperCase() + "_" + role.toUpperCase();
        return TARGETS.getOrDefault(key, TARGETS.get("MAINTENANCE_ADULT"));
    }

    public Map<String, Double> getTargetAsMap(String goal, String role) {
        GoalTarget t = getTarget(goal, role);
        return Map.of(
                "minCalories",  t.minCalories(),
                "maxCalories",  t.maxCalories(),
                "minProteinG",  t.minProteinG(),
                "minCarbsG",    t.minCarbsG(),
                "minFiberG",    t.minFiberG(),
                "minFatG",      t.minFatG(),
                "minCalciumMg", t.minCalciumMg(),
                "minIronMg",    t.minIronMg(),
                "minVitCMg",    t.minVitCMg()
        );
    }

    public List<String> getSupportedGoals() {
        return List.of("WEIGHT_LOSS", "MUSCLE_GAIN", "MAINTENANCE", "RECOVERY");
    }

    public List<String> getSupportedRoles() {
        return List.of("ADULT", "CHILD", "PREGNANT", "ELDERLY");
    }

    /**
     * Compute a weighted nutrition score (0-100) based on how close
     * the accumulated nutrients are to goal targets.
     */
    public int computeNutritionScore(Map<String, Double> nutrients, GoalTarget target) {
        double proteinScore  = Math.min(100, nutrients.getOrDefault("protein", 0.0) / target.minProteinG() * 100);
        double fiberScore    = Math.min(100, nutrients.getOrDefault("fiber",   0.0) / target.minFiberG()   * 100);
        double calorieScore  = computeCalorieScore(nutrients.getOrDefault("calories", 0.0), target.minCalories(), target.maxCalories());
        double calciumScore  = Math.min(100, nutrients.getOrDefault("calcium", 0.0) / target.minCalciumMg() * 100);
        double ironScore     = Math.min(100, nutrients.getOrDefault("iron",    0.0) / target.minIronMg()    * 100);
        double vitCScore     = Math.min(100, nutrients.getOrDefault("vitaminC",0.0) / target.minVitCMg()    * 100);

        // Weighted average: protein 30%, calories 25%, fiber 20%, micronutrients 25%
        return (int) (proteinScore * 0.30 + calorieScore * 0.25 + fiberScore * 0.20
                + calciumScore * 0.08 + ironScore * 0.08 + vitCScore * 0.09);
    }

    private double computeCalorieScore(double cal, double minCal, double maxCal) {
        if (cal >= minCal && cal <= maxCal) return 100.0;
        if (cal < minCal)  return (cal / minCal) * 100;
        // Over target
        double over = cal - maxCal;
        return Math.max(0, 100 - (over / maxCal) * 100);
    }
}
