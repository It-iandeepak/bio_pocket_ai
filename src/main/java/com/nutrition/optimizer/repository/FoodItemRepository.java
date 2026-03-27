package com.nutrition.optimizer.repository;

import com.nutrition.optimizer.entity.FoodItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository contract for FoodItem data access.
 *
 * <p>The current data source is the in-memory {@code FoodDataService}.
 * Implement this interface (e.g. with JPA, MongoDB, or an external API)
 * to switch persistence layers without touching service code.</p>
 */
@Repository
public interface FoodItemRepository {

    /** Return all available food items. */
    List<FoodItem> findAll();

    /** Find a food item by its exact name (case-insensitive). */
    Optional<FoodItem> findByName(String name);

    /** Return all vegetarian food items. */
    List<FoodItem> findByVegetarian(boolean vegetarian);

    /** Return food items belonging to the given food group. */
    List<FoodItem> findByFoodGroup(String foodGroup);
}
