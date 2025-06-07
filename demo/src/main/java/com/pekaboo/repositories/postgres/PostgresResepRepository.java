package com.pekaboo.repositories.postgres;

import com.pekaboo.entities.Resep;
import com.pekaboo.entities.User;
import com.pekaboo.entities.Jadwal;
import com.pekaboo.repositories.ResepRepository;
import com.pekaboo.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresResepRepository implements ResepRepository {

    private final PostgresUserRepository userRepo = new PostgresUserRepository();
    private final PostgresJadwalRepository jadwalRepo = new PostgresJadwalRepository();

    @Override
    public List<Resep> getAllResep() {
        List<Resep> resepList = new ArrayList<>();
        String sql = "SELECT * FROM resep ORDER BY idresep DESC";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Resep resep = mapResultSetToResep(rs);
                resepList.add(resep);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching all resep: " + e.getMessage());
            e.printStackTrace();
        }

        return resepList;
    }

    @Override
    public Resep getResepById(int id) {
        String sql = "SELECT * FROM resep WHERE idresep = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToResep(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching resep by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void addResep(Resep resep) {
        String sql =
            "INSERT INTO resep (pluskanan, pluskiri, minuskanan, minuskiri," + 
            "cylkanan, cylkiri, axiskanan, axiskiri, pd, " +
            "idpelanggan, idoptometris, idjadwal) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, resep.getPlusKanan());
            stmt.setDouble(2, resep.getPlusKiri());
            stmt.setDouble(3, resep.getMinusKanan());
            stmt.setDouble(4, resep.getMinusKiri());
            stmt.setDouble(5, resep.getCylKanan());
            stmt.setDouble(6, resep.getCylKiri());
            stmt.setDouble(7, resep.getAxisKanan());
            stmt.setDouble(8, resep.getAxisKiri());
            stmt.setDouble(9, resep.getPd());

            stmt.setInt(10, resep.getIdPelanggan());
            stmt.setInt(11, resep.getIdOptometris());
            stmt.setInt(12, resep.getIdJadwal());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        resep.setIdResep(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Resep added successfully with ID: " + resep.getIdResep());
            }

        } catch (SQLException e) {
            System.err.println("Error adding resep: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateResep(Resep resep) {
        String sql =
            "UPDATE resep SET " +
            "pluskanan = ?, pluskiri = ?, minuskanan = ?, minuskiri = ?, " +
            "cylkanan = ?, cylkiri = ?, axiskanan = ?, axiskiri = ?, pd = ?, " +
            "idpelanggan = ?, idoptometris = ?, idjadwal = ? " +
            "WHERE idresep = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, resep.getPlusKanan());
            stmt.setDouble(2, resep.getPlusKiri());
            stmt.setDouble(3, resep.getMinusKanan());
            stmt.setDouble(4, resep.getMinusKiri());
            stmt.setDouble(5, resep.getCylKanan());
            stmt.setDouble(6, resep.getCylKiri());
            stmt.setDouble(7, resep.getAxisKanan());
            stmt.setDouble(8, resep.getAxisKiri());
            stmt.setDouble(9, resep.getPd());

            stmt.setInt(10, resep.getIdPelanggan());
            stmt.setInt(11, resep.getIdOptometris());
            stmt.setInt(12, resep.getIdJadwal());

            stmt.setInt(13, resep.getIdResep());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Resep updated successfully: " + resep.getIdResep());
            } else {
                System.out.println("No resep found with ID: " + resep.getIdResep());
            }

        } catch (SQLException e) {
            System.err.println("Error updating resep: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteResep(int id) {
        String sql = "DELETE FROM resep WHERE idresep = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Resep deleted successfully: " + id);
            } else {
                System.out.println("No resep found with ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error deleting resep: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public List<Resep> getResepByPelanggan(User pelanggan) {
        List<Resep> resepList = new ArrayList<>();
        String sql = "SELECT * FROM resep WHERE idpelanggan = ? ORDER BY idresep DESC";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pelanggan.getIdUser());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Resep resep = mapResultSetToResep(rs);
                    resepList.add(resep);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching resep by pelanggan: " + e.getMessage());
            e.printStackTrace();
        }

        return resepList;
    }

    private Resep mapResultSetToResep(ResultSet rs) throws SQLException {
        User pelanggan = userRepo.getUserById(rs.getInt("idpelanggan"));
        User optometris = userRepo.getUserById(rs.getInt("idoptometris"));
        Jadwal jadwal = jadwalRepo.getJadwalById(rs.getInt("idjadwal"));

        Resep resep = new Resep();
        resep.setIdResep(rs.getInt("idresep"));
        resep.setPlusKanan(rs.getDouble("pluskanan"));
        resep.setPlusKiri(rs.getDouble("pluskiri"));
        resep.setMinusKanan(rs.getDouble("minuskanan"));
        resep.setMinusKiri(rs.getDouble("minuskiri"));
        resep.setCylKanan(rs.getDouble("cylkanan"));
        resep.setCylKiri(rs.getDouble("cylkiri"));
        resep.setAxisKanan(rs.getDouble("axiskanan"));
        resep.setAxisKiri(rs.getDouble("axiskiri"));
        resep.setPd(rs.getDouble("pd"));

        resep.setPelanggan(pelanggan);
        resep.setOptometris(optometris);
        resep.setJadwal(jadwal);

        return resep;
    }
}