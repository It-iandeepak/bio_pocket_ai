package com.nutrition.optimizer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealItem {
    private FoodItem foodItem;
    private double quantityG;        // grams recommended
    private double actualCost;       // ₹
    private List<Nutrient> nutrients; // after cooking adjustments
    private String cookingMethod;
    private String reason;           // why this food was recommended
}
