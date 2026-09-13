package com.concessionaria.testdrive;

import com.concessionaria.testdrive.dto.TestDriveDTO;
import com.concessionaria.testdrive.model.TestDrive;
import com.concessionaria.testdrive.model.TestDrive.StatusTestDrive;
import com.concessionaria.testdrive.repository.TestDriveRepository;
import com.concessionaria.testdrive.service.TestDriveService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestDriveServiceTest {

    @Mock
    private TestDriveRepository repository;

    @InjectMocks
    private TestDriveService service;

    @Test
    void deveAgendarTestDrive() {
        TestDriveDTO dto = new TestDriveDTO();
        dto.setCarroId(1L);
        dto.setCarroMarca("Toyota"); dto.setCarroModelo("Corolla");
        dto.setClienteNome("Apolo"); dto.setClienteTelefone("51999990001");
        dto.setDataAgendada(LocalDateTime.now().plusDays(1));

        TestDrive salvo = new TestDrive();
        salvo.setId(1L);
        salvo.setStatus(StatusTestDrive.AGENDADO);

        when(repository.save(any(TestDrive.class))).thenReturn(salvo);

        TestDrive resultado = service.agendar(dto);

        assertNotNull(resultado);
        assertEquals(StatusTestDrive.AGENDADO, resultado.getStatus());
        verify(repository, times(1)).save(any(TestDrive.class));
    }

    @Test
    void deveRealizarTestDrive() {
        TestDrive td = new TestDrive();
        td.setId(1L);
        td.setStatus(StatusTestDrive.AGENDADO);

        when(repository.findById(1L)).thenReturn(Optional.of(td));
        when(repository.save(any(TestDrive.class))).thenReturn(td);

        TestDrive resultado = service.realizarTestDrive(1L);

        assertEquals(StatusTestDrive.REALIZADO, resultado.getStatus());
        verify(repository, times(1)).save(any(TestDrive.class));
    }

    @Test
    void deveLancarErroAoRealizarTestDriveNaoAgendado() {
        TestDrive td = new TestDrive();
        td.setId(1L);
        td.setStatus(StatusTestDrive.CANCELADO);

        when(repository.findById(1L)).thenReturn(Optional.of(td));

        assertThrows(RuntimeException.class, () -> service.realizarTestDrive(1L));
    }

    @Test
    void deveCancelarTestDrive() {
        TestDrive td = new TestDrive();
        td.setId(1L);
        td.setStatus(StatusTestDrive.AGENDADO);

        when(repository.findById(1L)).thenReturn(Optional.of(td));
        when(repository.save(any(TestDrive.class))).thenReturn(td);

        TestDrive resultado = service.cancelar(1L);

        assertEquals(StatusTestDrive.CANCELADO, resultado.getStatus());
    }

    @Test
    void deveLancarErroAoCancelarTestDriveRealizado() {
        TestDrive td = new TestDrive();
        td.setId(1L);
        td.setStatus(StatusTestDrive.REALIZADO);

        when(repository.findById(1L)).thenReturn(Optional.of(td));

        assertThrows(RuntimeException.class, () -> service.cancelar(1L));
    }

    @Test
    void deveLancarErroQuandoNaoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.buscarPorId(99L));
    }
}