package com.nutrition.optimizer.service;

import com.nutrition.optimizer.entity.FoodItem;
import com.nutrition.optimizer.entity.Nutrient;
import com.nutrition.optimizer.model.NutrientTargetRequest;
import com.nutrition.optimizer.model.NutrientTargetResponse;
import com.nutrition.optimizer.model.NutrientTargetResponse.FoodOption;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.ToDoubleFunction;

/**
 * NutrientTargetService — nutrient-target-first optimization mode.
 *
 * <p><b>Cooking logic:</b>
 * <ul>
 *   <li>bestEatenAs = "RAW"  → nutrients taken as-is (no loss). e.g. fruits, guava, banana</li>
 *   <li>bestEatenAs = "COOKED" → CookingLossService retention applied using bestCookingMethod</li>
 *   <li>bestEatenAs = "BOTH" → shown as "RAW preferred" (raw values used, max nutrients)</li>
 * </ul>
 * </p>
 *
 * Algorithm:
 * 1. Filter by diet type.
 * 2. For EACH food: compute effective (post-cooking) nutrient values.
 * 3. Score by total target-nutrient-per-rupee (using effective values).
 * 4. Greedy pick until all targets met.
 * 5. Return foods sorted low → high cost with full breakdown.
 */
@Service
public class NutrientTargetService {

    private final FoodDataService    foodDataService;
    private final CookingLossService cookingLossService;

    /** Maps response-facing nutrient key → CookingLossService Nutrient.name */
    private static final Map<String, String> KEY_TO_NUTRIENT_NAME = Map.ofEntries(
            Map.entry("fiber",      "Fiber"),
            Map.entry("protein",    "Protein"),
            Map.entry("calories",   "Calories (kcal)"),
            Map.entry("calcium",    "Calcium"),
            Map.entry("iron",       "Iron"),
            Map.entry("vitaminC",   "Vitamin C"),
            Map.entry("vitaminA",   "Vitamin A"),
            Map.entry("vitaminB12", "Vitamin B12"),
            Map.entry("folate",     "Folate"),
            Map.entry("zinc",       "Zinc"),
            Map.entry("magnesium",  "Magnesium"),
            Map.entry("potassium",  "Potassium"),
            Map.entry("omega3",     "Omega-3"),
            Map.entry("fat",        "Fat"),
            Map.entry("carbs",      "Carbs")
    );

    /** Maps nutrient key → raw value extractor from FoodItem */
    private static final Map<String, ToDoubleFunction<FoodItem>> RAW_EXTRACTORS = Map.ofEntries(
            Map.entry("fiber",      FoodItem::getFiberG),
            Map.entry("protein",    FoodItem::getProteinG),
            Map.entry("calories",   FoodItem::getCaloriesKcal),
            Map.entry("calcium",    FoodItem::getCalciumMg),
            Map.entry("iron",       FoodItem::getIronMg),
            Map.entry("vitaminC",   FoodItem::getVitaminCMg),
            Map.entry("vitaminA",   FoodItem::getVitaminAMcg),
            Map.entry("vitaminB12", FoodItem::getVitaminB12Mcg),
            Map.entry("folate",     FoodItem::getFolateMcg),
            Map.entry("zinc",       FoodItem::getZincMg),
            Map.entry("magnesium",  FoodItem::getMagnesiumMg),
            Map.entry("potassium",  FoodItem::getPotassiumMg),
            Map.entry("omega3",     FoodItem::getOmega3G),
            Map.entry("fat",        FoodItem::getFatG),
            Map.entry("carbs",      FoodItem::getCarbsG)
    );

    public NutrientTargetService(FoodDataService foodDataService, CookingLossService cookingLossService) {
        this.foodDataService    = foodDataService;
        this.cookingLossService = cookingLossService;
    }

