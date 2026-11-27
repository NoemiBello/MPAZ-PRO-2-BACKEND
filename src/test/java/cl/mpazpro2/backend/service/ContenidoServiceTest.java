package cl.mpazpro2.backend.service;

import cl.mpazpro2.backend.model.Contenido;
import cl.mpazpro2.backend.model.EstadoContenido;
import cl.mpazpro2.backend.repository.ContenidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContenidoServiceTest {

    @Mock
    private ContenidoRepository repository;

    @InjectMocks
    private ContenidoService service;

    private Contenido crearContenidoEjemplo(Long id, String titulo) {
        Contenido c = new Contenido();
        c.setId(id);
        c.setAsignatura("Lenguaje");
        c.setUnidad("Unidad 1");
        c.setObjetivo("OA 5 - Comprension lectora");
        c.setTipo("Lectura");
        c.setTitulo(titulo);
        c.setContenido("Lee el primer capitulo de Papelucho.");
        c.setPregunta("Que fue lo mas divertido que le paso?");
        c.setImagenUri("content://galeria/papelucho.jpg");
        c.setEstado(EstadoContenido.PENDIENTE);
        return c;
    }

    @Test
    void listar_retornaTodosLosContenidos() {
        List<Contenido> lista = List.of(
                crearContenidoEjemplo(1L, "Lectura 1"),
                crearContenidoEjemplo(2L, "Lectura 2")
        );
        when(repository.findAll()).thenReturn(lista);

        List<Contenido> resultado = service.listar();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Lectura 1");
        verify(repository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_retornaContenidoCompleto() {
        Contenido esperado = crearContenidoEjemplo(1L, "Papelucho");
        esperado.setEstado(EstadoContenido.APROBADO);
        when(repository.findById(1L)).thenReturn(Optional.of(esperado));

        Contenido resultado = service.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getTitulo()).isEqualTo("Papelucho");
        assertThat(resultado.getPregunta()).isNotBlank();
        assertThat(resultado.getImagenUri()).isEqualTo("content://galeria/papelucho.jpg");
        assertThat(resultado.getEstado()).isEqualTo(EstadoContenido.APROBADO);
    }

    @Test
    void buscarPorId_cuandoNoExiste_lanzaNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException rse = (ResponseStatusException) ex;
                    assertThat(rse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                });
    }

    @Test
    void crear_limpiaIdYGuardaContenidoConEstadoInicial() {
        Contenido entrada = crearContenidoEjemplo(999L, "Nuevo contenido");
        when(repository.save(any(Contenido.class))).thenAnswer(inv -> {
            Contenido guardado = inv.getArgument(0);
            assertThat(guardado.getId()).isNull();
            guardado.setId(10L);
            return guardado;
        });

        Contenido creado = service.crear(entrada);

        assertThat(creado.getId()).isEqualTo(10L);
        assertThat(creado.getTitulo()).isEqualTo("Nuevo contenido");
        assertThat(creado.getEstado()).isEqualTo(EstadoContenido.PENDIENTE);
    }

    @Test
    void actualizar_actualizaTodosLosCamposIncluyendoPreguntaImagenYEstado() {
        Contenido existente = crearContenidoEjemplo(5L, "Titulo original");
        Contenido nuevosDatos = crearContenidoEjemplo(null, "Titulo actualizado");
        nuevosDatos.setAsignatura("Matematica");
        nuevosDatos.setPregunta("Cuanto es 15 + 10?");
        nuevosDatos.setImagenUri("content://galeria/nueva.png");
        nuevosDatos.setEstado(EstadoContenido.APROBADO);

        when(repository.findById(5L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Contenido.class))).thenAnswer(inv -> inv.getArgument(0));

        Contenido actualizado = service.actualizar(5L, nuevosDatos);

        assertThat(actualizado.getId()).isEqualTo(5L);
        assertThat(actualizado.getTitulo()).isEqualTo("Titulo actualizado");
        assertThat(actualizado.getAsignatura()).isEqualTo("Matematica");
        assertThat(actualizado.getPregunta()).isEqualTo("Cuanto es 15 + 10?");
        assertThat(actualizado.getImagenUri()).isEqualTo("content://galeria/nueva.png");
        assertThat(actualizado.getEstado()).isEqualTo(EstadoContenido.APROBADO);
    }

    @Test
    void eliminar_cuandoExiste_eliminaRegistro() {
        Contenido existente = crearContenidoEjemplo(3L, "Para eliminar");
        when(repository.findById(3L)).thenReturn(Optional.of(existente));

        service.eliminar(3L);

        verify(repository).delete(existente);
    }

    @Test
    void eliminar_cuandoNoExiste_lanzaNotFound() {
        when(repository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.eliminar(404L))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException rse = (ResponseStatusException) ex;
                    assertThat(rse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                });
    }
}
