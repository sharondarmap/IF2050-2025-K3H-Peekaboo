package com.pekaboo.repositories;

import java.util.List;

import com.pekaboo.entities.Jadwal;
import com.pekaboo.entities.User;

public interface JadwalRepository {
    List<Jadwal> getAllProduct();
    void addJadwal(Jadwal jadwal);
    void deleteJadwal(int idJadwal);
    void updateJadwal(Jadwal jadwal);
    Jadwal getJadwalById(int idJadwal);
    Jadwal getJadwalByOptometris(User optometris);
}
