package com.pekaboo.entities;

public class Product {
    private int id;
    private int stock;
    private int price;
    private String name;
    private String brand;
    private String color;
    private String size;
    private String imagePath;

    public Product() {
        // Default constructor
    }
    
    public Product (int id, int stock, int price, String name, String brand, String color, String size, String imagePath) {
        this.id = id;
        this.stock = stock;
        this.price = price;
        this.name = name;
        this.brand = brand;
        this.color = color;
        this.size = size;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImagePath(){
        return imagePath;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }
    
}