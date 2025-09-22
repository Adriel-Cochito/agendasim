package com.agendasim.service;

import com.agendasim.enums.TipoDisponibilidade;
import com.agendasim.model.Disponibilidade;
import com.agendasim.repository.DisponibilidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DisponibilidadeService {

    @Autowired
    private DisponibilidadeRepository disponibilidadeRepository;

    @Transactional
    public Disponibilidade salvar(Disponibilidade disponibilidade) {
        switch (disponibilidade.getTipo()) {
            case GRADE:
            case BLOQUEIO_GRADE:
                disponibilidade.setDataHoraInicio(null);
                disponibilidade.setDataHoraFim(null);
                break;

            case BLOQUEIO:
            case LIBERADO:
                disponibilidade.setDiasSemana(null);
                disponibilidade.setHoraInicio(null);
                disponibilidade.setHoraFim(null);
                break;
        }

        if (disponibilidade.getTipo() == TipoDisponibilidade.BLOQUEIO || disponibilidade.getTipo() == TipoDisponibilidade.BLOQUEIO_GRADE) {
            return disponibilidadeRepository.save(disponibilidade);
        }

        if (existeConflito(disponibilidade)) {
            String mensagem = switch (disponibilidade.getTipo()) {
                case GRADE -> "Conflito de horário com disponibilidade existente para os dias selecionados.";
                case LIBERADO -> "Conflito de horário com disponibilidade existente.";
                default -> "Conflito de horário com um bloqueio existente.";
            };
            throw new RuntimeException(mensagem);
        }

        return disponibilidadeRepository.save(disponibilidade);
    }

    @Transactional
    public Disponibilidade atualizar(Long id, Disponibilidade disponibilidade) {
        Disponibilidade atual = buscarPorId(id);
        disponibilidade.setId(atual.getId()); // Garante atualização
        return disponibilidadeRepository.save(disponibilidade);
    }

    public Disponibilidade buscarPorId(Long id) {
        return disponibilidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidade não encontrada"));
    }

    @Transactional
    public void deletar(Long id) {
        if (!disponibilidadeRepository.existsById(id)) {
            throw new RuntimeException("Disponibilidade com ID " + id + " não encontrado");
        }
        disponibilidadeRepository.deleteById(id);
    }

    public List<Disponibilidade> listarPorEmpresa(Long empresaId) {
        return disponibilidadeRepository.findByEmpresaId(empresaId);
    }

    public List<Disponibilidade> listarPorEmpresaEProfissional(Long empresaId, Long profissionalId) {
        return disponibilidadeRepository.findByEmpresaIdAndProfissionalId(empresaId, profissionalId);
    }

    public List<Disponibilidade> listarPorEmpresaProfissionalEData(Long empresaId, Long profissionalId, LocalDate data) {
        return disponibilidadeRepository.findByEmpresaProfissionalEData(empresaId, profissionalId);
    }

    /**
     * Verifica se existe conflito de disponibilidade
     */
    private boolean existeConflito(Disponibilidade disponibilidade) {
        if (disponibilidade.getTipo().name().equals("GRADE") || disponibilidade.getTipo().name().equals("BLOQUEIO_GRADE")) {
            // Para tipo GRADE e BLOQUEIO_GRADE, verificar conflito de dias da semana
            Long count = disponibilidadeRepository.countConflitoDisponibilidadeGrade(
                disponibilidade.getProfissional().getId(),
                disponibilidade.getEmpresa().getId(),
                disponibilidade.getId() != null ? disponibilidade.getId() : 0L
            );
            return count > 0;
        } else {
            // Para BLOQUEIO/LIBERADO, verificar sobreposição de intervalos
            Long count = disponibilidadeRepository.countConflitoDisponibilidadeIntervalo(
                disponibilidade.getProfissional().getId(),
                disponibilidade.getEmpresa().getId(),
                disponibilidade.getId() != null ? disponibilidade.getId() : 0L,
                disponibilidade.getDataHoraInicio(),
                disponibilidade.getDataHoraFim()
            );
            return count > 0;
        }
    }
}
