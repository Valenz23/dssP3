package dss.pvalenz23.practica1.servicios;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import dss.pvalenz23.practica1.modelos.Producto;

@Service
@RequiredArgsConstructor
public class ServicioCarrito {

    private List<Producto> carrito = new ArrayList<>();

    public List<Producto> getCarrito(){
        return carrito;
    }

    public Producto addProductoCarrito(Producto producto){
        carrito.add(producto);
        return producto;
    }

    public boolean deleteProductoCarritoById(Long id){
        return carrito.removeIf(product -> product.getId().equals(id));
    }

    public void clearCarrito(){
        carrito.clear();
    }
}
