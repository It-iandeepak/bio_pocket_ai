package com.nutrition.optimizer.exception;

/**
 * Thrown when a requested resource (food item, user profile, etc.) cannot be found.
 */
public class ResourceNotFoundException extends NutritionException {

    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(resourceName + " not found with identifier: " + identifier);
    }
}
