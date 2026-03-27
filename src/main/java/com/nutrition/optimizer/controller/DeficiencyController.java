package com.nutrition.optimizer.controller;

import com.nutrition.optimizer.model.DeficiencyResponse;
import com.nutrition.optimizer.service.DeficiencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * DeficiencyController — exposes /api/deficiency endpoint.
 */
@RestController
@RequestMapping("/api")
public class DeficiencyController {

    private final DeficiencyService deficiencyService;

    public DeficiencyController(DeficiencyService deficiencyService) {
        this.deficiencyService = deficiencyService;
    }

    /**
     * POST /api/deficiency
     * Detects nutrient deficiencies from symptoms.
     *
     * Example: { "symptoms": ["fatigue", "hair_fall", "pale_skin"] }
     */
    @PostMapping("/deficiency")
    public ResponseEntity<DeficiencyResponse> detect(@RequestBody Map<String, List<String>> body) {
        List<String> symptoms = body.getOrDefault("symptoms", List.of());
        DeficiencyResponse response = deficiencyService.detect(symptoms);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/deficiency/symptoms
     * Returns all supported symptom keys.
     */
    @GetMapping("/deficiency/symptoms")
    public ResponseEntity<List<String>> getAllSymptoms() {
        return ResponseEntity.ok(deficiencyService.getAllSymptoms());
    }
}
