package literalura.principal;

import literalura.model.Autor;
import literalura.model.DatosLibro;
import literalura.model.Libro;
import literalura.model.RespuestaLibros;
import literalura.repository.AutoresRepository;
import literalura.repository.LibrosRepository;
import literalura.service.ConsumoAPI;
import literalura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibrosRepository repositoryLibro;
    private AutoresRepository repositoryAutor;
    private Optional<Libro> libroBuscado;
    private Optional<Autor> autorBuscado;
    private List<Libro> libros;
    private List<Autor> autores;

    public Principal(LibrosRepository repositoryLibro, AutoresRepository repositoryAutor) {
        this.repositoryLibro = repositoryLibro;
        this.repositoryAutor = repositoryAutor;
    }

    public void showMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    
                    1 - Buscar libro por título 
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    
                    0 - Salir
                    
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibroRegistrado();
                    break;
                case 3:
                    listarAutorRegistrado();
                    break;
                case 4:
                    listarAutorVivoAnio();
                    break;
                case 5:
                    listarLibroIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibro() {
        System.out.println("Escribe el título del libro que deseas buscar:");
        var nombreLibro = teclado.nextLine();
        libroBuscado = repositoryLibro.findByTituloContainsIgnoreCase(nombreLibro);
        autorBuscado = repositoryAutor.findByNameContainsIgnoreCase(nombreLibro);
        if (libroBuscado.isEmpty() && autorBuscado.isEmpty()) {
            RespuestaLibros librosRes = getDatosLibro(nombreLibro);
            if (librosRes.count() > 0) {
                librosRes.results().stream().findFirst().ifPresent(DatosLibro::showLibro);
                DatosLibro lib = librosRes.results().stream().findFirst().get();
                Libro nuevoLib = Libro.datosDeLibro(lib);
                repositoryAutor.save(nuevoLib.getAutores());
                repositoryLibro.save(nuevoLib);
            } else {
                System.out.println("No se encontró el libro");
            }
        } else {
            if (!autorBuscado.isEmpty()) {
                libroBuscado = repositoryLibro.findByAutores_id(autorBuscado.get().getId());
            }
            System.out.println(libroBuscado.get());
        }
    }

    private RespuestaLibros getDatosLibro(String nombreLibro) {
        String urlbase = "https://gutendex.com/books/";
        RespuestaLibros libroRes = null;
        if (nombreLibro != null) {
            var json = consumoApi.obtenerDatos(urlbase + "?search=" + nombreLibro.replace(" ", "%20"));
            libroRes = conversor.obtenerDatos(json, RespuestaLibros.class);
        }
        return libroRes;
    }

    private void listarLibroRegistrado() {
        libros = repositoryLibro.findAll();
        libros.stream().forEach(System.out::println);
    }

    private void listarAutorRegistrado() {
        autores = repositoryAutor.findAllConLibros();
        autores.stream().forEach(System.out::println);
    }

    private void listarAutorVivoAnio() {
        System.out.println("Escribe el año que deseas:");
        var anio = teclado.nextInt();
        autores = repositoryAutor.findByAutorVivo(anio);
        autores.stream().forEach(System.out::println);
    }

    private void listarLibroIdioma() {
        System.out.println("Escribe el idioma que deseas:");
        var idioma = teclado.nextLine();
        libros = repositoryLibro.findByIdiomas(idioma);
        libros.stream().forEach(System.out::println);
    }

}
