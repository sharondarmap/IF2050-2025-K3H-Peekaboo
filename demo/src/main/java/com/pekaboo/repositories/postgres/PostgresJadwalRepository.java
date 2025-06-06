package com.pekaboo.repositories.postgres;

import com.pekaboo.entities.Jadwal;
import com.pekaboo.entities.StatusJadwal;
import com.pekaboo.entities.User;
import com.pekaboo.repositories.JadwalRepository;
import com.pekaboo.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresJadwalRepository implements JadwalRepository{
    @Override
    public List<Jadwal> getAllProduct() {
        List<Jadwal> jadwalList = new ArrayList<>();
        String sql = "SELECT * FROM jadwal";

        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            PostgresUserRepository userRepo = new PostgresUserRepository();

            while (rs.next()) {
                int idOptometris = rs.getInt("idoptometris");
                User optometris = userRepo.getUserById(idOptometris); // pastikan userStatus-nya = "OPTOMETRIS"

                Jadwal jadwal = new Jadwal();
                jadwal.setIdJadwal(rs.getInt("idjadwal"));
                jadwal.setStatusJadwal(StatusJadwal.valueOf(rs.getString("statusjadwal")));
                jadwal.setOptometris(optometris);

                jadwalList.add(jadwal);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jadwalList;
    }

    @Override
    public void addJadwal(Jadwal jadwal) {
        String sql = "INSERT INTO jadwal (statusjadwal, idoptometris) VALUES (?, ?)";

        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, jadwal.getStatusJadwal().name()); // Assuming enum StatusJadwal
            stmt.setInt(2, jadwal.getOptometris().getIdUser()); // Access id from User

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteJadwal(int idJadwal){
        String sql = "DELETE FROM jadwal WHERE idjadwal = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idJadwal);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateJadwal(Jadwal jadwal) {
        String sql = "UPDATE jadwal SET statusjadwal = ?, idoptometris = ? WHERE idjadwal = ?";

        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, jadwal.getStatusJadwal().name());
            stmt.setInt(2, jadwal.getOptometris().getIdUser());
            stmt.setInt(3, jadwal.getIdJadwal());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Jadwal getJadwalById(int idJadwal) {
        String sql = "SELECT * FROM jadwal WHERE idjadwal = ?";

        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idJadwal);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idOptometris = rs.getInt("idoptometris");
                    User optometris = new PostgresUserRepository().getUserById(idOptometris);

                    Jadwal jadwal = new Jadwal();
                    jadwal.setIdJadwal(rs.getInt("idjadwal"));
                    jadwal.setStatusJadwal(StatusJadwal.valueOf(rs.getString("statusjadwal")));
                    jadwal.setOptometris(optometris);

                    return jadwal;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Jadwal getJadwalByOptometris(User optometris) {
        String sql = "SELECT * FROM jadwal WHERE idoptometris = ? LIMIT 1";

        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, optometris.getIdUser());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Jadwal jadwal = new Jadwal();
                    jadwal.setIdJadwal(rs.getInt("idjadwal"));
                    jadwal.setStatusJadwal(StatusJadwal.valueOf(rs.getString("statusjadwal")));
                    jadwal.setOptometris(optometris);

                    return jadwal;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
