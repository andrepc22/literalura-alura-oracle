package literalura.repository;

import literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutoresRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNameContainsIgnoreCase(String nombreAutor);

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros")
    List<Autor> findAllConLibros();

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros WHERE (a.anioNacimiento <= :anio AND a.anioDeceso > :anio)")
    List<Autor> findByAutorVivo(int anio);
}
