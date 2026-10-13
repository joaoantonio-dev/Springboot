# Leque de Vagas — a API

Projeto de referência do curso **Introdução ao Spring Boot** (NickDev). É o
outro lado do mural de vagas: o programa que guarda as vagas, as empresas e as
pessoas, e as entrega para quem pedir.

O código que roda aqui está **no ponto da aula 02**: o esqueleto em camadas,
com dados fixos em memória e só as rotas de leitura. **A aula 03 já está no
repositório, comentada**, arquivo por arquivo, sob um cabeçalho `AULA 03` que
diz o que descomentar e o que apagar no lugar.

Ele existe para você comparar com o seu: abrir um arquivo, ver como ficou aqui
e entender por quê. Não é para clonar e entregar — o que vale nota é o da sua
equipe, com as decisões dela e um commit por pessoa.

A tela que consome esta API é
[lequedevagas-web](https://github.com/Um-Leque-de-Tecnologia/lequedevagas-web),
projeto da disciplina de Introdução ao Next.js. Os dados daqui são os mesmos de
lá — é de propósito.

## Rodar

```bash
./mvnw spring-boot:run     # Mac e Linux
mvnw.cmd spring-boot:run   # Windows
```

Precisa de **JDK 21**. Sobe em <http://localhost:8080> — procure a linha
`Tomcat started on port 8080` no log.

Depois de subir, abra o `requisicoes.http` e clique em **Send Request** em cima
de cada chamada. O IntelliJ roda direto; no VS Code, instale a extensão REST
Client. Nada de Insomnia ou Postman: um arquivo de texto faz o mesmo e vai
versionado no Git, o que Insomnia nenhum faz de graça.

## As quatro frentes

O projeto é de equipe, e cada pessoa carrega uma frente o semestre inteiro. Este
repositório é de referência, então tem as quatro juntas — no da sua equipe, cada
uma é de alguém, e o `git log` é a evidência disso.

| Frente | Recurso | Arquivos |
| --- | --- | --- |
| Filipe Fernandes | Vaga | `Vaga`, `VagaEntrada`, `VagaResposta`, `VagaRepository`, `VagaService`, `VagaController` |
| João Antonio | Empresa | `Empresa`, `EmpresaRepository`, `EmpresaService`, `EmpresaController` |
| Luis Guilherme | Pessoa | `Pessoa`, `PessoaRepository`, `PessoaService`, `PessoaController` |
| Guilherme Santana | Busca e números | `EstatisticasService`, `EstatisticasController` — e, na aula 03, `BuscaService` e `BuscaController` |

A frente 4 não tem recurso próprio: ela lê o `VagaRepository` da frente 1. É de
propósito — buscar é uma responsabilidade, não uma tabela.

## O que está ativo: a aula 02

O esqueleto do projeto, com dados fixos em memória mas já separado em
controller, service e repository. Toda dependência chega pronta pelo construtor,
em campo `final`. Nenhum `new` entre as classes.

| Rota | Frente | Devolve |
| --- | --- | --- |
| `GET /vagas` | 1 | as 12 vagas |
| `GET /vagas/{id}` | 1 | uma lista de uma vaga — ou `[]` |
| `GET /empresas` | 2 | as 5 empresas |
| `GET /empresas/{slug}` | 2 | uma lista de uma empresa — ou `[]` |
| `GET /pessoas` | 3 | as 6 pessoas |
| `GET /pessoas/{id}` | 3 | uma lista de uma pessoa — ou `[]` |
| `GET /estatisticas` | 4 | os números do catálogo |

**Todas respondem 200 — inclusive as que não acham nada.** Isso não é descuido:
é o defeito combinado da aula 02. Buscar por id devolve lista porque ainda não
há como dizer "não achei", e lista vazia é a resposta honesta enquanto isso —
devolver `null` seria a desonesta. É exatamente o que a aula 03 conserta.

## O que está comentado: a aula 03

Em cada arquivo, depois do código que roda, há um bloco assim:

```java
// =========================================================================
// AULA 03 · o service passa a saber dizer "não achei"
// ...
// Para ativar: descomente o bloco abaixo e apague o buscarPorId da aula 02.
// =========================================================================
```

O que cada bloco traz:

| Onde | O que a aula 03 muda |
| --- | --- |
| `VagaRepository` | `List.of(...)` é imutável — vira campo `new ArrayList<>(List.of(...))`, e `todas()` passa a devolver `List.copyOf`, com `porId`, `salvar`, `trocar` e `remover` |
| `VagaService` | `Optional` em vez de lista, o id nascendo com `UUID.randomUUID()`, e o id do `PUT` vindo da URL |
| `VagaController` | `ResponseEntity` escolhendo entre 200 e 404, `201` com cabeçalho `Location`, `204` sem corpo |
| `Empresa*` | o mesmo, com o `Location` montado a partir do **slug que o cliente escolheu** — e sem `PUT`, porque trocar o slug seria trocar o endereço do recurso |
| `Pessoa*` | o CRUD completo, e o `idempotencia.http`, que prova o que PUT e DELETE têm e POST não tem |
| `BuscaService` e `BuscaController` | arquivos **inteiramente** da aula 03: três `@RequestParam` opcionais, num controller que convive com o da frente 1 no mesmo `/vagas` |
| `requisicoes.http` | as chamadas novas, comentadas no fim do arquivo |

Descomentar um bloco sozinho não compila: as três camadas de uma frente mudam
juntas. Ative repository, service e controller da mesma frente de uma vez — que
é, aliás, como a pessoa dona daquela frente vai trabalhar.

A tabela de status que a aula 03 entrega:

| Rota | Status |
| --- | --- |
| `GET /vagas/{id}` · `GET /empresas/{slug}` · `GET /pessoas/{id}` | 200 · 404 |
| `POST /vagas` · `POST /empresas` · `POST /pessoas` | 201, com `Location` |
| `PUT /vagas/{id}` · `PUT /pessoas/{id}` | 200 · 404 |
| `DELETE` nas três frentes | 204 · 404 |
| `GET /vagas/busca` | 200 — inclusive `[]`, que aqui está certo |

## O que NÃO tem, de propósito

Se você achar que está faltando, provavelmente está — mas na aula seguinte.

- **DTO.** Na aula 03 o mesmo `record` é usado no corpo que entra e no que sai,
  aceitando que o `id` chega `null` no POST e é ignorado. Funciona, e é o
  suficiente por lá. Aula 04.
- **Validação.** Aula 04.
- **Tratamento de erro padronizado.** O 404 da aula 03 vem sem corpo. Problem
  Details é aula 04.
- **Banco de dados.** A fonte são três listas em memória. Aula 05 — e quando
  ela chegar, só o `Repository` muda.
- **Relacionamento entre Vaga e Empresa.** A vaga guarda `empresaSlug`, um
  texto solto. `@ManyToOne` é aula 06.
- **Candidatura.** A entidade existe no briefing, mas só aparece na aula 06.
- **Autenticação.** `GET /pessoas` é público e não há campo de senha. Aulas 10
  e 11.
- **Testes.** Só o `contextLoads` que veio do Initializr. Aula 09.

## Detalhes que costumam pegar

**O starter mudou de nome no Spring Boot 4.** O `pom.xml` traz
`spring-boot-starter-webmvc` e `spring-boot-starter-webmvc-test`, e não
`spring-boot-starter-web` e `spring-boot-starter-test`. Material e tutorial que
mostram os nomes antigos são de Boot 3 — funcionam, mas não é o que o
Initializr gera hoje.

**`required a bean of type 'VagaRepository'`.** Faltou o `@Repository`, ou a
classe está fora do pacote da `Application`. São as duas causas, sempre.

**Um `record` anotado.** `@Component` num record é sinal de que a diferença
entre dado e serviço não ficou clara. Record guarda; estereótipo é para quem
faz.

**`Map.of` reclamando de tipo.** Ele infere o tipo dos valores, e no
`EstatisticasService` eles são mistos — número, contagem e mapa. Por isso o
retorno é declarado `Map<String, Object>`.

**Os números não fecham.** As contagens por área e por senioridade têm que
somar o total de vagas. Se não somam, alguma vaga tem área escrita de dois
jeitos — `"Front-end"` e `"front-end"` são chaves diferentes.

**O `id` é texto.** O que vem da URL é sempre texto, então `"1"` e não `1`.
Comparar com `==` em vez de `.equals()` nunca bate, e `/vagas/1` devolve `[]`
sem erro nenhum.

**Os slugs combinam na mão.** Todo `empresaSlug` do `VagaRepository` tem que
existir como `slug` no `EmpresaRepository`, escrito igual. Quando desencontram,
nada quebra agora — quebra na aula 06, longe de onde foi criado.

Os erros que aparecem só depois de ativar a aula 03 — `Ambiguous mapping`,
`UnsupportedOperationException` no POST, `415` sem `Content-Type`, o PUT que
grava `null` no id — estão comentados junto do bloco de cada frente.

## Estrutura

```
requisicoes.http                 as chamadas da aula 02, versionadas
idempotencia.http                a evidência da frente 3 (aula 03, comentado)
pom.xml                          Spring Boot 4.1.1, Java 21
src/main/
├── java/br/edu/faculdade/vagas/
│   ├── VagasApplication.java    veio pronta do Initializr
│   ├── Vaga.java                frente 1 · record, sem anotação
│   ├── VagaRepository.java      @Repository — onde o dado está
│   ├── VagaService.java         @Service — onde a decisão mora
│   ├── VagaController.java      @RestController — o único que fala HTTP
│   ├── Empresa*.java            frente 2 · o identificador é o slug
│   ├── Pessoa*.java             frente 3 · a mesma forma, terceira vez
│   ├── EstatisticasService.java frente 4 · lê o repository da frente 1
│   ├── EstatisticasController.java
│   ├── BuscaService.java        frente 4 · aula 03, inteiro comentado
│   └── BuscaController.java     frente 4 · aula 03, inteiro comentado
└── resources/
    └── application.properties
```

Tudo no mesmo pacote da `Application` — é o que o `@ComponentScan` embutido no
`@SpringBootApplication` enxerga. Subpastas por camada também funcionam, desde
que fiquem dentro dele.

## Licença

Material didático do NickDev. Use para estudar, cite quando reaproveitar.
