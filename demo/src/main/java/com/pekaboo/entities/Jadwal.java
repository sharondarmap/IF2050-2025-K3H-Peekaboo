package com.pekaboo.entities;

import java.io.ObjectInputFilter.Status;
import java.time.LocalDate;
import java.time.LocalTime;

public class Jadwal {
    private int idJadwal;
    private StatusJadwal statusJadwal;
    private User optometris; 
    
    private LocalDate tanggal;
    private LocalTime jamMulai;
    private LocalTime jamSelesai;
    public static final LocalTime JAM_BUKA_TOKO = LocalTime.of(10, 0); // Default jam buka toko hardcode

    // Constructor, getter, setter
    public Jadwal(int idJadwal, User optometris){
        this.idJadwal = idJadwal;
        this.optometris = optometris;
        this.statusJadwal = StatusJadwal.AVAILABLE;
    }

    public Jadwal (){}

    public int getIdJadwal(){
        return idJadwal;
    }
    public StatusJadwal getStatusJadwal(){
        return statusJadwal;
    }
    public User getOptometris(){
        return optometris;
    }
    public LocalDate getTanggal() {
        return tanggal;
    }
    public LocalTime getJamMulai() {
        return jamMulai;
    }
    public LocalTime getJamSelesai() {
        return jamSelesai;
    }
    public String getOptometristName() { 
        return optometris != null ? optometris.getUsername() : "Unknown"; 
    }
    public boolean isReserved() { 
        return this.statusJadwal == StatusJadwal.RESERVED; 
    }
    public boolean isAvailable() { 
        return this.statusJadwal == StatusJadwal.AVAILABLE;
    }

    public void setIdJadwal(int idJadwal){
        this.idJadwal = idJadwal;
    }
    public void setStatusJadwal(StatusJadwal statusJadwal){
        this.statusJadwal = statusJadwal;
    }
    public void setOptometris(User optometris){
        this.optometris = optometris;
    }
    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }
    public void setJamMulai(LocalTime jamMulai) {
        this.jamMulai = jamMulai;
    }
    public void setJamSelesai(LocalTime jamSelesai) {
        this.jamSelesai = jamSelesai;
    }
    public void setReserved(boolean isReserved) {
        this.statusJadwal = isReserved ? StatusJadwal.RESERVED : StatusJadwal.AVAILABLE;
    }
}