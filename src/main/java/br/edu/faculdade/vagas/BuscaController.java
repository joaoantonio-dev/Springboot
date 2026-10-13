package br.edu.faculdade.vagas;

// =============================================================================
// AULA 03 · dois controllers no mesmo caminho base
//
// Este arquivo inteiro é da aula 03. Ele existe porque a frente 4 precisa de
// uma rota que começa com /vagas, e o VagaController é da frente 1 — duas
// pessoas no mesmo arquivo, na mesma semana, é conflito de merge na certa.
//
// O Spring aceita dois controllers no mesmo caminho base. O que ele recusa são
// duas rotas idênticas, e /vagas/busca não é /vagas/{id}. Também não há
// ambiguidade: ele escolhe por especificidade, e caminho literal ganha de
// variável — a ordem em que você declara não importa, ao contrário do que
// acontece em Express.
//
// Para ativar: descomente tudo abaixo (os imports junto).
// =============================================================================
//
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vagas")   // o mesmo caminho base do VagaController
public class BuscaController {

     private final BuscaService servico;

     public BuscaController(BuscaService servico) {
         this.servico = servico;
     }

     // Busca sem resultado devolve 200 com lista vazia, e isso está CERTO:
     // você procurou, o resultado é zero. É o oposto de pedir um id que não
     // existe, que é 404. Mesmo corpo, sentidos opostos.
     // Sem required = false, faltar um parâmetro vira 400 Bad Request — e a
     // busca só funcionaria com os três filtros preenchidos, o que não é
     // busca.
     @GetMapping("/busca")
    public List<Vaga> buscar(
             @RequestParam(required = false) String area,
            @RequestParam(required = false) String senioridade,
             @RequestParam(required = false) Boolean aceitaIniciante) {
         return servico.buscar(area, senioridade, aceitaIniciante);
     }
 }
