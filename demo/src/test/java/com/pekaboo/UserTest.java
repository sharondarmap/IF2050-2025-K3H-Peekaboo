package com.pekaboo;

import com.pekaboo.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Unit Test")
class UserTest {
    
    @Test
    @DisplayName("Should create user with correct properties")
    void shouldCreateUserWithCorrectProperties() {
        // Arrange
        User user = new User(1, "sharon", "password123", "sharon@example.com", "081234567890",
                           null, null, null, "PELANGGAN");
        
        // Act & Assert
        assertEquals(1, user.getIdUser());
        assertEquals("sharon", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("sharon@example.com", user.getEmail());
        assertEquals("081234567890", user.getNoTelepon());
        assertEquals("PELANGGAN", user.getUserStatus());
    }
    
    @Test
    @DisplayName("Should update user email correctly")
    void shouldUpdateUserEmailCorrectly() {
        // Arrange
        User user = new User(1, "sharon", "password123", "sharon@example.com", "081234567890",
                           null, null, null, "PELANGGAN");
        
        // Act
        user.setEmail("newemail@example.com");
        
        // Assert
        assertEquals("newemail@example.com", user.getEmail());
    }
}