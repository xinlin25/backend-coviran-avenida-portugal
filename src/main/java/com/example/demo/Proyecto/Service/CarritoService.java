package com.example.demo.Proyecto.Service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Proyecto.Enum.Estado;
import com.example.demo.Proyecto.Enum.EstadoCarrito;
import com.example.demo.Proyecto.Model.Carrito;
import com.example.demo.Proyecto.Model.CarritoItem;
import com.example.demo.Proyecto.Model.DetallePedido;
import com.example.demo.Proyecto.Model.Pedido;
import com.example.demo.Proyecto.Model.Producto;
import com.example.demo.Proyecto.Model.Usuario;
import com.example.demo.Proyecto.Repository.CarritoItemRepository;
import com.example.demo.Proyecto.Repository.CarritoRepository;
import com.example.demo.Proyecto.Repository.PedidoRepository;
import com.example.demo.Proyecto.Repository.ProductoRepository;
import com.example.demo.Proyecto.Repository.UsuarioRepository;

@Service
public class CarritoService {
    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;

    public CarritoService(
        CarritoRepository carritoRepository,
        CarritoItemRepository carritoItemRepository,
        ProductoRepository productoRepository,
        UsuarioRepository usuarioRepository,
        PedidoRepository pedidoRepository
    ) {
        this.carritoRepository = carritoRepository;
        this.carritoItemRepository = carritoItemRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public Carrito obtenerCarritoActivo(String correoUsuario) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return carritoRepository.findByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO)
        .orElseThrow(() -> new RuntimeException("No existe un carrito activo"));
    }

    @Transactional
    public Carrito añadirProducto(String correoUsuario, Long productoId, int cantidad) {
        if (cantidad <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0");

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = carritoRepository
                .findByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO)
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito();
                    nuevo.setUsuario(usuario);
                    nuevo.setEstado(EstadoCarrito.ACTIVO);
                    return carritoRepository.save(nuevo);
                });

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        CarritoItem item = carritoItemRepository
                .findByCarritoAndProducto(carrito, producto)
                .orElse(null);

        if (item != null) {
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            item = new CarritoItem();
            item.setCarrito(carrito);
            item.setProducto(producto);
            item.setCantidad(cantidad);
            item.setPrecioUnitario(producto.getPrecio().doubleValue());
            carrito.getItems().add(item);
        }

        carritoItemRepository.save(item);

        return carrito;
    }

    @Transactional
    public Pedido confirmarCarrito(String correoUsuario) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = carritoRepository.findByUsuarioAndEstado(usuario, EstadoCarrito.ACTIVO)
        .orElseThrow(() -> new RuntimeException("No existe un carrito activo"));

        if (carrito.getItems().isEmpty()) 
            throw new IllegalStateException("El carrito se encuentra vacío actualmente");
        
        Pedido pedido = new Pedido();
        pedido.setCliente(usuario);
        pedido.setFecha(LocalDate.now());
        pedido.setEstado(Estado.PENDIENTE);

        double total = 0;

        for (CarritoItem item : carrito.getItems()) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getProducto().getPrecio().doubleValue());

            pedido.getDetalles().add(detalle);

            total += detalle.getCantidad() * detalle.getPrecioUnitario();
        }

        pedido.setTotal(total);

        carrito.setEstado(EstadoCarrito.CONFIRMADO);

        return pedidoRepository.save(pedido);
    }
}