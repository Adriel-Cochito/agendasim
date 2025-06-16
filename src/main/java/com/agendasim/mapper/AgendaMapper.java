package com.agendasim.mapper;

import com.agendasim.dao.*;
import com.agendasim.model.Agenda;

import java.util.List;
import java.util.stream.Collectors;

public class AgendaMapper {

    public static AgendaAdminDAO toAdminDAO(Agenda agenda) {
        return new AgendaAdminDAO(
            agenda.getId(),
            agenda.getNomeCliente(),
            agenda.getTelefoneCliente(),
            agenda.getDataHora(),
            agenda.getStatus(),
            agenda.getProfissional().getId(),
            agenda.getProfissional().getNome(),
            agenda.getServico().getId(),
            agenda.getServico().getTitulo()
        );
    }

    public static AgendaClienteDAO toClienteDAO(Agenda agenda) {
        return new AgendaClienteDAO(
            agenda.getDataHora(),
            agenda.getServico().getDuracao()
        );
    }

    public static List<AgendaAdminDAO> toAdminDAOList(List<Agenda> agendas) {
        return agendas.stream().map(AgendaMapper::toAdminDAO).collect(Collectors.toList());
    }

    public static List<AgendaClienteDAO> toClienteDAOList(List<Agenda> agendas) {
        return agendas.stream().map(AgendaMapper::toClienteDAO).collect(Collectors.toList());
    }
}
