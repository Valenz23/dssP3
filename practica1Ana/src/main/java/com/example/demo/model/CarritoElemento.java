package com.example.demo.model;

public class CarritoElemento {

    private Product product;
    private int quantity;

    public CarritoElemento(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getters y setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
