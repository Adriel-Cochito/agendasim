package com.agendasim.repository;

import com.agendasim.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByServicoId(Long servicoId);

    List<Agenda> findByEmpresaId(Long empresaId);

    List<Agenda> findByServicoIdAndEmpresaId(Long servicoId, Long empresaId);

    Optional<Agenda> findByIdAndEmpresaId(Long id, Long empresaId);

    List<Agenda> findByEmpresaIdAndServicoIdAndProfissionalId(Long empresaId, Long servicoId, Long profissionalId);

    // Contagem de agendamentos por período
    @Query("SELECT COUNT(a) FROM Agenda a WHERE a.empresa.id = :empresaId AND a.dataHora BETWEEN :inicio AND :fim")
    Long countByEmpresaIdAndDataHoraBetween(@Param("empresaId") Long empresaId,
            @Param("inicio") Instant inicio,
            @Param("fim") Instant fim);

    // Agendamentos por status em um período - versão raw
    @Query("SELECT a.status, COUNT(a) FROM Agenda a WHERE a.empresa.id = :empresaId AND a.dataHora BETWEEN :inicio AND :fim GROUP BY a.status")
    List<Object[]> countByEmpresaIdAndDataHoraBetweenGroupByStatusRaw(@Param("empresaId") Long empresaId,
            @Param("inicio") Instant inicio,
            @Param("fim") Instant fim);

    // Método helper para converter em Map
    default Map<String, Long> countByEmpresaIdAndDataHoraBetweenGroupByStatus(Long empresaId, Instant inicio,
            Instant fim) {
        return countByEmpresaIdAndDataHoraBetweenGroupByStatusRaw(empresaId, inicio, fim)
                .stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> ((Number) obj[1]).longValue()));
    }


    // Top 5 serviços mais procurados
    @Query(value = "SELECT s.id, s.titulo, COUNT(a.id) FROM agendas a JOIN servicos s ON a.servico_id = s.id WHERE a.empresa_id = :empresaId GROUP BY s.id, s.titulo ORDER BY COUNT(a.id) DESC LIMIT 5", nativeQuery = true)
    List<Object[]> findTop5ServicosMaisProcurados(@Param("empresaId") Long empresaId);

    // Top 5 profissionais mais ocupados
    @Query(value = "SELECT p.id, p.nome, COUNT(a.id) FROM agendas a JOIN profissionais p ON a.profissional_id = p.id WHERE a.empresa_id = :empresaId GROUP BY p.id, p.nome ORDER BY COUNT(a.id) DESC LIMIT 5", nativeQuery = true)
    List<Object[]> findTop5ProfissionaisMaisOcupados(@Param("empresaId") Long empresaId);

    // Status dos agendamentos (geral) - versão raw
    @Query("SELECT a.status, COUNT(a) FROM Agenda a WHERE a.empresa.id = :empresaId GROUP BY a.status")
    List<Object[]> countByEmpresaIdGroupByStatusRaw(@Param("empresaId") Long empresaId);

    // Método helper para status geral
    default Map<String, Long> countByEmpresaIdGroupByStatus(Long empresaId) {
        return countByEmpresaIdGroupByStatusRaw(empresaId)
                .stream()
                .collect(Collectors.toMap(
                        obj -> (String) obj[0],
                        obj -> ((Number) obj[1]).longValue()));
    }

    // Contagem por status específico
    @Query("SELECT COUNT(a) FROM Agenda a WHERE a.empresa.id = :empresaId AND a.status = :status")
    Long countByEmpresaIdAndStatus(@Param("empresaId") Long empresaId, @Param("status") String status);

    // Conflitos de horário - versão simplificada
    @Query(value = "SELECT COUNT(*) FROM (SELECT profissional_id, data_hora FROM agendas WHERE empresa_id = :empresaId GROUP BY profissional_id, data_hora HAVING COUNT(*) > 1) AS conflitos", nativeQuery = true)
    Long countConflitosHorario(@Param("empresaId") Long empresaId);

    // CORREÇÃO: Query melhorada para agendamentos por dia
    @Query(value = "SELECT CAST(data_hora AS DATE) as dia, COUNT(*) as quantidade " +
                   "FROM agendas " +
                   "WHERE empresa_id = :empresaId " +
                   "AND CAST(data_hora AS DATE) >= :dataInicio " +
                   "AND CAST(data_hora AS DATE) <= :dataFim " +
                   "GROUP BY CAST(data_hora AS DATE) " +
                   "ORDER BY CAST(data_hora AS DATE)", nativeQuery = true)
    List<Object[]> findAgendamentosPorDia(@Param("empresaId") Long empresaId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim);

    // NOVO: Query para debug - contar total de agendamentos da empresa
    @Query("SELECT COUNT(a) FROM Agenda a WHERE a.empresa.id = :empresaId")
    Long countTotalAgendamentosByEmpresa(@Param("empresaId") Long empresaId);

    // NOVO: Query alternativa mais simples para agendamentos por dia
    @Query(value = "SELECT DATE(data_hora) as dia, COUNT(*) as qtd FROM agendas WHERE empresa_id = :empresaId GROUP BY DATE(data_hora) ORDER BY DATE(data_hora) DESC LIMIT 15", nativeQuery = true)
    List<Object[]> findUltimosAgendamentosPorDia(@Param("empresaId") Long empresaId);

}