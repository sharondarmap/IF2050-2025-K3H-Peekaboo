package com.pekaboo;

import com.pekaboo.entities.Jadwal;
import com.pekaboo.entities.StatusJadwal;
import com.pekaboo.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Jadwal Unit Test")
class JadwalTest {
    
    @Test
    @DisplayName("Should create jadwal with correct properties")
    void shouldCreateJadwalWithCorrectProperties() {
        // Arrange
        User optometris = new User(1, "dr_dinda", "password", "dinda@example.com", "081111111111", 
                                  null, null, null, "OPTOMETRIS");
        
        Jadwal jadwal = new Jadwal(1, optometris);
        jadwal.setTanggal(LocalDate.of(2024, 12, 25));
        jadwal.setJamMulai(LocalTime.of(10, 0));
        jadwal.setJamSelesai(LocalTime.of(11, 0));
        
        // Act & Assert
        assertEquals(1, jadwal.getIdJadwal());
        assertEquals(optometris, jadwal.getOptometris());
        assertEquals(StatusJadwal.AVAILABLE, jadwal.getStatusJadwal());
        assertEquals(LocalDate.of(2024, 12, 25), jadwal.getTanggal());
        assertEquals(LocalTime.of(10, 0), jadwal.getJamMulai());
        assertEquals(LocalTime.of(11, 0), jadwal.getJamSelesai());
    }
    
    @Test
    @DisplayName("Should check jadwal availability correctly")
    void shouldCheckJadwalAvailabilityCorrectly() {
        // Arrange
        User optometris = new User(1, "dr_dinda", "password", "dinda@example.com", "081111111111", 
                                  null, null, null, "OPTOMETRIS");
        Jadwal jadwal = new Jadwal(1, optometris);
        
        // Act & Assert - (awalnya available)
        assertTrue(jadwal.isAvailable());
        assertFalse(jadwal.isReserved());
        
        // Ganti status jadi reserved
        jadwal.setStatusJadwal(StatusJadwal.RESERVED);
        assertFalse(jadwal.isAvailable());
        assertTrue(jadwal.isReserved());
    }
    
    @Test
    @DisplayName("Should update jadwal time correctly")
    void shouldUpdateJadwalTimeCorrectly() {
        // Arrange
        User optometris = new User(1, "dr_dinda", "password", "dinda@example.com", "081111111111", 
                                  null, null, null, "OPTOMETRIS");
        Jadwal jadwal = new Jadwal(1, optometris);
        
        // Act
        jadwal.setJamMulai(LocalTime.of(14, 30));
        jadwal.setJamSelesai(LocalTime.of(15, 30));
        
        // Assert
        assertEquals(LocalTime.of(14, 30), jadwal.getJamMulai());
        assertEquals(LocalTime.of(15, 30), jadwal.getJamSelesai());
    }
}