package com.nutrition.optimizer.controller;

import com.nutrition.optimizer.entity.FoodItem;
import com.nutrition.optimizer.model.NutrientTargetRequest;
import com.nutrition.optimizer.model.NutrientTargetResponse;
import com.nutrition.optimizer.service.CookingLossService;
import com.nutrition.optimizer.service.FoodDataService;
import com.nutrition.optimizer.service.GoalService;
import com.nutrition.optimizer.service.NutrientTargetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * FoodsController — exposes /api/foods and related metadata endpoints.
 *
 * Two optimization modes:
 *  1. Budget-first  → POST /api/recommend   (NutritionController)
 *  2. Nutrient-target-first → POST /api/nutrient-target  (this controller)
 */
@RestController
@RequestMapping("/api")
public class FoodsController {

    private final FoodDataService       foodDataService;
    private final CookingLossService    cookingLossService;
    private final GoalService           goalService;
    private final NutrientTargetService nutrientTargetService;

    public FoodsController(FoodDataService fds, CookingLossService cls, GoalService gs,
                           NutrientTargetService nts) {
        this.foodDataService       = fds;
        this.cookingLossService    = cls;
        this.goalService           = gs;
        this.nutrientTargetService = nts;
    }

    /** GET /api/foods — full dataset */
    @GetMapping("/foods")
    public ResponseEntity<List<FoodItem>> getAllFoods(
            @RequestParam(required = false) String dietType,
            @RequestParam(required = false) String nutrient) {

        List<FoodItem> foods;
        if ("VEG".equalsIgnoreCase(dietType)) {
            foods = foodDataService.getVegFoods();
        } else if ("NON_VEG".equalsIgnoreCase(dietType)) {
            foods = foodDataService.getNonVegFoods();
        } else {
            foods = foodDataService.getAllFoods();
        }

        if (nutrient != null) {
            foods = foodDataService.searchByNutrient(nutrient);
        }
        return ResponseEntity.ok(foods);
    }

    /** GET /api/foods/group/{group} — e.g. /api/foods/group/legume */
    @GetMapping("/foods/group/{group}")
    public ResponseEntity<List<FoodItem>> getFoodsByGroup(@PathVariable String group) {
        return ResponseEntity.ok(foodDataService.getFoodsByGroup(group));
    }

    /** GET /api/cooking/retention?method=STEAM */
    @GetMapping("/cooking/retention")
    public ResponseEntity<Map<String, Integer>> getRetention(
            @RequestParam(defaultValue = "STEAM") String method) {
        return ResponseEntity.ok(cookingLossService.getRetentionSummary(method));
    }

    /** GET /api/goals — list supported goals and roles */
    @GetMapping("/goals")
    public ResponseEntity<Map<String, List<String>>> getGoals() {
        return ResponseEntity.ok(Map.of(
                "goals", goalService.getSupportedGoals(),
                "roles", goalService.getSupportedRoles()
        ));
    }

    /**
     * POST /api/nutrient-target
     * Nutrient-target-first mode: find cheapest foods that meet the given nutrient targets.
     *
     * Example:
     * {
     *   "dietType": "VEG",
     *   "nutrientTargets": { "fiber": 50.0, "protein": 40.0 },
     *   "maxBudgetInr": 150,
     *   "cookingMethod": "RAW"
     * }
     */
    @PostMapping("/nutrient-target")
    public ResponseEntity<NutrientTargetResponse> findByNutrientTarget(
            @RequestBody NutrientTargetRequest request) {
        return ResponseEntity.ok(nutrientTargetService.findCheapestToMeetTargets(request));
    }
}
