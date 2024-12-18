package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByName(String name);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPriceBetween(double minPrice, double maxPrice);

    long countByPriceGreaterThanEqual(double minPrice);

    void deleteByPriceLessThan(double minPrice);

    List<Product> findAllByOrderByPriceDesc(Pageable pageable);
 
}