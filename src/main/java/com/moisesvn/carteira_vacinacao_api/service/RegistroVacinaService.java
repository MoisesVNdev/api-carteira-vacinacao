package com.moisesvn.carteira_vacinacao_api.service;

import com.moisesvn.carteira_vacinacao_api.dto.response.CalendarioVacinalItemResponseDTO;
import com.moisesvn.carteira_vacinacao_api.dto.request.RegistroVacinaRequestDTO;
import com.moisesvn.carteira_vacinacao_api.dto.response.RegistroVacinaResponseDTO;
import com.moisesvn.carteira_vacinacao_api.exception.*;
import com.moisesvn.carteira_vacinacao_api.mapper.RegistroVacinaMapper;
import com.moisesvn.carteira_vacinacao_api.model.*;
import com.moisesvn.carteira_vacinacao_api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar operações com a tabela `registro_vacina`.
 * 
 * Contém a lógica de negócio do calendário vacinal personalizado:
 * - Cálculo da data prevista de cada dose
 * - Cálculo do status dinâmico (APLICADA, PENDENTE, ATRASADA)
 * - Validação de hierarquia de doses
 * - Validação de posse da pessoa pelo usuário autenticado
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegistroVacinaService {

    private final RegistroVacinaRepository registroVacinaRepository;
    private final EsquemaVacinalRepository esquemaVacinalRepository;
    private final PessoaRepository pessoaRepository;
    private final ResponsavelRepository responsavelRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Retorna o calendário vacinal personalizado de uma pessoa.
     * 
     * Cruza todo o catálogo de esquemas vacinais com os registros existentes,
     * calculando dinamicamente o status de cada dose.
     *
     * @param pessoaId ID da pessoa
     * @return Lista de itens do calendário com status calculados
     * @throws PessoaNaoEncontradaException se a pessoa não existe
     * @throws PessoaNaoPertenceAoUsuarioException se a pessoa não pertence ao usuário autenticado
     */
    @Transactional(readOnly = true)
    public List<CalendarioVacinalItemResponseDTO> gerarCalendarioVacinal(Long pessoaId) {
        log.info("Gerando calendário vacinal para pessoa ID: {}", pessoaId);
        
        // 1. Validar posse da pessoa
        validarPosseDaPessoa(pessoaId);
        
        // 2. Buscar pessoa e data de nascimento
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new PessoaNaoEncontradaException(pessoaId));
        LocalDate dataNascimento = pessoa.getDataNascimento();
        
        // 3. Buscar todos os registros de vacina da pessoa
        List<RegistroVacina> registros = registroVacinaRepository.findByPessoaId(pessoaId);
        Map<Long, RegistroVacina> registrosPorEsquema = registros.stream()
                .collect(Collectors.toMap(r -> r.getEsquemaVacinal().getId(), r -> r));
        
        // 4. Buscar todos os esquemas vacinais (catálogo completo)
        List<EsquemaVacinal> todosEsquemas = esquemaVacinalRepository.findAllByOrderByIdadeRecomendadaMesesAsc();
        
        // 5. Construir o calendário cruzando esquemas com registros
        List<CalendarioVacinalItemResponseDTO> calendario = new ArrayList<>();
        LocalDate hoje = LocalDate.now();
        
        for (EsquemaVacinal esquema : todosEsquemas) {
            // Calcular data prevista: data_nascimento + idade_recomendada_meses
            LocalDate dataPrevista = dataNascimento.plusMonths(esquema.getIdadeRecomendadaMeses());
            
            // Verificar se existe registro para esta dose
            RegistroVacina registro = registrosPorEsquema.get(esquema.getId());
            
            // Calcular status
            StatusVacinal status;
            RegistroVacinaResponseDTO registroDto = null;
            
            if (registro != null) {
                status = StatusVacinal.APLICADA;
                registroDto = RegistroVacinaMapper.toResponseDto(registro);
            } else if (dataPrevista.isAfter(hoje) || dataPrevista.isEqual(hoje)) {
                status = StatusVacinal.PENDENTE;
            } else {
                status = StatusVacinal.ATRASADA;
            }
            
            // Adicionar item ao calendário
            calendario.add(new CalendarioVacinalItemResponseDTO(
                esquema.getVacina().getId(),
                esquema.getVacina().getNome(),
                esquema.getId(),
                esquema.getDescricaoDose(),
                esquema.getIdadeRecomendadaMeses(),
                dataPrevista,
                status,
                registroDto
            ));
        }
        
        log.info("Calendário vacinal gerado com sucesso. Total de doses: {}", calendario.size());
        return calendario;
    }

    /**
     * Lista apenas as doses aplicadas (histórico de vacinação).
     *
     * @param pessoaId ID da pessoa
     * @return Lista de registros de vacina (comprovantes)
     * @throws PessoaNaoEncontradaException se a pessoa não existe
     * @throws PessoaNaoPertenceAoUsuarioException se a pessoa não pertence ao usuário autenticado
     */
    @Transactional(readOnly = true)
    public List<RegistroVacinaResponseDTO> findHistoricoByPessoaId(Long pessoaId) {
        log.debug("Listando histórico de vacinação da pessoa ID: {}", pessoaId);
        
        // Validar posse da pessoa
        validarPosseDaPessoa(pessoaId);
        
        // Valida se pessoa existe
        if (!pessoaRepository.existsById(pessoaId)) {
            throw new PessoaNaoEncontradaException(pessoaId);
        }
        
        return registroVacinaRepository.findByPessoaId(pessoaId).stream()
                .map(RegistroVacinaMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Registra a aplicação de uma dose de vacina.
     * 
     * Validações realizadas:
     * - Pessoa pertence ao usuário autenticado
     * - Esquema vacinal existe
     * - Dose não está duplicada
     * - Dose anterior da mesma vacina já foi aplicada (hierarquia)
     *
     * @param dto Dados do registro
     * @return DTO do registro criado
     * @throws PessoaNaoEncontradaException se a pessoa não existe
     * @throws PessoaNaoPertenceAoUsuarioException se a pessoa não pertence ao usuário autenticado
     * @throws EsquemaVacinalNotFoundException se o esquema vacinal não existe
     * @throws RegistroVacinaDuplicadoException se a dose já está registrada
     * @throws DoseAnteriorNaoAplicadaException se a dose anterior não foi aplicada
     */
    @Transactional
    public RegistroVacinaResponseDTO create(RegistroVacinaRequestDTO dto) {
        log.info("Registrando aplicação de vacina. Pessoa ID: {}, Esquema ID: {}", 
                 dto.pessoaId(), dto.esquemaVacinalId());
        
        // 1. Validar posse da pessoa
        validarPosseDaPessoa(dto.pessoaId());
        
        // 2. Validar pessoa
        Pessoa pessoa = pessoaRepository.findById(dto.pessoaId())
                .orElseThrow(() -> new PessoaNaoEncontradaException(dto.pessoaId()));
        
        // 3. Validar esquema vacinal
        EsquemaVacinal esquemaVacinal = esquemaVacinalRepository.findById(dto.esquemaVacinalId())
                .orElseThrow(() -> new EsquemaVacinalNotFoundException(dto.esquemaVacinalId()));
        
        // 4. Validar duplicidade
        if (registroVacinaRepository.existsByPessoaIdAndEsquemaVacinalId(dto.pessoaId(), dto.esquemaVacinalId())) {
            log.warn("Tentativa de registro duplicado. Pessoa ID: {}, Esquema ID: {}", 
                     dto.pessoaId(), dto.esquemaVacinalId());
            throw new RegistroVacinaDuplicadoException(dto.pessoaId(), dto.esquemaVacinalId());
        }
        
        // 5. Validar hierarquia de doses
        validarHierarquiaDeDoses(pessoa, esquemaVacinal);
        
        // 6. Criar registro
        RegistroVacina registro = RegistroVacina.builder()
                .pessoa(pessoa)
                .esquemaVacinal(esquemaVacinal)
                .dataAplicacao(dto.dataAplicacao())
                .lote(dto.lote())
                .fabricante(dto.fabricante())
                .vacinador(dto.vacinador())
                .localAplicacao(dto.localAplicacao())
                .build();
        
        RegistroVacina saved = registroVacinaRepository.save(registro);
        log.info("Registro de vacina criado com sucesso. ID: {}", saved.getId());
        
        return RegistroVacinaMapper.toResponseDto(saved);
    }

    /**
     * Remove um registro de vacinação (para correção de erro).
     *
     * @param id ID do registro
     * @throws RegistroVacinaNotFoundException se o registro não existe
     * @throws PessoaNaoPertenceAoUsuarioException se a pessoa não pertence ao usuário autenticado
     */
    @Transactional
    public void delete(Long id) {
        log.info("Removendo registro de vacina ID: {}", id);
        
        RegistroVacina registro = registroVacinaRepository.findById(id)
                .orElseThrow(() -> new RegistroVacinaNotFoundException(id));
        
        // Validar posse da pessoa
        validarPosseDaPessoa(registro.getPessoa().getId());
        
        registroVacinaRepository.delete(registro);
        log.info("Registro de vacina removido com sucesso. ID: {}", id);
    }

    /**
     * Valida se a pessoa pertence ao usuário autenticado.
     *
     * @param pessoaId ID da pessoa
     * @throws PessoaNaoPertenceAoUsuarioException se a pessoa não pertence ao usuário
     * @throws UsuarioNaoEncontradoException se o usuário autenticado não é encontrado
     */
    private void validarPosseDaPessoa(Long pessoaId) {
        String emailAutenticado = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        
        Usuario usuario = usuarioRepository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário autenticado não encontrado"));
        
        if (!responsavelRepository.existsByUsuarioIdAndPessoaId(usuario.getId(), pessoaId)) {
            log.warn("Acesso negado. Pessoa ID {} não pertence ao usuário ID {}", pessoaId, usuario.getId());
            throw new PessoaNaoPertenceAoUsuarioException(pessoaId);
        }
    }

    /**
     * Valida a hierarquia de doses: verifica se a dose anterior da mesma vacina
     * já foi aplicada antes de permitir o registro da dose atual.
     * 
     * Lógica:
     * - Busca todas as doses da mesma vacina ordenadas por idade recomendada
     * - Identifica a posição da dose atual
     * - Se não for a primeira dose, verifica se a dose anterior está aplicada
     *
     * @param pessoa Pessoa que está recebendo a vacina
     * @param esquemaAtual Esquema vacinal da dose a ser registrada
     * @throws DoseAnteriorNaoAplicadaException se a dose anterior não foi aplicada
     */
    private void validarHierarquiaDeDoses(Pessoa pessoa, EsquemaVacinal esquemaAtual) {
        Long vacinaId = esquemaAtual.getVacina().getId();
        
        // Buscar todos os esquemas da mesma vacina ordenados por idade
        List<EsquemaVacinal> dosesDaVacina = esquemaVacinalRepository
                .findByVacinaIdOrderByIdadeRecomendadaMesesAsc(vacinaId);
        
        // Se há apenas uma dose ou se é a primeira dose, não precisa validar
        if (dosesDaVacina.size() <= 1) {
            return;
        }
        
        // Encontrar a posição da dose atual
        int posicaoDoseAtual = -1;
        for (int i = 0; i < dosesDaVacina.size(); i++) {
            if (dosesDaVacina.get(i).getId().equals(esquemaAtual.getId())) {
                posicaoDoseAtual = i;
                break;
            }
        }
        
        // Se é a primeira dose (índice 0), não precisa validar
        if (posicaoDoseAtual == 0) {
            return;
        }
        
        // Verificar se a dose anterior está aplicada
        EsquemaVacinal doseAnterior = dosesDaVacina.get(posicaoDoseAtual - 1);
        boolean doseAnteriorAplicada = registroVacinaRepository
                .existsByPessoaIdAndEsquemaVacinalId(pessoa.getId(), doseAnterior.getId());
        
        if (!doseAnteriorAplicada) {
            log.warn("Dose anterior não aplicada. Vacina: {}, Dose atual: {}, Dose anterior: {}", 
                     esquemaAtual.getVacina().getNome(), 
                     esquemaAtual.getDescricaoDose(), 
                     doseAnterior.getDescricaoDose());
            throw new DoseAnteriorNaoAplicadaException(
                esquemaAtual.getVacina().getNome(),
                esquemaAtual.getDescricaoDose(),
                doseAnterior.getDescricaoDose()
            );
        }
    }
}
