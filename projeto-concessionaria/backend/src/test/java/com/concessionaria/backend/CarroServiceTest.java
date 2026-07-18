package com.concessionaria.backend;

import com.concessionaria.backend.dto.CarroDTO;
import com.concessionaria.backend.model.Carro;
import com.concessionaria.backend.model.Carro.StatusCarro;
import com.concessionaria.backend.repository.CarroRepository;
import com.concessionaria.backend.service.AuditoriaService;
import com.concessionaria.backend.service.CarroService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  //mockito: simula dependências
public class CarroServiceTest {

    @Mock
    private CarroRepository repository;

    @Mock
    private AuditoriaService auditoriaService;

    @InjectMocks
    private CarroService service;

    @Test
    void deveCadastrarCarro() {
        CarroDTO dto = new CarroDTO();
        dto.setMarca("Toyota"); dto.setModelo("Corolla");
        dto.setAno(2022); dto.setPreco(95000.0);

        Carro carroSalvo = new Carro();
        carroSalvo.setId(1L);
        carroSalvo.setMarca("Toyota");
        carroSalvo.setModelo("Corolla");

        when(repository.save(any(Carro.class))).thenReturn(carroSalvo);

        Carro resultado = service.cadastrar(dto);

        assertNotNull(resultado);
        assertEquals("Toyota", resultado.getMarca());
        verify(repository, times(1)).save(any(Carro.class));
        verify(auditoriaService, times(1))
                .registrar(eq("Carro"), any(), eq("CRIACAO"), isNull(), anyString());
    }

    @Test
    void deveLancarExcecaoQuandoCarroNaoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void deveDeletarCarro() {
        Carro carro = new Carro();
        carro.setId(1L); carro.setMarca("Toyota");
        carro.setModelo("Corolla"); carro.setAno(2022);
        carro.setPreco(95000.0); carro.setStatus(StatusCarro.DISPONIVEL);

        when(repository.findById(1L)).thenReturn(Optional.of(carro));
        doNothing().when(repository).deleteById(1L);

        service.deletar(1L);

        verify(repository, times(1)).deleteById(1L);
        verify(auditoriaService, times(1))
                .registrar(eq("Carro"), eq(1L), eq("REMOCAO"), anyString(), isNull());
    }
}
