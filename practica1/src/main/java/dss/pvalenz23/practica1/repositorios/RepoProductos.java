package dss.pvalenz23.practica1.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import dss.pvalenz23.practica1.modelos.Producto;

public interface RepoProductos extends JpaRepository<Producto, Long> {

    // metodo para buscar por nombre
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
