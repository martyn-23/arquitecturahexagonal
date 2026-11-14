package com.example.demo.infrastructure.persistence;

import com.example.demo.infrastructure.persistence.entity.DetallePedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoJpaRepository extends JpaRepository<DetallePedidoEntity, Long> {
}
