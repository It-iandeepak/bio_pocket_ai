package com.nutrition.optimizer.service;

import com.nutrition.optimizer.entity.FoodItem;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * FoodCombinationService — STEP 10.
 * Smart food combination engine that suggests synergistic food pairings
 * to maximize nutritional completeness and bioavailability.
 */
@Service
public class FoodCombinationService {

    public record FoodCombo(String combo, String benefit, String example) {}

    // Name-based synergy rules (food1 + food2 → benefit)
    private static final List<FoodCombo> COMBO_RULES = List.of(
        new FoodCombo("Dal + Rice",        "Complete protein: Dal provides lysine; rice provides methionine → together all essential amino acids.", "Rajma-chawal, Dal-rice, Moong khichdi"),
        new FoodCombo("Spinach + Lemon",   "Vitamin C from lemon boosts non-heme iron absorption from spinach by up to 3×.", "Palak dal with lemon squeeze, spinach salad + lemon dressing"),
        new FoodCombo("Turmeric + Black Pepper", "Piperine in black pepper increases curcumin (turmeric) absorption by 2000%.", "Haldi milk with pinch of black pepper"),
        new FoodCombo("Dal + Ghee",        "Fat-soluble vitamins (A, D, E, K) in dal are absorbed better. Ghee also slows glycemic spike of dal.", "Tadka dal with ghee"),
        new FoodCombo("Oats + Milk",       "Milk provides complete protein and calcium; oats supply fiber and beta-glucan. Muscle-building + gut health combo.", "Overnight oats, oat porridge with milk"),
        new FoodCombo("Banana + Peanuts",  "Carbs from banana + protein/fat from peanuts → sustained energy release. Ideal pre-workout snack.", "Banana peanut butter sandwich, banana+peanut smoothie"),
        new FoodCombo("Egg + Spinach",     "Iron in spinach + Vitamin C synergy; egg provides complete protein + Vitamin B12 for brain health.", "Spinach omelette, egg bhurji with palak"),
        new FoodCombo("Tomato + Olive Oil","Lycopene (fat-soluble antioxidant) in tomato absorbs 4× better when eaten with fat.", "Tomato salad with olive oil, pasta with tomato sauce"),
        new FoodCombo("Curd + Rice",       "Probiotics in curd improve gut microbiome. Rice provides easy energy. Cooling combo with anti-inflammatory benefits.", "Curd rice, thayir sadam"),
        new FoodCombo("Carrot + Ghee",     "Beta-carotene (Vitamin A precursor) in carrot is fat-soluble; a small amount of fat doubles absorption.", "Gajar sabzi with ghee, carrot with cream dressing"),
        new FoodCombo("Fish + Rice",       "Complete meal: fish provides omega-3 + complete protein; rice provides carbs for energy. Heart-healthy combo.", "Fish curry with rice, fish pulao"),
        new FoodCombo("Peanuts + Jaggery", "Iron from jaggery + Vitamin C trace in peanuts. Traditional Indian energy snack balancing iron + energy.", "Peanut chikki, peanut-jaggery ladoo"),
        new FoodCombo("Chana + Lemon",     "Vitamin C from lemon increases plant iron (from chana) absorption significantly.", "Chana chaat with lemon, chickpea salad"),
        new FoodCombo("Ragi + Milk",       "Ragi — richest grain in calcium; milk amplifies calcium. Best combo for bone health.", "Ragi porridge with milk, ragi malt"),
        new FoodCombo("Almonds + Banana",  "Magnesium + potassium from banana; healthy fats + Vitamin E from almonds → muscle relaxation + energy.", "Banana almond smoothie, trail mix"),
        new FoodCombo("Sesame + Roti",      "Calcium from sesame seeds directly fortifies roti. Traditional til roti — excellent for bones.", "Til (sesame) roti, til paratha"),
        new FoodCombo("Soybean + Rice",    "Soya provides all essential amino acids + iron; rice provides carbs. Together → complete protein meal.", "Soya chunks pulao, soya curry rice"),
        new FoodCombo("Drumstick + Dal",   "Moringa leaves are high in iron & calcium; dal provides protein. Combined bioavailability is excellent.", "Drumstick sambar, moringa dal"),
        new FoodCombo("Flaxseed + Curd",   "Omega-3 from flaxseed + probiotics from curd → anti-inflammatory gut-friendly combo.", "Flaxseed lassi, flaxseed raita"),
        new FoodCombo("Paneer + Spinach",  "Complete protein (paneer) + iron-rich greens (spinach) + calcium → bone + muscle health.", "Palak paneer — the ultimate Indian health combination"),
        new FoodCombo("Honey + Lemon Water","Vitamin C from lemon + antioxidants from honey. Anti-bacterial + immunity booster. Best on empty stomach.", "Warm honey-lemon water, detox drink"),
        new FoodCombo("Bajra + Curd",      "Bajra (pearl millet) is iron-rich; curd acts as probiotic + cools the heating nature of bajra.", "Bajra khichdi with curd, bajra roti with curd"),
        new FoodCombo("Tofu + Broccoli",   "Both high in calcium; together they provide Vitamin C (broccoli enhances iron from tofu). Plant-based power combo.", "Tofu broccoli stir-fry, tofu-broccoli bowl"),
        new FoodCombo("Amla + Dal",        "Amla (highest Vitamin C food) dramatically boosts iron / protein absorption from dal.", "Amla chutney with dal, amla in lentil soup")
    );

    /**
     * Finds applicable synergy combos from the selected meal items.
     */
    public List<String> findCombinations(List<FoodItem> selectedFoods) {
        List<String> suggestions = new ArrayList<>();
        Set<String> foodNames = new HashSet<>();
        for (FoodItem f : selectedFoods) {
            foodNames.add(f.getName().toLowerCase());
            // Also add synergistic hints from the FoodItem's synergisticWith list
            if (f.getSynergisticWith() != null) {
                foodNames.addAll(Arrays.asList(f.getSynergisticWith()));
            }
        }

        for (FoodCombo rule : COMBO_RULES) {
            String[] parts = rule.combo().split("\\+");
            boolean partialMatch = false;
            for (String part : parts) {
                if (foodNames.stream().anyMatch(n -> n.contains(part.trim().toLowerCase()))) {
                    partialMatch = true;
                    break;
                }
            }
            if (partialMatch) {
                suggestions.add("🍽️ " + rule.combo() + " — " + rule.benefit() + " (Example: " + rule.example() + ")");
            }
        }

        if (suggestions.isEmpty()) {
            suggestions.add("🥗 Tip: Combine legumes (dal/rajma) with a grain (rice/roti) for complete protein in every meal.");
            suggestions.add("🍋 Add lemon juice to any iron-rich dish to boost iron absorption by up to 3×.");
        }
        return suggestions;
    }

    public List<FoodCombo> getAllCombos() { return COMBO_RULES; }
}
