package com.pekaboo.repositories;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.pekaboo.entities.Jadwal;
import com.pekaboo.entities.User;

public interface JadwalRepository {
    List<Jadwal> getAllJadwal();
    void addJadwal(Jadwal jadwal);
    void deleteJadwal(int idJadwal);
    void updateJadwal(Jadwal jadwal);
    Jadwal getJadwalById(int idJadwal);
    Jadwal getJadwalByOptometris(User optometris);

    Map<LocalDate, Integer> getSlotTersediaBulan(YearMonth yearMonth);
    List<Jadwal> getJadwalByTanggal(LocalDate tanggal);
}
