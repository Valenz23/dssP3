package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/api/productos")
public class ControladorRestProducto {
@Autowired
    private ProductService servicioProducto;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Product> products;

        if (query == null || query.isEmpty()) {
            products = servicioProducto.getAllProducts();
        } else {
            products = servicioProducto.searchProducts(query);
        }

        return ResponseEntity.ok(products);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductoById(@PathVariable("id") Long id) {
        Product producto = (Product) servicioProducto.getProductById(id);
        return ResponseEntity.ok(producto);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addProducto(@RequestParam String nombre, @RequestParam double precio) {
        Product nuevoProduct = new Product();
        nuevoProduct.setName(nombre);
        nuevoProduct.setPrice(precio);
        servicioProducto.saveProduct(nuevoProduct);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable("id") Long id, @RequestBody Product updatedProduct) {
        Product Product = (com.example.demo.model.Product) servicioProducto.getProductById(id);
        Product.setName(updatedProduct.getName());
        Product.setPrice(updatedProduct.getPrice());
        servicioProducto.saveProduct(updatedProduct);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        servicioProducto.deleteProduct(id);
        return ResponseEntity.ok().build();        
    }

}
