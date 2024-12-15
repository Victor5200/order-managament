package com.ordermanagement.repository;

import com.ordermanagement.model.entity.Pedido;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    boolean existsById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Pedido> findById(Long id);
}
