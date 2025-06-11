package com.pekaboo.repositories;

import java.util.List;
import com.pekaboo.entities.Pesanan;

public interface PesananRepository {
    List<Pesanan> getAllPesanan();
    void addPesanan(Pesanan pesanan);
    void deletePesanan(int idPesanan);
    void updatePesanan(Pesanan pesanan);
    Pesanan getPesananById(int idPesanan);
    List<Pesanan> getPesananByPelanggan(int idPelanggan);
    List<Pesanan> getPesananByStatus(String status);
}