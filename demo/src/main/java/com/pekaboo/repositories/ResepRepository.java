package com.pekaboo.repositories;

import com.pekaboo.entities.Resep;
import com.pekaboo.entities.User;
import com.pekaboo.entities.Jadwal;
import java.util.List;

public interface ResepRepository {
    List<Resep> getAllResep();
    Resep getResepById(int id);
    Resep getResepByJadwal(Jadwal idJadwal);
    void addResep(Resep resep);
    void deleteResep(int id);
    void updateResep(Resep resep);
    List<Resep> getResepByPelanggan(User pelanggan);
}
