package com.concessionaria.backend.repository;

import com.concessionaria.backend.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByClienteId(Long clienteId);
    boolean existsByCarroId(Long carroId);
}
