package com.ordermanagement.repository;

import com.ordermanagement.model.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    boolean existsById(Long id);
}
