package com.nutrition.optimizer.exception;

/**
 * Base exception for the Nutrition Optimizer domain.
 */
public class NutritionException extends RuntimeException {

    public NutritionException(String message) {
        super(message);
    }

    public NutritionException(String message, Throwable cause) {
        super(message, cause);
    }
}
