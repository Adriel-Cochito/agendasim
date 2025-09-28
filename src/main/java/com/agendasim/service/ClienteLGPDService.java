package com.agendasim.service;

import com.agendasim.dto.lgpd.BuscarClienteLGPDRequest;
import com.agendasim.dto.lgpd.ClienteLGPDResumoDTO;
import com.agendasim.model.Agenda;
import com.agendasim.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteLGPDService {

    private final AgendaRepository agendaRepository;

    @Transactional(readOnly = true)
    public ClienteLGPDResumoDTO buscarResumoDadosCliente(BuscarClienteLGPDRequest request) {
        try {
            // Validação básica
            if (request.getNome() == null || request.getNome().trim().isEmpty()) {
                log.warn("Nome do cliente não fornecido");
                return criarResumoVazio();
            }
            
            if (request.getTelefone() == null || request.getTelefone().trim().isEmpty()) {
                log.warn("Telefone do cliente não fornecido");
                return criarResumoVazio();
            }

            log.info("Buscando resumo de dados LGPD para cliente: {} - {}", request.getNome(), request.getTelefone());

            // Buscar agendamentos do cliente
            List<Agenda> agendamentos = agendaRepository.findByNomeClienteAndTelefoneCliente(
                request.getNome().trim(), 
                request.getTelefone().trim()
            );

            if (agendamentos.isEmpty()) {
                log.info("Nenhum agendamento encontrado para o cliente");
                return criarResumoVazio();
            }

            log.info("Encontrados {} agendamentos para o cliente", agendamentos.size());

            // Analisar tipos de dados
            ClienteLGPDResumoDTO.TiposDadosDTO tiposDados = analisarTiposDados(agendamentos);
            log.debug("Tipos de dados analisados: {}", tiposDados);

            // Agrupar por empresa
            List<ClienteLGPDResumoDTO.EmpresaResumoDTO> empresas = agruparPorEmpresa(agendamentos);
            log.debug("Empresas encontradas: {}", empresas.size());

            // Calcular período
            ClienteLGPDResumoDTO.PeriodoResumoDTO periodo = calcularPeriodo(agendamentos);
            log.debug("Período calculado: {} - {}", periodo.getPrimeiroAgendamento(), periodo.getUltimoAgendamento());

            return new ClienteLGPDResumoDTO(
                true,
                agendamentos.size(),
                tiposDados,
                empresas,
                periodo
            );
        } catch (Exception e) {
            log.error("Erro ao buscar resumo de dados LGPD para cliente: {} - {}", 
                request.getNome(), request.getTelefone(), e);
            return criarResumoVazio();
        }
    }

    @Transactional
    public void anonimizarDadosCliente(BuscarClienteLGPDRequest request) {
        // Validação básica
        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            log.warn("Nome do cliente não fornecido para anonimização");
            return;
        }
        
        if (request.getTelefone() == null || request.getTelefone().trim().isEmpty()) {
            log.warn("Telefone do cliente não fornecido para anonimização");
            return;
        }

        log.info("Anonimizando dados do cliente: {} - {}", request.getNome(), request.getTelefone());

        List<Agenda> agendamentos = agendaRepository.findByNomeClienteAndTelefoneCliente(
            request.getNome().trim(), 
            request.getTelefone().trim()
        );

        if (agendamentos.isEmpty()) {
            log.warn("Nenhum agendamento encontrado para anonimizar");
            return;
        }

        // Anonimizar dados sensíveis
        for (Agenda agenda : agendamentos) {
            agenda.setNomeCliente("***ANONIMIZADO***");
            agenda.setTelefoneCliente("***ANONIMIZADO***");
            agenda.setEmailCliente(null);
            agenda.setEnderecoCliente(null);
            agenda.setCpfCliente(null);
            agenda.setObservacoes("***DADOS ANONIMIZADOS***");
        }

        agendaRepository.saveAll(agendamentos);
        log.info("Dados anonimizados com sucesso para {} agendamentos", agendamentos.size());
    }

    private ClienteLGPDResumoDTO criarResumoVazio() {
        return new ClienteLGPDResumoDTO(
            false,
            0,
            new ClienteLGPDResumoDTO.TiposDadosDTO(false, false, false, false, false, false),
            new ArrayList<>(),
            new ClienteLGPDResumoDTO.PeriodoResumoDTO("", "")
        );
    }

    private ClienteLGPDResumoDTO.TiposDadosDTO analisarTiposDados(List<Agenda> agendamentos) {
        boolean temNome = agendamentos.stream().anyMatch(a -> a.getNomeCliente() != null && !a.getNomeCliente().trim().isEmpty());
        boolean temTelefone = agendamentos.stream().anyMatch(a -> a.getTelefoneCliente() != null && !a.getTelefoneCliente().trim().isEmpty());
        boolean temEmail = agendamentos.stream().anyMatch(a -> a.getEmailCliente() != null && !a.getEmailCliente().trim().isEmpty());
        boolean temEndereco = agendamentos.stream().anyMatch(a -> a.getEnderecoCliente() != null && !a.getEnderecoCliente().trim().isEmpty());
        boolean temCpf = agendamentos.stream().anyMatch(a -> a.getCpfCliente() != null && !a.getCpfCliente().trim().isEmpty());
        boolean temObservacoes = agendamentos.stream().anyMatch(a -> a.getObservacoes() != null && !a.getObservacoes().trim().isEmpty());

        return new ClienteLGPDResumoDTO.TiposDadosDTO(
            temNome, temTelefone, temEmail, temEndereco, temCpf, temObservacoes
        );
    }

    private List<ClienteLGPDResumoDTO.EmpresaResumoDTO> agruparPorEmpresa(List<Agenda> agendamentos) {
        Map<Long, List<Agenda>> agendamentosPorEmpresa = agendamentos.stream()
            .filter(a -> a.getEmpresaId() != null)
            .collect(Collectors.groupingBy(Agenda::getEmpresaId));

        return agendamentosPorEmpresa.entrySet().stream()
            .map(entry -> {
                Long empresaId = entry.getKey();
                List<Agenda> agendamentosEmpresa = entry.getValue();
                
                String nomeEmpresa = "Empresa " + empresaId;
                if (!agendamentosEmpresa.isEmpty() && agendamentosEmpresa.get(0).getEmpresa() != null) {
                    nomeEmpresa = agendamentosEmpresa.get(0).getEmpresa().getNome();
                }

                return new ClienteLGPDResumoDTO.EmpresaResumoDTO(
                    empresaId,
                    nomeEmpresa,
                    agendamentosEmpresa.size()
                );
            })
            .collect(Collectors.toList());
    }

    private ClienteLGPDResumoDTO.PeriodoResumoDTO calcularPeriodo(List<Agenda> agendamentos) {
        if (agendamentos.isEmpty()) {
            return new ClienteLGPDResumoDTO.PeriodoResumoDTO("", "");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        String primeiroAgendamento = agendamentos.stream()
            .map(Agenda::getDataAgendamento)
            .filter(data -> data != null)
            .min(java.time.LocalDate::compareTo)
            .map(formatter::format)
            .orElse("");

        String ultimoAgendamento = agendamentos.stream()
            .map(Agenda::getDataAgendamento)
            .filter(data -> data != null)
            .max(java.time.LocalDate::compareTo)
            .map(formatter::format)
            .orElse("");

        return new ClienteLGPDResumoDTO.PeriodoResumoDTO(primeiroAgendamento, ultimoAgendamento);
    }
}
