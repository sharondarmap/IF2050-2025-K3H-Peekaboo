package com.pekaboo.repositories.postgres;

import com.pekaboo.entities.Jadwal;
import com.pekaboo.entities.StatusJadwal;
import com.pekaboo.entities.User;
import com.pekaboo.repositories.JadwalRepository;
import com.pekaboo.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class PostgresJadwalRepository implements JadwalRepository{
    @Override
    public List<Jadwal> getAllJadwal() {
        List<Jadwal> jadwalList = new ArrayList<>();
        String sql = 
            "SELECT j.idjadwal, j.idoptometris, j.tanggal, j.jam_mulai, j.jam_selesai, j.statusjadwal, " +
            "u.username, " +
            "CASE WHEN r.idjadwal IS NOT NULL THEN true ELSE false END as is_reserved " +
            "FROM jadwal j " +
            "JOIN users u ON j.idoptometris = u.iduser " +
            "LEFT JOIN reservasi r ON j.idjadwal = r.idjadwal AND r.statusreservasi != 'CANCELLED' " +
            "ORDER BY j.tanggal, j.jam_mulai";

        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            PostgresUserRepository userRepo = new PostgresUserRepository();

            while (rs.next()) {
                int idOptometris = rs.getInt("idoptometris");
                User optometris = userRepo.getUserById(idOptometris); // pastikan userStatus-nya = "OPTOMETRIS"

                Jadwal jadwal = new Jadwal();
                jadwal.setIdJadwal(rs.getInt("idjadwal"));
                jadwal.setOptometris(optometris);
                jadwal.setTanggal(rs.getDate("tanggal").toLocalDate());
                jadwal.setJamMulai(rs.getTime("jam_mulai").toLocalTime());
                jadwal.setJamSelesai(rs.getTime("jam_selesai").toLocalTime());
                boolean isReserved = rs.getBoolean("is_reserved");
                jadwal.setStatusJadwal(isReserved ? StatusJadwal.RESERVED : StatusJadwal.AVAILABLE);

                jadwalList.add(jadwal);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jadwalList;
    }

    @Override
    public void addJadwal(Jadwal jadwal) {
        String sql = "INSERT INTO jadwal (idoptometris, tanggal, jam_mulai, jam_selesai, statusjadwal) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jadwal.getOptometris().getIdUser());
            stmt.setDate(2, Date.valueOf(jadwal.getTanggal()));
            stmt.setTime(3, Time.valueOf(jadwal.getJamMulai()));
            stmt.setTime(4, Time.valueOf(jadwal.getJamSelesai()));
            stmt.setObject(5, jadwal.getStatusJadwal(), java.sql.Types.OTHER);


            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteJadwal(int idJadwal){
        Jadwal jadwal = getJadwalById(idJadwal);
        if (jadwal != null && jadwal.getStatusJadwal() == StatusJadwal.RESERVED) {
            throw new IllegalStateException("Cannot delete jadwal that is already reserved");
        }

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
        if (jadwal.getStatusJadwal() == StatusJadwal.RESERVED) {
            throw new IllegalStateException("Cannot update jadwal that is already reserved");
        }

        String sql = "UPDATE jadwal SET idoptometris = ?, tanggal = ?, jam_mulai = ?, jam_selesai = ?, statusjadwal = ? WHERE idjadwal = ?";

        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jadwal.getOptometris().getIdUser());
            stmt.setDate(2, Date.valueOf(jadwal.getTanggal()));
            stmt.setTime(3, Time.valueOf(jadwal.getJamMulai()));
            stmt.setTime(4, Time.valueOf(jadwal.getJamSelesai()));
            stmt.setObject(5, jadwal.getStatusJadwal(), java.sql.Types.OTHER);
            stmt.setInt(6, jadwal.getIdJadwal());

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
                    jadwal.setTanggal(rs.getDate("tanggal").toLocalDate());
                    jadwal.setJamMulai(rs.getTime("jam_mulai").toLocalTime());
                    jadwal.setJamSelesai(rs.getTime("jam_selesai").toLocalTime());

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

    @Override
    public Map<LocalDate, Integer> getSlotTersediaBulan(YearMonth bulan) {
        Map<LocalDate, Integer> slotMap = new HashMap<>();
        String sql = 
            "SELECT j.tanggal, " +
            "COUNT(CASE WHEN r.idjadwal IS NULL THEN 1 END) as available_slots " +
            "FROM jadwal j " +
            "LEFT JOIN reservasi r ON j.idjadwal = r.idjadwal AND r.statusreservasi != 'CANCELLED' " +
            "WHERE EXTRACT(YEAR FROM j.tanggal) = ? " +
            "AND EXTRACT(MONTH FROM j.tanggal) = ? " +
            "GROUP BY j.tanggal " +
            "HAVING COUNT(CASE WHEN r.idjadwal IS NULL THEN 1 END) > 0";

        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bulan.getYear());
            stmt.setInt(2, bulan.getMonthValue());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate tanggal = rs.getDate("tanggal").toLocalDate();
                    int available_slots = rs.getInt("available_slots");
                    slotMap.put(tanggal, available_slots);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return slotMap;
    }

    @Override
    public List<Jadwal> getJadwalByTanggal(LocalDate tanggal) {
        List<Jadwal> jadwalList = new ArrayList<>();
        String sql = 
            "SELECT j.idjadwal, j.idoptometris, j.tanggal, j.jam_mulai, j.jam_selesai, j.statusjadwal, " +
            "u.username, " +
            "CASE WHEN r.idjadwal IS NOT NULL THEN true ELSE false END as is_reserved " +
            "FROM jadwal j " +
            "JOIN users u ON j.idoptometris = u.iduser " +
            "LEFT JOIN reservasi r ON j.idjadwal = r.idjadwal AND r.statusreservasi != 'CANCELLED' " +
            "WHERE j.tanggal = ? " +
            "ORDER BY j.jam_mulai";

        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, java.sql.Date.valueOf(tanggal));
            
            try (ResultSet rs = stmt.executeQuery()) {
                PostgresUserRepository userRepo = new PostgresUserRepository();
                
                while (rs.next()) {
                    int idOptometris = rs.getInt("idoptometris");
                    User optometris = userRepo.getUserById(idOptometris);

                    Jadwal jadwal = new Jadwal();
                    jadwal.setIdJadwal(rs.getInt("idjadwal"));
                    jadwal.setOptometris(optometris);
                    jadwal.setTanggal(rs.getDate("tanggal").toLocalDate());
                    jadwal.setJamMulai(rs.getTime("jam_mulai").toLocalTime());
                    jadwal.setJamSelesai(rs.getTime("jam_selesai").toLocalTime());

                    boolean isReserved = rs.getBoolean("is_reserved");
                    jadwal.setStatusJadwal(isReserved ? StatusJadwal.RESERVED : StatusJadwal.AVAILABLE);

                    jadwalList.add(jadwal);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jadwalList;
    }

}
