package com.agendasim.service;

import com.agendasim.dto.AgendaAdminDTO;
import com.agendasim.dto.AgendaClienteDTO;
import com.agendasim.exception.ConflitoAgendamentoException;
import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Agenda;
import com.agendasim.model.Empresa;
import com.agendasim.model.Profissional;
import com.agendasim.model.Servico;
import com.agendasim.repository.AgendaRepository;
import com.agendasim.repository.DisponibilidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private DisponibilidadeRepository disponibilidadeRepository;

    @InjectMocks
    private AgendaService agendaService;

    private Agenda agenda;
    private Empresa empresa;
    private Profissional profissional;
    private Servico servico;

    @BeforeEach
    void setUp() {
        // Setup empresa
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNome("Empresa Teste");

        // Setup profissional
        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setNome("Profissional Teste");
        profissional.setEmail("prof@teste.com");

        // Setup serviço
        servico = new Servico();
        servico.setId(1L);
        servico.setTitulo("Serviço Teste");
        servico.setDuracao(60);

        // Setup agenda
        agenda = new Agenda();
        agenda.setId(1L);
        agenda.setNomeCliente("Cliente Teste");
        agenda.setTelefoneCliente("11999999999");
        agenda.setDataHora(Instant.now().plusSeconds(3600)); // 1 hora no futuro
        agenda.setStatus("AGENDADO");
        agenda.setEmpresa(empresa);
        agenda.setProfissional(profissional);
        agenda.setServico(servico);
    }

    @Test
    void testListarTodos() {
        // Given
        List<Agenda> agendas = Arrays.asList(agenda);
        when(agendaRepository.findAll()).thenReturn(agendas);

        // When
        List<Agenda> resultado = agendaService.listarTodos();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(agenda, resultado.get(0));
        verify(agendaRepository).findAll();
    }

    @Test
    void testCriarAgendaComSucesso() {
        // Given
        when(agendaRepository.countConflitoAgendamento(anyLong(), anyLong(), any(Instant.class), anyLong()))
                .thenReturn(0L);
        when(disponibilidadeRepository.countConflitoBloqueio(anyLong(), anyLong()))
                .thenReturn(0L);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);

        // When
        Agenda resultado = agendaService.criar(agenda, 1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getEmpresa().getId());
        verify(agendaRepository).save(agenda);
    }

    @Test
    void testCriarAgendaComConflito() {
        // Given
        when(agendaRepository.countConflitoAgendamento(anyLong(), anyLong(), any(Instant.class), anyLong()))
                .thenReturn(1L); // Conflito encontrado

        // When & Then
        ConflitoAgendamentoException exception = assertThrows(
                ConflitoAgendamentoException.class,
                () -> agendaService.criar(agenda, 1L)
        );

        assertEquals("Não é possível criar o agendamento: conflito de horário com outro agendamento ou bloqueio existente.", 
                exception.getMessage());
        verify(agendaRepository, never()).save(any(Agenda.class));
    }

    @Test
    void testBuscarPorIdComSucesso() {
        // Given
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        // When
        Agenda resultado = agendaService.buscarPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(agenda, resultado);
        verify(agendaRepository).findById(1L);
    }

    @Test
    void testBuscarPorIdNaoEncontrado() {
        // Given
        when(agendaRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> agendaService.buscarPorId(999L)
        );

        assertEquals("Agenda id=999 não encontrada", exception.getMessage());
    }

    @Test
    void testExcluirComSucesso() {
        // Given
        when(agendaRepository.existsById(1L)).thenReturn(true);

        // When
        agendaService.excluir(1L);

        // Then
        verify(agendaRepository).existsById(1L);
        verify(agendaRepository).deleteById(1L);
    }

    @Test
    void testExcluirNaoEncontrado() {
        // Given
        when(agendaRepository.existsById(999L)).thenReturn(false);

        // When & Then
        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> agendaService.excluir(999L)
        );

        assertEquals("Agenda não encontrada com ID: 999", exception.getMessage());
        verify(agendaRepository, never()).deleteById(anyLong());
    }

    @Test
    void testAtualizarComSucesso() {
        // Given
        Agenda agendaAtualizada = new Agenda();
        agendaAtualizada.setNomeCliente("Cliente Atualizado");
        agendaAtualizada.setTelefoneCliente("11888888888");
        agendaAtualizada.setDataHora(Instant.now().plusSeconds(7200)); // 2 horas no futuro
        agendaAtualizada.setStatus("CONFIRMADO");
        agendaAtualizada.setProfissional(profissional);
        agendaAtualizada.setServico(servico);

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(agendaRepository.countConflitoAgendamento(anyLong(), anyLong(), any(Instant.class), anyLong()))
                .thenReturn(0L);
        when(disponibilidadeRepository.countConflitoBloqueio(anyLong(), anyLong()))
                .thenReturn(0L);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);

        // When
        Agenda resultado = agendaService.atualizar(1L, agendaAtualizada, 1L);

        // Then
        assertNotNull(resultado);
        verify(agendaRepository).save(any(Agenda.class));
    }

    @Test
    void testListarPorEmpresa() {
        // Given
        List<Agenda> agendas = Arrays.asList(agenda);
        when(agendaRepository.findByEmpresaId(1L)).thenReturn(agendas);

        // When
        List<Agenda> resultado = agendaService.listarPorEmpresa(1L);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(agenda, resultado.get(0));
        verify(agendaRepository).findByEmpresaId(1L);
    }

    @Test
    void testListarPorServico() {
        // Given
        List<Agenda> agendas = Arrays.asList(agenda);
        when(agendaRepository.findByServicoIdAndEmpresaId(1L, 1L)).thenReturn(agendas);

        // When
        List<Agenda> resultado = agendaService.listarPorServico(1L, 1L);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(agenda, resultado.get(0));
        verify(agendaRepository).findByServicoIdAndEmpresaId(1L, 1L);
    }

    @Test
    void testListarParaAdmin() {
        // Given
        List<Agenda> agendas = Arrays.asList(agenda);
        when(agendaRepository.findByEmpresaId(1L)).thenReturn(agendas);

        // When
        List<AgendaAdminDTO> resultado = agendaService.listarParaAdmin(1L);

        // Then
        assertNotNull(resultado);
        verify(agendaRepository).findByEmpresaId(1L);
    }

    @Test
    void testListarParaCliente() {
        // Given
        List<Agenda> agendas = Arrays.asList(agenda);
        when(agendaRepository.findByEmpresaServicoEProfissional(1L, 1L, 1L)).thenReturn(agendas);

        // When
        List<AgendaClienteDTO> resultado = agendaService.listarParaCliente(1L, 1L, 1L);

        // Then
        assertNotNull(resultado);
        verify(agendaRepository).findByEmpresaServicoEProfissional(1L, 1L, 1L);
    }

    @Test
    void testListarParaClienteData() {
        // Given
        LocalDate data = LocalDate.now();
        List<Agenda> agendas = Arrays.asList(agenda);
        when(agendaRepository.findByEmpresaServicoEProfissionalEData(
                anyLong(), anyLong(), anyLong(), any(Instant.class), any(Instant.class)))
                .thenReturn(agendas);

        // When
        List<AgendaClienteDTO> resultado = agendaService.listarParaClienteData(1L, 1L, 1L, data);

        // Then
        assertNotNull(resultado);
        verify(agendaRepository).findByEmpresaServicoEProfissionalEData(
                anyLong(), anyLong(), anyLong(), any(Instant.class), any(Instant.class));
    }

    @Test
    void testListarPorEmpresaProfissionalEData() {
        // Given
        LocalDate data = LocalDate.now();
        List<Agenda> agendas = Arrays.asList(agenda);
        when(agendaRepository.findByEmpresaProfissionalEData(
                anyLong(), anyLong(), any(Instant.class), any(Instant.class)))
                .thenReturn(agendas);

        // When
        List<AgendaAdminDTO> resultado = agendaService.listarPorEmpresaProfissionalEData(1L, 1L, data);

        // Then
        assertNotNull(resultado);
        verify(agendaRepository).findByEmpresaProfissionalEData(
                anyLong(), anyLong(), any(Instant.class), any(Instant.class));
    }
}
