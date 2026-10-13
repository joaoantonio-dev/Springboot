package br.edu.faculdade.vagas;
 
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.net.URI;
import java.util.List;
 
/**
 * Recebe o pedido e devolve a resposta. Cada método tem uma linha.
 *
 * Leia o método em voz alta: "devolve as vagas". Nenhum if, nenhum for, nenhum
 * número solto. Se aparecer algum, é regra — e regra é do servico.
 *
 * Nada de Vaga aparece aqui: o que entra é VagaEntrada, o que sai é
 * VagaResposta. O modelo fica do lado de dentro.
 */
@RestController
@RequestMapping("/vagas")
public class VagaController {
 
    private final VagaService servico;
 
    public VagaController(VagaService servico) {
        this.servico = servico;
    }
 
    @GetMapping
    public List<VagaResposta> listar() {
        return servico.listar();
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<VagaResposta> porId(@PathVariable String id) {
        return servico.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
 
    @PostMapping
    public ResponseEntity<VagaResposta> criar(@Valid @RequestBody VagaEntrada entrada) {
        VagaResposta salva = servico.criar(entrada);
        URI onde = URI.create("/vagas/" + salva.id());
        return ResponseEntity.created(onde).body(salva);
    }
 
    @PutMapping("/{id}")
    public ResponseEntity<VagaResposta> trocar(@PathVariable String id,
                                               @Valid @RequestBody VagaEntrada nova) {
        return servico.trocar(id, nova)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
 
    // ResponseEntity<Void>: não há corpo nenhum para tipar. Devolver o objeto
    // recém-apagado sugeriria que ele ainda está lá.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable String id) {
        return servico.apagar(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}
 
