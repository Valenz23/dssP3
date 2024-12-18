package com.example.demo.service;


import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
	private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductByName(String nombre) {
        return productRepository.findByName(nombre);
    }
    
    public Object getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
    	productRepository.deleteById(id);
    }

    public List<Product> searchProducts(String query) {
    return productRepository.findByNameContainingIgnoreCase(query);
}

    public List<Product> filterProductsByPrice(double minPrice, double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public long countProductsAbovePrice(double price) {
        return productRepository.countByPriceGreaterThanEqual(price);
    }

    public void deleteProductsBelowPrice(double price) {
    }
    
}