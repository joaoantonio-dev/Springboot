package br.edu.faculdade.vagas;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService servico;

    public EmpresaController(EmpresaService servico) {
        this.servico = servico;
    }

    @GetMapping
    public List<Empresa> listar() {
        return servico.listar();
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Empresa> porSlug(@PathVariable String slug) {
        return servico.buscarPorSlug(slug)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Empresa> criar(@Valid @RequestBody EmpresaEntrada entrada) {
        Empresa salva = servico.criar(entrada);
        URI onde = URI.create("/empresas/" + salva.slug());
        return ResponseEntity.created(onde).body(salva);
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> apagar(@PathVariable String slug) {
        return servico.apagar(slug)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}

public ResponseEntity<Empresa> criar(@RequestBody Empresa nova) {
    Empresa salva = service.criar(nova);

    // Na parte 1 o Location aponta para um id que a API inventou.
    // Aqui aponta para o slug que O CLIENTE escolheu. Mesmo 201,
    // responsabilidade sobre o endereço em lados opostos.
    URI onde = URI.create("/empresas/" + salva.slug());
    return ResponseEntity.created(onde).body(salva);
}
