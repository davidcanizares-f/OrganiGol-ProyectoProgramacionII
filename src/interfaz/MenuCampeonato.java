package interfaz;

import modelo.Alineacion;
import modelo.Campeonato;
import modelo.Categoria;
import modelo.Equipo;
import modelo.EstadoJugador;
import modelo.EstadoPartido;
import modelo.Jugador;
import modelo.Partido;
import negocio.Validador;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Menú del módulo 2: campeonato y preparación del partido (RF03, RF04 y RF05).
 * Uso: new MenuCampeonato().mostrar(campeonato, scanner);
 *
 * Toda la validación vive en el modelo (Campeonato, Partido, Alineacion):
 * este menú solo pide datos, llama al modelo y muestra el texto que devuelve.
 */
public class MenuCampeonato {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private static final String CAMPOS_INCOMPLETOS = "Error: campos obligatorios incompletos.";
    private static final String SIN_PARTIDO = "Error: debe seleccionar un partido.";
    private static final String SIN_PARTIDOS = "El campeonato no tiene partidos programados.";
    private static final String SELECCION_INVALIDA = "Error: selección inválida.";
    private static final String EQUIPOS_NO_INSCRITOS = "Error: ambos equipos deben estar inscritos en el campeonato.";

    public void mostrar(Campeonato campeonato, Scanner sc) {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== CAMPEONATO Y PREPARACIÓN ===");
            System.out.println("1. Registrar campeonato");
            System.out.println("2. Consultar campeonato");
            System.out.println("3. Modificar campeonato");
            System.out.println("4. Crear partido");
            System.out.println("5. Consultar partidos");
            System.out.println("6. Registrar alineación");
            System.out.println("7. Consultar alineaciones");
            System.out.println("0. Volver");
            opcion = numero(pedir(sc, "Opción: "));

            System.out.println();

            switch (opcion) {
                case 1:
                    registrarCampeonato(campeonato, sc);
                    break;
                case 2:
                    consultarCampeonato(campeonato);
                    break;
                case 3:
                    modificarCampeonato(campeonato, sc);
                    break;
                case 4:
                    crearPartido(campeonato, sc);
                    break;
                case 5:
                    consultarPartidos(campeonato, sc);
                    break;
                case 6:
                    registrarAlineacion(campeonato, sc);
                    break;
                case 7:
                    consultarAlineaciones(campeonato, sc);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

            // Si ya no hay más entrada (por ejemplo, una prueba con texto fijo), se sale
            if (opcion != 0 && !sc.hasNextLine()) {
                opcion = 0;
            }
        } while (opcion != 0);
    }

    // ======================================================================================
    // RF03: campeonato
    // ======================================================================================

    private void registrarCampeonato(Campeonato campeonato, Scanner sc) {
        String nombre = pedir(sc, "Nombre del campeonato (3 a 50 caracteres): ");
        listarCategorias();
        String textoCategoria = pedir(sc, "Categoría (número): ");

        if (nombre.isEmpty() || textoCategoria.isEmpty()) {
            System.out.println(CAMPOS_INCOMPLETOS);
            return;
        }

        Categoria categoria = categoriaDe(textoCategoria);
        String error = campeonato.validarConfiguracion(nombre, categoria);

        if (error != null) {
            System.out.println(error);
            return;
        }

        campeonato.configurar(nombre, categoria);
        System.out.println("Campeonato '" + campeonato.getNombre()
                + "' configurado correctamente para la categoría " + campeonato.getCategoria() + ".");
    }

    private void consultarCampeonato(Campeonato campeonato) {
        if (!campeonato.estaConfigurado()) {
            System.out.println("Todavía no se ha configurado el campeonato.");
            return;
        }

        System.out.println("Nombre: " + campeonato.getNombre());
        System.out.println("Categoría: " + campeonato.getCategoria());
        System.out.println("Equipos inscritos: " + campeonato.getEquipos().size());
        System.out.println("Partidos cerrados: " + campeonato.partidosCerrados().size()
                + " de " + campeonato.getPartidos().size());

        if (campeonato.estaListo()) {
            System.out.println("Campeonato listo para crear partidos.");
        } else {
            System.out.println("Campeonato en configuración: faltan " + campeonato.equiposFaltantes()
                    + " equipo(s) para poder crear partidos.");
        }
    }

