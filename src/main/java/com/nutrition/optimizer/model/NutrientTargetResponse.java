package com.nutrition.optimizer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Response for nutrient-target-first mode.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NutrientTargetResponse {

    /** Whether all requested nutrient targets were fully met. */
    private boolean allTargetsMet;

    /** Total cost of selected foods (₹). */
    private double totalCostInr;

    /**
     * Foods sorted low → high cost, each 100g serving.
     * These collectively meet the nutrient targets.
     */
    private List<FoodOption> foods;

    /**
     * How much of each target nutrient is covered by the selected foods.
     * e.g. { "fiber": 52.3, "protein": 41.0 }
     */
    private Map<String, Double> achievedNutrients;

    /**
     * Targets that could NOT be met (e.g. budget too tight, food not available).
     */
    private List<String> unmetTargets;

    /** Human-readable tips. */
    private List<String> tips;

    // ─── Inner DTO ─────────────────────────────────────────────────────────────

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FoodOption {
        private String  name;
        private boolean vegetarian;
        private double  costPer100g;
        private double  quantityG;
        private double  totalCost;
        /** "Eat RAW" or "Cook: STEAM" — tells user exactly how to prepare */
        private String  eatAs;
        /** Effective nutrient values after cooking loss (or raw if eaten raw) */
        private Map<String, Double> nutrients;
        /** Why this food was picked and its nutrient-per-rupee value */
        private String reason;
    }
}
