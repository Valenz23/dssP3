package dss.pvalenz23.practica1.controladores;

import java.io.FileNotFoundException;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;

import dss.pvalenz23.practica1.modelos.Producto;
import dss.pvalenz23.practica1.servicios.ServicioCarrito;
import dss.pvalenz23.practica1.servicios.ServicioPDF;

@RestController
@RequestMapping("/api/carrito")
public class ControladorCarrito {

    @Autowired
    private ServicioCarrito servicioCarrito;

    @Autowired
    private ServicioPDF servicioPDF;

    @GetMapping
    public ResponseEntity<List<Producto>> getCarrito() {
        List<Producto> productosCarrito = servicioCarrito.getCarrito();
        return ResponseEntity.ok(productosCarrito);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProductoCarrito(@RequestBody Producto producto) {
        servicioCarrito.addProductoCarrito(producto);
        return ResponseEntity.ok("Producto añadido al carrito.");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProductoCarrito(@PathVariable Long id) {
        servicioCarrito.deleteProductoCarritoById(id);
        return ResponseEntity.ok("Producto eliminado del carrito.");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCarrito() {
        servicioCarrito.clearCarrito();
        return ResponseEntity.ok("Carrito limpiado correctamente.");
    }

    @GetMapping("/comprar")
    public ResponseEntity<byte[]> generarPdfCompra() throws FileNotFoundException {
        try {
            byte[] pdfBytes = servicioPDF.generarPdfCompra(servicioCarrito.getCarrito());
            servicioCarrito.clearCarrito();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "compra.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (DocumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
