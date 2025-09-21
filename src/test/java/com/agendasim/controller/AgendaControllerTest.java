package com.agendasim.controller;

import com.agendasim.dto.AgendaAdminDTO;
import com.agendasim.dto.AgendaClienteDTO;
import com.agendasim.exception.ConflitoAgendamentoException;
import com.agendasim.exception.GlobalExceptionHandler;
import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Agenda;
import com.agendasim.model.Empresa;
import com.agendasim.model.Profissional;
import com.agendasim.model.Servico;
import com.agendasim.service.AgendaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AgendaControllerTest {

    @Mock
    private AgendaService agendaService;

    @InjectMocks
    private AgendaController agendaController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Agenda agenda;
    private Empresa empresa;
    private Profissional profissional;
    private Servico servico;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        mockMvc = MockMvcBuilders
                .standaloneSetup(agendaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

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
        agenda.setTelefoneCliente("+55 31 99999-8888");
        agenda.setDataHora(Instant.now().plusSeconds(3600));
        agenda.setStatus("AGENDADO");
        agenda.setEmpresa(empresa);
        agenda.setProfissional(profissional);
        agenda.setServico(servico);
    }

    @Test
    void testListarPorEmpresa() throws Exception {
        // Given
        List<Agenda> agendas = Arrays.asList(agenda);
        when(agendaService.listarPorEmpresa(1L)).thenReturn(agendas);

        // When & Then
        mockMvc.perform(get("/agendas")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nomeCliente").value("Cliente Teste"));

        verify(agendaService).listarPorEmpresa(1L);
    }

    @Test
    void testCriarAgenda() throws Exception {
        // Given
        when(agendaService.criar(any(Agenda.class), eq(1L))).thenReturn(agenda);

        // When & Then
        mockMvc.perform(post("/agendas")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agenda)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeCliente").value("Cliente Teste"));

        verify(agendaService).criar(any(Agenda.class), eq(1L));
    }

    @Test
    void testCriarAgendaComConflito() throws Exception {
        // Given
        when(agendaService.criar(any(Agenda.class), eq(1L)))
                .thenThrow(new ConflitoAgendamentoException("Conflito de horário"));

        // When & Then
        mockMvc.perform(post("/agendas")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agenda)))
                .andExpect(status().isConflict());

        verify(agendaService).criar(any(Agenda.class), eq(1L));
    }

    @Test
    void testBuscarAgenda() throws Exception {
        // Given
        when(agendaService.buscarPorId(1L)).thenReturn(agenda);

        // When & Then
        mockMvc.perform(get("/agendas/1")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeCliente").value("Cliente Teste"));

        verify(agendaService).buscarPorId(1L);
    }

    @Test
    void testBuscarAgendaNaoEncontrada() throws Exception {
        // Given
        when(agendaService.buscarPorId(999L))
                .thenThrow(new RecursoNaoEncontradoException("Agenda não encontrada"));

        // When & Then
        mockMvc.perform(get("/agendas/999")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(agendaService).buscarPorId(999L);
    }

    @Test
    void testAtualizarAgenda() throws Exception {
        // Given
        when(agendaService.atualizar(eq(1L), any(Agenda.class), eq(1L))).thenReturn(agenda);

        // When & Then
        mockMvc.perform(put("/agendas/1")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agenda)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeCliente").value("Cliente Teste"));

        verify(agendaService).atualizar(eq(1L), any(Agenda.class), eq(1L));
    }

    @Test
    void testExcluirAgenda() throws Exception {
        // Given
        doNothing().when(agendaService).excluir(1L);

        // When & Then
        mockMvc.perform(delete("/agendas/1")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(agendaService).excluir(1L);
    }

    @Test
    void testExcluirAgendaNaoEncontrada() throws Exception {
        // Given
        doThrow(new RecursoNaoEncontradoException("Agenda não encontrada"))
                .when(agendaService).excluir(999L);

        // When & Then
        mockMvc.perform(delete("/agendas/999")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(agendaService).excluir(999L);
    }

    @Test
    void testListarPorServico() throws Exception {
        // Given
        List<Agenda> agendas = Arrays.asList(agenda);
        when(agendaService.listarPorServico(1L, 1L)).thenReturn(agendas);

        // When & Then
        mockMvc.perform(get("/agendas/servico/1")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(agendaService).listarPorServico(1L, 1L);
    }

    @Test
    void testListarParaAdmin() throws Exception {
        // Given
        AgendaAdminDTO adminDTO = new AgendaAdminDTO();
        adminDTO.setId(1L);
        adminDTO.setNomeCliente("Cliente Teste");
        List<AgendaAdminDTO> adminDTOs = Arrays.asList(adminDTO);
        when(agendaService.listarParaAdmin(1L)).thenReturn(adminDTOs);

        // When & Then
        mockMvc.perform(get("/agendas/admin")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nomeCliente").value("Cliente Teste"));

        verify(agendaService).listarParaAdmin(1L);
    }

    @Test
    void testListarParaAdminEData() throws Exception {
        // Given
        AgendaAdminDTO adminDTO = new AgendaAdminDTO();
        adminDTO.setId(1L);
        adminDTO.setNomeCliente("Cliente Teste");
        List<AgendaAdminDTO> adminDTOs = Arrays.asList(adminDTO);
        LocalDate data = LocalDate.now();
        when(agendaService.listarPorEmpresaProfissionalEData(1L, 1L, data)).thenReturn(adminDTOs);

        // When & Then
        mockMvc.perform(get("/agendas/admin/data")
                        .param("empresaId", "1")
                        .param("profissionalId", "1")
                        .param("data", data.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(agendaService).listarPorEmpresaProfissionalEData(1L, 1L, data);
    }

    @Test
    void testListarParaCliente() throws Exception {
        // Given
        AgendaClienteDTO clienteDTO = new AgendaClienteDTO();
        clienteDTO.setDataHora(Instant.now().plusSeconds(3600));
        clienteDTO.setDuracao(60);
        List<AgendaClienteDTO> clienteDTOs = Arrays.asList(clienteDTO);
        when(agendaService.listarParaCliente(1L, 1L, 1L)).thenReturn(clienteDTOs);

        // When & Then
        mockMvc.perform(get("/agendas/cliente")
                        .param("empresaId", "1")
                        .param("servicoId", "1")
                        .param("profissionalId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].duracao").value(60));

        verify(agendaService).listarParaCliente(1L, 1L, 1L);
    }
}
