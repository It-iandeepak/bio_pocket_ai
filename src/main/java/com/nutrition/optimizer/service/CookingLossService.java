package com.nutrition.optimizer.service;

import com.nutrition.optimizer.entity.FoodItem;
import com.nutrition.optimizer.entity.Nutrient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CookingLossService â€” STEP 4.
 * Applies nutrient retention factors based on cooking method.
 * Retention factors are based on USDA / ICMR cooking loss studies.
 */
@Service
public class CookingLossService {

    // Retention rates per cooking method: nutrient â†’ retention (0.0â€“1.0)
    // Format: method â†’ { nutrientKey â†’ retentionFactor }
    private static final Map<String, Map<String, Double>> RETENTION = Map.of(
        "RAW", Map.ofEntries(
            Map.entry("vitaminC", 1.00), Map.entry("folate", 1.00), Map.entry("vitaminB12", 1.00),
            Map.entry("vitaminA", 1.00), Map.entry("iron", 1.00), Map.entry("calcium", 1.00),
            Map.entry("protein", 1.00), Map.entry("carbs", 1.00), Map.entry("fiber", 1.00),
            Map.entry("fat", 1.00), Map.entry("magnesium", 1.00), Map.entry("zinc", 1.00),
            Map.entry("potassium",1.00), Map.entry("calories", 1.00), Map.entry("omega3", 1.00)
        ),
        "BOIL", Map.ofEntries(
            Map.entry("vitaminC", 0.50), Map.entry("folate", 0.55), Map.entry("vitaminB12", 0.80),
            Map.entry("vitaminA", 0.85), Map.entry("iron", 0.90), Map.entry("calcium", 0.88),
            Map.entry("protein", 0.85), Map.entry("carbs", 0.95), Map.entry("fiber", 0.90),
            Map.entry("fat", 0.90), Map.entry("magnesium", 0.80), Map.entry("zinc", 0.85),
            Map.entry("potassium",0.70), Map.entry("calories", 0.92), Map.entry("omega3", 0.78)
        ),
        "STEAM", Map.ofEntries(
            Map.entry("vitaminC", 0.75), Map.entry("folate", 0.70), Map.entry("vitaminB12", 0.85),
            Map.entry("vitaminA", 0.90), Map.entry("iron", 0.95), Map.entry("calcium", 0.95),
            Map.entry("protein", 0.92), Map.entry("carbs", 0.97), Map.entry("fiber", 0.95),
            Map.entry("fat", 0.92), Map.entry("magnesium", 0.90), Map.entry("zinc", 0.90),
            Map.entry("potassium",0.85), Map.entry("calories", 0.95), Map.entry("omega3", 0.85)
        ),
        "FRY", Map.ofEntries(
            Map.entry("vitaminC", 0.35), Map.entry("folate", 0.45), Map.entry("vitaminB12", 0.75),
            Map.entry("vitaminA", 0.95), Map.entry("iron", 0.92), Map.entry("calcium", 0.90),
            Map.entry("protein", 0.88), Map.entry("carbs", 0.92), Map.entry("fiber", 0.82),
            Map.entry("fat", 1.20), Map.entry("magnesium", 0.78), Map.entry("zinc", 0.85),
            Map.entry("potassium",0.75), Map.entry("calories", 1.20), Map.entry("omega3", 0.60)
        ),
        "BAKE", Map.ofEntries(
            Map.entry("vitaminC", 0.45), Map.entry("folate", 0.50), Map.entry("vitaminB12", 0.80),
            Map.entry("vitaminA", 0.88), Map.entry("iron", 0.93), Map.entry("calcium", 0.90),
            Map.entry("protein", 0.90), Map.entry("carbs", 0.97), Map.entry("fiber", 0.92),
            Map.entry("fat", 0.95), Map.entry("magnesium", 0.85), Map.entry("zinc", 0.88),
            Map.entry("potassium",0.80), Map.entry("calories", 0.98), Map.entry("omega3", 0.72)
        )
    );

