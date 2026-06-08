package com.concessionaria.backend.repository;

import com.concessionaria.backend.model.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarroRepository extends JpaRepository<Carro, Long> {
    List<Carro> findByStatus(Carro.StatusCarro status);
    List<Carro> findByMarcaIgnoreCase(String marca);
}