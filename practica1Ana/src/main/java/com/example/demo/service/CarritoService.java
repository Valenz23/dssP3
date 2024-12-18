package com.example.demo.service;

import com.example.demo.model.CarritoElemento;
import com.example.demo.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarritoService {

    private final List<CarritoElemento> cart = new ArrayList<>();

    public void addProduct(Product product) {
        for (CarritoElemento item : cart) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cart.add(new CarritoElemento(product, 1));
    }

    public List<CarritoElemento> getCartItems() {
        return cart;
    }

    public void clearCart() {
        cart.clear();
    }

    public void removeProduct(Long productId) {
        cart.removeIf(item -> item.getProduct().getId().equals(productId));
    }
}