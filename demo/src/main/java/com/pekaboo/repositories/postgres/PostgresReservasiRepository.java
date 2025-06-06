package com.pekaboo.repositories.postgres;

import com.pekaboo.entities.Jadwal;
import com.pekaboo.entities.Reservasi;
import com.pekaboo.entities.StatusJadwal;
import com.pekaboo.entities.User;
import com.pekaboo.repositories.ReservasiRepository;
import com.pekaboo.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresReservasiRepository implements ReservasiRepository {

    private final PostgresUserRepository userRepo = new PostgresUserRepository();
    private final PostgresJadwalRepository jadwalRepo = new PostgresJadwalRepository();

    @Override
    public void addReservasi(Reservasi reservasi) {
        String sql = "INSERT INTO reservasi (statusreservasi, idoptometris, idpelanggan, idjadwal) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reservasi.getStatusReservasi());
            stmt.setInt(2, reservasi.getOptometris().getIdUser());
            stmt.setInt(3, reservasi.getPelanggan().getIdUser());
            stmt.setInt(4, reservasi.getJadwal().getIdJadwal());

            stmt.executeUpdate();

            // Update jadwal status to RESERVED
            reservasi.getJadwal().setStatusJadwal(StatusJadwal.RESERVED);
            jadwalRepo.updateJadwal(reservasi.getJadwal());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Reservasi> getReservasiByPelanggan(User pelanggan) {
        List<Reservasi> list = new ArrayList<>();
        String sql = "SELECT * FROM reservasi WHERE idpelanggan = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pelanggan.getIdUser());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User optometris = userRepo.getUserById(rs.getInt("idoptometris"));
                    Jadwal jadwal = jadwalRepo.getJadwalById(rs.getInt("idjadwal"));

                    Reservasi reservasi = new Reservasi();
                    reservasi.setIdReservasi(rs.getInt("idreservasi"));
                    reservasi.setStatusReservasi(rs.getString("statusreservasi"));
                    reservasi.setOptometris(optometris);
                    reservasi.setPelanggan(pelanggan);
                    reservasi.setJadwal(jadwal);

                    list.add(reservasi);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean isJadwalReserved(Jadwal jadwal) {
        String sql = "SELECT COUNT(*) FROM reservasi WHERE idjadwal = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jadwal.getIdJadwal());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Reservasi getReservasiByJadwal(Jadwal jadwal) {
        String sql = "SELECT * FROM reservasi WHERE idjadwal = ? LIMIT 1";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jadwal.getIdJadwal());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User optometris = userRepo.getUserById(rs.getInt("idoptometris"));
                    User pelanggan = userRepo.getUserById(rs.getInt("idpelanggan"));

                    Reservasi reservasi = new Reservasi();
                    reservasi.setIdReservasi(rs.getInt("idreservasi"));
                    reservasi.setStatusReservasi(rs.getString("statusreservasi"));
                    reservasi.setOptometris(optometris);
                    reservasi.setPelanggan(pelanggan);
                    reservasi.setJadwal(jadwal);

                    return reservasi;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteReservasi(int idReservasi) {
        String sql = "DELETE FROM reservasi WHERE idreservasi = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReservasi);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
