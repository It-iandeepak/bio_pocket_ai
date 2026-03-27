package com.nutrition.optimizer.service;

import com.nutrition.optimizer.entity.FoodItem;
import com.nutrition.optimizer.entity.MealItem;
import com.nutrition.optimizer.entity.Nutrient;
import com.nutrition.optimizer.model.DeficiencyResponse;
import com.nutrition.optimizer.model.MealPlanResponse;
import com.nutrition.optimizer.model.MealPlanResponse.MealItemDTO;
import com.nutrition.optimizer.model.UserInputRequest;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * NutritionService — the main orchestrator.
 * Wires together all sub-services and returns a complete MealPlanResponse.
 */
@Service
public class NutritionService {

    private final FoodDataService      foodDataService;
    private final DeficiencyService    deficiencyService;
    private final GoalService          goalService;
    private final CookingLossService   cookingLossService;
    private final OptimizationService  optimizationService;
    private final FoodCombinationService combinationService;

    public NutritionService(FoodDataService fd, DeficiencyService ds, GoalService gs,
                            CookingLossService cls, OptimizationService os, FoodCombinationService fcs) {
        this.foodDataService    = fd;
        this.deficiencyService  = ds;
        this.goalService        = gs;
        this.cookingLossService = cls;
        this.optimizationService= os;
        this.combinationService = fcs;
    }

    public MealPlanResponse generateMealPlan(UserInputRequest req) {

        // ── 1. Filter foods by diet preference ─────────────────────────────────
        List<FoodItem> candidateFoods = "VEG".equalsIgnoreCase(req.getDietType())
                ? foodDataService.getVegFoods()
                : foodDataService.getAllFoods();

        // ── 2. Apply nutrient query filters if provided ─────────────────────────
        if (req.getNutrientQuery() != null && !req.getNutrientQuery().isEmpty()) {
            Set<FoodItem> filtered = new LinkedHashSet<>();
            for (String query : req.getNutrientQuery()) {
                filtered.addAll(foodDataService.searchByNutrient(query));
            }
            // Only keep foods that are in both the candidate list and the filter
            candidateFoods = candidateFoods.stream()
                    .filter(filtered::contains)
                    .toList();
        }

        // ── 3. Detect deficiencies and extract priority nutrients ───────────────
        List<String> symptoms = req.getSymptoms() != null ? req.getSymptoms() : List.of();
        DeficiencyResponse deficiency = deficiencyService.detect(symptoms);
        List<String> priorityNutrients = deficiency.getDetectedNutrientDeficiencies();

        // ── 4. Get goal targets ─────────────────────────────────────────────────
        GoalService.GoalTarget goalTarget = goalService.getTarget(req.getGoal(), req.getRole());

        // ── 5. Run optimization (Knapsack) ──────────────────────────────────────
        String cookingMethod = req.getCookingMethod() != null ? req.getCookingMethod() : "STEAM";
        OptimizationService.OptimizationRequest optReq = new OptimizationService.OptimizationRequest(
                candidateFoods, req.getBudgetInr(), req.getGoal(), req.getRole(),
                cookingMethod, priorityNutrients, goalTarget
        );
        OptimizationService.OptimizationResult optResult = optimizationService.optimize(optReq);

        // ── 6. Find food combinations ───────────────────────────────────────────
        List<FoodItem> selectedFoods = optResult.selectedItems().stream()
                .map(MealItem::getFoodItem).toList();
        List<String> combos = combinationService.findCombinations(selectedFoods);

        // ── 7. Compute nutrition score ─────────────────────────────────────────
        int score = goalService.computeNutritionScore(optResult.totalNutrients(), goalTarget);

        // ── 8. Build health suggestions ────────────────────────────────────────
        List<String> suggestions = buildHealthSuggestions(req, deficiency, optResult.totalNutrients(), goalTarget);

        // ── 9. Map to DTO ──────────────────────────────────────────────────────
        List<MealItemDTO> dtos = optResult.selectedItems().stream()
                .map(item -> toDTO(item, optResult.totalNutrients()))
                .toList();

        double budgetLeft = req.getBudgetInr() - optResult.totalCost();
        double usedPct    = (optResult.totalCost() / req.getBudgetInr()) * 100;

        return MealPlanResponse.builder()
                .success(true)
                .goal(req.getGoal())
                .role(req.getRole())
                .budgetInr(req.getBudgetInr())
                .totalCostUsed(Math.round(optResult.totalCost() * 100.0) / 100.0)
                .budgetLeftover(Math.round(budgetLeft * 100.0) / 100.0)
                .nutritionScore(score + "/100")
                .mealItems(dtos)
                .totalNutrients(roundNutrients(optResult.totalNutrients()))
                .goalTargets(goalService.getTargetAsMap(req.getGoal(), req.getRole()))
                .deficienciesDetected(deficiency.getDetectedNutrientDeficiencies())
                .healthSuggestions(suggestions)
                .foodCombinations(combos)
                .build();
    }

