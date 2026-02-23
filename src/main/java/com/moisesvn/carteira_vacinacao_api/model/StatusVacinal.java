package com.moisesvn.carteira_vacinacao_api.model;

/**
 * Enum que define os possíveis status de uma dose do esquema vacinal
 * no calendário personalizado de vacinação.
 * 
 * Os status são calculados dinamicamente na camada Service:
 * - APLICADA: dose registrada em registro_vacina
 * - PENDENTE: dose não aplicada e data prevista >= hoje
 * - ATRASADA: dose não aplicada e data prevista < hoje
 */
public enum StatusVacinal {
    
    /**
     * Dose já foi aplicada e registrada no sistema.
     */
    APLICADA,
    
    /**
     * Dose não aplicada, mas dentro do prazo (data prevista >= hoje).
     */
    PENDENTE,
    
    /**
     * Dose não aplicada e fora do prazo (data prevista < hoje).
     */
    ATRASADA
}
