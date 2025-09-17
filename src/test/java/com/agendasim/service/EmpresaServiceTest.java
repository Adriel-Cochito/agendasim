package com.agendasim.service;

import com.agendasim.dto.CriarEmpresaComOwnerDTO;
import com.agendasim.dto.EmpresaComOwnerResponseDTO;
import com.agendasim.enums.Perfil;
import com.agendasim.exception.EmailJaCadastradoException;
import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Empresa;
import com.agendasim.model.Profissional;
import com.agendasim.repository.EmpresaRepository;
import com.agendasim.repository.ProfissionalRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private ProfissionalService profissionalService;

    @Mock
    private ProfissionalRepository profissionalRepository;

    @InjectMocks
    private EmpresaService empresaService;

    private Empresa empresa;
    private Profissional profissional;
    private CriarEmpresaComOwnerDTO dto;

    @BeforeEach
    void setUp() {
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
        profissional.setSenha("senha123");
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
    void testListarTodas() {
        // Given
        List<Empresa> empresas = Arrays.asList(empresa);
        when(empresaRepository.findAll()).thenReturn(empresas);

        // When
        List<Empresa> resultado = empresaService.listarTodas();

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Empresa Teste", resultado.get(0).getNome());
        verify(empresaRepository).findAll();
    }

    @Test
    void testCriar() {
        // Given
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

        // When
        Empresa resultado = empresaService.criar(empresa);

        // Then
        assertNotNull(resultado);
        assertEquals("Empresa Teste", resultado.getNome());
        verify(empresaRepository).save(empresa);
    }

    @Test
    void testCriarEmpresaComOwnerComSucesso() {
        // Given
        when(profissionalRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);
        when(profissionalService.salvar(any(Profissional.class))).thenReturn(profissional);

        // When
        EmpresaComOwnerResponseDTO resultado = empresaService.criarEmpresaComOwner(dto);

        // Then
        assertNotNull(resultado);
        assertEquals("Empresa Teste", resultado.getEmpresa().getNome());
        assertEquals("Profissional Teste", resultado.getProfissional().getNome());
        verify(profissionalRepository).findByEmail("prof@teste.com");
        verify(empresaRepository).save(any(Empresa.class));
        verify(profissionalService).salvar(any(Profissional.class));
    }

    @Test
    void testCriarEmpresaComOwnerComEmailJaCadastrado() {
        // Given
        when(profissionalRepository.findByEmail(anyString())).thenReturn(Optional.of(profissional));

        // When & Then
        assertThrows(EmailJaCadastradoException.class, () -> {
            empresaService.criarEmpresaComOwner(dto);
        });

        verify(profissionalRepository).findByEmail("prof@teste.com");
        verify(empresaRepository, never()).save(any(Empresa.class));
        verify(profissionalService, never()).salvar(any(Profissional.class));
    }

    @Test
    void testCriarEmpresaComOwnerComErroDeConcorrencia() {
        // Given
        when(profissionalRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);
        when(profissionalService.salvar(any(Profissional.class)))
            .thenThrow(new DataIntegrityViolationException("Email já cadastrado"));

        // When & Then
        assertThrows(EmailJaCadastradoException.class, () -> {
            empresaService.criarEmpresaComOwner(dto);
        });

        verify(profissionalRepository).findByEmail("prof@teste.com");
        verify(empresaRepository).save(any(Empresa.class));
        verify(profissionalService).salvar(any(Profissional.class));
    }

    @Test
    void testBuscarPorIdComSucesso() {
        // Given
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));

        // When
        Empresa resultado = empresaService.buscarPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("Empresa Teste", resultado.getNome());
        verify(empresaRepository).findById(1L);
    }

    @Test
    void testBuscarPorIdNaoEncontrado() {
        // Given
        when(empresaRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            empresaService.buscarPorId(999L);
        });

        verify(empresaRepository).findById(999L);
    }

    @Test
    void testExcluirComSucesso() {
        // Given
        when(empresaRepository.existsById(1L)).thenReturn(true);

        // When
        empresaService.excluir(1L);

        // Then
        verify(empresaRepository).existsById(1L);
        verify(empresaRepository).deleteById(1L);
    }

    @Test
    void testExcluirNaoEncontrado() {
        // Given
        when(empresaRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            empresaService.excluir(999L);
        });

        verify(empresaRepository).existsById(999L);
        verify(empresaRepository, never()).deleteById(any());
    }

    @Test
    void testAtualizarComSucesso() {
        // Given
        Empresa empresaAtualizada = new Empresa();
        empresaAtualizada.setNome("Empresa Atualizada");
        empresaAtualizada.setEmail("nova@teste.com");
        empresaAtualizada.setTelefone("+55 31 88888-7777");
        empresaAtualizada.setCnpj("98.765.432/0001-10");
        empresaAtualizada.setAtivo(false);

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

        // When
        Empresa resultado = empresaService.atualizar(1L, empresaAtualizada);

        // Then
        assertNotNull(resultado);
        verify(empresaRepository).findById(1L);
        verify(empresaRepository).save(any(Empresa.class));
    }

    @Test
    void testAtualizarNaoEncontrado() {
        // Given
        when(empresaRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            empresaService.atualizar(999L, empresa);
        });

        verify(empresaRepository).findById(999L);
        verify(empresaRepository, never()).save(any());
    }
}
