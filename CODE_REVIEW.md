# Code Review — Carteira de Vacinação API

**Data:** 18 de fevereiro de 2026  
**Versão:** 0.0.1-SNAPSHOT  
**Status:** ✅ Projeto bem estruturado com pontos críticos a corrigir

---

## 📊 Resumo Executivo

| Aspecto | Avaliação | Observação |
|---------|-----------|-----------|
| **Arquitetura** | ✅ Excelente | Camadas bem definidas, separação de responsabilidades clara |
| **SOLID/DRY/KISS** | ⚠️ Bom | Alguns desvios menores em DTOs e logging |
| **Segurança** | ✅ Très Bem | JWT corretamente implementado, BCrypt, autenticação stateless |
| **Tratamento de Erro** | ✅ Bien | GlobalExceptionHandler bem estruturado |
| **Compatibilidade** | ✅ Excelente | Spring Boot 4.0.2, Java 21, JJWT 0.11.5 — tudo conforme |
| **Padrões de Código** | ⚠️ Necessita Ajustes | Entidade com @Data, DTOs não-Record, falta de transacionais |

---

## ✅ Pontos Positivos

### 1. **Arquitetura em Camadas** (Excelente)
- ✓ Estrutura correta: `controller/` → `service/` → `repository/`
- ✓ DTOs isolam entidades JPA das requisições/respostas
- ✓ Exceções customizadas em pacote dedicado
- ✓ Configuração centralizada em `config/`
- ✓ Segurança isolada em `security/`

### 2. **Camada Controller** (Conforme Orientações)
- ✓ Usar `@RestController` e `@RequestMapping`
- ✓ Retorna `ResponseEntity<T>` com status HTTP correto
- ✓ Usa `@Valid` em `@RequestBody`
- ✓ Nenhum acesso direto ao Repository
- ✓ Todas as respostas em DTOs

### 3. **Segurança JWT** (Implementação Correta)
- ✓ Usa JJWT 0.11.5 (versão exata do projeto)
- ✓ Chave secreta lida de properties
- ✓ Padrão correto de geração/validação de token
- ✓ Filtro estende `OncePerRequestFilter`
- ✓ Adiciona filtro antes de `UsernamePasswordAuthenticationFilter`
- ✓ `SecurityFilterChain` com `SessionCreationPolicy.STATELESS`
- ✓ CSRF desabilitado para API stateless

### 4. **Tratamento de Exceções** (Centralizado)
- ✓ `@RestControllerAdvice` com `@ExceptionHandler`
- ✓ Mapeia exceções de domínio para status HTTP corretos
  - 404 → `UsuarioNaoEncontradoException`
  - 409 → `EmailJaCadastradoException`
  - 401 → `InvalidCredentialsException`, `TokenInvalidoException`
  - 400 → `MethodArgumentNotValidException`
- ✓ Logging com `@Slf4j`
- ✓ Mensagens genéricas para não expor detalhes sensíveis

### 5. **Injeção de Dependências**
- ✓ Construtor com `@RequiredArgsConstructor`
- ✓ Sem `@Autowired` em campos
- ✓ Dependências `final`

### 6. **DTOs com Validação**
- ✓ `@NotBlank`, `@Email` presentes
- ✓ RegisterRequestDTO com validação forte de senha (regex)
- ✓ Sem exposição de `password` em ResponseDTOs

### 7. **Banco de Dados**
- ✓ JPA/Hibernate corretamente configurado
- ✓ PostgreSQL driver incluso
- ✓ HikariCP com pool adequado
- ✓ Unique constraint em email
- ✓ `@PrePersist` para data de cadastro

### 8. **Versionamento do Projeto**
- ✓ Spring Boot 4.0.2 (versão correta)
- ✓ Java 21 (conforme requisito)
- ✓ Maven com configuração apropriada

### 9. **Logging**
- ✓ `@Slf4j` usado em `JwtService`, `AuthenticationService`, `JwtAuthenticationFilter`
- ✓ Sem `System.out.println` detectado
- ✓ Informações sensíveis não logadas (senhas, tokens completos)