    // ─── DTOs ─────────────────────────────────────────────────────────────────
    private MealItemDTO toDTO(MealItem item, Map<String, Double> totalNutrients) {
        Map<String, Double> nAfterCooking = new LinkedHashMap<>();
        for (Nutrient n : item.getNutrients()) {
            nAfterCooking.put(n.getName(), n.getCookedAmount());
        }
        return MealItemDTO.builder()
                .foodName(item.getFoodItem().getName())
                .vegetarian(item.getFoodItem().isVegetarian())
                .quantityG(item.getQuantityG())
                .cost(item.getActualCost())
                .cookingMethod(item.getCookingMethod())
                .recommendation(cookingLossService.recommendCookingMethod(item.getFoodItem()))
                .nutrientsAfterCooking(nAfterCooking)
                .reason(item.getReason())
                .build();
    }

    // ─── HEALTH SUGGESTIONS ───────────────────────────────────────────────────
    private List<String> buildHealthSuggestions(UserInputRequest req,
                                                 DeficiencyResponse deficiency,
                                                 Map<String, Double> nutrients,
                                                 GoalService.GoalTarget target) {
        List<String> tips = new ArrayList<>();

        // Deficiency-specific tips
        tips.addAll(deficiency.getHealthTips());

        // Goal-based tips
        switch (req.getGoal().toUpperCase()) {
            case "WEIGHT_LOSS"  -> tips.add("✅ Eat fiber-rich foods first in meals to reduce overall calorie intake.");
            case "MUSCLE_GAIN"  -> tips.add("✅ Consume protein within 45 minutes post-workout for optimal muscle synthesis.");
            case "RECOVERY"     -> tips.add("✅ Include anti-inflammatory foods rich in omega-3 and Vitamin C daily.");
            case "MAINTENANCE"  -> tips.add("✅ Maintain a consistent meal schedule; avoid skipping meals to regulate metabolism.");
        }

        // Role-based tips
        switch (req.getRole().toUpperCase()) {
            case "CHILD"    -> tips.add("👶 Children need calcium 800mg+/day. Include milk, ragi, or paneer every day.");
            case "PREGNANT" -> tips.add("🤰 Folate is critical in the first trimester. Take 400mcg/day from lentils, spinach, and amla.");
            case "ELDERLY"  -> tips.add("👴 Vitamin D + Calcium critically decline with age. Add mushroom, egg, and dairy daily.");
        }

        // Nutrients gap tips
        double protein = nutrients.getOrDefault("protein", 0.0);
        if (protein < target.minProteinG() * 0.8) tips.add("⚠️ Protein below target. Add more legumes (dal, chana) or eggs to boost intake.");
        double fiber = nutrients.getOrDefault("fiber", 0.0);
        if (fiber < target.minFiberG() * 0.8) tips.add("⚠️ Fiber is low. Include oats, chia seeds, or guava to improve gut health.");
        double iron = nutrients.getOrDefault("iron", 0.0);
        if (iron < target.minIronMg() * 0.7) tips.add("⚠️ Iron is low. Add bajra, rajma, or palak + squeeze lemon for better absorption.");

        // Budget tip
        tips.add("💰 Budget tip: Moong dal (₹8/100g) and oats (₹8/100g) give the best nutrition per rupee!");
        return tips;
    }

    private Map<String, Double> roundNutrients(Map<String, Double> nutrients) {
        Map<String, Double> rounded = new LinkedHashMap<>();
        nutrients.forEach((k, v) -> rounded.put(k, Math.round(v * 100.0) / 100.0));
        return rounded;
    }
}
