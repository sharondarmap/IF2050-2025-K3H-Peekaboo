package com.pekaboo.util;

public class checkout {
    private static int quantity;
    
    /**
     * Sets the quantity for the checkout
     * 
     * @param quantity The quantity to set
     */
    public static void setQuantity(int quantity) {
        checkout.quantity = quantity;
    }
    
    /**
     * Gets the current quantity
     * 
     * @return The current quantity
     */
    public static int getQuantity() {
        return quantity;
    }
}
