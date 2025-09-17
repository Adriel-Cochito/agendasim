package com.agendasim.integration;

import com.agendasim.model.Agenda;
import com.agendasim.model.Empresa;
import com.agendasim.model.Profissional;
import com.agendasim.model.Servico;
import com.agendasim.repository.AgendaRepository;
import com.agendasim.repository.EmpresaRepository;
import com.agendasim.repository.ProfissionalRepository;
import com.agendasim.repository.ServicoRepository;
import com.agendasim.service.AgendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AgendaIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AgendaRepository agendaRepository;


    private Empresa empresa;
    private Profissional profissional;
    private Servico servico;

    @BeforeEach
    void setUp() {
        
        // Setup empresa
        empresa = new Empresa();
        empresa.setNome("Empresa Teste");
        empresa.setCnpj("12345678000199");
        empresa = entityManager.persistAndFlush(empresa);

        // Setup profissional
        profissional = new Profissional();
        profissional.setNome("Profissional Teste");
        profissional.setEmail("prof@teste.com");
        profissional.setSenha("senha123");
        profissional.setEmpresaId(empresa.getId());
        profissional = entityManager.persistAndFlush(profissional);

        // Setup serviço
        servico = new Servico();
        servico.setTitulo("Serviço Teste");
        servico.setDuracao(60);
        servico.setEmpresaId(empresa.getId());
        servico = entityManager.persistAndFlush(servico);
    }

    @Test
    void testCriarAgendaComSucesso() {
        // Given
        Agenda agenda = new Agenda();
        agenda.setNomeCliente("Cliente Teste");
        agenda.setTelefoneCliente("11999999999");
        agenda.setDataHora(Instant.now().plusSeconds(3600));
        agenda.setStatus("AGENDADO");
        agenda.setEmpresa(empresa);
        agenda.setProfissional(profissional);
        agenda.setServico(servico);

        // When
        Agenda agendaSalva = agendaRepository.save(agenda);

        // Then
        assertNotNull(agendaSalva.getId());
        assertEquals("Cliente Teste", agendaSalva.getNomeCliente());
        assertEquals(empresa.getId(), agendaSalva.getEmpresa().getId());
        assertEquals(profissional.getId(), agendaSalva.getProfissional().getId());
        assertEquals(servico.getId(), agendaSalva.getServico().getId());
    }

    @Test
    void testBuscarAgendaPorId() {
        // Given
        Agenda agenda = new Agenda();
        agenda.setNomeCliente("Cliente Teste");
        agenda.setTelefoneCliente("11999999999");
        agenda.setDataHora(Instant.now().plusSeconds(3600));
        agenda.setStatus("AGENDADO");
        agenda.setEmpresa(empresa);
        agenda.setProfissional(profissional);
        agenda.setServico(servico);
        agenda = entityManager.persistAndFlush(agenda);

        // When
        var agendaEncontrada = agendaRepository.findById(agenda.getId());

        // Then
        assertTrue(agendaEncontrada.isPresent());
        assertEquals(agenda.getId(), agendaEncontrada.get().getId());
        assertEquals("Cliente Teste", agendaEncontrada.get().getNomeCliente());
    }

    @Test
    void testListarAgendasPorEmpresa() {
        // Given
        Agenda agenda1 = new Agenda();
        agenda1.setNomeCliente("Cliente 1");
        agenda1.setTelefoneCliente("11999999999");
        agenda1.setDataHora(Instant.now().plusSeconds(3600));
        agenda1.setStatus("AGENDADO");
        agenda1.setEmpresa(empresa);
        agenda1.setProfissional(profissional);
        agenda1.setServico(servico);
        entityManager.persistAndFlush(agenda1);

        Agenda agenda2 = new Agenda();
        agenda2.setNomeCliente("Cliente 2");
        agenda2.setTelefoneCliente("11888888888");
        agenda2.setDataHora(Instant.now().plusSeconds(7200));
        agenda2.setStatus("AGENDADO");
        agenda2.setEmpresa(empresa);
        agenda2.setProfissional(profissional);
        agenda2.setServico(servico);
        entityManager.persistAndFlush(agenda2);

        // When
        List<Agenda> agendas = agendaRepository.findByEmpresaId(empresa.getId());

        // Then
        assertEquals(2, agendas.size());
        assertTrue(agendas.stream().anyMatch(a -> a.getNomeCliente().equals("Cliente 1")));
        assertTrue(agendas.stream().anyMatch(a -> a.getNomeCliente().equals("Cliente 2")));
    }

    @Test
    void testListarAgendasPorServico() {
        // Given
        Agenda agenda1 = new Agenda();
        agenda1.setNomeCliente("Cliente 1");
        agenda1.setTelefoneCliente("11999999999");
        agenda1.setDataHora(Instant.now().plusSeconds(3600));
        agenda1.setStatus("AGENDADO");
        agenda1.setEmpresa(empresa);
        agenda1.setProfissional(profissional);
        agenda1.setServico(servico);
        entityManager.persistAndFlush(agenda1);

        // When
        List<Agenda> agendas = agendaRepository.findByServicoIdAndEmpresaId(servico.getId(), empresa.getId());

        // Then
        assertEquals(1, agendas.size());
        assertEquals("Cliente 1", agendas.get(0).getNomeCliente());
        assertEquals(servico.getId(), agendas.get(0).getServico().getId());
    }

    @Test
    void testAtualizarAgenda() {
        // Given
        Agenda agenda = new Agenda();
        agenda.setNomeCliente("Cliente Original");
        agenda.setTelefoneCliente("11999999999");
        agenda.setDataHora(Instant.now().plusSeconds(3600));
        agenda.setStatus("AGENDADO");
        agenda.setEmpresa(empresa);
        agenda.setProfissional(profissional);
        agenda.setServico(servico);
        agenda = entityManager.persistAndFlush(agenda);

        // When
        agenda.setNomeCliente("Cliente Atualizado");
        agenda.setStatus("CONFIRMADO");
        Agenda agendaAtualizada = agendaRepository.save(agenda);

        // Then
        assertEquals("Cliente Atualizado", agendaAtualizada.getNomeCliente());
        assertEquals("CONFIRMADO", agendaAtualizada.getStatus());
    }

    @Test
    void testExcluirAgenda() {
        // Given
        Agenda agenda = new Agenda();
        agenda.setNomeCliente("Cliente Teste");
        agenda.setTelefoneCliente("11999999999");
        agenda.setDataHora(Instant.now().plusSeconds(3600));
        agenda.setStatus("AGENDADO");
        agenda.setEmpresa(empresa);
        agenda.setProfissional(profissional);
        agenda.setServico(servico);
        agenda = entityManager.persistAndFlush(agenda);

        Long agendaId = agenda.getId();

        // When
        agendaRepository.deleteById(agendaId);

        // Then
        var agendaExcluida = agendaRepository.findById(agendaId);
        assertTrue(agendaExcluida.isEmpty());
    }
}
