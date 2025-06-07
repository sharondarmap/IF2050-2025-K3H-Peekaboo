package com.pekaboo.repositories.postgres;

import com.pekaboo.entities.User;
import com.pekaboo.repositories.UserRepository;
import com.pekaboo.util.DatabaseConnector;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostgresUserRepository implements UserRepository {

    @Override
    public void saveUser(User user) {
        String sql = "INSERT INTO users (username, password, email, no_telepon, tanggal_lahir, jenis_kelamin, alamat, user_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getNoTelepon());
            stmt.setDate(5, Date.valueOf(user.getTanggalLahir()));
            stmt.setString(6, user.getJenisKelamin());
            stmt.setString(7, user.getAlamat());
            stmt.setString(8, user.getUserStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE iduser = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User getUserByCredentials(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // For security: ideally hash & compare

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, no_telepon = ?, " +
                     "tanggal_lahir = ?, jenis_kelamin = ?, alamat = ?, user_status = ? WHERE iduser = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getNoTelepon());
            stmt.setDate(5, Date.valueOf(user.getTanggalLahir()));
            stmt.setString(6, user.getJenisKelamin());
            stmt.setString(7, user.getAlamat());
            stmt.setString(8, user.getUserStatus());
            stmt.setInt(9, user.getIdUser());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE iduser = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    //Helper method to map a ResultSet to a User object
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("iduser"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("no_telepon"),
                rs.getDate("tanggal_lahir").toLocalDate(),
                rs.getString("jenis_kelamin"),
                rs.getString("alamat"),
                rs.getString("user_status")
        );
    }
}
