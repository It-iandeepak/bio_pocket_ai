package com.nutrition.optimizer.service;

import com.nutrition.optimizer.model.DeficiencyResponse;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * DeficiencyService — maps user-reported symptoms to nutrient deficiencies
 * and returns food suggestions for each deficiency.
 * STEP 3 logic (rule-based mapping).
 */
@Service
public class DeficiencyService {

    // ─── symptom → list of deficient nutrients ────────────────────────────────
    private static final Map<String, List<String>> SYMPTOM_NUTRIENT_MAP = new LinkedHashMap<>();

    // ─── nutrient → food suggestions ─────────────────────────────────────────
    private static final Map<String, List<String>> NUTRIENT_FOOD_MAP = new LinkedHashMap<>();

    // ─── nutrient → health tip ───────────────────────────────────────────────
    private static final Map<String, String> NUTRIENT_TIP_MAP = new LinkedHashMap<>();

    static {
        // ── SYMPTOMS ────────────────────────────────────────────────────────────
        SYMPTOM_NUTRIENT_MAP.put("fatigue",         List.of("Iron", "Vitamin B12", "Magnesium", "Folate"));
        SYMPTOM_NUTRIENT_MAP.put("weakness",         List.of("Iron", "Protein", "Vitamin B12", "Potassium"));
        SYMPTOM_NUTRIENT_MAP.put("hair_fall",        List.of("Protein", "Iron", "Zinc", "Biotin"));
        SYMPTOM_NUTRIENT_MAP.put("brittle_nails",    List.of("Iron", "Calcium", "Zinc", "Protein"));
        SYMPTOM_NUTRIENT_MAP.put("pale_skin",        List.of("Iron", "Vitamin B12", "Folate"));
        SYMPTOM_NUTRIENT_MAP.put("bone_pain",        List.of("Calcium", "Vitamin D", "Magnesium", "Phosphorus"));
        SYMPTOM_NUTRIENT_MAP.put("muscle_cramps",    List.of("Magnesium", "Potassium", "Calcium", "Sodium"));
        SYMPTOM_NUTRIENT_MAP.put("dry_skin",         List.of("Vitamin A", "Omega-3", "Vitamin E", "Zinc"));
        SYMPTOM_NUTRIENT_MAP.put("poor_vision",      List.of("Vitamin A", "Zinc", "Lutein"));
        SYMPTOM_NUTRIENT_MAP.put("bleeding_gums",    List.of("Vitamin C", "Vitamin K"));
        SYMPTOM_NUTRIENT_MAP.put("slow_healing",     List.of("Vitamin C", "Zinc", "Protein"));
        SYMPTOM_NUTRIENT_MAP.put("frequent_illness", List.of("Vitamin C", "Vitamin D", "Zinc", "Iron"));
        SYMPTOM_NUTRIENT_MAP.put("constipation",     List.of("Fiber", "Magnesium", "Water"));
        SYMPTOM_NUTRIENT_MAP.put("night_blindness",  List.of("Vitamin A"));
        SYMPTOM_NUTRIENT_MAP.put("numbness_tingling",List.of("Vitamin B12", "Magnesium"));
        SYMPTOM_NUTRIENT_MAP.put("depression_anxiety",List.of("Omega-3", "Vitamin D", "Magnesium", "Folate"));
        SYMPTOM_NUTRIENT_MAP.put("weight_gain",      List.of("Fiber", "Protein"));
        SYMPTOM_NUTRIENT_MAP.put("swelling",         List.of("Protein", "Sodium", "Potassium"));
        SYMPTOM_NUTRIENT_MAP.put("poor_memory",      List.of("Omega-3", "Vitamin B12", "Iron", "Choline"));
        SYMPTOM_NUTRIENT_MAP.put("irregular_heartbeat", List.of("Magnesium", "Potassium", "Calcium"));
        SYMPTOM_NUTRIENT_MAP.put("high_blood_sugar", List.of("Fiber", "Chromium", "Magnesium"));
        SYMPTOM_NUTRIENT_MAP.put("split_ends",       List.of("Protein", "Omega-3", "Biotin"));
        SYMPTOM_NUTRIENT_MAP.put("low_immunity",     List.of("Vitamin C", "Zinc", "Vitamin D", "Iron"));
        SYMPTOM_NUTRIENT_MAP.put("joint_pain",       List.of("Omega-3", "Vitamin D", "Calcium", "Vitamin C"));
        SYMPTOM_NUTRIENT_MAP.put("mouth_ulcers",     List.of("Vitamin B12", "Iron", "Folate", "Zinc"));
        SYMPTOM_NUTRIENT_MAP.put("acne",             List.of("Zinc", "Vitamin A", "Omega-3"));
        SYMPTOM_NUTRIENT_MAP.put("high_cholesterol", List.of("Fiber", "Omega-3"));
        SYMPTOM_NUTRIENT_MAP.put("thyroid_issues",   List.of("Iodine", "Selenium", "Zinc"));

        // ── FOOD SUGGESTIONS ────────────────────────────────────────────────────
        NUTRIENT_FOOD_MAP.put("Iron",        List.of("Bajra","Rajma","Spinach (Palak)","Masoor Dal","Chicken Liver","Jaggery","Methi (Fenugreek Leaves)","Sesame Seeds","Tamarind","Horse Gram (Kulthi)"));
        NUTRIENT_FOOD_MAP.put("Protein",     List.of("Egg","Chicken Breast","Soybean","Moong Dal","Paneer","Fish (Tuna)","Chana","Rajma","Peanuts","Kala Chana"));
        NUTRIENT_FOOD_MAP.put("Calcium",     List.of("Milk (Cow)","Paneer","Ragi (Finger Millet)","Sesame Seeds","Fish (Rohu)","Drumstick (Moringa)","Amaranth Leaves","Poppy Seeds","Chia Seeds","Horse Gram (Kulthi)"));
        NUTRIENT_FOOD_MAP.put("Vitamin B12", List.of("Egg","Chicken Liver","Fish (Tuna)","Mutton (Goat)","Milk (Cow)","Cheese","Prawns/Shrimp","Curd (Yogurt)"));
        NUTRIENT_FOOD_MAP.put("Magnesium",   List.of("Oats","Almonds","Pumpkin Seeds","Sunflower Seeds","Bajra (Pearl Millet)","Chia Seeds","Flaxseeds","Sesame Seeds","Toor Dal","Spinach (Palak)"));
        NUTRIENT_FOOD_MAP.put("Zinc",        List.of("Pumpkin Seeds","Chicken Breast","Cashews","Sesame Seeds","Rajma","Oats","Wheat Flour (Atta)","Flaxseeds","Egg","Mutton (Goat)"));
        NUTRIENT_FOOD_MAP.put("Vitamin A",   List.of("Carrot","Sweet Potato","Spinach (Palak)","Drumstick (Moringa)","Egg","Mango","Papaya","Chicken Liver","Beetroot Leaves","Pumpkin Seeds"));
        NUTRIENT_FOOD_MAP.put("Vitamin C",   List.of("Amla (Indian Gooseberry)","Guava","Capsicum (Bell Pepper)","Orange","Papaya","Drumstick (Moringa)","Lemon","Strawberry","Tomato","Pineapple"));
        NUTRIENT_FOOD_MAP.put("Omega-3",     List.of("Flaxseeds","Walnuts","Chia Seeds","Fish (Tuna)","Fish (Rohu)","Soybean","Pumpkin Seeds","Sunflower Seeds","Egg"));
        NUTRIENT_FOOD_MAP.put("Folate",      List.of("Moong Dal","Masoor Dal","Chana Dal","Spinach (Palak)","Rajma","Beetroot","Liver","Asparagus","Soybean","Peas (Matar)"));
        NUTRIENT_FOOD_MAP.put("Fiber",       List.of("Oats","Chia Seeds","Flaxseeds","Rajma","Chana Dal","Sweet Potato","Guava","Apple","Moong Dal","Lady Finger (Okra)"));
        NUTRIENT_FOOD_MAP.put("Potassium",   List.of("Banana","Sweet Potato","Coconut Water","Drumstick (Moringa)","Spinach (Palak)","Rajma","Potato","Orange","Toor Dal","Mango"));
        NUTRIENT_FOOD_MAP.put("Vitamin D",   List.of("Egg","Fish (Tuna)","Mushroom","Milk (Cow)","Chicken Liver","Soya Milk"));
        NUTRIENT_FOOD_MAP.put("Iodine",      List.of("Fish (Rohu)","Fish (Tuna)","Prawns/Shrimp","Egg","Milk (Cow)","Curd (Yogurt)"));
        NUTRIENT_FOOD_MAP.put("Biotin",      List.of("Egg","Soybean","Almonds","Sweet Potato","Oats","Wheat Flour (Atta)"));
        NUTRIENT_FOOD_MAP.put("Choline",     List.of("Egg","Chicken Liver","Soybean","Fish (Rohu)","Milk (Cow)"));
        NUTRIENT_FOOD_MAP.put("Selenium",    List.of("Fish (Tuna)","Egg","Chicken Breast","Sunflower Seeds","Oats"));
        NUTRIENT_FOOD_MAP.put("Chromium",    List.of("Jowar (Sorghum)","Wheat Flour (Atta)","Oats","Egg","Broccoli"));
        NUTRIENT_FOOD_MAP.put("Vitamin E",   List.of("Almonds","Sunflower Seeds","Olive Oil","Peanuts","Walnuts","Chia Seeds"));
        NUTRIENT_FOOD_MAP.put("Vitamin K",   List.of("Spinach (Palak)","Methi (Fenugreek Leaves)","Amaranth Leaves","Broccoli","Cabbage"));
        NUTRIENT_FOOD_MAP.put("Lutein",      List.of("Spinach (Palak)","Broccoli","Carrot","Peas (Matar)","Egg"));
        NUTRIENT_FOOD_MAP.put("Sodium",      List.of("Buttermilk (Chaas)","Cheese","Soya Sauce","Bread (Whole Wheat)"));
        NUTRIENT_FOOD_MAP.put("Water",       List.of("Cucumber","Bottle Gourd (Lauki)","Coconut Water","Watermelon","Tomato","Buttermilk (Chaas)"));
        NUTRIENT_FOOD_MAP.put("Phosphorus",  List.of("Milk (Cow)","Egg","Chicken Breast","Pumpkin Seeds","Rajma","Oats"));

        // ── HEALTH TIPS ─────────────────────────────────────────────────────────
        NUTRIENT_TIP_MAP.put("Iron",        "Eat iron-rich foods with Vitamin C (e.g., lemon + dal) to boost absorption. Avoid tea/coffee 1hr after iron-rich meals.");
        NUTRIENT_TIP_MAP.put("Protein",     "Distribute protein intake across all meals. Combine plant proteins (dal + rice) for complete amino acids profile.");
        NUTRIENT_TIP_MAP.put("Calcium",     "Pair calcium-rich foods with Vitamin D sources. Avoid excess oxalate (spinach) with calcium simultaneously.");
        NUTRIENT_TIP_MAP.put("Vitamin B12", "B12 is mainly found in animal foods. Vegans should prioritize fortified plant milks or B12-fortified cereals.");
        NUTRIENT_TIP_MAP.put("Magnesium",   "Stress depletes magnesium. Soak nuts/seeds overnight for better absorption.");
        NUTRIENT_TIP_MAP.put("Zinc",        "Zinc absorption is reduced by phytates. Soaking legumes before cooking helps unlock zinc.");
        NUTRIENT_TIP_MAP.put("Vitamin A",   "Fat-soluble vitamin — always consume with a small amount of healthy fat (ghee, olive oil) for better absorption.");
        NUTRIENT_TIP_MAP.put("Vitamin C",   "Vitamin C is heat-sensitive. Eat raw fruits and lightly steamed vegetables to preserve it.");
        NUTRIENT_TIP_MAP.put("Omega-3",     "ALA (plant omega-3) conversion to EPA/DHA is low. Include fatty fish 2x/week OR consider algae supplements for vegans.");
        NUTRIENT_TIP_MAP.put("Folate",      "Critical during pregnancy. Cook lightly as folate is heat-sensitive. Include daily legumes and leafy greens.");
        NUTRIENT_TIP_MAP.put("Fiber",       "Increase fiber gradually with adequate water intake to avoid constipation and bloating.");
        NUTRIENT_TIP_MAP.put("Vitamin D",   "Sunlight for 15–20 min daily helps synthesize Vitamin D. Supplements often needed in deficient individuals.");
        NUTRIENT_TIP_MAP.put("Potassium",   "Potassium balances sodium. High-sodium diets need more potassium-rich foods for heart health.");
        NUTRIENT_TIP_MAP.put("Iodine",      "Use iodized salt. Avoid excess raw cruciferous vegetables (goitrogens) if thyroid-affected.");
    }