### 10. **Configuração de Segurança**
- ✓ `BCryptPasswordEncoder` injetável via `@Bean`
- ✓ Endpoints públicos bem definidos (`/auth/**`, `/health`, `/actuator/**`)
- ✓ `UserDetailsServiceImpl` carrega usuário por email
- ✓ `AuthenticationManager` exposto como `@Bean`

---

## ⚠️ Pontos de Melhoria — CRÍTICOS

### 1. **❌ CRÍTICO: Entidade `Usuario` com `@Data`** → 🔴 **CORRIGIR IMEDIATAMENTE**

**Problema:**
```java
@Entity
@Data  // ❌ Problema: gera equals/hashCode com TODOS os campos
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario { ... }
```

**Por que é problema:**
- `@Data` gera `equals()` e `hashCode()` baseados em **todos os campos**
- Lazy loading de campos relacionados (quando implementar) causa inconsistência
- Entidades são comparadas por **identidade** no JPA, não por valor
- Viola padrão: "Entidades devem usar equals/hashCode baseado apenas em `id`"

**Solução:**
```java
@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    // ... campos ...

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        return id != null && id.equals(((Usuario) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
```

---

### 2. **❌ CRÍTICO: `UsuarioService` sem `@Transactional` e sem `@Slf4j`** → 🔴 **CORRIGIR**

**Problema:**
```java
@Service
@RequiredArgsConstructor
public class UsuarioService {  // ❌ Faltam @Transactional e @Slf4j
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) { ... }  // ❌ Sem @Transactional
    public List<UsuarioResponseDTO> listarTodos() { ... }  // ❌ Sem @Transactional(readOnly=true)
    public UsuarioResponseDTO buscarPorId(Long id) { ... }  // ❌ Sem @Transactional(readOnly=true)
    public UsuarioResponseDTO atualizar(...) { ... }  // ❌ Sem @Transactional
    public void excluir(Long id) { ... }  // ❌ Sem @Transactional
}
```

**Solução:**
```java
@Service
@RequiredArgsConstructor
@Slf4j  // ✅ Adicionar
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional  // ✅ Adicionar
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        log.info("Criando novo usuário com email: {}", dto.getEmail());  // ✅ Log
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException(dto.getEmail());
        }
        // ... resto do código ...
    }

    @Transactional(readOnly = true)  // ✅ Adicionar
    public List<UsuarioResponseDTO> listarTodos() {
        log.debug("Listando todos os usuários");
        return usuarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)  // ✅ Adicionar
    public UsuarioResponseDTO buscarPorId(Long id) {
        log.debug("Buscando usuário por id: {}", id);
        return toResponseDTO(buscarEntidadePorId(id));
    }

    @Transactional  // ✅ Adicionar
    public UsuarioResponseDTO atualizar(Long id, UsuarioUpdateRequestDTO dto) {
        log.info("Atualizando usuário com id: {}", id);
        Usuario usuario = buscarEntidadePorId(id);
        usuario.setNomeCompleto(dto.getNomeCompleto());
        return toResponseDTO(usuarioRepository.save(usuario));
    }

    @Transactional  // ✅ Adicionar
    public void excluir(Long id) {
        log.info("Deletando usuário com id: {}", id);
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontradoException(id);
        }
        usuarioRepository.deleteById(id);
    }
}
```

---

### 3. **⚠️ IMPORTANTE: LoginResponseDTO e UsuarioResponseDTO não são Records** → 🟡 **CORRIGIR**

**Problema:**
```java
@Data
@Builder
@AllArgsConstructor
public class LoginResponseDTO {  // ❌ Classe mutável
    private String token;
    private String tipo;
    private long expiraEm;
}

@Data
@Builder
public class UsuarioResponseDTO {  // ❌ Classe mutável
    private Long id;
    private String nomeCompleto;
    private String email;
    private LocalDateTime dataCadastro;
}
```

**Instrução violada:** "Use **Java Records** para DTOs imutáveis (preferível para respostas)."

**Solução:**
```java
/**
 * DTO de resposta com token JWT e metadados.
 */
public record LoginResponseDTO(
    String token,
    String tipo,
    long expiraEm
) {}

/**
 * DTO de resposta com dados públicos do usuário.
 */
public record UsuarioResponseDTO(
    Long id,
    String nomeCompleto,
    String email,
    LocalDateTime dataCadastro
) {}
```

