package com.pekaboo.repositories.postgres;

import com.pekaboo.entities.Product;
import com.pekaboo.repositories.ProductRepo;
import com.pekaboo.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresProdukRepository implements ProductRepo {

    @Override
    public List<Product> getAllProduct() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM produk";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(extractProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public Product getProductById(int id) {
        String sql = "SELECT * FROM produk WHERE idproduk = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractProduct(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void addProduct(Product product) {
        String sql = "UPDATE produk SET stok = stok + ? WHERE idproduk = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getStock());
            stmt.setInt(2, product.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reduceProduct(int id, int quantity) {
        String sql = "UPDATE produk SET stok = stok - ? WHERE idproduk = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addNewProduct(Product product) {
        String sql = "INSERT INTO produk (namaproduk, brand, harga, stok, warna, ukuran, berat) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getBrand());
            stmt.setInt(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getColor());
            stmt.setString(6, product.getSize());
            stmt.setDouble(7, 0); // karena `berat` tidak ada di entitas kamu

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(int id) {
        String sql = "DELETE FROM produk WHERE idproduk = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product) {
        String sql = "UPDATE produk SET namaproduk = ?, brand = ?, harga = ?, stok = ?, warna = ?, ukuran = ? WHERE idproduk = ?";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getBrand());
            stmt.setInt(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getColor());
            stmt.setString(6, product.getSize());
            stmt.setInt(7, product.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Product extractProduct(ResultSet rs) throws SQLException {
        int id = rs.getInt("idproduk");
        int stock = rs.getInt("stok");
        int price = rs.getInt("harga");
        String name = rs.getString("namaproduk");
        String brand = rs.getString("brand");
        String color = rs.getString("warna");
        String size = rs.getString("ukuran");

        // imagePath tidak disimpan di database, bisa diatur default atau nanti ditambahkan
        return new Product(id, stock, price, name, brand, color, size, null);
    }
}
