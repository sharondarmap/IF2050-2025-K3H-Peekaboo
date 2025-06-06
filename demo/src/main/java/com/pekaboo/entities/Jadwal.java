package com.pekaboo.entities;

import java.io.ObjectInputFilter.Status;
import java.time.LocalTime;

public class Jadwal {
    private int idJadwal;
    private StatusJadwal statusJadwal;
    private User optometris; 
    public static final LocalTime JAM_BUKA_TOKO = LocalTime.of(10, 0); // Default jam buka toko hardcode

    // Constructor, getter, setter
    Jadwal(int idJadwal, User optometris){
        this.idJadwal = idJadwal;
        this.optometris = optometris;
        this.statusJadwal = StatusJadwal.AVAILABLE;
    }

    public int getIdJadwal(){
        return idJadwal;
    }
    public StatusJadwal geStatusJadwal(){
        return statusJadwal;
    }
    public User getOtometris(){
        return optometris;
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
}
