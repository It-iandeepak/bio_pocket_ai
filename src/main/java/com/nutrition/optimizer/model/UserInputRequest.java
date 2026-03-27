package com.nutrition.optimizer.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInputRequest {

    @Min(value = 1, message = "Budget must be at least ₹1")
    private double budgetInr;

    @NotBlank(message = "Diet type is required")
    private String dietType;    // "VEG" or "NON_VEG"

    @NotBlank(message = "Goal is required")
    private String goal;        // "WEIGHT_LOSS" | "MUSCLE_GAIN" | "MAINTENANCE" | "RECOVERY"

    @NotBlank(message = "Role is required")
    private String role;        // "CHILD" | "ADULT" | "PREGNANT" | "ELDERLY"

    private List<String> symptoms;     // optional, e.g. ["fatigue","hair_fall"]
    private List<String> nutrientQuery; // optional, e.g. ["high_protein","high_fiber"]
    private String cookingMethod;      // "STEAM" | "BOIL" | "FRY" | "RAW" (default: STEAM)
    private String preferredCombos;    // optional hint
}