**Benefícios:**
- Imutáveis por padrão
- Sem getters/setters gerados
- `equals()`, `hashCode()`, `toString()` automáticos
- Menos código boilerplate
- Intenção clara: "Este é um DTO de transferência"

---

### 4. **⚠️ IMPORTANTE: `GlobalExceptionHandler` sem campo `caminho` (path)** → 🟡 **CORRIGIR**

**Problema:**
```java
private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String mensagem) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", status.value());
    body.put("erro", mensagem);
    // ❌ Faltam: "caminho" (path), "detalhes" ou outro campo descritivo
    return ResponseEntity.status(status).body(body);
}
```

**Instrução esperada:** 
> "O `ErrorResponse` deve conter: `timestamp`, `status`, `erro`, `mensagem`, `caminho`."

**Solução — Opção 1: Usar Record (Recomendado)**
```java
/**
 * Estrutura padronizada de resposta para erros da API.
 */
public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String erro,
    String mensagem,
    String caminho
) {}
```

**Solução — Opção 2: Melhorar Handler**
```java
@ExceptionHandler(UsuarioNaoEncontradoException.class)
public ResponseEntity<ErrorResponse> handleNaoEncontrado(
        UsuarioNaoEncontradoException ex,
        HttpServletRequest request) {  // ✅ Injetar request
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "UsuarioNaoEncontradoException",
                ex.getMessage(),
                request.getRequestURI()  // ✅ Capturar caminho
            ));
}

private ResponseEntity<ErrorResponse> buildResponse(
        HttpStatus status,
        String erro,
        String mensagem,
        String caminho) {
    return ResponseEntity.status(status)
            .body(new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                erro,
                mensagem,
                caminho
            ));
}
```

---

### 5. **⚠️ IMPORTANTE: `AuthenticationService` está em `service/` mas é de segurança** → 🟡 **CONSIDERAR**

**Problema:**
```
src/main/java/com/moisesvn/carteira_vacinacao_api/
├── service/
│   ├── AuthenticationService.java  // ❌ Lógica de autenticação não é "regra de negócio"
│   └── UsuarioService.java
└── security/
    ├── JwtService.java
    ├── JwtAuthenticationFilter.java
    └── SecurityConfiguration.java
```

**Reflexão:**
- `AuthenticationService` coordena JWT e credenciais, não é "lógica de negócio"
- `UsuarioService` é verdadeiro "service" de domínio
- Ambos funcionam juntos, mas estão separados por camada

**Opções:**
1. **Mover para `security/`** (recomendado se focar em segurança)
2. **Deixar em `service/`** (aceitável se considerar orchestração de login como "regra de negócio")
3. **Renomear pacote para `orchestration/` ou `domain/`** (futuro)

**Recomendação:** Por enquanto, é aceitável deixar em `service/`, mas considere em refatorações futuras.

---

### 6. **⚠️ IMPORTANTE: `UsuarioController` permite POST sem autenticação** → 🟡 **REVISAR REQUISITO**

**Código atual:**
```java
@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ...
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/health", "/actuator/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()  // ❌ Permitir sem token?
                .anyRequest().authenticated()
            )
            // ...
    }
}
```

**Pergunta de requisito:**
- Qualquer pessoa pode registrar um novo usuário (sem token)?
  - ✅ Se SIM: está correto
  - ❌ Se NÃO: remover `.requestMatchers(HttpMethod.POST, "/usuarios").permitAll()`

**Recomendação:**
- **Manter público** se for sistema de autoregistro aberto
- **Remover** se apenas administradores podem criar usuários

**Cenário provável (autoregistro):**
```java
// Criar endpoint /auth/register em vez de POST /usuarios
// POST /usuarios ficaria protegido
.requestMatchers("/auth/register").permitAll()  // Público
.anyRequest().authenticated()  // Resto protegido
```

Seu design atual tem:
- `POST /auth/register` (público) — via `AuthController`
- `POST /usuarios` (público) — via `UsuarioController`

