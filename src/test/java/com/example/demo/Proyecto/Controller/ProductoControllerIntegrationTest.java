package com.example.demo.Proyecto.Controller;

import com.example.demo.Proyecto.Model.Categoria;
import com.example.demo.Proyecto.Model.Producto;
import com.example.demo.Proyecto.Repository.CategoriaRepository;
import com.example.demo.Proyecto.Repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProductoControllerIntegrationTest {
    //Probar con .\mvnw.cmd test
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void listarProductosActivos_devuelve200YLista() throws Exception {

        Categoria categoria = new Categoria();
        categoria.setNombre("Snacks");
        categoria = categoriaRepository.save(categoria);

        Producto producto = new Producto();
        producto.setNombre("Patatas");
        producto.setDescripcion("Bolsa de patatas");
        producto.setPrecio(new BigDecimal("2.00"));
        producto.setCategoria(categoria);
        producto.setActivo(true);

        productoRepository.save(producto);

        mockMvc.perform(get("/productos/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Patatas"));
    }
}