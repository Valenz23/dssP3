package dss.pvalenz23.practica1.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
