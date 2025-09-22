package com.agendasim.controller;

import com.agendasim.dto.CriarEmpresaComOwnerDTO;
import com.agendasim.dto.EmpresaComOwnerResponseDTO;
import com.agendasim.enums.Perfil;
import com.agendasim.exception.GlobalExceptionHandler;
import com.agendasim.model.Empresa;
import com.agendasim.model.Profissional;
import com.agendasim.service.EmpresaService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmpresaControllerTest {

    @Mock
    private EmpresaService empresaService;

    @InjectMocks
    private EmpresaController empresaController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Empresa empresa;
    private Profissional profissional;
    private CriarEmpresaComOwnerDTO dto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        mockMvc = MockMvcBuilders
                .standaloneSetup(empresaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
        
        // Setup empresa
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNome("Empresa Teste");
        empresa.setEmail("empresa@teste.com");
        empresa.setTelefone("+55 31 99999-8888");
        empresa.setCnpj("12.345.678/0001-90");
        empresa.setAtivo(true);

        // Setup profissional
        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setNome("Profissional Teste");
        profissional.setEmail("prof@teste.com");
        profissional.setPerfil(Perfil.OWNER);
        profissional.setEmpresaId(1L);
        profissional.setAtivo(true);

        // Setup DTO
        dto = new CriarEmpresaComOwnerDTO();
        dto.setNomeEmpresa("Empresa Teste");
        dto.setEmailEmpresa("empresa@teste.com");
        dto.setTelefoneEmpresa("+55 31 99999-8888");
        dto.setCnpjEmpresa("12.345.678/0001-90");
        dto.setAtivoEmpresa(true);
        dto.setNomeProfissional("Profissional Teste");
        dto.setEmailProfissional("prof@teste.com");
        dto.setSenhaProfissional("senha123");
        dto.setPerfilProfissional(Perfil.OWNER);
        dto.setAtivoProfissional(true);
    }

    @Test
    void testListar() throws Exception {
        // Given
        List<Empresa> empresas = Arrays.asList(empresa);
        when(empresaService.listarTodas()).thenReturn(empresas);

        // When & Then
        mockMvc.perform(get("/empresas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("Empresa Teste"))
                .andExpect(jsonPath("$[0].email").value("empresa@teste.com"));
    }

    @Test
    void testCriar() throws Exception {
        // Given
        when(empresaService.criar(any(Empresa.class))).thenReturn(empresa);

        // When & Then
        mockMvc.perform(post("/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empresa)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Empresa Teste"))
                .andExpect(jsonPath("$.email").value("empresa@teste.com"));
    }

    @Test
    void testCriarComOwner() throws Exception {
        // Given
        EmpresaComOwnerResponseDTO response = new EmpresaComOwnerResponseDTO(empresa, profissional);
        when(empresaService.criarEmpresaComOwner(any(CriarEmpresaComOwnerDTO.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/empresas/com-owner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empresa.nome").value("Empresa Teste"))
                .andExpect(jsonPath("$.profissional.nome").value("Profissional Teste"));
    }

    @Test
    void testBuscarPorId() throws Exception {
        // Given
        when(empresaService.buscarPorId(1L)).thenReturn(empresa);

        // When & Then
        mockMvc.perform(get("/empresas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Empresa Teste"))
                .andExpect(jsonPath("$.email").value("empresa@teste.com"));
    }

    @Test
    void testAtualizar() throws Exception {
        // Given
        when(empresaService.atualizar(anyLong(), any(Empresa.class))).thenReturn(empresa);

        // When & Then
        mockMvc.perform(put("/empresas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empresa)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Empresa Teste"))
                .andExpect(jsonPath("$.email").value("empresa@teste.com"));
    }

    @Test
    void testExcluir() throws Exception {
        // When & Then
        mockMvc.perform(delete("/empresas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
