package com.concessionaria.backend;

import com.concessionaria.backend.dto.VendaDTO;
import com.concessionaria.backend.model.Carro;
import com.concessionaria.backend.model.Carro.StatusCarro;
import com.concessionaria.backend.model.Cliente;
import com.concessionaria.backend.model.Venda;
import com.concessionaria.backend.repository.VendaRepository;
import com.concessionaria.backend.service.AuditoriaService;
import com.concessionaria.backend.service.CarroService;
import com.concessionaria.backend.service.ClienteService;
import com.concessionaria.backend.service.VendaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;
    @Mock
    private CarroService carroService;
    @Mock
    private ClienteService clienteService;
    @Mock
    private AuditoriaService auditoriaService;

    @InjectMocks
    private VendaService vendaService;

    @Test
    void deveRealizarVendaComSucesso() {
        Carro carro = new Carro();
        carro.setId(1L); carro.setPreco(95000.0);
        carro.setStatus(StatusCarro.DISPONIVEL);

        Cliente cliente = new Cliente();
        cliente.setId(1L); cliente.setNome("João Silva");

        Venda vendaSalva = new Venda();
        vendaSalva.setId(1L);
        vendaSalva.setCarro(carro);
        vendaSalva.setCliente(cliente);
        vendaSalva.setValorFinal(95000.0);

        when(carroService.buscarPorId(1L)).thenReturn(carro);
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaSalva);

        VendaDTO dto = new VendaDTO();
        dto.setCarroId(1L); dto.setClienteId(1L);

        Venda resultado = vendaService.realizar(dto);

        assertNotNull(resultado);
        assertEquals(StatusCarro.VENDIDO, carro.getStatus());
        verify(vendaRepository, times(1)).save(any(Venda.class));
    }

    @Test
    void deveLancarErroAoVenderCarroIndisponivel() {
        Carro carro = new Carro();
        carro.setId(1L); carro.setStatus(StatusCarro.VENDIDO);

        when(carroService.buscarPorId(1L)).thenReturn(carro);

        VendaDTO dto = new VendaDTO();
        dto.setCarroId(1L); dto.setClienteId(1L);

        assertThrows(RuntimeException.class, () -> vendaService.realizar(dto));
    }
}
