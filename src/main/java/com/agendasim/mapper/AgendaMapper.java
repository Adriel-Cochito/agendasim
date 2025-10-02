package com.agendasim.mapper;

import com.agendasim.dto.*;
import com.agendasim.model.Agenda;

import java.util.List;
import java.util.stream.Collectors;

public class AgendaMapper {

    public static AgendaAdminDTO toAdminDAO(Agenda agenda) {
        return new AgendaAdminDTO(
            agenda.getId(),
            agenda.getNomeCliente(),
            agenda.getTelefoneCliente(),
            agenda.getDataHora(),
            agenda.getStatus(),
            agenda.getObservacoes(),
            agenda.getProfissional().getId(),
            agenda.getProfissional().getNome(),
            agenda.getServico().getId(),
            agenda.getServico().getTitulo()
                   );
    }

    public static AgendaClienteDTO toClienteDAO(Agenda agenda) {
        return new AgendaClienteDTO(
            agenda.getDataHora(),
            agenda.getServico().getDuracao()
        );
    }

    public static List<AgendaAdminDTO> toAdminDAOList(List<Agenda> agendas) {
        return agendas.stream().map(AgendaMapper::toAdminDAO).collect(Collectors.toList());
    }

    public static List<AgendaClienteDTO> toClienteDAOList(List<Agenda> agendas) {
        return agendas.stream().map(AgendaMapper::toClienteDAO).collect(Collectors.toList());
    }
}