Ambos criam usuários. **Redundância?** Considere remover um ou unificar.

---

## 🟡 Pontos de Melhoria — IMPORTANTES

### 7. **Sem Javadoc em métodos públicos do `UsuarioService`**

Adicionar documentação em português:
```java
/**
 * Cria um novo usuário validando unicidade de email.
 * A senha é codificada com BCrypt antes de persistir.
 *
 * @param dto Dados do novo usuário
 * @return DTO com o usuário criado
 * @throws EmailJaCadastradoException se o email já existe
 */
@Transactional
public UsuarioResponseDTO criar(UsuarioRequestDTO dto) { ... }
```

---

### 8. **Sem mapeador centralizado (Mapper)**

Código atual (disperso em métodos privados):
```java
private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
    return UsuarioResponseDTO.builder()
            .id(usuario.getId())
            .nomeCompleto(usuario.getNomeCompleto())
            .email(usuario.getEmail())
            .dataCadastro(usuario.getDataCadastro())
            .build();
}
```

**Melhoria (com Records):**
```java
public class UsuarioMapper {
    public static UsuarioResponseDTO toDto(Usuario usuario) {
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNomeCompleto(),
            usuario.getEmail(),
            usuario.getDataCadastro()
        );
    }
}
```

Uso: `usuarioRepository.findAll().stream().map(UsuarioMapper::toDto)`

---

### 9. **Sem suporte a paginação em listarTodos()**

Base atual retorna todos:
```java
public List<UsuarioResponseDTO> listarTodos() {
    return usuarioRepository.findAll().stream() // ❌ Sem limite
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
}
```

**Melhoria (com Spring Data):**
```java
@Transactional(readOnly = true)
public Page<UsuarioResponseDTO> listarTodos(Pageable pageable) {
    return usuarioRepository.findAll(pageable)
            .map(UsuarioMapper::toDto);
}
```

Controller:
```java
@GetMapping
public ResponseEntity<Page<UsuarioResponseDTO>> listarTodos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(
        usuarioService.listarTodos(PageRequest.of(page, size))
    );
}
```

---

### 10. **Secret JWT em plain text em application.properties**

**Problema:**
```properties
jwt.secret=VGhpcyBpcyBhIHZlcnkgc2VjdXJlIHNlY3JldCBrZXkgdGhhdCBpcyBhdCBsZWFzdCAyNTYgYml0cyBsb25nIGZvciBIUzI1NiBKV1Qgc2lnbmluZw==
```

**Solução (Recomendada):**
```properties
# application.properties (desenvolvimento local)
jwt.secret=${JWT_SECRET:VGhpcyBpcyBhIHZlcnkgc2VjdXJlIHNlY3JldCBrZXkgdGhhdCBpcyBhdCBsZWFzdCAyNTYgYml0cyBsb25nIGZvciBIUzI1NiBKV1Qgc2lnbmluZw==}
jwt.expiration=${JWT_EXPIRATION:3600000}
```

**Docker Compose:**
```yaml
environment:
  JWT_SECRET: "sua-chave-secreta-em-producao"
  JWT_EXPIRATION: "3600000"
```

---

### 11. **Expressão regular de senha complexa**

Validação em `RegisterRequestDTO`:
```java
@Pattern(
    regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
    message = "Senha deve ter no mínimo 8 caracteres, uma letra maiúscula, um número e um caractere especial"
)
private String senha;
```

**Feedback:**
- ✅ Bem estruturado
- ⚠️ Difícil de ler/manter
- ✨ Considere extrair para constante:

```java
public class SenhaValidator {
    // Mínimo 8 chars, 1 maiúscula, 1 número, 1 special
    public static final String REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$";
}
```

---

### 12. **Tratamento de erro na conversão de UsuarioRequestDTO**

Código em `AuthenticationService.registrar()`:
```java
public UsuarioResponseDTO registrar(RegisterRequestDTO request) {
    // Reutiliza UsuarioService para manter DRY
    com.moisesvn.carteira_vacinacao_api.dto.UsuarioRequestDTO dto =
            new com.moisesvn.carteira_vacinacao_api.dto.UsuarioRequestDTO();  // ❌ Criar manualmente
    dto.setNomeCompleto(request.getNomeCompleto());
    dto.setEmail(request.getEmail());
    dto.setSenha(request.getSenha());

    return usuarioService.criar(dto);
}
```

