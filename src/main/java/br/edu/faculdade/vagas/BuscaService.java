@Service
public class BuscaService {

    // O repository da parte 1, não o service dela — igual à aula 02.
    private final VagaRepository repository;

    public BuscaService(VagaRepository repository) {
        this.repository = repository;
    }

    // Boolean, e não boolean: só o objeto pode ser null, e null é
    // exatamente como "o filtro não foi enviado" chega aqui.
    public List<Vaga> buscar(String area, String senioridade,
                             Boolean aceitaIniciante) {
        return repository.todas().stream()
            .filter(v -> area == null
                      || v.area().equalsIgnoreCase(area))
            .filter(v -> senioridade == null
                      || v.senioridade().equalsIgnoreCase(senioridade))
            .filter(v -> aceitaIniciante == null
                      || v.aceitaIniciante() == aceitaIniciante)
            .toList();
    }
}
