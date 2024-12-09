package dss.pvalenz23.practica1.controladores;

import java.io.FileNotFoundException;

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

import com.itextpdf.text.DocumentException;

import dss.pvalenz23.practica1.modelos.Producto;
import dss.pvalenz23.practica1.servicios.ServicioCarrito;
import dss.pvalenz23.practica1.servicios.ServicioPDF;

@Controller
@RequestMapping("carrito")
public class ControladorCarrito {

    @Autowired
    private ServicioCarrito servicioCarrito;

    @Autowired
    private ServicioPDF servicioPDF;

    @GetMapping
    public String getCarrito(Model model){        
        model.addAttribute("productosCarrito", servicioCarrito.getCarrito());

        
        double total = servicioCarrito.getCarrito().isEmpty() ? 0.0 : servicioCarrito.getCarrito().stream()
            .mapToDouble(producto -> producto.getPrecio())
            .sum(); 
        model.addAttribute("total", total);

        return "carrito";
    }

    @PostMapping("add")
    public String addProductoCarrito(@RequestParam("id") Long id, @RequestParam("nombre") String nombre, @RequestParam("precio") double precio){
        Producto nuevo = new Producto();
        nuevo.setId(id);
        nuevo.setNombre(nombre);
        nuevo.setPrecio(precio);
        servicioCarrito.addProductoCarrito(nuevo);        
        return "redirect:/carrito";
    }

    @PostMapping("delete")
    public String deleteProductoCarrito(@RequestParam("id") Long id){        
        servicioCarrito.deleteProductoCarritoById(id);
        return "redirect:/carrito";
    }

    @GetMapping("clear")
    public String clearCarrito(){
        servicioCarrito.clearCarrito();
        return "redirect:/carrito";
    }

    @GetMapping("comprar")
    public ResponseEntity<byte[]> generarPdfCompra(Model model) throws FileNotFoundException {

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
