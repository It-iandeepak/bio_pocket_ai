package com.nutrition.optimizer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Nutrient {
    private String name;          // e.g. "Iron", "Protein"
    private double rawAmount;     // before cooking (mg or g)
    private double cookedAmount;  // after cooking (mg or g)
    private String unit;          // "mg", "g", "mcg"
    private double retentionRate; // 0.0 - 1.0
}
