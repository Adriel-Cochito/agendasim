package com.agendasim.service;

import com.agendasim.dto.AgendaAdminDTO;
import com.agendasim.dto.AgendaClienteDTO;
import com.agendasim.exception.ConflitoAgendamentoException;
import com.agendasim.mapper.AgendaMapper;
import com.agendasim.model.Agenda;
import com.agendasim.model.Empresa;
import com.agendasim.repository.AgendaRepository;
import com.agendasim.repository.DisponibilidadeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;
    
    @Autowired
    private DisponibilidadeRepository disponibilidadeRepository;

    public List<Agenda> listarTodos() {
        return agendaRepository.findAll();
    }

    public Agenda criar(Agenda agenda, Long empresaId) {
        Empresa empresa = new Empresa();
        empresa.setId(empresaId);
        agenda.setEmpresa(empresa);

        // Validar conflitos de horário antes de salvar
        if (existeConflitoAgendamento(agenda)) {
            throw new ConflitoAgendamentoException("Não é possível criar o agendamento: conflito de horário com outro agendamento ou bloqueio existente.");
        }

        return agendaRepository.save(agenda);
    }

    public Agenda buscarPorId(Long id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new com.agendasim.exception.RecursoNaoEncontradoException("Agenda id=" + id + " não encontrada"));
    }

    public void excluir(Long id) {
        if (!agendaRepository.existsById(id)) {
            throw new com.agendasim.exception.RecursoNaoEncontradoException("Agenda não encontrada com ID: " + id);
        }
        agendaRepository.deleteById(id);
    }

    public Agenda atualizar(Long id, Agenda agenda, Long empresaId) {
        // Aqui você define a empresa no objeto agenda
        Empresa empresa = new Empresa();
        empresa.setId(empresaId);
        agenda.setEmpresa(empresa);
        agenda.setId(id); // Definir o ID para a validação

        // Validar conflitos de horário antes de atualizar
        if (existeConflitoAgendamento(agenda)) {
            throw new ConflitoAgendamentoException("Não é possível atualizar o agendamento: conflito de horário com outro agendamento ou bloqueio existente.");
        }

        Agenda existente = buscarPorId(id);
        existente.setNomeCliente(agenda.getNomeCliente());
        existente.setTelefoneCliente(agenda.getTelefoneCliente());
        existente.setDataHora(agenda.getDataHora());
        existente.setStatus(agenda.getStatus());
        existente.setServico(agenda.getServico());
        existente.setProfissional(agenda.getProfissional());
        existente.setEmpresa(agenda.getEmpresa());

        return agendaRepository.save(existente);
    }

    public List<Agenda> listarPorEmpresa(Long empresaId) {
        return agendaRepository.findByEmpresaId(empresaId);
    }

    public List<Agenda> listarPorServico(Long servicoId, Long empresaId) {
        return agendaRepository.findByServicoIdAndEmpresaId(servicoId, empresaId);
    }

    public List<AgendaAdminDTO> listarParaAdmin(Long empresaId) {
        return AgendaMapper.toAdminDAOList(agendaRepository.findByEmpresaId(empresaId));
    }

    public List<AgendaClienteDTO> listarParaCliente(Long empresaId, Long servicoId, Long profissionalId) {
        return AgendaMapper.toClienteDAOList(
            agendaRepository.findByEmpresaServicoEProfissional(empresaId, servicoId, profissionalId)
        );
    }

    public List<AgendaClienteDTO> listarParaClienteData(Long empresaId, Long servicoId, Long profissionalId, LocalDate data) {
        // Converte LocalDate para Instant considerando UTC
        java.time.Instant inicioDoDia = data.atStartOfDay().toInstant(ZoneOffset.UTC);
        java.time.Instant fimDoDia = data.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        
        return AgendaMapper.toClienteDAOList(
            agendaRepository.findByEmpresaServicoEProfissionalEData(empresaId, servicoId, profissionalId, inicioDoDia, fimDoDia)
        );
    }

    public List<AgendaAdminDTO> listarPorEmpresaProfissionalEData(Long empresaId, Long profissionalId, LocalDate data) {
        // Converte LocalDate para Instant considerando UTC
        java.time.Instant inicioDoDia = data.atStartOfDay().toInstant(ZoneOffset.UTC);
        java.time.Instant fimDoDia = data.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        
        return AgendaMapper.toAdminDAOList(agendaRepository.findByEmpresaProfissionalEData(empresaId, profissionalId, inicioDoDia, fimDoDia));
    }

    /**
     * Verifica se existe conflito de agendamento (outro agendamento ou bloqueio)
     */
    private boolean existeConflitoAgendamento(Agenda agenda) {
        // Verificar conflito com outros agendamentos no mesmo horário
        Long countAgenda = agendaRepository.countConflitoAgendamento(
            agenda.getEmpresa().getId(),
            agenda.getProfissional().getId(),
            agenda.getDataHora(),
            agenda.getId() != null ? agenda.getId() : 0L
        );
        
        if (countAgenda > 0) {
            return true;
        }
        
        // Verificar conflito com bloqueios (BLOQUEIO e BLOQUEIO_GRADE)
        LocalDate data = agenda.getDataHora().atZone(ZoneOffset.UTC).toLocalDate();
        int diaSemana = data.getDayOfWeek().getValue(); // 1=Seg, ..., 7=Dom
        if (diaSemana == 7) diaSemana = 0; // H2 usa 0=Dom
        
        Long countBloqueio = disponibilidadeRepository.countConflitoBloqueio(
            agenda.getEmpresa().getId(),
            agenda.getProfissional().getId(),
            diaSemana,
            data
        );
        
        return countBloqueio > 0;
    }
}