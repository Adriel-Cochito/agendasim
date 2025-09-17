package com.agendasim.service;

import com.agendasim.exception.IntegridadeReferencialException;
import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Profissional;
import com.agendasim.model.Servico;
import com.agendasim.repository.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicoServiceTest {

    @Mock
    private ServicoRepository servicoRepository;

    @InjectMocks
    private ServicoService servicoService;

    private Servico servico;
    private Profissional profissional;

    @BeforeEach
    void setUp() {
        // Setup profissional
        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setNome("Profissional Teste");
        profissional.setEmail("prof@teste.com");
        profissional.setEmpresaId(1L);

        // Setup serviço
        servico = new Servico();
        servico.setId(1L);
        servico.setTitulo("Serviço Teste");
        servico.setDescricao("Descrição do serviço");
        servico.setPreco(100.0);
        servico.setDuracao(60);
        servico.setEmpresaId(1L);
        servico.setAtivo(true);
        servico.setProfissionais(Arrays.asList(profissional));
    }

    @Test
    void testListarTodos() {
        // Given
        List<Servico> servicos = Arrays.asList(servico);
        when(servicoRepository.findAll()).thenReturn(servicos);

        // When
        List<Servico> resultado = servicoService.listarTodos();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Serviço Teste", resultado.get(0).getTitulo());
        verify(servicoRepository).findAll();
    }

    @Test
    void testCriar() {
        // Given
        when(servicoRepository.save(any(Servico.class))).thenReturn(servico);

        // When
        Servico resultado = servicoService.criar(servico);

        // Then
        assertNotNull(resultado);
        assertEquals("Serviço Teste", resultado.getTitulo());
        verify(servicoRepository).save(servico);
    }

    @Test
    void testBuscarPorIdComSucesso() {
        // Given
        when(servicoRepository.findByIdAndEmpresaId(1L, 1L)).thenReturn(Optional.of(servico));

        // When
        Servico resultado = servicoService.buscarPorId(1L, 1L);

        // Then
        assertNotNull(resultado);
        assertEquals("Serviço Teste", resultado.getTitulo());
        verify(servicoRepository).findByIdAndEmpresaId(1L, 1L);
    }

    @Test
    void testBuscarPorIdNaoEncontrado() {
        // Given
        when(servicoRepository.findByIdAndEmpresaId(999L, 1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            servicoService.buscarPorId(999L, 1L);
        });

        verify(servicoRepository).findByIdAndEmpresaId(999L, 1L);
    }

    @Test
    void testExcluirComSucesso() {
        // Given
        when(servicoRepository.existsById(1L)).thenReturn(true);

        // When
        servicoService.excluir(1L);

        // Then
        verify(servicoRepository).existsById(1L);
        verify(servicoRepository).deleteById(1L);
    }

    @Test
    void testExcluirNaoEncontrado() {
        // Given
        when(servicoRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            servicoService.excluir(999L);
        });

        verify(servicoRepository).existsById(999L);
        verify(servicoRepository, never()).deleteById(any());
    }

    @Test
    void testExcluirComIntegridadeReferencial() {
        // Given
        when(servicoRepository.existsById(1L)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("Foreign key constraint"))
            .when(servicoRepository).deleteById(1L);

        // When & Then
        assertThrows(IntegridadeReferencialException.class, () -> {
            servicoService.excluir(1L);
        });

        verify(servicoRepository).existsById(1L);
        verify(servicoRepository).deleteById(1L);
    }

    @Test
    void testAtualizarComSucesso() {
        // Given
        Servico servicoAtualizado = new Servico();
        servicoAtualizado.setTitulo("Serviço Atualizado");
        servicoAtualizado.setDescricao("Nova descrição");
        servicoAtualizado.setPreco(150.0);
        servicoAtualizado.setDuracao(90);
        servicoAtualizado.setEmpresaId(1L);
        servicoAtualizado.setAtivo(false);
        servicoAtualizado.setProfissionais(Arrays.asList(profissional));

        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));
        when(servicoRepository.save(any(Servico.class))).thenReturn(servico);

        // When
        Servico resultado = servicoService.atualizar(1L, servicoAtualizado);

        // Then
        assertNotNull(resultado);
        verify(servicoRepository).findById(1L);
        verify(servicoRepository).save(any(Servico.class));
    }

    @Test
    void testAtualizarNaoEncontrado() {
        // Given
        when(servicoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            servicoService.atualizar(999L, servico);
        });

        verify(servicoRepository).findById(999L);
        verify(servicoRepository, never()).save(any());
    }

    @Test
    void testListarPorEmpresa() {
        // Given
        List<Servico> servicos = Arrays.asList(servico);
        when(servicoRepository.findByEmpresaId(1L)).thenReturn(servicos);

        // When
        List<Servico> resultado = servicoService.listarPorEmpresa(1L);

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Serviço Teste", resultado.get(0).getTitulo());
        verify(servicoRepository).findByEmpresaId(1L);
    }
}
