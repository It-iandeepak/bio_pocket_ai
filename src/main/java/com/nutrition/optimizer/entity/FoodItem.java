package com.nutrition.optimizer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {
    private String name;
    private double costPer100g;       // â‚¹ per 100g
    private boolean vegetarian;
    private String bestEatenAs;       // "RAW", "COOKED", "BOTH"
    private String bestCookingMethod; // "STEAM", "BOIL", "FRY", "RAW"

    // Macros (per 100g raw)
    private double proteinG;
    private double carbsG;
    private double fiberG;
    private double fatG;
    private double caloriesKcal;

    // Micros (per 100g raw)
    private double calciumMg;
    private double ironMg;
    private double vitaminCMg;
    private double vitaminAMcg;
    private double vitaminB12Mcg;
    private double magnesiumMg;
    private double potassiumMg;
    private double sodiumMg;
    private double zincMg;
    private double folateMcg;
    private double omega3G;

    // Metadata
    private double glycemicIndex;
    private double absorptionRate; // 0.0 - 1.0
    private String[] foodGroups;   // e.g. ["legume", "complete-protein"]
    private String[] synergisticWith; // e.g. ["rice", "ghee"]
}

