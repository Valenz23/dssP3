package dss.pvalenz23.practica1.controladores;

import java.util.List;

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

import dss.pvalenz23.practica1.modelos.Producto;
import dss.pvalenz23.practica1.servicios.ServicioProducto;

@RestController
@RequestMapping("/api/productos")
public class ControladorRestProducto {
@Autowired
    private ServicioProducto servicioProducto;

    @GetMapping
    public ResponseEntity<List<Producto>> getProductos(@RequestParam(value = "query", required = false) String query) {
        List<Producto> productos = servicioProducto.getProductoByNombre(query);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable("id") Long id) {
        Producto producto = servicioProducto.getProductoById(id);
        return ResponseEntity.ok(producto);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addProducto(@RequestParam String nombre, @RequestParam double precio) {
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(nombre);
        nuevoProducto.setPrecio(precio);
        servicioProducto.saveProducto(nuevoProducto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateProducto(@PathVariable("id") Long id, @RequestBody Producto updatedProducto) {
        Producto producto = servicioProducto.getProductoById(id);
        producto.setNombre(updatedProducto.getNombre());
        producto.setPrecio(updatedProducto.getPrecio());
        servicioProducto.saveProducto(updatedProducto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable("id") Long id) {
        servicioProducto.deleteProducto(id);
        return ResponseEntity.ok().build();        
    }

}
