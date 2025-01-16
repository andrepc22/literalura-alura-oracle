package literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        Integer id,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutor> autores,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Integer descargas
) {
    public void showLibro() {
        String autores = this.autores.stream()
                .map(DatosAutor::name)
                .reduce((a, b) -> a + ", " + b)
                .orElse("N/A");
        String idiomas = String.join(", ", this.idiomas);

        String showlibro = String.format("""
                        
                        ----- LIBRO -----
                        Título: %s
                        Autor: %s
                        Idioma: %s
                        Descargas: %s
                        -----------------
                        """,
                this.titulo,
                autores,
                idiomas,
                this.descargas);
        System.out.println(showlibro);
    }
}