package com.nutrition.optimizer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private String id;
    private String name;
    private int age;
    private String role;       // CHILD, ADULT, PREGNANT, ELDERLY
    private String goal;       // WEIGHT_LOSS, MUSCLE_GAIN, MAINTENANCE, RECOVERY
    private String dietType;   // VEG, NON_VEG
    private double budgetInr;  // ₹ total
    private String[] symptoms; // e.g. ["fatigue","hair_fall"]
}
