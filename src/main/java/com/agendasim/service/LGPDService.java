package com.agendasim.service;

import com.agendasim.model.Agenda;
import com.agendasim.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LGPDService {

    private final AgendaRepository agendaRepository;
    private final AuditoriaService auditoriaService;

    /**
     * Anonimizar todos os agendamentos de um cliente específico
     * Busca por nome e telefone em todas as empresas
     */
    @Transactional
    public int anonimizarTodosAgendamentosCliente(String nomeCliente, String telefoneCliente) {
        log.info("Anonimizando cliente: {} - {}", nomeCliente, telefoneCliente);
        
        // Buscar todos os agendamentos do cliente
        List<Agenda> agendamentos = agendaRepository.findByNomeClienteAndTelefoneCliente(nomeCliente, telefoneCliente);
        
        if (agendamentos.isEmpty()) {
            throw new RuntimeException("Nenhum agendamento encontrado para o cliente: " + nomeCliente);
        }
        
        // Verificar se já foi anonimizado
        boolean jaAnonimizado = agendamentos.stream()
            .anyMatch(a -> a.getNomeCliente().startsWith("Cliente_Anonimizado_"));
        
        if (jaAnonimizado) {
            throw new RuntimeException("Cliente já foi anonimizado anteriormente");
        }
        
        // Anonimizar todos os agendamentos
        int contador = 0;
        for (Agenda agendamento : agendamentos) {
            agendamento.setNomeCliente("Cliente_Anonimizado_" + agendamento.getId());
            agendamento.setTelefoneCliente("+55 31 00000-0000");
            contador++;
        }
        
        // Salvar todas as alterações
        agendaRepository.saveAll(agendamentos);
        
        // Registrar na auditoria
        auditoriaService.logarAcao(null, "CLIENTE_ANONIMIZADO_GLOBAL", "agenda", null, 
            "Cliente: " + nomeCliente + " - " + telefoneCliente, 
            "Agendamentos anonimizados: " + contador);
        
        log.info("Cliente anonimizado globalmente: {} - {} agendamentos: {}", nomeCliente, telefoneCliente, contador);
        
        return contador;
    }
    
    /**
     * Contar quantos agendamentos um cliente tem
     */
    public int contarAgendamentosCliente(String nomeCliente, String telefoneCliente) {
        return agendaRepository.findByNomeClienteAndTelefoneCliente(nomeCliente, telefoneCliente).size();
    }
    
    /**
     * Verificar se cliente já foi anonimizado
     */
    public boolean verificarClienteJaAnonimizado(String nomeCliente, String telefoneCliente) {
        List<Agenda> agendamentos = agendaRepository.findByNomeClienteAndTelefoneCliente(nomeCliente, telefoneCliente);
        return agendamentos.stream()
            .anyMatch(a -> a.getNomeCliente().startsWith("Cliente_Anonimizado_"));
    }
}
