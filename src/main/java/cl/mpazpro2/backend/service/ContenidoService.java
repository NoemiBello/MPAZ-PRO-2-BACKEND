
package cl.mpazpro2.backend.service;

import cl.mpazpro2.backend.model.Contenido;
import cl.mpazpro2.backend.repository.ContenidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ContenidoService {

    private final ContenidoRepository repository;

    public ContenidoService(ContenidoRepository repository) {
        this.repository = repository;
    }

    // GET: Listar todos los contenidos
    public List<Contenido> listar() {
        return repository.findAll();
    }

    // GET: Buscar contenido por ID
    public Contenido buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Contenido no encontrado"
                ));
    }

    // POST: Crear contenido
    public Contenido crear(Contenido contenido) {
        contenido.setId(null);
        return repository.save(contenido);
    }

    // PUT: Actualizar contenido
    public Contenido actualizar(Long id, Contenido datos) {

        Contenido contenido = buscarPorId(id);

        contenido.setAsignatura(datos.getAsignatura());
        contenido.setUnidad(datos.getUnidad());
        contenido.setObjetivo(datos.getObjetivo());
        contenido.setTipo(datos.getTipo());
        contenido.setTitulo(datos.getTitulo());
        contenido.setContenido(datos.getContenido());
        contenido.setPregunta(datos.getPregunta());

        return repository.save(contenido);
    }

    // DELETE: Eliminar contenido
    public void eliminar(Long id) {
        Contenido contenido = buscarPorId(id);
        repository.delete(contenido);
    }
}
