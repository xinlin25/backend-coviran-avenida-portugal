package com.example.demo.Proyecto.Repository;

import com.example.demo.Proyecto.Model.Categoria;
import com.example.demo.Proyecto.Model.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductoRepositoryIntegrationTest {
    //Probar con .\mvnw.cmd test
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void guardarProducto_yBuscarPorNombre_funcionaCorrectamente() {

        Categoria categoria = new Categoria();
        categoria.setNombre("Bebidas");
        categoria = categoriaRepository.save(categoria);

        Producto producto = new Producto();
        producto.setNombre("Coca Cola");
        producto.setDescripcion("Refresco");
        producto.setPrecio(new BigDecimal("1.50"));
        producto.setCategoria(categoria);

        productoRepository.save(producto);

        Optional<Producto> resultado = productoRepository.findByNombre("Coca Cola");

        assertTrue(resultado.isPresent());
        assertEquals("Coca Cola", resultado.get().getNombre());
        assertEquals("Bebidas", resultado.get().getCategoria().getNombre());
    }
}