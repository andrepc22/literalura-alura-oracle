package literalura.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String name;
    private Integer anioNacimiento;
    private Integer anioDeceso;
    @OneToMany(mappedBy = "autores", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Libro> libros;

    public Autor() {}

    public String getName() {
        return name;
    }

    public static Autor datosDeAutor(DatosAutor datosAutor) {
        Autor autor = new Autor();
        autor.name = datosAutor.name();
        autor.anioDeceso = datosAutor.anioDeceso();
        autor.anioNacimiento = datosAutor.anioNacimiento();
        return autor;
    }

    @Override
    public String toString(){
        return String.format("""
                Autor: %s
                Fecha de nacimiento: %s
                Fecha de fallecimiento: %s
                Libros: %s
                """,
                this.name,
                this.anioNacimiento,
                this.anioDeceso,
                this.libros.stream().map(Libro::getTitulo).collect(Collectors.joining(", "))
                );
    }
}