    private void modificarCampeonato(Campeonato campeonato, Scanner sc) {
        if (!campeonato.estaConfigurado()) {
            // validarModificacion devuelve primero el error de "primero debe configurar"
            System.out.println(campeonato.validarModificacion(null, null));
            return;
        }

        System.out.println("Actual: '" + campeonato.getNombre() + "', categoría " + campeonato.getCategoria());
        String nombre = pedir(sc, "Nombre del campeonato (3 a 50 caracteres): ");
        listarCategorias();
        String textoCategoria = pedir(sc, "Categoría (número): ");

        if (nombre.isEmpty() || textoCategoria.isEmpty()) {
            System.out.println(CAMPOS_INCOMPLETOS);
            return;
        }

        Categoria categoria = categoriaDe(textoCategoria);
        String error = campeonato.validarModificacion(nombre, categoria);

        if (error != null) {
            System.out.println(error);
            return;
        }

        campeonato.modificar(nombre, categoria);
        System.out.println("Campeonato actualizado correctamente.");
    }

    // ======================================================================================
    // RF04: partidos
    // ======================================================================================

    private void crearPartido(Campeonato campeonato, Scanner sc) {
        // Primero los errores de estado del campeonato (sin configurar / menos de 4 equipos)
        String error = campeonato.validarListoParaPartidos();

        if (error != null) {
            System.out.println(error);
            return;
        }

        listarEquipos(campeonato);
        String textoLocal = pedir(sc, "Equipo local (número): ");
        String textoVisitante = pedir(sc, "Equipo visitante (número): ");
        String textoFecha = pedir(sc, "Fecha (dd/mm/aaaa): ");
        String textoHora = pedir(sc, "Hora (HH:mm): ");
        String cancha = pedir(sc, "Cancha (3 a 40 caracteres): ");

        if (textoLocal.isEmpty() || textoVisitante.isEmpty() || textoFecha.isEmpty()
                || textoHora.isEmpty() || cancha.isEmpty()) {
            System.out.println(CAMPOS_INCOMPLETOS);
            return;
        }

        Equipo local = equipoDe(campeonato, textoLocal);
        Equipo visitante = equipoDe(campeonato, textoVisitante);

        if (local == null || visitante == null) {
            System.out.println(EQUIPOS_NO_INSCRITOS);
            return;
        }

        if (!Validador.fechaValida(textoFecha)) {
            System.out.println("Error: la fecha debe tener el formato dd/mm/aaaa y ser válida.");
            return;
        }

        if (!Validador.horaValida(textoHora)) {
            System.out.println("Error: la hora debe tener el formato HH:mm.");
            return;
        }

        LocalDate fecha = LocalDate.parse(textoFecha, FORMATO_FECHA);
        LocalTime hora = LocalTime.parse(textoHora, FORMATO_HORA);

        error = campeonato.validarCrearPartido(local, visitante, fecha, hora, cancha);

        if (error != null) {
            System.out.println(error);
            return;
        }

        Partido partido = campeonato.crearPartido(local, visitante, fecha, hora, cancha);

        System.out.println("Partido creado: " + partido.getLocal().getNombre()
                + " vs " + partido.getVisitante().getNombre()
                + ", el " + partido.getFecha().format(FORMATO_FECHA)
                + " a las " + partido.getHora().format(FORMATO_HORA)
                + " en " + partido.getCancha() + ". Estado: programado.");
    }

    private void consultarPartidos(Campeonato campeonato, Scanner sc) {
        if (campeonato.getPartidos().isEmpty()) {
            System.out.println(SIN_PARTIDOS);
            return;
        }

        listarEquipos(campeonato);
        String textoEquipo = pedir(sc, "Equipo (número, Enter = todos): ");
        System.out.println("Estados: 1. programado  2. en registro  3. finalizado  4. cerrado  5. suspendido");
        String textoEstado = pedir(sc, "Estado (número, Enter = todos): ");

        Equipo equipo = null;
        EstadoPartido estado = null;

        if (!textoEquipo.isEmpty()) {
            equipo = equipoDe(campeonato, textoEquipo);

            if (equipo == null) {
                System.out.println(SELECCION_INVALIDA);
                return;
            }
        }

        if (!textoEstado.isEmpty()) {
            int n = numero(textoEstado);

            if (n < 1 || n > EstadoPartido.values().length) {
                System.out.println(SELECCION_INVALIDA);
                return;
            }

            estado = EstadoPartido.values()[n - 1];
        }

        List<Partido> partidos = campeonato.filtrarPartidos(equipo, estado);

        if (partidos.isEmpty()) {
            System.out.println("No se encontraron partidos con los filtros seleccionados.");
            return;
        }

        System.out.println();
        for (int i = 0; i < partidos.size(); i++) {
            System.out.println((i + 1) + ". " + describirPartido(partidos.get(i)));
        }
    }

