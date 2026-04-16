package com.example.demo.Proyecto.Service;

import com.example.demo.Proyecto.Enum.EstadoCarrito;
import com.example.demo.Proyecto.Model.*;
import com.example.demo.Proyecto.Repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CarritoItemRepository carritoItemRepository;

    @InjectMocks
    private CarritoService carritoService;

    @Test
    void añadirProducto_productoExistente_incrementaCantidad() {
        //Probar con .\mvnw.cmd test
        // Usuario
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@email.com");

        // Carrito
        Carrito carrito = new Carrito();
        carrito.setEstado(EstadoCarrito.ACTIVO);
        carrito.setUsuario(usuario);
        carrito.setItems(new ArrayList<>());

        // Producto
        Producto producto = new Producto();
        producto.setPrecio(BigDecimal.valueOf(10));

        // Item existente
        CarritoItem item = new CarritoItem();
        item.setCantidad(2);
        item.setCarrito(carrito);
        item.setProducto(producto);

        // Simulaciones
        when(usuarioRepository.findByCorreo("test@email.com"))
                .thenReturn(Optional.of(usuario));

        when(carritoRepository.findByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO))
                .thenReturn(Optional.of(carrito));

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        when(carritoItemRepository.findByCarritoAndProducto(carrito, producto))
                .thenReturn(Optional.of(item));

        // Ejecutamos
        Carrito resultado = carritoService.añadirProducto("test@email.com", 1L, 3);

        // Verificamos incremento
        assertEquals(5, item.getCantidad());

        // Verificamos guardado
        verify(carritoItemRepository, times(1)).save(item);

        assertEquals(carrito, resultado);
    }

    @Test
    void añadirProducto_cantidadInvalida_lanzaExcepcion() {

        assertThrows(IllegalArgumentException.class, () -> {
            carritoService.añadirProducto("test@email.com", 1L, 0);
        });
    }
}