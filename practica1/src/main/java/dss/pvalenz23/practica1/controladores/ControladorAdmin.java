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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dss.pvalenz23.practica1.modelos.Producto;
import dss.pvalenz23.practica1.servicios.ServicioProducto;

@Controller
@RequestMapping("admin")
public class ControladorAdmin {

    @Autowired
    private ServicioProducto servicioProducto;

    @GetMapping
    public String getProductos(@RequestParam(value = "query", required = false) String query, Model model) {
        List<Producto> productos = servicioProducto.getProductoByNombre(query);

        if(query != null && !query.isEmpty()){
            model.addAttribute("titulo", "Resultados de búsqueda para: " + query);
        } 
        else {
            model.addAttribute("titulo", "Lista de productos");
        }

        model.addAttribute("productos", productos);
        return "admin";
    }

    @GetMapping("nuevo")
    public String formularioNuevoProducto(Model model){
        model.addAttribute("productoDetalles", new Producto());
        return "admin";
    }

    @PostMapping("detalles")
    public String formularioEditarProducto(@RequestParam("id") Long id, Model model){
        Producto producto = servicioProducto.getProductoById(id);
        model.addAttribute("titulo", "Lista de productos");
        model.addAttribute("productoDetalles", producto);
        model.addAttribute("productos", servicioProducto.getAllProductos());
        return "admin";
    }

    @PostMapping("add")
    public String addProducto(@RequestParam("nombre") String nombre, @RequestParam("precio") double precio) {
        servicioProducto.saveProducto(new Producto(nombre, precio));
        return "redirect:/admin";
    }

    @PostMapping("delete")
    public String eliminarProducto(@RequestParam("id") Long id) {
        servicioProducto.deleteProducto(id);
        return "redirect:/admin"; 
    }

    @PostMapping("update")
    public String actualizarProducto(@RequestParam("id") Long id, @RequestParam("nombre") String nombre, @RequestParam("precio") double precio) {
        Producto producto = servicioProducto.getProductoById(id);
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        servicioProducto.saveProducto(producto);
        return "redirect:/admin"; 
    }

    @GetMapping("exportarSQL")
    public ResponseEntity<byte[]> exportarBaseDeDatos() throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        // Encabezados SQL
        writer.println("-- Script para exportar productos");
        writer.println("SET FOREIGN_KEY_CHECKS = 0;"); // Deshabilitamos chequeo de claves foráneas

        // INSERTs
        for (Producto producto : servicioProducto.getAllProductos()) {
            writer.printf("INSERT INTO productos (id, nombre, precio) VALUES (%d, '%s', %.2f);\n",
                    producto.getId(), producto.getNombre().replace("'", "''"), producto.getPrecio());
        }

        writer.println("SET FOREIGN_KEY_CHECKS = 1;"); // Habilitamos chequeo de claves foráneas
        writer.flush(); 
        byte[] sqlBytes = out.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/sql"));
        headers.setContentDispositionFormData("attachment", "datos.sql");

        return new ResponseEntity<>(sqlBytes, headers, HttpStatus.OK);
    }

}
