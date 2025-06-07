package com.pekaboo.entities;

public class Resep {
    private int idResep;
    private double plusKanan;
    private double plusKiri;
    private double minusKanan;
    private double minusKiri;
    private double cylKanan;
    private double cylKiri;
    private double axisKanan;
    private double axisKiri;
    private double pd;
    
    private User pelanggan;
    private User optometris;
    private Jadwal jadwal;

    public Resep() {}

    public Resep(double plusKanan, double plusKiri, double minusKanan, double minusKiri,
                 double cylKanan, double cylKiri, double axisKanan, double axisKiri,
                 double pd, User pelanggan, User optometris, Jadwal jadwal) {
        this.plusKanan = plusKanan;
        this.plusKiri = plusKiri;
        this.minusKanan = minusKanan;
        this.minusKiri = minusKiri;
        this.cylKanan = cylKanan;
        this.cylKiri = cylKiri;
        this.axisKanan = axisKanan;
        this.axisKiri = axisKiri;
        this.pd = pd;
        this.pelanggan = pelanggan;
        this.optometris = optometris;
        this.jadwal = jadwal;
    }

    public int getIdResep() {
        return idResep;
    }

    public void setIdResep(int idResep) {
        this.idResep = idResep;
    }

    public double getPlusKanan() {
        return plusKanan;
    }

    public void setPlusKanan(double plusKanan) {
        this.plusKanan = plusKanan;
    }

    public double getPlusKiri() {
        return plusKiri;
    }

    public void setPlusKiri(double plusKiri) {
        this.plusKiri = plusKiri;
    }

    public double getMinusKanan() {
        return minusKanan;
    }

    public void setMinusKanan(double minusKanan) {
        this.minusKanan = minusKanan;
    }

    public double getMinusKiri() {
        return minusKiri;
    }

    public void setMinusKiri(double minusKiri) {
        this.minusKiri = minusKiri;
    }

    public double getCylKanan() {
        return cylKanan;
    }

    public void setCylKanan(double cylKanan) {
        this.cylKanan = cylKanan;
    }

    public double getCylKiri() {
        return cylKiri;
    }

    public void setCylKiri(double cylKiri) {
        this.cylKiri = cylKiri;
    }

    public double getAxisKanan() {
        return axisKanan;
    }

    public void setAxisKanan(double axisKanan) {
        this.axisKanan = axisKanan;
    }

    public double getAxisKiri() {
        return axisKiri;
    }

    public void setAxisKiri(double axisKiri) {
        this.axisKiri = axisKiri;
    }

    public double getPd() {
        return pd;
    }

    public void setPd(double pd) {
        this.pd = pd;
    }


    public User getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(User pelanggan) {
        this.pelanggan = pelanggan;
    }

    public User getOptometris() {
        return optometris;
    }

    public void setOptometris(User optometris) {
        this.optometris = optometris;
    }

    public Jadwal getJadwal() {
        return jadwal;
    }

    public void setJadwal(Jadwal jadwal) {
        this.jadwal = jadwal;
    }

    public int getIdPelanggan() {
        return pelanggan != null ? pelanggan.getIdUser() : 0;
    }

    public int getIdOptometris() {
        return optometris != null ? optometris.getIdUser() : 0;
    }

    public int getIdJadwal() {
        return jadwal != null ? jadwal.getIdJadwal() : 0;
    }

    public String getFormattedRightEye() {
        StringBuilder sb = new StringBuilder();
        
        if (plusKanan != 0.0) {
            sb.append(String.format("PLUS: %+.2f", plusKanan));
        }
        if (minusKanan != 0.0) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(String.format("MINUS: %+.2f", minusKanan));
        }
        if (cylKanan != 0.0) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(String.format("CYL: %+.2f", cylKanan));
        }
        if (axisKanan != 0.0 && cylKanan != 0.0) {
            sb.append(String.format(" AXIS: %.0f°", axisKanan));
        }
        
        return sb.length() > 0 ? sb.toString() : "No correction";
    }

    public String getFormattedLeftEye() {
        StringBuilder sb = new StringBuilder();
        if (plusKiri != 0.0) {
            sb.append(String.format("PLUS: %+.2f", plusKiri));
        }
        if (minusKiri != 0.0) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(String.format("MINUS: %+.2f", minusKiri));
        }
        if (cylKiri != 0.0) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(String.format("CYL: %+.2f", cylKiri));
        }
        if (axisKiri != 0.0 && cylKiri != 0.0) {
            sb.append(String.format(" AXIS: %.0f°", axisKiri));
        }
        
        return sb.length() > 0 ? sb.toString() : "No correction";
    }
}