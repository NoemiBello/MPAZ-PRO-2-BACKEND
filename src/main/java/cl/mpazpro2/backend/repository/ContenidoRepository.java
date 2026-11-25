
package cl.mpazpro2.backend.repository;

import cl.mpazpro2.backend.model.Contenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContenidoRepository extends JpaRepository<Contenido, Long> {
}
