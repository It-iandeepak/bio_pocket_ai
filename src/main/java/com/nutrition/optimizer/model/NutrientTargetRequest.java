package com.nutrition.optimizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Request for nutrient-target-first mode.
 *
 * <p>Instead of a budget, the user specifies nutrient targets they want to hit
 * (e.g. 50g fiber, 40g protein) and the system finds the cheapest combination
 * of foods that meets those targets, sorted low → high cost.</p>
 *
 * Example:
 * <pre>{@code
 * {
 *   "dietType": "VEG",
 *   "nutrientTargets": { "fiber": 50.0, "protein": 40.0 },
 *   "maxBudgetInr": 200,
 *   "cookingMethod": "STEAM"
 * }
 * }</pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutrientTargetRequest {

    /** "VEG" or "NON_VEG" */
    private String dietType;

    /**
     * Nutrient name → target amount.
     * Supported keys: fiber, protein, calcium, iron, vitaminC, vitaminA,
     *                 vitaminB12, folate, zinc, magnesium, potassium, omega3, calories
     */
    private Map<String, Double> nutrientTargets;

    /**
     * Optional hard ceiling on spend (₹).
     * If null the system will pick the minimum-cost combination regardless of cost.
     */
    private Double maxBudgetInr;

    /** "STEAM" | "BOIL" | "FRY" | "RAW"  (default: RAW for raw nutrient values) */
    private String cookingMethod;
}
