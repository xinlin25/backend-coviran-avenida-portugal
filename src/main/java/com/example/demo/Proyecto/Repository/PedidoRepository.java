package com.example.demo.Proyecto.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.Proyecto.Model.Pedido;
import com.example.demo.Proyecto.Enum.Estado;


@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByEmpleadoId(Long empleadoId);

    List<Pedido> findByEstado(Estado estado);
}
