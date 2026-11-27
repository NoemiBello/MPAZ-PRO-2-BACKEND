package cl.mpazpro2.backend.repository;

import cl.mpazpro2.backend.model.Contenido;
import cl.mpazpro2.backend.model.EstadoContenido;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testrepodb;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ContenidoRepositoryTest {

    @Autowired
    private ContenidoRepository contenidoRepository;

    @Test
    void guardarYConsultarContenido_persisteTodosLosCamposEnH2() {
        Contenido nuevo = new Contenido();
        nuevo.setAsignatura("Lenguaje");
        nuevo.setUnidad("Unidad 2");
        nuevo.setObjetivo("OA 8 - Desarrollar el gusto por la lectura");
        nuevo.setTipo("Actividad");
        nuevo.setTitulo("Lectura de Papelucho");
        nuevo.setContenido("Lee en voz alta el primer parrafo.");
        nuevo.setPregunta("Quien es el personaje principal?");
        nuevo.setImagenUri("content://galeria/libro.png");
        nuevo.setEstado(EstadoContenido.APROBADO);

        Contenido guardado = contenidoRepository.save(nuevo);

        assertThat(guardado.getId()).isNotNull();

        Optional<Contenido> recuperado = contenidoRepository.findById(guardado.getId());
        assertThat(recuperado).isPresent();
        assertThat(recuperado.get().getTitulo()).isEqualTo("Lectura de Papelucho");
        assertThat(recuperado.get().getEstado()).isEqualTo(EstadoContenido.APROBADO);
        assertThat(recuperado.get().getImagenUri()).isEqualTo("content://galeria/libro.png");
    }

    @Test
    void eliminarContenido_borraRegistroDeLaBaseDeDatos() {
        Contenido nuevo = new Contenido();
        nuevo.setAsignatura("Matematica");
        nuevo.setUnidad("Unidad 1");
        nuevo.setObjetivo("OA 9 - Adicion y sustraccion");
        nuevo.setTipo("Evaluacion");
        nuevo.setTitulo("Sumas hasta 100");
        nuevo.setContenido("Resuelve 25 + 14.");
        nuevo.setEstado(EstadoContenido.PENDIENTE);

        Contenido guardado = contenidoRepository.save(nuevo);
        Long id = guardado.getId();

        contenidoRepository.deleteById(id);

        assertThat(contenidoRepository.findById(id)).isEmpty();
    }
}
