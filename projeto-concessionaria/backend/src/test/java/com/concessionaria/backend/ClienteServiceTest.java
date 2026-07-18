package com.concessionaria.backend;

import com.concessionaria.backend.dto.ClienteDTO;
import com.concessionaria.backend.model.Cliente;
import com.concessionaria.backend.repository.ClienteRepository;
import com.concessionaria.backend.service.AuditoriaService;
import com.concessionaria.backend.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @Mock
    private AuditoriaService auditoriaService;

    @InjectMocks
    private ClienteService service;

    @Test
    void deveCadastrarCliente() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("João Silva");
        dto.setCpf("111.111.111-11");
        dto.setEmail("joao@email.com");
        dto.setTelefone("51999990001");

        Cliente salvo = new Cliente();
        salvo.setId(1L);
        salvo.setNome("João Silva");
        salvo.setCpf("111.111.111-11");
        salvo.setEmail("joao@email.com");
        salvo.setTelefone("51999990001");

        when(repository.existsByCpf("111.111.111-11")).thenReturn(false);
        when(repository.save(any(Cliente.class))).thenReturn(salvo);

        Cliente resultado = service.cadastrar(dto);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(repository, times(1)).save(any(Cliente.class));
        verify(auditoriaService, times(1))
                .registrar(eq("Cliente"), any(), eq("CRIACAO"), isNull(), anyString());
    }

    @Test
    void deveLancarErroParaCpfDuplicado() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("Maria");
        dto.setCpf("111.111.111-11");

        when(repository.existsByCpf("111.111.111-11")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.cadastrar(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void deveAtualizarCliente() {
        Cliente existente = new Cliente();
        existente.setId(1L);
        existente.setNome("João");
        existente.setCpf("111.111.111-11");
        existente.setEmail("joao@email.com");
        existente.setTelefone("51999990001");

        ClienteDTO dto = new ClienteDTO();
        dto.setNome("João Atualizado");
        dto.setEmail("novo@email.com");
        dto.setTelefone("51999990099");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Cliente.class))).thenReturn(existente);

        Cliente resultado = service.atualizar(1L, dto);

        assertNotNull(resultado);
        verify(auditoriaService, times(1))
                .registrar(eq("Cliente"), eq(1L), eq("ATUALIZACAO"), anyString(), anyString());
    }

    @Test
    void deveDeletarCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João");
        cliente.setCpf("111.111.111-11");

        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(repository).deleteById(1L);

        service.deletar(1L);

        verify(repository, times(1)).deleteById(1L);
        verify(auditoriaService, times(1))
                .registrar(eq("Cliente"), eq(1L), eq("REMOCAO"), anyString(), isNull());
    }

    @Test
    void deveLancarErroQuandoClienteNaoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.buscarPorId(99L));
    }
}
