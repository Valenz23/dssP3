package dss.pvalenz23.practica1.controladores;

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

import dss.pvalenz23.practica1.modelos.Producto;
import dss.pvalenz23.practica1.servicios.ServicioCarrito;

@RestController
@RequestMapping("/api/carrito")
public class ControladorRestCarrito {

    @Autowired
    private ServicioCarrito servicioCarrito;

    @GetMapping
    public ResponseEntity<List<Producto>> getCarrito() {
        List<Producto> productosCarrito = servicioCarrito.getCarrito();
        return ResponseEntity.ok(productosCarrito);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProductoCarrito(@RequestBody Producto producto) {
        servicioCarrito.addProductoCarrito(producto);
        return ResponseEntity.ok("Producto añadido");
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteProductoCarrito(@PathVariable("id") Long id){
        servicioCarrito.deleteProductoCarritoById(id);
        return ResponseEntity.ok("Producto eliminado");
    }

    @PostMapping("/clear")
    public ResponseEntity<Void> clearCarrito() {
        servicioCarrito.clearCarrito();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comprar")
    public ResponseEntity<Void> checkout() {
        servicioCarrito.clearCarrito();
        return ResponseEntity.ok().build();
    }
    
}

