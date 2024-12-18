package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CarritoElemento;
import com.example.demo.model.Product;
import com.example.demo.service.CarritoService;

@RestController
@RequestMapping("/api/carrito")
public class ControladorRestCarrito {

    @Autowired
    private CarritoService servicioCarrito;

    @GetMapping
    public ResponseEntity<List<CarritoElemento>> getCarrito() {
        List<CarritoElemento> productosCarrito = servicioCarrito.getCartItems();
        return ResponseEntity.ok(productosCarrito);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProductoCarrito(@RequestBody Product producto) {
        servicioCarrito.addProduct(producto);
        return ResponseEntity.ok("Producto añadido");
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteProductoCarrito(@PathVariable("id") Long id){
        servicioCarrito.removeProduct(id);
        return ResponseEntity.ok("Producto eliminado");
    }

    @PostMapping("/clear")
    public ResponseEntity<Void> clearCarrito() {
        servicioCarrito.clearCart();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comprar")
    public ResponseEntity<Void> checkout() {
        servicioCarrito.clearCart();
        return ResponseEntity.ok().build();
    }
    
}

