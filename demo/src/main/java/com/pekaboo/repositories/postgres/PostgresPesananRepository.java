package com.pekaboo.repositories.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.pekaboo.entities.Pesanan;
import com.pekaboo.repositories.PesananRepository;
import com.pekaboo.util.DatabaseConnector;

public class PostgresPesananRepository implements PesananRepository {

    @Override
    public List<Pesanan> getAllPesanan() {
        List<Pesanan> pesananList = new ArrayList<>();
        String sql = "SELECT * FROM pesanan";
        
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Pesanan pesanan = mapResultSetToPesanan(rs);
                pesananList.add(pesanan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pesananList;
    }

    @Override
    public void addPesanan(Pesanan pesanan) {
        String sql = "INSERT INTO pesanan (tanggalpesanan, total, alamat, idpelanggan, idproduk) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            if (pesanan.getTanggalPesanan() != null && !pesanan.getTanggalPesanan().isEmpty()) {
                ps.setTimestamp(1, Timestamp.valueOf(pesanan.getTanggalPesanan()));
            } else {
                ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            }
            
            ps.setInt(2, pesanan.getTotalPesanan());
            ps.setString(3, pesanan.getAlamatPesanan());
            ps.setInt(4, pesanan.getIdPelangganPemesan());
            ps.setInt(5, pesanan.getIdProdukPesanan());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePesanan(int idPesanan) {
        String sql = "DELETE FROM pesanan WHERE idpesanan = ?";
        
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idPesanan);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePesanan(Pesanan pesanan) {
        String sql = "UPDATE pesanan SET tanggalpesanan = ?, total = ?, alamat = ?, " +
                     "idpelanggan = ?, idproduk = ? WHERE idpesanan = ?";
        
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            if (pesanan.getTanggalPesanan() != null && !pesanan.getTanggalPesanan().isEmpty()) {
                ps.setTimestamp(1, Timestamp.valueOf(pesanan.getTanggalPesanan()));
            } else {
                ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            }
            
            ps.setInt(2, pesanan.getTotalPesanan());
            ps.setString(3, pesanan.getAlamatPesanan());
            ps.setInt(4, pesanan.getIdPelangganPemesan());
            ps.setInt(5, pesanan.getIdProdukPesanan());
            ps.setInt(6, pesanan.getIdPesanan());
            
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Pesanan getPesananById(int idPesanan) {
        String sql = "SELECT * FROM pesanan WHERE idpesanan = ?";
        
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idPesanan);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPesanan(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public List<Pesanan> getPesananByPelanggan(int idPelanggan) {
        List<Pesanan> pesananList = new ArrayList<>();
        String sql = "SELECT * FROM pesanan WHERE idpelanggan = ?";
        
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idPelanggan);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Pesanan pesanan = mapResultSetToPesanan(rs);
                pesananList.add(pesanan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pesananList;
    }

    @Override
    public List<Pesanan> getPesananByStatus(String status) {
        List<Pesanan> pesananList = new ArrayList<>();
        String sql = "SELECT * FROM pesanan WHERE status = ?";
        
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Pesanan pesanan = mapResultSetToPesanan(rs);
                pesananList.add(pesanan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pesananList;
    }

    private Pesanan mapResultSetToPesanan(ResultSet rs) throws SQLException {
        Pesanan pesanan = new Pesanan();
        pesanan.setIdPesanan(rs.getInt("idpesanan"));
        
        java.sql.Timestamp timestamp = rs.getTimestamp("tanggalpesanan");
        if (timestamp != null) {
            pesanan.setTanggalPesanan(timestamp.toString()); // format: yyyy-mm-dd hh:mm:ss.fffffffff
        } else {
            pesanan.setTanggalPesanan(null);
        }
        
        pesanan.setTotalPesanan(rs.getInt("total"));
        
        String alamat = rs.getString("alamat");
        pesanan.setAlamatPesanan(alamat != null ? alamat : "");
        
        pesanan.setIdPelangganPemesan(rs.getInt("idpelanggan"));
        pesanan.setIdProdukPesanan(rs.getInt("idproduk"));
        
        return pesanan;
    }
}