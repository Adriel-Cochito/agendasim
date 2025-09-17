package com.agendasim.integration;

import com.agendasim.config.TestContainersConfig;
import com.agendasim.exception.ConflitoAgendamentoException;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = TestContainersConfig.class)
@Transactional
class AgendaServiceIntegrationTest {

    @Autowired
    private AgendaService agendaService;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    private Empresa empresa;
    private Profissional profissional;
    private Servico servico;

    @BeforeEach
    void setUp() {
        // Limpar dados de teste
        agendaRepository.deleteAll();
        servicoRepository.deleteAll();
        profissionalRepository.deleteAll();
        empresaRepository.deleteAll();

        // Setup empresa
        empresa = new Empresa();
        empresa.setNome("Empresa Teste");
        empresa.setCnpj("12345678000199");
        empresa = empresaRepository.save(empresa);

        // Setup profissional
        profissional = new Profissional();
        profissional.setNome("Profissional Teste");
        profissional.setEmail("prof@teste.com");
        profissional.setSenha("senha123");
        profissional.setEmpresaId(empresa.getId());
        profissional = profissionalRepository.save(profissional);

        // Setup serviço
        servico = new Servico();
        servico.setTitulo("Serviço Teste");
        servico.setDuracao(60);
        servico.setEmpresaId(empresa.getId());
        servico = servicoRepository.save(servico);
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
        Agenda agendaSalva = agendaService.criar(agenda, empresa.getId());

        // Then
        assertNotNull(agendaSalva.getId());
        assertEquals("Cliente Teste", agendaSalva.getNomeCliente());
        assertEquals(empresa.getId(), agendaSalva.getEmpresa().getId());
        assertEquals(profissional.getId(), agendaSalva.getProfissional().getId());
        assertEquals(servico.getId(), agendaSalva.getServico().getId());
    }

    @Test
    void testCriarAgendaComConflito() {
        // Given
        Agenda agenda1 = new Agenda();
        agenda1.setNomeCliente("Cliente 1");
        agenda1.setTelefoneCliente("11999999999");
        agenda1.setDataHora(Instant.now().plusSeconds(3600));
        agenda1.setStatus("AGENDADO");
        agenda1.setEmpresa(empresa);
        agenda1.setProfissional(profissional);
        agenda1.setServico(servico);
        agendaService.criar(agenda1, empresa.getId());

        // Tentar criar agenda no mesmo horário
        Agenda agenda2 = new Agenda();
        agenda2.setNomeCliente("Cliente 2");
        agenda2.setTelefoneCliente("11888888888");
        agenda2.setDataHora(Instant.now().plusSeconds(3600)); // Mesmo horário
        agenda2.setStatus("AGENDADO");
        agenda2.setEmpresa(empresa);
        agenda2.setProfissional(profissional);
        agenda2.setServico(servico);

        // When & Then
        assertThrows(ConflitoAgendamentoException.class, () -> {
            agendaService.criar(agenda2, empresa.getId());
        });
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
        agendaService.criar(agenda1, empresa.getId());

        Agenda agenda2 = new Agenda();
        agenda2.setNomeCliente("Cliente 2");
        agenda2.setTelefoneCliente("11888888888");
        agenda2.setDataHora(Instant.now().plusSeconds(7200));
        agenda2.setStatus("AGENDADO");
        agenda2.setEmpresa(empresa);
        agenda2.setProfissional(profissional);
        agenda2.setServico(servico);
        agendaService.criar(agenda2, empresa.getId());

        // When
        List<Agenda> agendas = agendaService.listarPorEmpresa(empresa.getId());

        // Then
        assertEquals(2, agendas.size());
        assertTrue(agendas.stream().anyMatch(a -> a.getNomeCliente().equals("Cliente 1")));
        assertTrue(agendas.stream().anyMatch(a -> a.getNomeCliente().equals("Cliente 2")));
    }

    @Test
    void testListarAgendasPorServico() {
        // Given
        Agenda agenda = new Agenda();
        agenda.setNomeCliente("Cliente Teste");
        agenda.setTelefoneCliente("11999999999");
        agenda.setDataHora(Instant.now().plusSeconds(3600));
        agenda.setStatus("AGENDADO");
        agenda.setEmpresa(empresa);
        agenda.setProfissional(profissional);
        agenda.setServico(servico);
        agendaService.criar(agenda, empresa.getId());

        // When
        List<Agenda> agendas = agendaService.listarPorServico(servico.getId(), empresa.getId());

        // Then
        assertEquals(1, agendas.size());
        assertEquals("Cliente Teste", agendas.get(0).getNomeCliente());
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
        agenda = agendaService.criar(agenda, empresa.getId());

        // When
        agenda.setNomeCliente("Cliente Atualizado");
        agenda.setStatus("CONFIRMADO");
        Agenda agendaAtualizada = agendaService.atualizar(agenda.getId(), agenda, empresa.getId());

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
        agenda = agendaService.criar(agenda, empresa.getId());

        Long agendaId = agenda.getId();

        // When
        agendaService.excluir(agendaId);

        // Then
        assertThrows(Exception.class, () -> {
            agendaService.buscarPorId(agendaId);
        });
    }

    @Test
    void testListarParaAdmin() {
        // Given
        Agenda agenda = new Agenda();
        agenda.setNomeCliente("Cliente Teste");
        agenda.setTelefoneCliente("11999999999");
        agenda.setDataHora(Instant.now().plusSeconds(3600));
        agenda.setStatus("AGENDADO");
        agenda.setEmpresa(empresa);
        agenda.setProfissional(profissional);
        agenda.setServico(servico);
        agendaService.criar(agenda, empresa.getId());

        // When
        var adminDTOs = agendaService.listarParaAdmin(empresa.getId());

        // Then
        assertNotNull(adminDTOs);
        assertEquals(1, adminDTOs.size());
    }

    @Test
    void testListarParaCliente() {
        // Given
        Agenda agenda = new Agenda();
        agenda.setNomeCliente("Cliente Teste");
        agenda.setTelefoneCliente("11999999999");
        agenda.setDataHora(Instant.now().plusSeconds(3600));
        agenda.setStatus("AGENDADO");
        agenda.setEmpresa(empresa);
        agenda.setProfissional(profissional);
        agenda.setServico(servico);
        agendaService.criar(agenda, empresa.getId());

        // When
        var clienteDTOs = agendaService.listarParaCliente(empresa.getId(), servico.getId(), profissional.getId());

        // Then
        assertNotNull(clienteDTOs);
        assertEquals(1, clienteDTOs.size());
    }

    @Test
    void testListarParaClienteData() {
        // Given
        LocalDate data = LocalDate.now();
        Agenda agenda = new Agenda();
        agenda.setNomeCliente("Cliente Teste");
        agenda.setTelefoneCliente("11999999999");
        agenda.setDataHora(data.atStartOfDay().toInstant(java.time.ZoneOffset.UTC).plusSeconds(3600));
        agenda.setStatus("AGENDADO");
        agenda.setEmpresa(empresa);
        agenda.setProfissional(profissional);
        agenda.setServico(servico);
        agendaService.criar(agenda, empresa.getId());

        // When
        var clienteDTOs = agendaService.listarParaClienteData(empresa.getId(), servico.getId(), profissional.getId(), data);

        // Then
        assertNotNull(clienteDTOs);
        assertEquals(1, clienteDTOs.size());
    }
}
