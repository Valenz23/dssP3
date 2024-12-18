package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getAllProducts(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Product> products;

        if (query == null || query.isEmpty()) {
            products = productService.getAllProducts();
        } else {
            products = productService.searchProducts(query);
        }

        model.addAttribute("products", products);
        model.addAttribute("query", query);

        return "products";
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/new", "/edit/{id}"})
    public String showProductForm(@PathVariable(required = false) Long id, Model model) {
        Product product;
        if (id != null) {
            product = productRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
        } else {
            product = new Product(); 
        }
        model.addAttribute("product", product);
        return "formulario-producto"; 
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/products"; 
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
