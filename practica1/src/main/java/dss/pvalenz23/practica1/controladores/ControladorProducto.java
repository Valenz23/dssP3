package dss.pvalenz23.practica1.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dss.pvalenz23.practica1.modelos.Producto;
import dss.pvalenz23.practica1.servicios.ServicioProducto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("productos")
public class ControladorProducto {

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
        return "productos";
    }
    
}
