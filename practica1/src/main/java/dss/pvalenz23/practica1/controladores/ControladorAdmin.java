package dss.pvalenz23.practica1.controladores;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/api/admin")
public class ControladorAdmin {

    @Autowired
    private ServicioProducto servicioProducto;

    @GetMapping
    public ResponseEntity<List<Producto>> getProductos(@RequestParam(value = "query", required = false) String query) {
        List<Producto> productos = servicioProducto.getProductoByNombre(query);
        return ResponseEntity.ok(productos);
    }

    @PostMapping("/nuevo")
    public ResponseEntity<Producto> addProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = servicioProducto.saveProducto(producto);
        return ResponseEntity.ok(nuevoProducto);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<Producto> actualizarProducto(@RequestBody Producto producto) {
        Producto productoActualizado = servicioProducto.saveProducto(producto);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Long id) {
        servicioProducto.deleteProducto(id);
        return ResponseEntity.ok("Producto eliminado correctamente.");
    }

    @GetMapping("/exportarSQL")
    public ResponseEntity<byte[]> exportarBaseDeDatos() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        writer.println("-- Script para exportar productos");
        writer.println("SET FOREIGN_KEY_CHECKS = 0;");

        for (Producto producto : servicioProducto.getAllProductos()) {
            writer.printf("INSERT INTO productos (id, nombre, precio) VALUES (%d, '%s', %.2f);\n",
                    producto.getId(), producto.getNombre().replace("'", "''"), producto.getPrecio());
        }

        writer.println("SET FOREIGN_KEY_CHECKS = 1;");
        writer.flush();
        byte[] sqlBytes = out.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/sql"));
        headers.setContentDispositionFormData("attachment", "datos.sql");

        return new ResponseEntity<>(sqlBytes, headers, HttpStatus.OK);
    }
}