    public DeficiencyResponse detect(List<String> symptoms) {
        Set<String> deficientNutrients = new LinkedHashSet<>();
        for (String symptom : symptoms) {
            String normalized = symptom.toLowerCase().replace(" ", "_");
            List<String> nutrients = SYMPTOM_NUTRIENT_MAP.getOrDefault(normalized, List.of());
            deficientNutrients.addAll(nutrients);
        }

        Map<String, List<String>> nutrientToFoods = new LinkedHashMap<>();
        List<String> healthTips = new ArrayList<>();
        for (String nutrient : deficientNutrients) {
            nutrientToFoods.put(nutrient, NUTRIENT_FOOD_MAP.getOrDefault(nutrient, List.of()));
            String tip = NUTRIENT_TIP_MAP.get(nutrient);
            if (tip != null) healthTips.add("💡 [" + nutrient + "]: " + tip);
        }

        return DeficiencyResponse.builder()
                .symptoms(symptoms)
                .detectedNutrientDeficiencies(new ArrayList<>(deficientNutrients))
                .nutrientToFoods(nutrientToFoods)
                .healthTips(healthTips)
                .build();
    }

    public List<String> getAllSymptoms() {
        return new ArrayList<>(SYMPTOM_NUTRIENT_MAP.keySet());
    }

    public static Map<String, List<String>> getNutrientFoodMap() {
        return Collections.unmodifiableMap(NUTRIENT_FOOD_MAP);
    }

    public static List<String> getFoodsForNutrient(String nutrient) {
        return NUTRIENT_FOOD_MAP.getOrDefault(nutrient, List.of());
    }
}
