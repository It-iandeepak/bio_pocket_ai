package com.nutrition.optimizer.service;

import com.nutrition.optimizer.entity.FoodItem;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * FoodDataService â€” loads the embedded food dataset (100g per unit).
 * All nutrition values are per 100g of raw food.
 * Columns: name, costPer100g, veg, calories, protein, carbs, fiber, fat,
 *          calcium, iron, vitC, vitA, vitB12, magnesium, potassium, sodium,
 *          zinc, folate, omega3, GI, absorptionRate, bestEatenAs, bestCookingMethod,
 *          foodGroups[], synergisticWith[]
 */
@Service
public class FoodDataService {

    private final List<FoodItem> allFoods = new ArrayList<>();

    public FoodDataService() {
        loadDataset();
    }

    private void loadDataset() {
        // â”€â”€â”€ CEREALS & GRAINS â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        allFoods.add(build("Rice (Raw)", 2.0, true, 362, 7.0, 78.0, 0.5, 0.5, 10, 0.7, 0, 0, 0, 35, 115, 1, 1.1, 8, 0, 73, 0.85, "COOKED", "BOIL", arr("cereal", "carb"), arr("dal", "rajma", "kadhi")));
        allFoods.add(build("Wheat Flour (Atta)", 2.5, true, 340, 12.0, 70.0, 2.7, 1.5, 33, 4.6, 0, 0, 0, 138, 284, 2, 2.7, 57, 0, 68, 0.82, "COOKED", "BAKE", arr("cereal", "carb"), arr("dal", "sabzi")));
        allFoods.add(build("Oats", 8.0, true, 389, 17.0, 66.0, 10.6, 7.0, 54, 4.7, 0, 0, 0, 177, 429, 2, 3.6, 56, 0.11, 55, 0.83, "COOKED", "BOIL", arr("cereal", "fiber"), arr("milk", "banana", "nuts")));
        allFoods.add(build("Jowar (Sorghum)", 3.0, true, 349, 10.4, 72.6, 2.0, 1.9, 25, 4.1, 0, 0, 0, 165, 350, 6, 1.7, 20, 0, 62, 0.80, "COOKED", "BAKE", arr("cereal", "gluten-free"), arr("dal", "curd")));
        allFoods.add(build("Bajra (Pearl Millet)", 3.0, true, 378, 11.0, 67.0, 1.2, 5.0, 42, 16.9, 0, 0, 0, 137, 307, 10, 3.1, 85, 0, 55, 0.78, "COOKED", "BAKE", arr("cereal", "iron-rich"), arr("dal", "ghee")));
        allFoods.add(build("Poha (Flattened Rice)", 4.0, true, 333, 6.3, 74.0, 0.4, 0.8, 14, 1.3, 0, 0, 0, 31, 76, 2, 0.7, 7, 0, 70, 0.80, "COOKED", "STEAM", arr("cereal", "light-meal"), arr("vegetables", "lime")));
        allFoods.add(build("Bread (Whole Wheat)", 6.0, true, 247, 13.0, 41.0, 7.0, 3.4, 107, 3.6, 0, 0, 0, 76, 297, 472, 2.5, 43, 0.11, 51, 0.79, "COOKED", "BAKE", arr("cereal", "fiber"), arr("egg", "peanut-butter")));

        // â”€â”€â”€ PULSES & LEGUMES â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        allFoods.add(build("Moong Dal (Split)", 8.0, true, 347, 24.0, 59.0, 16.0, 1.1, 132, 6.7, 4.8, 14, 0, 189, 1246, 15, 2.7, 625, 0.03, 35, 0.85, "COOKED", "BOIL", arr("legume", "protein"), arr("rice", "roti")));
        allFoods.add(build("Masoor Dal (Red Lentil)", 7.0, true, 352, 25.8, 60.1, 10.9, 1.1, 56, 7.6, 3.1, 8, 0, 122, 955, 6, 4.8, 479, 0.02, 32, 0.85, "COOKED", "BOIL", arr("legume", "protein", "iron-rich"), arr("rice", "roti", "spinach")));
        allFoods.add(build("Chana Dal (Bengal Gram)", 6.0, true, 364, 20.1, 61.0, 17.0, 5.0, 203, 5.3, 3.0, 6, 0, 166, 875, 66, 3.4, 557, 0, 30, 0.83, "COOKED", "BOIL", arr("legume", "fiber"), arr("rice", "jeera")));
        allFoods.add(build("Rajma (Kidney Beans)", 10.0, true, 333, 22.9, 60.0, 15.5, 1.5, 143, 8.2, 4.5, 12, 0, 140, 1406, 28, 2.8, 394, 0.07, 29, 0.82, "COOKED", "BOIL", arr("legume", "protein"), arr("rice", "roti")));
        allFoods.add(build("Chana (Chickpeas)", 8.0, true, 364, 19.3, 60.7, 17.4, 6.0, 105, 6.2, 4.0, 9, 0, 115, 875, 24, 3.4, 557, 0.10, 28, 0.84, "COOKED", "BOIL", arr("legume", "protein", "fiber"), arr("rice", "lemon", "spinach")));
        allFoods.add(build("Soybean", 10.0, true, 446, 36.5, 30.2, 9.3, 19.9, 277, 15.7, 6.0, 5, 0, 280, 1797, 2, 4.9, 375, 0.60, 16, 0.88, "COOKED", "BOIL", arr("legume", "complete-protein", "omega3"), arr("rice", "vegetables")));
        allFoods.add(build("Peanuts", 10.0, true, 567, 26.0, 16.0, 8.5, 49.0, 92, 4.6, 0, 0, 0, 168, 705, 18, 3.3, 240, 0.003, 14, 0.82, "BOTH", "RAW", arr("legume", "protein", "fat"), arr("jaggery", "coconut")));
        allFoods.add(build("Toor Dal (Pigeon Pea)", 9.0, true, 335, 22.3, 57.6, 15.0, 1.7, 73, 5.2, 1.9, 11, 0, 178, 1392, 17, 2.9, 456, 0, 29, 0.84, "COOKED", "BOIL", arr("legume", "protein"), arr("rice", "ghee", "tamarind")));

        // â”€â”€â”€ VEGETABLES â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        allFoods.add(build("Spinach (Palak)", 3.0, true, 23, 2.9, 3.6, 2.2, 0.4, 99, 2.7, 28.1, 469, 0, 79, 558, 79, 0.5, 194, 0.14, 15, 0.65, "BOTH", "STEAM", arr("vegetable", "iron-rich", "vitC", "vitA"), arr("lemon", "dal", "paneer")));
        allFoods.add(build("Broccoli", 15.0, true, 34, 2.8, 6.6, 2.6, 0.4, 47, 0.7, 89.2, 31, 0, 21, 316, 33, 0.4, 63, 0.11, 15, 0.68, "BOTH", "STEAM", arr("vegetable", "vitC", "fiber"), arr("paneer", "olive-oil", "garlic")));
        allFoods.add(build("Carrot", 3.0, true, 41, 0.9, 9.6, 2.8, 0.2, 33, 0.3, 5.9, 835, 0, 12, 320, 69, 0.2, 19, 0, 39, 0.70, "BOTH", "RAW", arr("vegetable", "vitA"), arr("cucumber", "hummus", "daal")));
        allFoods.add(build("Tomato", 5.0, true, 18, 0.9, 3.9, 1.2, 0.2, 10, 0.3, 13.7, 42, 0, 11, 237, 5, 0.2, 15, 0, 30, 0.72, "BOTH", "RAW", arr("vegetable", "vitC", "antioxidant"), arr("dal", "rice", "salad")));
        allFoods.add(build("Onion", 3.0, true, 40, 1.1, 9.3, 1.7, 0.1, 23, 0.2, 7.4, 0, 0, 10, 146, 4, 0.2, 19, 0, 10, 0.75, "BOTH", "RAW", arr("vegetable", "flavour"), arr("dal", "sabzi", "rice")));
        allFoods.add(build("Potato", 2.0, true, 77, 2.0, 17.0, 2.2, 0.1, 12, 0.8, 19.7, 2, 0, 23, 421, 6, 0.3, 15, 0, 78, 0.80, "COOKED", "BOIL", arr("vegetable", "carb", "potassium"), arr("dal", "vegetables", "curd")));
        allFoods.add(build("Sweet Potato", 4.0, true, 86, 1.6, 20.0, 3.0, 0.1, 30, 0.6, 2.4, 709, 0, 25, 337, 55, 0.3, 11, 0, 54, 0.82, "COOKED", "BOIL", arr("vegetable", "vitA", "carb"), arr("dal", "curd", "nuts")));
        allFoods.add(build("Bottle Gourd (Lauki)", 2.0, true, 15, 0.6, 3.4, 0.5, 0.1, 26, 0.2, 10.1, 10, 0, 10, 150, 2, 0.1, 6, 0, 15, 0.72, "COOKED", "STEAM", arr("vegetable", "low-cal", "hydrating"), arr("dal", "roti")));
        allFoods.add(build("Peas (Matar)", 5.0, true, 81, 5.4, 14.5, 5.1, 0.4, 25, 1.5, 40.0, 38, 0, 33, 244, 5, 1.2, 65, 0.05, 51, 0.80, "COOKED", "STEAM", arr("vegetable", "protein", "fiber"), arr("rice", "paneer", "potato")));
        allFoods.add(build("Beetroot", 3.0, true, 43, 1.6, 9.6, 2.8, 0.2, 16, 0.8, 4.9, 2, 0, 23, 325, 78, 0.4, 109, 0, 64, 0.68, "BOTH", "RAW", arr("vegetable", "folate", "iron"), arr("carrot", "lemon", "curd")));
        allFoods.add(build("Cauliflower", 6.0, true, 25, 1.9, 5.0, 2.0, 0.3, 22, 0.4, 48.2, 1, 0, 15, 299, 30, 0.3, 57, 0.03, 15, 0.70, "BOTH", "STEAM", arr("vegetable", "vitC", "fiber"), arr("peas", "potato", "dal")));
        allFoods.add(build("Capsicum (Bell Pepper)", 10.0, true, 31, 1.0, 6.0, 2.1, 0.3, 7, 0.4, 127.7, 18, 0, 10, 211, 4, 0.3, 46, 0, 25, 0.70, "BOTH", "RAW", arr("vegetable", "vitC"), arr("paneer", "chicken", "salad")));
        allFoods.add(build("Mushroom", 15.0, true, 22, 3.1, 3.3, 1.0, 0.3, 3, 0.5, 2.1, 0, 0.04, 9, 318, 5, 0.5, 17, 0.04, 15, 0.75, "COOKED", "STEAM", arr("vegetable", "protein", "vitD"), arr("garlic", "paneer", "chicken")));
        allFoods.add(build("Lady Finger (Okra)", 5.0, true, 33, 1.9, 7.5, 3.2, 0.2, 82, 0.6, 23.0, 36, 0, 57, 299, 7, 0.6, 88, 0, 20, 0.70, "COOKED", "STEAM", arr("vegetable", "fiber", "vitC"), arr("dal", "roti", "tomato")));
        allFoods.add(build("Drumstick (Moringa)", 5.0, true, 64, 9.4, 8.3, 2.0, 1.4, 185, 4.0, 141.4, 378, 0, 147, 337, 42, 0.6, 40, 0, 25, 0.72, "COOKED", "STEAM", arr("vegetable", "vitA", "iron", "calcium-rich"), arr("dal", "coconut")));
        allFoods.add(build("Cucumber", 2.0, true, 15, 0.7, 3.6, 0.5, 0.1, 16, 0.3, 2.8, 5, 0, 13, 147, 2, 0.2, 7, 0, 15, 0.68, "RAW", "RAW", arr("vegetable", "hydrating", "low-cal"), arr("curd", "carrot", "tomato")));
        allFoods.add(build("Bitter Gourd (Karela)", 4.0, true, 17, 1.0, 3.7, 2.8, 0.2, 19, 0.4, 84.0, 24, 0, 17, 296, 5, 0.8, 72, 0, 18, 0.68, "COOKED", "STEAM", arr("vegetable", "diabetic-friendly", "vitC"), arr("onion", "dal")));

        // â”€â”€â”€ DAIRY & EGGS â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        allFoods.add(build("Milk (Cow)", 6.0, true, 61, 3.2, 4.8, 0, 3.3, 113, 0.1, 0.9, 46, 0.35, 10, 150, 43, 0.4, 5, 0, 40, 0.92, "BOTH", "BOIL", arr("dairy", "calcium-rich", "protein", "vitB12"), arr("oats", "banana", "dal")));
        allFoods.add(build("Curd (Yogurt)", 5.0, true, 61, 3.5, 4.7, 0, 3.3, 121, 0.1, 0.5, 25, 0.28, 11, 155, 46, 0.5, 7, 0, 36, 0.91, "RAW", "RAW", arr("dairy", "probiotic", "protein"), arr("spinach", "roti", "rice")));
        allFoods.add(build("Paneer", 30.0, true, 265, 18.3, 1.2, 0, 20.8, 208, 0.2, 0.1, 52, 0.23, 23, 91, 92, 2.6, 7, 0.12, 25, 0.90, "COOKED", "STEAM", arr("dairy", "protein", "calcium-rich"), arr("spinach", "peas", "roti")));
        allFoods.add(build("Egg", 7.0, false, 155, 13.0, 1.1, 0, 11.0, 56, 1.2, 0, 149, 1.11, 12, 138, 124, 1.3, 44, 0.04, 35, 0.91, "COOKED", "BOIL", arr("complete-protein", "vitB12", "vitA"), arr("bread", "spinach", "milk")));
        allFoods.add(build("Ghee", 70.0, true, 900, 0, 0, 0, 99.8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.02, 60, 0.95, "COOKED", "RAW", arr("dairy", "fat", "fat-soluble-vit-carrier"), arr("dal", "roti", "rice")));
        allFoods.add(build("Cheese", 40.0, false, 402, 25.0, 1.3, 0, 33.0, 721, 0.7, 0, 265, 0.83, 28, 96, 620, 3.5, 18, 0.05, 40, 0.88, "BOTH", "RAW", arr("dairy", "calcium-rich", "protein", "vitB12"), arr("bread", "egg", "vegetables")));
        allFoods.add(build("Buttermilk (Chaas)", 2.0, true, 40, 3.3, 5.0, 0, 0.9, 116, 0.1, 0.5, 25, 0.22, 10, 151, 105, 0.4, 5, 0, 40, 0.90, "RAW", "RAW", arr("dairy", "probiotic", "low-cal"), arr("jeera", "dal", "rice")));

        // â”€â”€â”€ FRUITS â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        allFoods.add(build("Banana", 2.0, true, 89, 1.1, 22.8, 2.6, 0.3, 5, 0.3, 8.7, 3, 0, 27, 358, 1, 0.2, 20, 0.03, 52, 0.82, "RAW", "RAW", arr("fruit", "potassium", "carb", "energy"), arr("oats", "milk", "peanuts")));
        allFoods.add(build("Apple", 20.0, true, 52, 0.3, 14.0, 2.4, 0.2, 6, 0.1, 4.6, 3, 0, 5, 107, 1, 0.04, 3, 0.01, 38, 0.75, "RAW", "RAW", arr("fruit", "fiber", "antioxidant"), arr("peanut-butter", "curd", "oats")));
        allFoods.add(build("Orange", 6.0, true, 47, 0.9, 11.8, 2.4, 0.1, 40, 0.1, 53.2, 11, 0, 10, 181, 0, 0.1, 30, 0.01, 45, 0.78, "RAW", "RAW", arr("fruit", "vitC"), arr("spinach", "dal", "salad")));
        allFoods.add(build("Papaya", 4.0, true, 43, 0.5, 10.8, 1.7, 0.3, 20, 0.1, 60.9, 47, 0, 21, 182, 8, 0.1, 37, 0.01, 59, 0.76, "RAW", "RAW", arr("fruit", "vitC", "vitA", "digestive"), arr("lime", "honey", "curd")));
        allFoods.add(build("Guava", 5.0, true, 68, 2.6, 14.3, 5.4, 1.0, 18, 0.3, 228.3, 31, 0, 22, 417, 2, 0.2, 49, 0.01, 31, 0.78, "RAW", "RAW", arr("fruit", "vitC", "fiber"), arr("lime", "salt", "chaat-masala")));
        allFoods.add(build("Mango", 8.0, true, 60, 0.8, 15.0, 1.6, 0.4, 11, 0.2, 36.4, 54, 0, 10, 168, 1, 0.1, 43, 0.02, 56, 0.75, "RAW", "RAW", arr("fruit", "vitA", "vitC", "carb"), arr("curd", "milk", "banana")));
        allFoods.add(build("Pomegranate", 12.0, true, 83, 1.3, 18.7, 4.0, 1.2, 10, 0.3, 10.2, 0, 0, 12, 236, 3, 0.4, 38, 0.05, 35, 0.72, "RAW", "RAW", arr("fruit", "antioxidant", "iron"), arr("salad", "curd", "spinach")));
        allFoods.add(build("Lemon", 2.0, true, 29, 1.1, 9.3, 2.8, 0.3, 26, 0.6, 53.0, 1, 0, 8, 138, 2, 0.1, 11, 0.01, 20, 0.75, "RAW", "RAW", arr("fruit", "vitC", "iron-enhancer"), arr("spinach", "dal", "salad")));

        // â”€â”€â”€ NUTS & SEEDS â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        allFoods.add(build("Almonds", 70.0, true, 579, 21.2, 21.6, 12.5, 49.9, 264, 3.7, 0, 0, 0, 270, 733, 1, 3.1, 44, 0, 0, 0.85, "RAW", "RAW", arr("nut", "vitE", "fat", "protein"), arr("milk", "oats", "banana")));
        allFoods.add(build("Walnuts", 80.0, true, 654, 15.2, 13.7, 6.7, 65.2, 98, 2.9, 1.3, 1, 0, 158, 441, 2, 3.1, 98, 9.08, 15, 0.84, "RAW", "RAW", arr("nut", "omega3", "fat", "protein"), arr("dal", "oats", "salad")));
        allFoods.add(build("Flaxseeds", 15.0, true, 534, 18.3, 28.9, 27.3, 42.2, 255, 5.7, 0.6, 0, 0, 392, 813, 30, 4.3, 87, 22.8, 35, 0.79, "RAW", "RAW", arr("seed", "omega3", "fiber"), arr("curd", "salad", "oats")));
        allFoods.add(build("Sesame Seeds", 20.0, true, 573, 17.7, 23.5, 11.8, 49.7, 975, 14.6, 0, 5, 0, 351, 468, 11, 7.8, 97, 0.38, 55, 0.72, "BOTH", "RAW", arr("seed", "calcium-rich", "fat", "iron"), arr("roti", "dal", "rice")));
        allFoods.add(build("Chia Seeds", 30.0, true, 486, 16.5, 42.1, 34.4, 30.7, 631, 7.7, 1.6, 54, 0, 335, 407, 16, 4.6, 49, 17.83, 1, 0.80, "RAW", "RAW", arr("seed", "omega3", "fiber", "calcium-rich"), arr("water", "oats", "smoothie")));
        allFoods.add(build("Cashews", 50.0, true, 553, 18.2, 30.2, 3.3, 43.9, 37, 6.7, 0.5, 0, 0, 292, 660, 12, 5.8, 25, 0.07, 22, 0.83, "RAW", "RAW", arr("nut", "fat", "protein", "zinc"), arr("curry", "milk", "vegetables")));
        allFoods.add(build("Pumpkin Seeds", 30.0, true, 559, 30.2, 10.7, 6.0, 49.1, 46, 8.1, 0, 16, 0, 592, 809, 7, 7.6, 58, 0.10, 25, 0.82, "RAW", "RAW", arr("seed", "zinc", "protein", "magnesium"), arr("salad", "oats", "dal")));

        // â”€â”€â”€ NON-VEG PROTEINS â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        allFoods.add(build("Chicken Breast", 20.0, false, 165, 31.0, 0, 0, 3.6, 15, 1.0, 0, 9, 0.34, 29, 256, 74, 1.0, 4, 0.04, 0, 0.93, "COOKED", "BOIL", arr("non-veg", "complete-protein", "low-fat"), arr("rice", "vegetables", "roti")));
        allFoods.add(build("Chicken Liver", 15.0, false, 116, 16.9, 0.9, 0, 4.8, 11, 11.0, 27.9, 3296, 16.58, 18, 230, 71, 6.1, 589, 0.40, 0, 0.85, "COOKED", "BOIL", arr("non-veg", "iron-rich", "vitA", "vitB12", "complete-protein"), arr("onion", "tomato", "roti")));
        allFoods.add(build("Egg White", 5.0, false, 52, 10.9, 0.7, 0, 0.2, 7, 0.1, 0, 0, 0.09, 11, 163, 166, 0.1, 4, 0, 0, 0.92, "COOKED", "BOIL", arr("protein", "low-fat", "low-cal"), arr("vegetables", "bread", "salad")));
        allFoods.add(build("Fish (Rohu)", 15.0, false, 97, 17.6, 0, 0, 2.6, 650, 1.0, 0, 0, 2.10, 22, 275, 72, 0.7, 15, 0.31, 0, 0.90, "COOKED", "STEAM", arr("non-veg", "protein", "omega3", "calcium-rich"), arr("rice", "dal", "vegetables")));
        allFoods.add(build("Fish (Tuna)", 25.0, false, 132, 28.0, 0, 0, 1.0, 16, 1.0, 0, 655, 9.43, 30, 279, 43, 0.8, 2, 0.25, 0, 0.92, "COOKED", "STEAM", arr("non-veg", "protein", "omega3", "vitD"), arr("rice", "vegetables", "lemon")));
        allFoods.add(build("Mutton (Goat)", 30.0, false, 122, 25.6, 0, 0, 2.1, 13, 2.2, 0, 0, 2.35, 22, 283, 72, 3.8, 8, 0.47, 0, 0.88, "COOKED", "BOIL", arr("non-veg", "iron-rich", "protein", "zinc"), arr("rice", "roti", "dal")));
        allFoods.add(build("Prawns/Shrimp", 25.0, false, 99, 24.0, 0.2, 0, 0.3, 52, 2.4, 2.0, 54, 1.11, 39, 259, 119, 1.6, 37, 0.29, 0, 0.90, "COOKED", "STEAM", arr("non-veg", "protein", "zinc"), arr("rice", "vegetables", "lemon")));

        // â”€â”€â”€ SPICES & CONDIMENTS â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        allFoods.add(build("Turmeric", 15.0, true, 354, 7.8, 64.9, 21.1, 9.9, 183, 41.4, 25.9, 0, 0, 193, 2525, 38, 4.4, 39, 0, 0, 0.55, "COOKED", "BOIL", arr("spice", "anti-inflammatory"), arr("milk", "dal", "rice")));
        allFoods.add(build("Garlic", 15.0, true, 149, 6.4, 33.1, 2.1, 0.5, 181, 1.7, 31.2, 0, 0, 25, 401, 17, 1.2, 3, 0, 30, 0.70, "BOTH", "RAW", arr("spice", "immune", "anti-inflammatory"), arr("dal", "vegetables", "chicken")));
        allFoods.add(build("Ginger", 8.0, true, 80, 1.8, 17.8, 2.0, 0.8, 16, 0.6, 5.0, 0, 0, 43, 415, 13, 0.3, 11, 0, 15, 0.65, "BOTH", "RAW", arr("spice", "digestive", "anti-nausea"), arr("dal", "tea", "vegetables")));
        allFoods.add(build("Amla (Indian Gooseberry)", 4.0, true, 44, 0.9, 10.2, 4.3, 0.6, 50, 0.3, 600.0, 15, 0, 10, 198, 1, 0.3, 6, 0, 25, 0.75, "RAW", "RAW", arr("fruit", "vitC", "antioxidant", "iron-enhancer"), arr("honey", "spinach", "dal")));

        // â”€â”€â”€ OILS & SWEETENERS â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        allFoods.add(build("Coconut Oil", 20.0, true, 890, 0, 0, 0, 100.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 80, 0.90, "COOKED", "RAW", arr("fat", "mct"), arr("dal", "vegetables", "rice")));
        allFoods.add(build("Olive Oil", 60.0, true, 884, 0, 0, 0, 100.0, 0, 0.6, 0, 0, 0, 0, 1, 2, 0, 0, 0.76, 55, 0.90, "BOTH", "RAW", arr("fat", "omega9", "heart-healthy"), arr("salad", "vegetables", "dal")));
        allFoods.add(build("Jaggery", 5.0, true, 383, 0.4, 98.0, 0, 0.1, 80, 11.4, 7.5, 0, 0, 160, 1056, 30, 0.8, 0, 0, 84, 0.85, "RAW", "RAW", arr("sweetener", "iron", "energy"), arr("roti", "dal", "banana")));
        allFoods.add(build("Honey", 30.0, true, 304, 0.3, 82.4, 0.2, 0, 6, 0.4, 0.5, 0, 0, 2, 52, 4, 0.2, 2, 0, 58, 0.88, "RAW", "RAW", arr("sweetener", "antioxidant"), arr("oats", "milk", "lemon")));
        allFoods.add(build("Sugar", 4.0, true, 387, 0, 100.0, 0, 0, 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 65, 0.95, "RAW", "RAW", arr("sweetener", "high-gi"), arr("tea", "milk")));

        // â”€â”€â”€ BEVERAGES / MISC â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        allFoods.add(build("Coconut Water", 2.0, true, 19, 0.7, 3.7, 1.1, 0.2, 24, 0.3, 2.4, 0, 0, 25, 250, 105, 0.1, 3, 0, 54, 0.85, "RAW", "RAW", arr("beverage", "electrolyte", "potassium"), arr("lemon", "banana")));
        allFoods.add(build("Soybeans Tofu", 15.0, true, 76, 8.1, 1.9, 0.3, 4.8, 350, 5.4, 0.1, 0, 0, 30, 121, 7, 0.8, 15, 0.24, 14, 0.86, "COOKED", "STEAM", arr("legume", "complete-protein", "calcium-rich"), arr("vegetables", "rice", "roti")));
        allFoods.add(build("Green Tea", 5.0, true, 1, 0.2, 0.2, 0, 0, 0, 0, 0, 0, 0, 1, 8, 1, 0, 0, 0, 28, 0.80, "RAW", "RAW", arr("beverage", "antioxidant"), arr("lemon", "honey")));
        allFoods.add(build("Pineapple", 5.0, true, 50, 0.5, 13.1, 1.4, 0.1, 13, 0.3, 47.8, 3, 0, 12, 109, 1, 0.1, 18, 0.07, 59, 0.75, "RAW", "RAW", arr("fruit", "vitC", "digestive-enzyme"), arr("curd", "salad", "smoothie")));
        allFoods.add(build("Beetroot Leaves", 3.0, true, 19, 1.9, 4.3, 3.5, 0.1, 114, 2.6, 11.0, 183, 0, 33, 396, 106, 0.3, 15, 0, 11, 0.68, "COOKED", "STEAM", arr("vegetable", "vitA", "calcium-rich", "iron"), arr("dal", "roti", "lemon")));
        allFoods.add(build("Corn (Maize)", 3.0, true, 365, 9.4, 74.3, 7.3, 4.7, 7, 2.7, 0, 11, 0, 127, 287, 35, 2.2, 19, 0.08, 70, 0.78, "COOKED", "BOIL", arr("cereal", "carb", "fiber"), arr("butter", "dal", "salad")));
        allFoods.add(build("Sunflower Seeds", 15.0, true, 584, 20.8, 20.0, 8.6, 51.5, 78, 5.3, 1.4, 1, 0, 325, 645, 9, 5.0, 227, 0.07, 35, 0.83, "RAW", "RAW", arr("seed", "vitE", "magnesium", "protein"), arr("salad", "oats", "yogurt")));
        allFoods.add(build("Sesame (Til) Chikki", 8.0, true, 498, 11.8, 53.0, 3.5, 29.0, 570, 8.5, 0, 3, 0, 185, 280, 7, 3.9, 60, 0.18, 55, 0.70, "BOTH", "RAW", arr("snack", "calcium-rich", "iron", "energy"), arr("milk", "banana", "tea")));
        allFoods.add(build("Poppy Seeds", 30.0, true, 525, 17.99, 28.1, 19.5, 41.6, 1438, 9.8, 1.0, 0, 0, 347, 719, 26, 7.9, 87, 0, 20, 0.68, "BOTH", "RAW", arr("seed", "calcium-rich", "magnesium"), arr("roti", "dal", "curry")));
        allFoods.add(build("Kala Chana", 7.0, true, 364, 22.5, 60.5, 12.2, 5.0, 202, 4.9, 4.0, 7, 0, 165, 860, 25, 3.0, 557, 0, 29, 0.82, "COOKED", "BOIL", arr("legume", "protein", "fiber"), arr("rice", "roti", "onion")));
        allFoods.add(build("Kale Fish", 0, false, 91, 18.0, 0, 0, 1.2, 74, 0.9, 0, 15, 3.10, 27, 401, 61, 0.5, 12, 0.28, 0, 0.88, "COOKED", "STEAM", arr("non-veg", "vitB12", "protein"), arr("rice", "lemon", "onion")));
        allFoods.add(build("Horse Gram (Kulthi)", 5.0, true, 321, 22.0, 57.2, 5.3, 0, 287, 6.77, 0, 0, 0, 100, 0, 277, 3.1, 0, 0, 0, 0.82, "COOKED", "BOIL", arr("legume", "protein", "calcium-rich", "iron-rich"), arr("rice", "roti")));
        allFoods.add(build("Methi (Fenugreek Leaves)", 3.0, true, 49, 4.4, 6.0, 6.7, 0.9, 395, 1.9, 1.2, 395, 0, 191, 770, 76, 1.0, 57, 0, 25, 0.65, "COOKED", "STEAM", arr("vegetable", "iron-rich", "calcium-rich", "diabetic-friendly"), arr("dal", "roti", "rice")));
        allFoods.add(build("Groundnut Oil", 15.0, true, 884, 0, 0, 0, 100.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 60, 0.90, "COOKED", "RAW", arr("fat", "vitE"), arr("stir-fry", "dal", "vegetables")));
        allFoods.add(build("Ragi (Finger Millet)", 3.0, true, 336, 7.3, 72.9, 3.6, 1.5, 364, 3.9, 0, 0, 0, 137, 408, 11, 2.5, 18, 0, 68, 0.78, "COOKED", "BOIL", arr("cereal", "calcium-rich", "gluten-free", "iron-rich"), arr("milk", "dal", "vegetables")));
        allFoods.add(build("Amaranth Leaves", 5.0, true, 23, 2.5, 4.0, 0.2, 0.3, 215, 2.3, 43.3, 292, 0, 55, 611, 20, 0.9, 85, 0, 15, 0.70, "COOKED", "STEAM", arr("vegetable", "calcium-rich", "vitC", "iron"), arr("dal", "roti", "lemon")));
        allFoods.add(build("Tamarind", 5.0, true, 239, 2.8, 62.5, 5.1, 0.6, 74, 2.8, 3.5, 2, 0, 92, 628, 28, 0.1, 14, 0, 23, 0.72, "BOTH", "RAW", arr("condiment", "iron", "vitB"), arr("dal", "rice", "chutney")));
        allFoods.add(build("Tempeh", 25.0, true, 193, 20.3, 9.4, 0, 10.8, 111, 2.7, 0, 0, 0.08, 81, 412, 9, 1.1, 24, 0.51, 0, 0.86, "COOKED", "STEAM", arr("legume", "complete-protein", "probiotic"), arr("rice", "vegetables", "roti")));
        allFoods.add(build("Soya Sauce", 1.0, false, 53, 7.3, 5.6, 0.4, 0.6, 130, 2.4, 0, 0, 0.08, 141, 478, 5629, 0.7, 7, 0, 25, 0.65, "COOKED", "RAW", arr("condiment", "sodium"), arr("rice", "stir-fry")));
        allFoods.add(build("Olive", 30.0, true, 145, 1.0, 3.8, 3.3, 15.3, 88, 3.3, 0, 0, 0, 11, 8, 735, 0.2, 0, 0, 0, 0.80, "BOTH", "RAW", arr("fat", "vitE", "antioxidant"), arr("salad", "bread", "pasta")));
        allFoods.add(build("Coconut (Fresh)", 5.0, true, 354, 3.3, 15.2, 9.0, 33.5, 14, 2.4, 3.3, 0, 0, 32, 356, 20, 1.1, 26, 0, 45, 0.82, "BOTH", "RAW", arr("fruit", "fat", "mct", "fiber"), arr("chutney", "curry", "rice")));
        allFoods.add(build("Capsicum Green", 5.0, true, 20, 0.9, 4.6, 1.7, 0.2, 10, 0.4, 80.4, 18, 0, 10, 175, 3, 0.3, 10, 0, 15, 0.72, "BOTH", "RAW", arr("vegetable", "vitC", "low-cal"), arr("paneer", "chicken", "salad")));
        allFoods.add(build("Pear", 10.0, true, 57, 0.4, 15.2, 3.1, 0.1, 9, 0.2, 4.3, 1, 0, 7, 116, 1, 0.1, 7, 0.01, 38, 0.75, "RAW", "RAW", arr("fruit", "fiber"), arr("curd", "apple", "honey")));
        allFoods.add(build("Mosambi (Sweet Lime)", 5.0, true, 43, 0.8, 9.3, 0.5, 0.3, 40, 0.7, 50.0, 1, 0, 12, 490, 2, 0.2, 30, 0.01, 43, 0.76, "RAW", "RAW", arr("fruit", "vitC"), arr("salad", "salt", "dal")));
        allFoods.add(build("Soya Milk", 5.0, true, 54, 3.3, 6.3, 0.5, 1.8, 25, 0.5, 0, 0, 0, 25, 118, 51, 0.3, 15, 0.20, 34, 0.84, "RAW", "BOIL", arr("dairy-alternative", "protein", "vitB12"), arr("oats", "cereal", "banana")));
    }

