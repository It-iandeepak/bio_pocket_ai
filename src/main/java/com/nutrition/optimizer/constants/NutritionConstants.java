package com.nutrition.optimizer.constants;

/**
 * Application-wide nutritional constants used across services.
 */
public final class NutritionConstants {

    private NutritionConstants() {}

    // ─── Default serving size ────────────────────────────────────────────────
    public static final double DEFAULT_SERVING_G = 100.0;

    // ─── Budget utilisation threshold ────────────────────────────────────────
    /** Stop greedy selection when this fraction of the budget is spent. */
    public static final double BUDGET_UTILISATION_THRESHOLD = 0.95;

    // ─── Minimum meaningful cost (prevents division-by-zero) ─────────────────
    public static final double MIN_COST = 0.1;

    // ─── Daily Reference Intakes (approximate, adult) ────────────────────────
    public static final double RDI_PROTEIN_G        = 50.0;
    public static final double RDI_FIBER_G          = 25.0;
    public static final double RDI_CALCIUM_MG       = 1000.0;
    public static final double RDI_IRON_MG          = 18.0;
    public static final double RDI_VITAMIN_C_MG     = 90.0;
    public static final double RDI_VITAMIN_A_MCG    = 900.0;
    public static final double RDI_VITAMIN_B12_MCG  = 2.4;
    public static final double RDI_FOLATE_MCG       = 400.0;
    public static final double RDI_ZINC_MG          = 11.0;
    public static final double RDI_MAGNESIUM_MG     = 420.0;
    public static final double RDI_POTASSIUM_MG     = 3500.0;
    public static final double RDI_OMEGA3_G         = 1.6;
}
