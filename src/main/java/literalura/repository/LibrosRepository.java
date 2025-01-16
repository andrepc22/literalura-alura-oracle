package literalura.repository;

import literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibrosRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloContainsIgnoreCase(String tituloLibro);
    Optional<Libro> findByAutores_id(Long id);

    List<Libro> findByIdiomas(String idioma);
}
