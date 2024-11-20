package dss.pvalenz23.practica1.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dss.pvalenz23.practica1.modelos.Producto;
import dss.pvalenz23.practica1.repositorios.RepoProductos;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicioProducto {

    @Autowired
    private RepoProductos repoProductos;

    public List<Producto> getAllProductos() {        
        return repoProductos.findAll();
    }

    public Producto getProductoById(Long id) {
        return repoProductos.findById(id).orElse(null);
    }

    public List<Producto> getProductoByNombre(String nombre) {
        if(nombre == null || nombre.isEmpty()){
            return getAllProductos();
        }
        return repoProductos.findByNombreContainingIgnoreCase(nombre);                             
    }

    public Producto saveProducto(Producto producto) {
        return repoProductos.save(producto);
    }

    public void deleteProducto(Long id) {
        repoProductos.deleteById(id);
    }

}
