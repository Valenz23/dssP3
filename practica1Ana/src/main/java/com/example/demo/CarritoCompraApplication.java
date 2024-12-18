package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@SpringBootApplication
public class CarritoCompraApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarritoCompraApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(ProductRepository productRepository) {
        return args -> {
	    	if (productRepository.count() == 0) {
	            productRepository.save(new Product("Producto 1", 19.99));
	            productRepository.save(new Product("Producto 2", 9.99));
	            productRepository.save(new Product("Producto 3", 29.99));
	            System.out.println("Productos iniciales añadidos");
	        } else {
	            System.out.println("Base de datos ya contiene productos");
	        }
        };
    }
}