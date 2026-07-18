package com.concessionaria.backend.repository;

import com.concessionaria.backend.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    //vendas por cliente
    List<Venda> findByClienteId(Long clienteId);

    //verifica se carro já foi vendido
    boolean existsByCarroId(Long carroId);

    //vendas em um período
    List<Venda> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim);

    //acima de um valor
    List<Venda> findByValorFinalGreaterThanEqual(Double valor);

    //total de receita
    @Query("SELECT SUM(v.valorFinal) FROM Venda v")
    Double calcularReceitaTotal();

    //vendas do mês atual
    @Query("SELECT v FROM Venda v WHERE MONTH(v.dataVenda) = MONTH(CURRENT_DATE) AND YEAR(v.dataVenda) = YEAR(CURRENT_DATE)")
    List<Venda> findVendasMesAtual();
}