    // ======================================================================================
    // RF05: alineaciones
    // ======================================================================================

    private void registrarAlineacion(Campeonato campeonato, Scanner sc) {
        Partido partido = elegirPartido(campeonato, sc);

        if (partido == null) {
            return;
        }

        System.out.println("Equipo que presenta la alineación:");
        System.out.println("1. " + partido.getLocal().getNombre() + " (local)");
        System.out.println("2. " + partido.getVisitante().getNombre() + " (visitante)");
        String textoEquipo = pedir(sc, "Equipo (número): ");

        if (textoEquipo.isEmpty()) {
            System.out.println(CAMPOS_INCOMPLETOS);
            return;
        }

        int n = numero(textoEquipo);
        Equipo equipo = null;

        if (n == 1) {
            equipo = partido.getLocal();
        } else if (n == 2) {
            equipo = partido.getVisitante();
        }

        if (equipo == null) {
            System.out.println("Error: el equipo seleccionado no juega en este partido.");
            return;
        }

        List<Jugador> plantel = equipo.getJugadores();
        listarJugadores(equipo);

        String textoTitulares = pedir(sc, "Titulares (números separados por coma, 7 a 11): ");
        String textoSuplentes = pedir(sc, "Suplentes (números separados por coma, 0 a 7; Enter = ninguno): ");
        String textoCapitan = pedir(sc, "Capitán (número): ");

        if (textoTitulares.isEmpty() || textoCapitan.isEmpty()) {
            System.out.println(CAMPOS_INCOMPLETOS);
            return;
        }

        List<Jugador> titulares = jugadoresDe(plantel, textoTitulares);
        List<Jugador> suplentes = jugadoresDe(plantel, textoSuplentes);

        if (titulares == null || suplentes == null) {
            System.out.println(SELECCION_INVALIDA);
            return;
        }

        // Si el número del capitán no existe queda null y el modelo informa los campos incompletos
        Jugador capitan = jugadorDe(plantel, textoCapitan);

        Alineacion alineacion = new Alineacion(equipo, titulares, suplentes, capitan);
        String error = partido.validarAlineacion(alineacion);

        if (error != null) {
            System.out.println(error);
            return;
        }

        boolean reemplazada = partido.registrarAlineacion(alineacion);

        System.out.println("Alineación de '" + equipo.getNombre() + "' aprobada: "
                + titulares.size() + " titulares, " + suplentes.size()
                + " suplentes. Capitán: " + nombreCompleto(capitan) + ".");

        if (reemplazada) {
            System.out.println("La alineación anterior de '" + equipo.getNombre() + "' fue reemplazada.");
        }
    }

    private void consultarAlineaciones(Campeonato campeonato, Scanner sc) {
        Partido partido = elegirPartido(campeonato, sc);

        if (partido == null) {
            return;
        }

        Alineacion local = partido.getAlineacion(partido.getLocal());
        Alineacion visitante = partido.getAlineacion(partido.getVisitante());

        if (local == null && visitante == null) {
            System.out.println("Todavía no se han registrado alineaciones para este partido.");
            return;
        }

        imprimirAlineacion(partido.getLocal(), local);
        imprimirAlineacion(partido.getVisitante(), visitante);
    }

    private void imprimirAlineacion(Equipo equipo, Alineacion alineacion) {
        System.out.println();

        if (alineacion == null) {
            System.out.println(equipo.getNombre() + ": sin alineación registrada.");
            return;
        }

        System.out.println("Alineación de '" + equipo.getNombre() + "' (aprobada)");
        System.out.println("Titulares:");
        for (Jugador j : alineacion.getTitulares()) {
            System.out.println("  " + describirJugador(j));
        }

        System.out.println("Suplentes:");
        if (alineacion.getSuplentes().isEmpty()) {
            System.out.println("  (ninguno)");
        }
        for (Jugador j : alineacion.getSuplentes()) {
            System.out.println("  " + describirJugador(j));
        }

        System.out.println("Capitán: " + nombreCompleto(alineacion.getCapitan()));
    }

    // ======================================================================================
    // Apoyo
    // ======================================================================================

