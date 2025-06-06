package com.pekaboo.repositories;

import com.pekaboo.entities.Product;
import java.util.List;

public interface ProductRepo {
    List<Product> getAllProduct();
    Product getProductById(int id);
    void addProduct(Product product); // ini buat nambah jumlah produk aja
    void reduceProduct(int id, int quantity); // ini buat ngurangin jumlah produk
    void addNewProduct(Product product); // ini buat nambah produk baru
    void deleteProduct(int id);
    void updateProduct(Product product);
}