**Problema:**
- Duplicação de mapeamento
- Usa `new` ao invés de construtor/builder
- Compqui nome qualificado completo

**Melhoria:**
```java
public UsuarioResponseDTO registrar(RegisterRequestDTO request) {
    UsuarioRequestDTO dto = new UsuarioRequestDTO();
    dto.setNomeCompleto(request.getNomeCompleto());
    dto.setEmail(request.getEmail());
    dto.setSenha(request.getSenha());
    return usuarioService.criar(dto);
}
```

Ou com `UsuarioRequestDTO` como Record (se tiver construtor):
```java
public UsuarioResponseDTO registrar(RegisterRequestDTO request) {
    return usuarioService.criar(
        new UsuarioRequestDTO(
            request.getNomeCompleto(),
            request.getEmail(),
            request.getSenha()
        )
    );
}
```

---

### 13. **Ausência de spring-boot-starter-validation**

**Status:** ✅ Presente no `pom.xml`
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Sem problemas, está ali. ✅

---

## 🟢 Pontos de Implementação Futura (YAGNI)

**Não implemente agora, mas esteja preparado:**

1. **Roles/Permissões** (ROLE_USER, ROLE_ADMIN)
   - `SecurityConfiguration` já tem `@EnableMethodSecurity`
   - `UserDetailsServiceImpl` retorna `ROLE_USER`
   - Adicionar campo `role` em `Usuario` quando necessário

2. **Refresh Tokens**
   - `JwtService` pode se expandir com método para refresh
   - Adicionar tabela `RefreshToken` no banco

3. **Auditoria** (quem criou, quando modificou)
   - Adicionar `@CreationTimestamp`, `@UpdateTimestamp` em `Usuario`
   - Adicionar `@EntityListeners(AuditingEntityListener.class)`

4. **Soft Delete**
   - Adicionar campo `deletedAt` e filtro automático

5. **CORS**
   - Não necessário para API interna
   - Quando consumida por SPA/mobile, adicionar `@CrossOrigin`

---

## 📋 Checklist de Correções Prioritárias

### 🔴 CRÍTICO (Fazer antes de qualquer release)
- [ ] Remover `@Data` de `Usuario` e implementar `equals/hashCode` baseado em `id`
- [ ] Adicionar `@Transactional` a métodos de escrita em `UsuarioService`
- [ ] Adicionar `@Slf4j` a `UsuarioService`
- [ ] Converter `LoginResponseDTO` e `UsuarioResponseDTO` para Records
- [ ] Adicionar campo `caminho` (path) em `GlobalExceptionHandler`

### 🟡 IMPORTANTE (Fazer na próxima sprint)
- [ ] Adicionar Javadoc em métodos públicos de `UsuarioService`
- [ ] Criar classe `UsuarioMapper` para centralizar conversões
- [ ] Avaliar se `POST /usuarios` deve ser público ou protegido
- [ ] Implementar paginação em `listarTodos()`
- [ ] Mover secret JWT para variável de ambiente

### 🟢 BÔNUS (Futuro)
- [ ] Extrair regex de senha para constante
- [ ] Eliminar redundância entre `/auth/register` e `POST /usuarios`
- [ ] Adicionar testes unitários
- [ ] Adicionar testes de integração

---

## 🎯 Conclusão

**Avaliação Geral: 7.5/10** — Código bem estruturado, segurança implementada corretamente, mas com desvios de padrão em DTOs e entidades que precisam ser corrigidos.

### Próximos Passos Recomendados:

1. **Semana 1:** Corrigir os 5 pontos críticos
2. **Semana 2:** Implementar melhorias importantes (Javadoc, Mapper, paginação)
3. **Semana 3:** Escrever testes unitários
4. **Semana 4:** Deploy em staging e testes de integração

---

**Revisão realizada por:** Copilot  
**Branch:** `feature/implentado-usuario`  
**Relatório gerado:** 18/02/2026