    public NutrientTargetResponse findCheapestToMeetTargets(NutrientTargetRequest req) {

        Map<String, Double> targets = req.getNutrientTargets() == null ? Map.of() : req.getNutrientTargets();
        double budgetCeiling = req.getMaxBudgetInr() != null ? req.getMaxBudgetInr() : Double.MAX_VALUE;

        // ── 1. Filter by diet ───────────────────────────────────────────────────
        List<FoodItem> candidates = "VEG".equalsIgnoreCase(req.getDietType())
                ? foodDataService.getVegFoods()
                : foodDataService.getAllFoods();

        // ── 2. Compute effective (post-cooking) nutrient map for each food ──────
        //       Key: food → { nutrientKey → effectiveAmount }
        Map<FoodItem, Map<String, Double>> effectiveNutrients = new LinkedHashMap<>();
        Map<FoodItem, String> effectiveCookingMethod = new LinkedHashMap<>();

        for (FoodItem food : candidates) {
            String actualMethod = resolveMethod(food);
            effectiveCookingMethod.put(food, actualMethod);
            effectiveNutrients.put(food, buildEffectiveMap(food, actualMethod, targets.keySet()));
        }

        // ── 3. Sort by: highest target-nutrient/₹ score (descending) ────────────
        List<FoodItem> sorted = new ArrayList<>(candidates);
        sorted.sort((a, b) -> {
            double scoreA = nutrientPerRupee(a, targets, effectiveNutrients);
            double scoreB = nutrientPerRupee(b, targets, effectiveNutrients);
            return Double.compare(scoreB, scoreA);
        });

        // ── 4. Greedy selection until all targets met ────────────────────────────
        List<FoodItem> selected = new ArrayList<>();
        Map<String, Double> accumulated = new LinkedHashMap<>();
        targets.keySet().forEach(k -> accumulated.put(k, 0.0));
        double totalCost = 0.0;

        for (FoodItem food : sorted) {
            if (totalCost + food.getCostPer100g() > budgetCeiling) continue;

            selected.add(food);
            totalCost += food.getCostPer100g();

            Map<String, Double> eff = effectiveNutrients.get(food);
            for (String nutrient : targets.keySet()) {
                double current = accumulated.getOrDefault(nutrient, 0.0);
                accumulated.put(nutrient, current + eff.getOrDefault(nutrient, 0.0));
            }

            if (allTargetsMet(accumulated, targets)) break;
        }

        // ── 5. Unmet targets ─────────────────────────────────────────────────────
        List<String> unmet = new ArrayList<>();
        for (Map.Entry<String, Double> t : targets.entrySet()) {
            double achieved = accumulated.getOrDefault(t.getKey(), 0.0);
            if (achieved < t.getValue()) {
                unmet.add(String.format("%s (%.1f achieved / %.1f needed)", t.getKey(), achieved, t.getValue()));
            }
        }

        // ── 6. Build response — sorted LOW → HIGH cost ───────────────────────────
        List<FoodOption> options = selected.stream()
                .sorted(Comparator.comparingDouble(FoodItem::getCostPer100g))
                .map(f -> toFoodOption(f, targets, effectiveNutrients.get(f), effectiveCookingMethod.get(f)))
                .toList();

        return NutrientTargetResponse.builder()
                .allTargetsMet(unmet.isEmpty())
                .totalCostInr(Math.round(totalCost * 100.0) / 100.0)
                .foods(options)
                .achievedNutrients(roundMap(accumulated))
                .unmetTargets(unmet)
                .tips(buildTips(targets, accumulated, unmet))
                .build();
    }

    // ─── Cooking logic ──────────────────────────────────────────────────────────

    /**
     * Decides which cooking method to use for nutrient calculation:
     * - RAW or BOTH → "RAW" (fruits, salad veggies — no loss)
     * - COOKED      → food's bestCookingMethod (STEAM / BOIL / etc.)
     */
    private String resolveMethod(FoodItem food) {
        String bestAs = food.getBestEatenAs();
        if ("RAW".equalsIgnoreCase(bestAs) || "BOTH".equalsIgnoreCase(bestAs)) {
            return "RAW";
        }
        // COOKED → use food's own recommended cooking method
        String method = food.getBestCookingMethod();
        return (method != null && !method.isBlank()) ? method.toUpperCase() : "STEAM";
    }

    /**
     * Uses CookingLossService to apply retention, then maps Nutrient list
     * back into our key-based map for only the requested nutrients.
     */
    private Map<String, Double> buildEffectiveMap(FoodItem food, String method, Set<String> requestedKeys) {
        List<Nutrient> nutrients = cookingLossService.applyRetention(food, method);

        // Build name → cookedAmount lookup
        Map<String, Double> byName = new LinkedHashMap<>();
        for (Nutrient n : nutrients) {
            byName.put(n.getName(), n.getCookedAmount());
        }

        // Map requested keys → effective values
        Map<String, Double> result = new LinkedHashMap<>();
        for (String key : requestedKeys) {
            String nutrientName = KEY_TO_NUTRIENT_NAME.get(key);
            if (nutrientName != null) {
                result.put(key, byName.getOrDefault(nutrientName, 0.0));
            }
        }
        return result;
    }

