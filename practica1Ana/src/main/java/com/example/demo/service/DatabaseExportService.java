package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

import java.util.List;

@Service
public class DatabaseExportService {

    @Autowired
    private ProductRepository productRepo;

    public byte[] exportDatabaseToSql() {
        List<Product> productList = productRepo.findAll();
        StringBuilder sqlBuilder = new StringBuilder();

        // encabezado del archivo SQL
        sqlBuilder.append("-- Exportación de productos\n");
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS product (\n");
        sqlBuilder.append("  id BIGINT PRIMARY KEY,\n");
        sqlBuilder.append("  name VARCHAR(255) NOT NULL,\n");
        sqlBuilder.append("  price DECIMAL(10, 2) NOT NULL\n");
        sqlBuilder.append(");\n\n");

        // Insertar cada producto
        for (Product product : productList) {
            sqlBuilder.append(String.format(
                "INSERT INTO product (id, name, price) VALUES (%d, '%s', %.2f);\n",
                product.getId(), 
                product.getName().replace("'", "''"), // Escapar comillas simples
                product.getPrice()
            ));
        }

        return sqlBuilder.toString().getBytes();
    }
}