    /**
     * Returns BEFORE and AFTER cooking nutrient list for a given FoodItem + cooking method.
     * Quantities relative to 100g.
     */
    public List<Nutrient> applyRetention(FoodItem food, String cookingMethod) {
        Map<String, Double> factors = RETENTION.getOrDefault(cookingMethod.toUpperCase(), RETENTION.get("STEAM"));

        List<Nutrient> nutrients = new ArrayList<>();
        nutrients.add(nutrient("Calories (kcal)", food.getCaloriesKcal(), "kcal", factors.get("calories")));
        nutrients.add(nutrient("Protein",         food.getProteinG(),    "g",    factors.get("protein")));
        nutrients.add(nutrient("Carbs",            food.getCarbsG(),      "g",    factors.get("carbs")));
        nutrients.add(nutrient("Fiber",            food.getFiberG(),      "g",    factors.get("fiber")));
        nutrients.add(nutrient("Fat",              food.getFatG(),        "g",    factors.get("fat")));
        nutrients.add(nutrient("Calcium",          food.getCalciumMg(),   "mg",   factors.get("calcium")));
        nutrients.add(nutrient("Iron",             food.getIronMg(),      "mg",   factors.get("iron")));
        nutrients.add(nutrient("Vitamin C",        food.getVitaminCMg(),  "mg",   factors.get("vitaminC")));
        nutrients.add(nutrient("Vitamin A",        food.getVitaminAMcg(), "mcg",  factors.get("vitaminA")));
        nutrients.add(nutrient("Vitamin B12",      food.getVitaminB12Mcg(),"mcg", factors.get("vitaminB12")));
        nutrients.add(nutrient("Magnesium",        food.getMagnesiumMg(), "mg",   factors.get("magnesium")));
        nutrients.add(nutrient("Potassium",        food.getPotassiumMg(), "mg",   factors.get("potassium")));
        nutrients.add(nutrient("Zinc",             food.getZincMg(),      "mg",   factors.get("zinc")));
        nutrients.add(nutrient("Folate",           food.getFolateMcg(),  "mcg",  factors.get("folate")));
        nutrients.add(nutrient("Omega-3",          food.getOmega3G(),     "g",    factors.get("omega3")));
        return nutrients;
    }

    /** Returns the recommended cooking method for max nutrition retention. */
    public String recommendCookingMethod(FoodItem food) {
        String bestAs  = food.getBestEatenAs();
        String bestCook= food.getBestCookingMethod();
        if ("RAW".equalsIgnoreCase(bestAs)) {
            return "RAW â€” eating this food raw preserves maximum nutrients (" +
                    food.getName() + " retains 100% of Vitamin C and folate raw vs " +
                    retentionPct("vitaminC", "BOIL") + "% when boiled).";
        }
        if ("BOTH".equalsIgnoreCase(bestAs)) {
            return "BOTH OK â€” " + food.getName() +
                    " can be eaten raw for max vitamins or " + bestCook.toLowerCase() +
                    "ed for easier digestion. Steaming is preferred over boiling to retain more nutrients.";
        }
        return "COOKED (" + bestCook + ") â€” light " + bestCook.toLowerCase() +
                "ing is best for " + food.getName() +
                ". Steaming preserves ~" + retentionPct("vitaminC", "STEAM") + "% of Vitamin C vs " +
                retentionPct("vitaminC", "FRY") + "% when fried.";
    }

    private int retentionPct(String nutrient, String method) {
        return (int) (RETENTION.get(method).getOrDefault(nutrient, 1.0) * 100);
    }

    private Nutrient nutrient(String name, double raw, String unit, double factor) {
        double cooked = Math.round(raw * factor * 100.0) / 100.0;
        return Nutrient.builder()
                .name(name).rawAmount(raw).cookedAmount(cooked)
                .unit(unit).retentionRate(factor).build();
    }

    public Map<String, Integer> getRetentionSummary(String cookingMethod) {
        Map<String, Double> factors = RETENTION.getOrDefault(cookingMethod.toUpperCase(), RETENTION.get("STEAM"));
        Map<String, Integer> summary = new java.util.LinkedHashMap<>();
        factors.forEach((k, v) -> summary.put(k, (int)(v * 100)));
        return summary;
    }
}

