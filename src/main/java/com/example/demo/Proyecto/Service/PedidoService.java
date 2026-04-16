package com.example.demo.Proyecto.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Proyecto.Enum.Estado;
import com.example.demo.Proyecto.Model.DetallePedido;
import com.example.demo.Proyecto.Model.Pedido;
import com.example.demo.Proyecto.Model.Usuario;
import com.example.demo.Proyecto.Repository.PedidoRepository;

import jakarta.transaction.Transactional;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    private double calcularTotal(Pedido pedido) {
        return pedido.getDetalles()
                .stream()
                .mapToDouble(detalle ->
                        detalle.getPrecioUnitario() * detalle.getCantidad()
                )
                .sum();
    }

    public Pedido crearPedido(Pedido pedido) {
        for (DetallePedido detalle : pedido.getDetalles()) {
            detalle.setPedido(pedido);
        }
        
        pedido.setFecha(LocalDate.now());
        pedido.setEstado(Estado.PENDIENTE);

        double total = calcularTotal(pedido);
        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    public Pedido crearPedidoParaCliente(Pedido pedido, Usuario cliente) {
        pedido.setCliente(cliente);
        pedido.setEstado(Estado.PENDIENTE);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public List<Pedido> listarPorEmpleado(Long empleadoId) {
        return pedidoRepository.findByEmpleadoId(empleadoId);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> buscarPorEstado(Estado estado) {
        return pedidoRepository.findByEstado(estado);
    }

    @Transactional
    public Pedido cambiarEstado(Long pedidoId, Estado nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
}