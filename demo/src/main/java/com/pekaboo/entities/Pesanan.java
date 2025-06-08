package com.pekaboo.entities;

public class Pesanan {
    private int idPesanan;
    private int totalPesanan;
    private String tanggalPesanan;
    private String alamatPesanan;
    private int idPelangganPemesan;
    private int idProdukPesanan;

    public Pesanan() {}

    public Pesanan(int idPesanan, int totalPesanan, String tanggalPesanan, String alamatPesanan, int idPelangganPemesan, int idProdukPesanan) {
        this.idPesanan = idPesanan;
        this.totalPesanan = totalPesanan;
        this.tanggalPesanan = tanggalPesanan;
        this.alamatPesanan = alamatPesanan;
        this.idPelangganPemesan = idPelangganPemesan;
        this.idProdukPesanan = idProdukPesanan;
    }

    public int getIdPesanan() {
        return idPesanan;
    }

    public int getTotalPesanan() {
        return totalPesanan;
    }

    public String getTanggalPesanan() {
        return tanggalPesanan;
    }

    public String getAlamatPesanan() {
        return alamatPesanan;
    }

    public int getIdPelangganPemesan() {
        return idPelangganPemesan;
    }

    public int getIdProdukPesanan() {
        return idProdukPesanan;
    }

    public void setTotalPesanan(int totalPesanan) {
        this.totalPesanan = totalPesanan;
    }

    public void setTanggalPesanan(String tanggalPesanan) {
        this.tanggalPesanan = tanggalPesanan;
    }

    public void setAlamatPesanan(String alamatPesanan) {
        this.alamatPesanan = alamatPesanan;
    }

    public void setIdPelangganPemesan(int idPelangganPemesan) {
        this.idPelangganPemesan = idPelangganPemesan;
    }

    public void setIdProdukPesanan(int idProdukPesanan) {
        this.idProdukPesanan = idProdukPesanan;
    }
}
