package br.edu.faculdade.vagas;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/vagas")
public class VagaController {

    private final VagaService service;

    public VagaController(VagaService service) {
        this.service = service;
    }

    // Lista sempre existe, mesmo vazia — 200 direto, sem ResponseEntity.
    @GetMapping
    public List<Vaga> listar() {
        return service.listar();
    }

    // map roda se achou, orElse se não achou. O if está aí dentro —
    // e o compilador não deixa você esquecer o segundo caso.
    @GetMapping("/{id}")
    public ResponseEntity<Vaga> porId(@PathVariable String id) {
        return service.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // 201 + Location: criei, e está bem ali.
    @PostMapping
    public ResponseEntity<Vaga> criar(@RequestBody Vaga nova) {
        Vaga salva = service.criar(nova);
        URI onde = URI.create("/vagas/" + salva.id());
        return ResponseEntity.created(onde).body(salva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vaga> trocar(@PathVariable String id,
                                       @RequestBody Vaga nova) {
        return service.trocar(id, nova)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // ResponseEntity<Void>: não há corpo nenhum para tipar.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable String id) {
        return service.apagar(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}
