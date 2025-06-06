package com.pekaboo.entities;

public class Reservasi {
    private int idReservasi;
    private String statusReservasi;
    private User optometris;  // userStatus = "OPTOMETRIS"
    private User pelanggan;   // userStatus = "PELANGGAN"
    private Jadwal jadwal;

    public Reservasi() {}

    public Reservasi(int idReservasi, String statusReservasi, User optometris, User pelanggan, Jadwal jadwal) {
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

    public String getStatusReservasi() {
        return statusReservasi;
    }

    public void setStatusReservasi(String statusReservasi) {
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
}
