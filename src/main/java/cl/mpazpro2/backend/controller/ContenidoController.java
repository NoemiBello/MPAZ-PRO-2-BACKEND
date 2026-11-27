
package cl.mpazpro2.backend.controller;

import cl.mpazpro2.backend.model.Contenido;
import cl.mpazpro2.backend.service.ContenidoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/contenidos")
public class ContenidoController {

    private final ContenidoService service;

    public ContenidoController(ContenidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Contenido> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Contenido buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Contenido crear(@Valid @RequestBody Contenido contenido) {
        return service.crear(contenido);
    }

    @PutMapping("/{id}")
    public Contenido actualizar(@PathVariable Long id,  @Valid @RequestBody Contenido contenido) {
        return service.actualizar(id, contenido);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
