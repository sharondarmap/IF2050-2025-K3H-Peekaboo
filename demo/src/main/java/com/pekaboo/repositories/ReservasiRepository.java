package com.pekaboo.repositories;

import com.pekaboo.entities.Reservasi;
import com.pekaboo.entities.User;
import com.pekaboo.entities.Jadwal;

import java.util.List;

public interface ReservasiRepository {
    List<Reservasi> getReservasiByPelanggan(User pelanggan);
    void addReservasi(Reservasi reservasi);
    boolean isJadwalReserved(Jadwal jadwal);
    Reservasi getReservasiByJadwal(Jadwal jadwal);
    void deleteReservasi(int idReservasi); //maskudnya buat cancel si reservasi
}
