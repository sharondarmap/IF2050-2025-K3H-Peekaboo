package com.pekaboo.entities;

import java.time.LocalDateTime;

public class Reservasi {
    private int idReservasi;
    private User optometris;  // userStatus = "OPTOMETRIS"
    private User pelanggan;   // userStatus = "PELANGGAN"
    private Jadwal jadwal;
    private StatusReservasi statusReservasi;
    private LocalDateTime tanggalReservasi;

    public Reservasi() {}

    public Reservasi(int idReservasi, StatusReservasi statusReservasi, User optometris, User pelanggan, Jadwal jadwal) {
        this.idReservasi = idReservasi;
        this.statusReservasi = statusReservasi;
        this.optometris = optometris;
        this.pelanggan = pelanggan;
        this.jadwal = jadwal;
    }

    public int getIdReservasi() {
        return idReservasi;
    }

    public void setIdReservasi(int idReservasi) {
        this.idReservasi = idReservasi;
    }

    public StatusReservasi getStatusReservasi() {
        return statusReservasi;
    }

    public void setStatusReservasi(StatusReservasi statusReservasi) {
        this.statusReservasi = statusReservasi;
    }

    public User getOptometris() {
        return optometris;
    }

    public void setOptometris(User optometris) {
        this.optometris = optometris;
    }

    public User getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(User pelanggan) {
        this.pelanggan = pelanggan;
    }

    public Jadwal getJadwal() {
        return jadwal;
    }

    public void setJadwal(Jadwal jadwal) {
        this.jadwal = jadwal;
    }

    public LocalDateTime getTanggalReservasi() {
        return tanggalReservasi;
    }
    
    public void setTanggalReservasi(LocalDateTime tanggalReservasi) {
        this.tanggalReservasi = tanggalReservasi;
    }
}
