package com.agendasim.service;

import com.agendasim.dto.ProfissionalPatchDTO;
import com.agendasim.enums.Perfil;
import com.agendasim.exception.RecursoNaoEncontradoException;
import com.agendasim.model.Profissional;
import com.agendasim.repository.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfissionalServiceTest {

    @Mock
    private ProfissionalRepository profissionalRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private ProfissionalService profissionalService;

    private Profissional profissional;

    @BeforeEach
    void setUp() {
        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setNome("Profissional Teste");
        profissional.setEmail("prof@teste.com");
        profissional.setSenha("senha123");
        profissional.setPerfil(Perfil.USER);
        profissional.setAtivo(true);
        profissional.setEmpresaId(1L);
    }

    @Test
    void testSalvarComSucesso() {
        // Given
        String senhaCriptografada = "senha_criptografada";
        when(passwordEncoder.encode("senha123")).thenReturn(senhaCriptografada);
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

        // When
        Profissional resultado = profissionalService.salvar(profissional);

        // Then
        assertNotNull(resultado);
        assertEquals(senhaCriptografada, resultado.getSenha());
        verify(passwordEncoder).encode("senha123");
        verify(profissionalRepository).save(profissional);
    }

    @Test
    void testListarTodos() {
        // Given
        List<Profissional> profissionais = Arrays.asList(profissional);
        when(profissionalRepository.findAll()).thenReturn(profissionais);

        // When
        List<Profissional> resultado = profissionalService.listarTodos();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(profissional, resultado.get(0));
        verify(profissionalRepository).findAll();
    }

    @Test
    void testBuscarPorIdComSucesso() {
        // Given
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));

        // When
        Profissional resultado = profissionalService.buscarPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(profissional, resultado);
        verify(profissionalRepository).findById(1L);
    }

    @Test
    void testBuscarPorIdNaoEncontrado() {
        // Given
        when(profissionalRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> profissionalService.buscarPorId(999L)
        );

        assertEquals("Profissional id=999 não encontrado", exception.getMessage());
    }

    @Test
    void testExcluirComSucesso() {
        // Given
        when(profissionalRepository.existsById(1L)).thenReturn(true);

        // When
        profissionalService.excluir(1L);

        // Then
        verify(profissionalRepository).existsById(1L);
        verify(profissionalRepository).deleteById(1L);
    }

    @Test
    void testExcluirNaoEncontrado() {
        // Given
        when(profissionalRepository.existsById(999L)).thenReturn(false);

        // When & Then
        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> profissionalService.excluir(999L)
        );

        assertEquals("Profissional com ID 999 não encontrado", exception.getMessage());
        verify(profissionalRepository, never()).deleteById(anyLong());
    }

    @Test
    void testAtualizarComSucesso() {
        // Given
        Profissional profissionalAtualizado = new Profissional();
        profissionalAtualizado.setNome("Profissional Atualizado");
        profissionalAtualizado.setEmail("prof.atualizado@teste.com");
        profissionalAtualizado.setPerfil(Perfil.ADMIN);
        profissionalAtualizado.setAtivo(false);
        profissionalAtualizado.setEmpresaId(2L);
        profissionalAtualizado.setGoogleAccessToken("token123");
        profissionalAtualizado.setGoogleRefreshToken("refresh123");

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

        // When
        Profissional resultado = profissionalService.atualizar(1L, profissionalAtualizado);

        // Then
        assertNotNull(resultado);
        verify(profissionalRepository).save(any(Profissional.class));
    }

    @Test
    void testAtualizarComSenha() {
        // Given
        Profissional profissionalAtualizado = new Profissional();
        profissionalAtualizado.setNome("Profissional Atualizado");
        profissionalAtualizado.setSenha("nova_senha123");

        String senhaCriptografada = "nova_senha_criptografada";
        when(passwordEncoder.encode("nova_senha123")).thenReturn(senhaCriptografada);
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

        // When
        Profissional resultado = profissionalService.atualizar(1L, profissionalAtualizado);

        // Then
        assertNotNull(resultado);
        verify(passwordEncoder).encode("nova_senha123");
        verify(profissionalRepository).save(any(Profissional.class));
    }

    @Test
    void testAtualizarSemSenha() {
        // Given
        Profissional profissionalAtualizado = new Profissional();
        profissionalAtualizado.setNome("Profissional Atualizado");
        profissionalAtualizado.setSenha(null); // Senha não fornecida

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

        // When
        Profissional resultado = profissionalService.atualizar(1L, profissionalAtualizado);

        // Then
        assertNotNull(resultado);
        verify(passwordEncoder, never()).encode(anyString());
        verify(profissionalRepository).save(any(Profissional.class));
    }

    @Test
    void testListarPorEmpresa() {
        // Given
        List<Profissional> profissionais = Arrays.asList(profissional);
        when(profissionalRepository.findByEmpresaId(1L)).thenReturn(profissionais);

        // When
        List<Profissional> resultado = profissionalService.listarPorEmpresa(1L);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(profissional, resultado.get(0));
        verify(profissionalRepository).findByEmpresaId(1L);
    }

    @Test
    void testAtualizarParcialComSucesso() {
        // Given
        ProfissionalPatchDTO patchDTO = new ProfissionalPatchDTO();
        patchDTO.setNome("Nome Atualizado");
        patchDTO.setEmail("email.atualizado@teste.com");
        patchDTO.setPerfil(Perfil.ADMIN);
        patchDTO.setAtivo(false);

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

        // When
        Profissional resultado = profissionalService.atualizarParcial(1L, patchDTO);

        // Then
        assertNotNull(resultado);
        verify(profissionalRepository).save(any(Profissional.class));
    }

    @Test
    void testAtualizarParcialComSenha() {
        // Given
        ProfissionalPatchDTO patchDTO = new ProfissionalPatchDTO();
        patchDTO.setNome("Nome Atualizado");
        patchDTO.setSenha("nova_senha123");

        String senhaCriptografada = "nova_senha_criptografada";
        when(passwordEncoder.encode("nova_senha123")).thenReturn(senhaCriptografada);
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

        // When
        Profissional resultado = profissionalService.atualizarParcial(1L, patchDTO);

        // Then
        assertNotNull(resultado);
        verify(passwordEncoder).encode("nova_senha123");
        verify(profissionalRepository).save(any(Profissional.class));
    }

    @Test
    void testAtualizarParcialComTokensGoogle() {
        // Given
        ProfissionalPatchDTO patchDTO = new ProfissionalPatchDTO();
        patchDTO.setGoogleAccessToken("novo_token");
        patchDTO.setGoogleRefreshToken("novo_refresh_token");

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

        // When
        Profissional resultado = profissionalService.atualizarParcial(1L, patchDTO);

        // Then
        assertNotNull(resultado);
        verify(profissionalRepository).save(any(Profissional.class));
    }

    @Test
    void testAtualizarParcialComSenhaVazia() {
        // Given
        ProfissionalPatchDTO patchDTO = new ProfissionalPatchDTO();
        patchDTO.setNome("Nome Atualizado");
        patchDTO.setSenha(""); // Senha vazia

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);

        // When
        Profissional resultado = profissionalService.atualizarParcial(1L, patchDTO);

        // Then
        assertNotNull(resultado);
        verify(passwordEncoder, never()).encode(anyString());
        verify(profissionalRepository).save(any(Profissional.class));
    }
}
