package literalura.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne
    private Autor autores;
    private String idiomas;
    private Integer descargas;

    public Libro() {}

    public static Libro datosDeLibro(DatosLibro datosLibro) {
        Libro libro = new Libro();
        libro.id = datosLibro.id().longValue();
        libro.titulo = datosLibro.titulo();
        libro.descargas = datosLibro.descargas();
        if (!datosLibro.autores().isEmpty())
            libro.autores = Autor.datosDeAutor(datosLibro.autores().get(0));
        if (!datosLibro.idiomas().isEmpty())
            libro.idiomas = datosLibro.idiomas().get(0);
        return libro;
    }

    @Override
    public String toString() {
        return String.format("""
                        
                        ----- LIBRO -----
                        Título: %s
                        Autor: %s
                        Idioma: %s
                        Descargas: %s
                        -----------------
                        """,
                this.titulo,
                this.autores.getName(),
                this.idiomas,
                this.descargas);
    }
}
