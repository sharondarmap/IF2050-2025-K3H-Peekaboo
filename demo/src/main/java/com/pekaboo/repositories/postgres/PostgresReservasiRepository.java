package com.pekaboo.repositories.postgres;

import com.pekaboo.entities.Jadwal;
import com.pekaboo.entities.Reservasi;
import com.pekaboo.entities.StatusJadwal;
import com.pekaboo.entities.StatusReservasi;
import com.pekaboo.entities.User;
import com.pekaboo.repositories.ReservasiRepository;
import com.pekaboo.util.DatabaseConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostgresReservasiRepository implements ReservasiRepository {

    private final PostgresUserRepository userRepo = new PostgresUserRepository();
    private final PostgresJadwalRepository jadwalRepo = new PostgresJadwalRepository();

    @Override
    public void addReservasi(Reservasi reservasi) {
        String sql = "INSERT INTO reservasi (statusreservasi, idoptometris, idpelanggan, idjadwal, tanggalreservasi) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reservasi.getStatusReservasi().name());
            stmt.setInt(2, reservasi.getOptometris().getIdUser());
            stmt.setInt(3, reservasi.getPelanggan().getIdUser());
            stmt.setInt(4, reservasi.getJadwal().getIdJadwal());
            stmt.setTimestamp(5, Timestamp.valueOf(reservasi.getTanggalReservasi()));

            stmt.executeUpdate();

            updateJadwalStatusToReserved(conn, reservasi.getJadwal().getIdJadwal());

            // Update jadwal status to RESERVED
            // reservasi.getJadwal().setStatusJadwal(StatusJadwal.RESERVED);
            // jadwalRepo.updateJadwal(reservasi.getJadwal());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateJadwalStatusToReserved(Connection conn, int idJadwal) throws SQLException {
        String updateSql = "UPDATE jadwal SET statusjadwal = 'RESERVED' WHERE idjadwal = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setInt(1, idJadwal);
            updateStmt.executeUpdate();
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
                    Reservasi reservasi = mapResultSetToReservasi(rs);
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
        String sql = "SELECT COUNT(*) FROM reservasi WHERE idjadwal = ? AND statusreservasi != 'CANCELLED'";

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
        String sql = "SELECT * FROM reservasi WHERE idjadwal = ? AND statusreservasi != 'CANCELLED' LIMIT 1";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jadwal.getIdJadwal());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservasi(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteReservasi(int idReservasi) {

        //TODO : kalo cancel reservasi benerin status jadwal
        String sql = "UPDATE reservasi SET statusreservasi = 'CANCELLED' WHERE idreservasi = ?";

        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReservasi);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Reservasi mapResultSetToReservasi(ResultSet rs) throws SQLException {
        // Load User and Jadwal objects
        User optometris = userRepo.getUserById(rs.getInt("idoptometris"));
        User pelanggan = userRepo.getUserById(rs.getInt("idpelanggan"));
        Jadwal jadwal = jadwalRepo.getJadwalById(rs.getInt("idjadwal"));

        Reservasi reservasi = new Reservasi();
        reservasi.setIdReservasi(rs.getInt("idreservasi"));
        reservasi.setOptometris(optometris);
        reservasi.setPelanggan(pelanggan);
        reservasi.setJadwal(jadwal);
        reservasi.setStatusReservasi(StatusReservasi.valueOf(rs.getString("statusreservasi")));
        
        Timestamp timestamp = rs.getTimestamp("tanggalreservasi");
        if (timestamp != null) {
            reservasi.setTanggalReservasi(timestamp.toLocalDateTime());
        }
        
        return reservasi;
    }
}
