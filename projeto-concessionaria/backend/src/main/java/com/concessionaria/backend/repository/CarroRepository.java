package com.concessionaria.backend.repository;

import com.concessionaria.backend.model.Carro;
import com.concessionaria.backend.model.Carro.StatusCarro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CarroRepository extends JpaRepository<Carro, Long> {

    //status
    List<Carro> findByStatus(StatusCarro status);

    //marca (ignora maiúsculas/minúsculas)
    List<Carro> findByMarcaIgnoreCase(String marca);

    //faixa de preço
    List<Carro> findByPrecoBetween(Double precoMin, Double precoMax);

    //ano
    List<Carro> findByAnoGreaterThanEqual(Integer ano);

    //marca E status
    List<Carro> findByMarcaIgnoreCaseAndStatus(String marca, StatusCarro status);

    //JPQL - carros por cor
    @Query("SELECT c FROM Carro c WHERE LOWER(c.cor) = LOWER(:cor)")
    List<Carro> findByCor(@Param("cor") String cor);

    //conta carros por status
    long countByStatus(StatusCarro status);
}