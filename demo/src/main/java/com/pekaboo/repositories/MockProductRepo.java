package com.pekaboo.repositories;

import com.pekaboo.entities.Product;
import java.util.ArrayList;
import java.util.List;


public class MockProductRepo implements ProductRepo {
    private static final String[] NAMA_PRODUK = {"Aviator Classic", "Wayfarer Ease", "Clubmaster Oval", "Hexagonal Flat", "Round Metal", "Erika Classic", "Justin Classic"};
    private static final String[] NAMA_BRAND = {"Ray-Ban", "Oakley", "Prada", "Gucci", "Celine", "Dior"};
    private static final String[] NAMA_WARNA = {"Hitam", "Coklat", "Emas", "Perak", "Biru Navy", "Hijau Army"};
    private List<Product> productList = new ArrayList<>();

    public MockProductRepo() {
        // Inisialisasi dengan beberapa produk contoh
        productList.add(new Product(1, 10, 10000, "Kacamata Jennie", "Celine", "Merah", "M", "/com/pekaboo/pembelian/assets/kacamatared.jpg"));
        productList.add(new Product(2, 5, 20000, "Kacamata Kucing", "Dior", "Biru", "L", "/com/pekaboo/pembelian/assets/kacamatablue.jpg"));
        productList.add(new Product(3, 0, 15000, "Kacamata Kambing", "Channel", "Hijau", "S", "/com/pekaboo/pembelian/assets/kacamata1.jpg"));

        for (int i = 4; i <= 23; i++) {
            productList.add(new Product(
                i, 
                (i % 5) + 1,
                100000 + (i * 15000),
                NAMA_PRODUK[i % NAMA_PRODUK.length], 
                NAMA_BRAND[i % NAMA_BRAND.length], 
                NAMA_WARNA[i % NAMA_WARNA.length], 
                "M", 
                "/com/pekaboo/pembelian/assets/kacamatablue.jpg"
            ));
        }
    }




    @Override
    public List<Product> getAllProduct() {
        return productList;
    }

    @Override
    public Product getProductById(int id) {
        return productList.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void addProduct(Product product) {
        Product existingProduct = getProductById(product.getId());
        if (existingProduct != null) {
            existingProduct.setStock(existingProduct.getStock() + product.getStock());
        } else {
            productList.add(product);
        }
    }

    @Override
    public void reduceProduct(int id, int quantity) {
        Product product = getProductById(id);
        if (product != null && product.getStock() >= quantity) {
            product.setStock(product.getStock() - quantity);
        }
    }

    @Override
    public void addNewProduct(Product product) {
        if (getProductById(product.getId()) == null) {
            productList.add(product);
        }
    }

    @Override
    public void deleteProduct(int id) {
        productList.removeIf(product -> product.getId() == id);
    }

    @Override
    public void updateProduct(Product product) {
        Product existingProduct = getProductById(product.getId());
        if (existingProduct != null) {
            existingProduct.setStock(product.getStock());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setName(product.getName());
            existingProduct.setBrand(product.getBrand());
            existingProduct.setColor(product.getColor());
            existingProduct.setSize(product.getSize());
            existingProduct.setImagePath(product.getImagePath());
        }
    }

    public List<Product> getProductList() {
        return productList;
    }

}