    // Muestra los partidos ordenados y devuelve el elegido (null, con su mensaje, si no hay o es inválido)
    private Partido elegirPartido(Campeonato campeonato, Scanner sc) {
        List<Partido> partidos = campeonato.filtrarPartidos(null, null);

        if (partidos.isEmpty()) {
            System.out.println(SIN_PARTIDOS);
            return null;
        }

        for (int i = 0; i < partidos.size(); i++) {
            System.out.println((i + 1) + ". " + describirPartido(partidos.get(i)));
        }

        int n = numero(pedir(sc, "Partido (número): "));

        if (n < 1 || n > partidos.size()) {
            System.out.println(SIN_PARTIDO);
            return null;
        }

        return partidos.get(n - 1);
    }

    private String describirPartido(Partido p) {
        String texto = p.getLocal().getNombre() + " vs " + p.getVisitante().getNombre()
                + " | " + p.getFecha().format(FORMATO_FECHA)
                + " " + p.getHora().format(FORMATO_HORA)
                + " | " + p.getCancha()
                + " | " + p.getEstado().name().toLowerCase().replace('_', ' ');

        // El marcador solo se muestra cuando el partido ya tiene el registro abierto
        if (p.getEstado() != EstadoPartido.PROGRAMADO) {
            texto += " | Marcador: " + p.golesDe(p.getLocal()) + " - " + p.golesDe(p.getVisitante());
        }

        return texto;
    }

    private String describirJugador(Jugador j) {
        return "[" + j.getCarne() + "] #" + j.getNumeroCamiseta() + " " + nombreCompleto(j);
    }

    private String nombreCompleto(Jugador j) {
        return j == null ? "-" : j.getNombres() + " " + j.getApellidos();
    }

    private void listarCategorias() {
        Categoria[] categorias = Categoria.values();

        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i + 1) + ". " + categorias[i]);
        }
    }

    private void listarEquipos(Campeonato campeonato) {
        List<Equipo> equipos = campeonato.getEquipos();

        for (int i = 0; i < equipos.size(); i++) {
            System.out.println((i + 1) + ". " + equipos.get(i).getNombre());
        }
    }

    private void listarJugadores(Equipo equipo) {
        List<Jugador> plantel = equipo.getJugadores();

        System.out.println("Jugadores de '" + equipo.getNombre() + "':");
        for (int i = 0; i < plantel.size(); i++) {
            Jugador j = plantel.get(i);
            String marca = "";

            if (j.getEstado() == EstadoJugador.SUSPENDIDO) {
                marca = " [suspendido]";
            } else if (j.getEstado() == EstadoJugador.DE_BAJA) {
                marca = " [de baja]";
            }

            System.out.println((i + 1) + ". " + describirJugador(j) + marca);
        }
    }

    private Categoria categoriaDe(String texto) {
        Categoria[] categorias = Categoria.values();
        int n = numero(texto);

        if (n >= 1 && n <= categorias.length) {
            return categorias[n - 1];
        }

        return null;
    }

    private Equipo equipoDe(Campeonato campeonato, String texto) {
        int n = numero(texto);
        List<Equipo> equipos = campeonato.getEquipos();

        if (n >= 1 && n <= equipos.size()) {
            return equipos.get(n - 1);
        }

        return null;
    }

    private Jugador jugadorDe(List<Jugador> plantel, String texto) {
        int n = numero(texto);

        if (n >= 1 && n <= plantel.size()) {
            return plantel.get(n - 1);
        }

        return null;
    }

    // Convierte "1, 3, 5" en la lista de jugadores; null si algún número no existe.
    // Un número repetido se conserva, para que Alineacion.validar lo informe como repetido.
    private List<Jugador> jugadoresDe(List<Jugador> plantel, String texto) {
        List<Jugador> lista = new ArrayList<>();

        if (texto.isEmpty()) {
            return lista;
        }

        for (String parte : texto.split(",")) {
            Jugador jugador = jugadorDe(plantel, parte.trim());

            if (jugador == null) {
                return null;
            }

            lista.add(jugador);
        }

        return lista;
    }

    // Número entero, o -1 si el texto está vacío o no es un número
    private int numero(String texto) {
        return Validador.aEntero(texto);
    }

    private String pedir(Scanner sc, String mensaje) {
        System.out.print(mensaje);

        if (!sc.hasNextLine()) {
            return "";
        }

        return sc.nextLine().trim();
    }
}