    // â”€â”€â”€ BUILDER HELPER â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    private FoodItem build(String name, double cost, boolean veg,
                           double cal, double prot, double carb, double fib, double fat,
                           double ca, double fe, double vitC, double vitA, double vitB12,
                           double mg, double k, double na, double zn, double folate,
                           double omega3, double gi, double absRate,
                           String bestEatenAs, String cookingMethod,
                           String[] groups, String[] synergy) {
        return FoodItem.builder()
                .name(name).costPer100g(cost).vegetarian(veg)
                .caloriesKcal(cal).proteinG(prot).carbsG(carb).fiberG(fib).fatG(fat)
                .calciumMg(ca).ironMg(fe).vitaminCMg(vitC).vitaminAMcg(vitA)
                .vitaminB12Mcg(vitB12).magnesiumMg(mg).potassiumMg(k).sodiumMg(na)
                .zincMg(zn).folateMcg(folate).omega3G(omega3)
                .glycemicIndex(gi).absorptionRate(absRate)
                .bestEatenAs(bestEatenAs).bestCookingMethod(cookingMethod)
                .foodGroups(groups).synergisticWith(synergy)
                .build();
    }

    private String[] arr(String... s) { return s; }

    // â”€â”€â”€ PUBLIC API â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
    public List<FoodItem> getAllFoods() { return Collections.unmodifiableList(allFoods); }

    public List<FoodItem> getVegFoods() {
        return allFoods.stream().filter(FoodItem::isVegetarian).toList();
    }

    public List<FoodItem> getNonVegFoods() {
        return allFoods.stream().filter(f -> !f.isVegetarian()).toList();
    }

    public List<FoodItem> getFoodsByGroup(String group) {
        return allFoods.stream()
                .filter(f -> f.getFoodGroups() != null &&
                        Arrays.asList(f.getFoodGroups()).contains(group))
                .toList();
    }

    public Optional<FoodItem> findByName(String name) {
        return allFoods.stream()
                .filter(f -> f.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<FoodItem> searchByNutrient(String nutrientType) {
        return switch (nutrientType.toLowerCase()) {
            case "high_protein"  -> allFoods.stream().filter(f -> f.getProteinG() >= 10).toList();
            case "high_fiber"    -> allFoods.stream().filter(f -> f.getFiberG() >= 5).toList();
            case "high_calcium"  -> allFoods.stream().filter(f -> f.getCalciumMg() >= 100).toList();
            case "high_iron"     -> allFoods.stream().filter(f -> f.getIronMg() >= 3.0).toList();
            case "high_vitc"     -> allFoods.stream().filter(f -> f.getVitaminCMg() >= 30).toList();
            case "high_omega3"   -> allFoods.stream().filter(f -> f.getOmega3G() >= 0.5).toList();
            case "low_calorie"   -> allFoods.stream().filter(f -> f.getCaloriesKcal() <= 50).toList();
            case "vitamin_rich"  -> allFoods.stream().filter(f -> f.getVitaminAMcg() > 50 || f.getVitaminCMg() > 30).toList();
            default              -> allFoods;
        };
    }
}

