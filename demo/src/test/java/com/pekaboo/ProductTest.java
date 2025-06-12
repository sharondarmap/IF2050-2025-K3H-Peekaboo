package com.pekaboo;

import com.pekaboo.entities.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Unit Test")
class ProductTest {
    
    @Test
    @DisplayName("Should create product with correct properties")
    void shouldCreateProductWithCorrectProperties() {
        // Arrange
        Product product = new Product(1, 10, 150000, "Kacamata Jennie", "Celine", "Hitam", "M", "/com/pekaboo/images/kacamata1.png");
        
        // Act & Assert
        assertEquals(1, product.getId());
        assertEquals(10, product.getStock());
        assertEquals(150000, product.getPrice());
        assertEquals("Kacamata Jennie", product.getName());
        assertEquals("Celine", product.getBrand());
        assertEquals("Hitam", product.getColor());
        assertEquals("M", product.getSize());
        assertEquals("/com/pekaboo/images/kacamata1.png", product.getImagePath());
    }
    
    @Test
    @DisplayName("Should update product stock correctly")
    void shouldUpdateProductStockCorrectly() {
        // Arrange
        Product product = new Product(2, 15, 200000, "Kacamata Lisa", "Chanel", "Coklat", "L", "/com/pekaboo/images/kacamata2.png");
        
        // Act
        product.setStock(25);
        
        // Assert
        assertEquals(25, product.getStock());
        assertEquals("/com/pekaboo/images/kacamata2.png", product.getImagePath());
    }
}