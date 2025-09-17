package com.agendasim.controller;

import com.agendasim.model.Profissional;
import com.agendasim.model.Servico;
import com.agendasim.service.ServicoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServicoController.class)
class ServicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicoService servicoService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testListarPorEmpresa() throws Exception {
        // Given
        List<Servico> servicos = Arrays.asList(servico);
        when(servicoService.listarPorEmpresa(1L)).thenReturn(servicos);

        // When & Then
        mockMvc.perform(get("/servicos")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].titulo").value("Serviço Teste"))
                .andExpect(jsonPath("$[0].preco").value(100.0))
                .andExpect(jsonPath("$[0].duracao").value(60));
    }

    @Test
    void testCriar() throws Exception {
        // Given
        when(servicoService.criar(any(Servico.class))).thenReturn(servico);

        // When & Then
        mockMvc.perform(post("/servicos")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servico)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Serviço Teste"))
                .andExpect(jsonPath("$.preco").value(100.0))
                .andExpect(jsonPath("$.duracao").value(60));
    }

    @Test
    void testBuscar() throws Exception {
        // Given
        when(servicoService.buscarPorId(1L, 1L)).thenReturn(servico);

        // When & Then
        mockMvc.perform(get("/servicos/1")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Serviço Teste"))
                .andExpect(jsonPath("$.preco").value(100.0))
                .andExpect(jsonPath("$.duracao").value(60));
    }

    @Test
    void testAtualizar() throws Exception {
        // Given
        when(servicoService.atualizar(anyLong(), any(Servico.class))).thenReturn(servico);

        // When & Then
        mockMvc.perform(put("/servicos/1")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servico)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Serviço Teste"))
                .andExpect(jsonPath("$.preco").value(100.0))
                .andExpect(jsonPath("$.duracao").value(60));
    }

    @Test
    void testExcluir() throws Exception {
        // When & Then
        mockMvc.perform(delete("/servicos/1")
                        .param("empresaId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
