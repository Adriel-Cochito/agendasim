package com.agendasim.service;

import com.agendasim.dto.dashboard.*;
import com.agendasim.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private AgendaRepository agendaRepository;
    
    @Autowired
    private ProfissionalRepository profissionalRepository;
    
    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private DisponibilidadeRepository disponibilidadeRepository;

    public DashboardResumoDTO obterResumo(Long empresaId) {
        return new DashboardResumoDTO(
            obterMetricasPrincipais(empresaId),
            obterIndicadoresGestao(empresaId),
            obterGraficos(empresaId),
            obterAlertas(empresaId)
        );
    }

    public MetricasPrincipaisDTO obterMetricasPrincipais(Long empresaId) {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicioHoje = hoje.atStartOfDay();
        LocalDateTime fimHoje = hoje.atTime(LocalTime.MAX);
        
        // Converter para Instant (UTC)
        Instant inicioInstant = inicioHoje.toInstant(ZoneOffset.UTC);
        Instant fimInstant = fimHoje.toInstant(ZoneOffset.UTC);

        // Agendamentos hoje - total
        Long totalHoje = agendaRepository.countByEmpresaIdAndDataHoraBetween(
            empresaId, inicioInstant, fimInstant
        );

        // Agendamentos por status hoje
        Map<String, Long> porStatus = agendaRepository.countByEmpresaIdAndDataHoraBetweenGroupByStatus(
            empresaId, inicioInstant, fimInstant
        );

        // Próximos 7 dias (amanhã até 7 dias)
        LocalDateTime inicio7Dias = LocalDate.now().plusDays(1).atStartOfDay();
        LocalDateTime fim7Dias = LocalDate.now().plusDays(7).atTime(LocalTime.MAX);
        
        Instant inicio7DiasInstant = inicio7Dias.toInstant(ZoneOffset.UTC);
        Instant fim7DiasInstant = fim7Dias.toInstant(ZoneOffset.UTC);
        
        Long proximosSeteDias = agendaRepository.countByEmpresaIdAndDataHoraBetween(
            empresaId, inicio7DiasInstant, fim7DiasInstant
        );

        // Taxa de ocupação hoje (simplificada - baseada em disponibilidades ativas)
        Long disponibilidadesAtivas = disponibilidadeRepository.countDisponibilidadesAtivas(empresaId);
        // Se não há disponibilidades, assumir 10 slots por dia como base
        Long horariosEstimados = disponibilidadesAtivas > 0 ? disponibilidadesAtivas * 8 : 10L; // 8 slots por disponibilidade
        
        Double taxaOcupacao = horariosEstimados > 0 ? 
            Math.min((totalHoje.doubleValue() / horariosEstimados) * 100, 100.0) : 0.0;

        AgendamentosHojeDTO agendamentosHoje = new AgendamentosHojeDTO(totalHoje, porStatus);
        
        return new MetricasPrincipaisDTO(agendamentosHoje, proximosSeteDias, taxaOcupacao);
    }

    public IndicadoresGestaoDTO obterIndicadoresGestao(Long empresaId) {
        Long profissionaisAtivos = profissionalRepository.countByEmpresaIdAndAtivoTrue(empresaId);
        Long servicosOferecidos = servicoRepository.countByEmpresaIdAndAtivoTrue(empresaId);
        
        return new IndicadoresGestaoDTO(profissionaisAtivos, servicosOferecidos);
    }

    public GraficosDTO obterGraficos(Long empresaId) {
        // Agendamentos por dia (últimos 15 dias)
        LocalDate dataInicio = LocalDate.now().minusDays(14);
        LocalDate dataFim = LocalDate.now();
        
        List<AgendamentoPorDiaDTO> agendamentosPorDia = agendaRepository
            .findAgendamentosPorDia(empresaId, dataInicio, dataFim)
            .stream()
            .map(obj -> new AgendamentoPorDiaDTO(
                ((java.sql.Date) obj[0]).toLocalDate(), 
                ((Number) obj[1]).longValue()
            ))
            .collect(Collectors.toList());

        // Top 5 serviços mais procurados
        List<ServicoRankingDTO> servicosMaisProcurados = agendaRepository
            .findTop5ServicosMaisProcurados(empresaId)
            .stream()
            .map(obj -> new ServicoRankingDTO(
                ((Number) obj[0]).longValue(),
                (String) obj[1],
                ((Number) obj[2]).longValue()
            ))
            .collect(Collectors.toList());

        // Top 5 profissionais mais ocupados
        List<ProfissionalRankingDTO> profissionaisMaisOcupados = agendaRepository
            .findTop5ProfissionaisMaisOcupados(empresaId)
            .stream()
            .map(obj -> new ProfissionalRankingDTO(
                ((Number) obj[0]).longValue(),
                (String) obj[1],
                ((Number) obj[2]).longValue()
            ))
            .collect(Collectors.toList());

        // Status dos agendamentos (geral - todos os tempos)
        Map<String, Long> statusAgendamentos = agendaRepository.countByEmpresaIdGroupByStatus(empresaId);

        return new GraficosDTO(agendamentosPorDia, servicosMaisProcurados, 
                              profissionaisMaisOcupados, statusAgendamentos);
    }

    public AlertasDTO obterAlertas(Long empresaId) {
        // Agendamentos para confirmar (status AGENDADO)
        Long agendamentosParaConfirmar = agendaRepository.countByEmpresaIdAndStatus(empresaId, "AGENDADO");

        // Conflitos de horário (mesmo profissional, mesmo horário)
        Long conflitosHorario = agendaRepository.countConflitosHorario(empresaId);

        // Profissionais sem disponibilidade configurada
        List<ProfissionalSemAgendaDTO> profissionaisSemAgenda = profissionalRepository
            .findProfissionaisSemDisponibilidade(empresaId)
            .stream()
            .map(obj -> new ProfissionalSemAgendaDTO(
                ((Number) obj[0]).longValue(),
                (String) obj[1]
            ))
            .collect(Collectors.toList());

        return new AlertasDTO(agendamentosParaConfirmar, conflitosHorario, profissionaisSemAgenda);
    }
}