    private double nutrientPerRupee(FoodItem food, Map<String, Double> targets,
                                    Map<FoodItem, Map<String, Double>> effectiveNutrients) {
        double cost = Math.max(food.getCostPer100g(), 0.1);
        Map<String, Double> eff = effectiveNutrients.getOrDefault(food, Map.of());
        double total = 0.0;
        for (String key : targets.keySet()) {
            total += eff.getOrDefault(key, 0.0);
        }
        return total / cost;
    }

    private boolean allTargetsMet(Map<String, Double> accumulated, Map<String, Double> targets) {
        for (Map.Entry<String, Double> t : targets.entrySet()) {
            if (accumulated.getOrDefault(t.getKey(), 0.0) < t.getValue()) return false;
        }
        return true;
    }

    // ─── Response builder ───────────────────────────────────────────────────────

    private FoodOption toFoodOption(FoodItem food, Map<String, Double> targets,
                                    Map<String, Double> effectiveValues, String usedMethod) {
        // Show effective (post-cooking) nutrient values for requested keys
        Map<String, Double> displayNutrients = new LinkedHashMap<>();
        for (String key : targets.keySet()) {
            displayNutrients.put(key, Math.round(effectiveValues.getOrDefault(key, 0.0) * 100.0) / 100.0);
        }

        String eatAs = resolveEatAs(food, usedMethod);
        String reason = buildReason(food, targets, effectiveValues, usedMethod);

        return FoodOption.builder()
                .name(food.getName())
                .vegetarian(food.isVegetarian())
                .costPer100g(food.getCostPer100g())
                .quantityG(100.0)
                .totalCost(food.getCostPer100g())
                .eatAs(eatAs)
                .nutrients(displayNutrients)
                .reason(reason)
                .build();
    }

    private String resolveEatAs(FoodItem food, String usedMethod) {
        if ("RAW".equals(usedMethod)) {
            return "RAW".equalsIgnoreCase(food.getBestEatenAs())
                    ? "Eat RAW — no cooking needed, full nutrients preserved"
                    : "Can eat RAW or cooked — RAW used here for max nutrients";
        }
        return "Cook: " + usedMethod + " (best method for " + food.getName() + ")";
    }

    private String buildReason(FoodItem food, Map<String, Double> targets,
                               Map<String, Double> effectiveValues, String usedMethod) {
        StringBuilder sb = new StringBuilder();
        double cost = Math.max(food.getCostPer100g(), 0.1);
        for (String key : targets.keySet()) {
            double val = effectiveValues.getOrDefault(key, 0.0);
            sb.append(String.format("%s=%.1f (%.2f per ₹), ", key, val, val / cost));
        }
        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        if ("RAW".equals(usedMethod)) sb.append(" | 🥗 RAW — 100% nutrient retention");
        else sb.append(" | 🍳 " + usedMethod + " — retention applied");
        return sb.toString();
    }

    private List<String> buildTips(Map<String, Double> targets, Map<String, Double> achieved, List<String> unmet) {
        List<String> tips = new ArrayList<>();
        if (unmet.isEmpty()) {
            tips.add("✅ All nutrient targets met!");
        } else {
            tips.add("⚠️ Some targets unmet — try increasing maxBudgetInr or switching to NON_VEG.");
            tips.addAll(unmet.stream().map(u -> "❌ Unmet: " + u).toList());
        }
        if (targets.containsKey("iron"))
            tips.add("💡 Iron tip: Eat iron-rich foods with Vitamin C (lemon/amla) for 3× better absorption.");
        if (targets.containsKey("calcium"))
            tips.add("💡 Calcium tip: Avoid tea/coffee 1 hour before/after calcium-rich foods.");
        if (targets.containsKey("vitaminC"))
            tips.add("💡 Vitamin C tip: Eat raw — cooking destroys 50–65% of Vitamin C.");
        if (targets.containsKey("folate"))
            tips.add("💡 Folate tip: Boiling destroys 45% of folate. Steam or eat raw where possible.");
        if (targets.containsKey("protein"))
            tips.add("💡 Protein tip: Combine dal + rice for complete amino acid profile.");
        return tips;
    }

    private Map<String, Double> roundMap(Map<String, Double> m) {
        Map<String, Double> r = new LinkedHashMap<>();
        m.forEach((k, v) -> r.put(k, Math.round(v * 100.0) / 100.0));
        return r;
    }
}
