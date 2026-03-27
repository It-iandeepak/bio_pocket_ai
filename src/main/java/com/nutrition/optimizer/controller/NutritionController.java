package com.nutrition.optimizer.controller;

import com.nutrition.optimizer.model.MealPlanResponse;
import com.nutrition.optimizer.model.UserInputRequest;
import com.nutrition.optimizer.service.NutritionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Main REST controller — exposes /api/recommend endpoint.
 */
@RestController
@RequestMapping("/api")
public class NutritionController {

    private final NutritionService nutritionService;

    public NutritionController(NutritionService nutritionService) {
        this.nutritionService = nutritionService;
    }

    /**
     * POST /api/recommend
     * Full meal plan generation based on user inputs.
     *
     * Example request body:
     * {
     *   "budgetInr": 100,
     *   "dietType": "VEG",
     *   "goal": "WEIGHT_LOSS",
     *   "role": "ADULT",
     *   "symptoms": ["fatigue", "hair_fall"],
     *   "nutrientQuery": ["high_protein"],
     *   "cookingMethod": "STEAM"
     * }
     */
    @PostMapping("/recommend")
    public ResponseEntity<MealPlanResponse> recommend(@Valid @RequestBody UserInputRequest request) {
        MealPlanResponse response = nutritionService.generateMealPlan(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/input — alias for /api/recommend (for voice input flow)
     */
    @PostMapping("/input")
    public ResponseEntity<MealPlanResponse> input(@Valid @RequestBody UserInputRequest request) {
        return recommend(request);
    }

    /**
     * GET /api/health — health check
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("AI Nutrition Optimizer is running 🥗");
    }
}
