package interfaz;

import modelo.Campeonato;
import modelo.Equipo;
import modelo.EstadoJugador;
import modelo.Jugador;
import negocio.Validador;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Menú del módulo 1: gestión de jugadores (RF02).
 * Uso: new MenuJugadores().mostrar(campeonato, scanner);
 *
 * Toda la validación vive en el modelo (Campeonato, Jugador, Validador):
 * este menú solo pide datos, llama al modelo y muestra el texto que devuelve.
 */
public class MenuJugadores {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu");

    private static final String CAMPOS_INCOMPLETOS = "Error: campos obligatorios incompletos.";
    private static final String SELECCION_INVALIDA = "Error: selección inválida.";
    private static final String SIN_EQUIPOS = "No hay equipos registrados.";
    private static final String SIN_EQUIPO = "Error: debe seleccionar un equipo.";
    private static final String SIN_JUGADOR = "Error: debe seleccionar un jugador.";
    private static final String FECHA_FORMATO = "Error: la fecha de nacimiento debe tener el formato dd/MM/aaaa.";

    public void mostrar(Campeonato campeonato, Scanner sc) {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== JUGADORES ===");
            System.out.println("1. Registrar jugador");
            System.out.println("2. Consultar jugadores");
            System.out.println("3. Modificar jugador");
            System.out.println("4. Dar de baja jugador");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            // Si ya no hay más entrada (fin de archivo), se sale del menú
            opcion = sc.hasNextLine() ? numero(sc.nextLine().trim()) : 0;

            System.out.println();

            switch (opcion) {
                case 1:
                    registrarJugador(campeonato, sc);
                    break;
                case 2:
                    consultarJugadores(campeonato, sc);
                    break;
                case 3:
                    modificarJugador(campeonato, sc);
                    break;
                case 4:
                    darDeBajaJugador(campeonato, sc);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    // ======================================================================================
    // RF02: jugadores
    // ======================================================================================

    private void registrarJugador(Campeonato campeonato, Scanner sc) {
        if (campeonato.getEquipos().isEmpty()) {
            System.out.println(SIN_EQUIPOS);
            return;
        }

        listarEquipos(campeonato);
        Equipo equipo = equipoDe(campeonato, pedir(sc, "Equipo del jugador (número): "));

        String nombres = pedir(sc, "Nombres: ");
        String apellidos = pedir(sc, "Apellidos: ");
        String cedula = pedir(sc, "Cédula (10 dígitos): ");
        String textoFecha = pedir(sc, "Fecha de nacimiento (dd/MM/aaaa): ");
        String textoCamiseta = pedir(sc, "Número de camiseta (1 a 99): ");

        if (textoCamiseta.isEmpty()) {
            System.out.println(CAMPOS_INCOMPLETOS);
            return;
        }

        if (fechaConFormatoInvalido(textoFecha)) {
            System.out.println(FECHA_FORMATO);
            return;
        }

        LocalDate fecha = fechaDe(textoFecha);
        int camiseta = numero(textoCamiseta);

        String error = campeonato.validarJugador(equipo, nombres, apellidos, cedula, fecha, camiseta);

        if (error != null) {
            System.out.println(error);
            return;
        }

        Jugador jugador = campeonato.inscribirJugador(equipo, nombres, apellidos, cedula, fecha, camiseta);

        System.out.println("Jugador '" + nombreCompleto(jugador) + "' inscrito en '" + equipo.getNombre()
                + "'. Carné asignado: " + jugador.getCarne() + ".");
    }

    private void consultarJugadores(Campeonato campeonato, Scanner sc) {
        if (campeonato.getEquipos().isEmpty()) {
            System.out.println(SIN_EQUIPOS);
            return;
        }

        listarEquipos(campeonato);
        String textoEquipo = pedir(sc, "Equipo (número, Enter para todos): ");
        Equipo equipo = null;

        if (!textoEquipo.isEmpty()) {
            equipo = equipoDe(campeonato, textoEquipo);

            if (equipo == null) {
                System.out.println(SELECCION_INVALIDA);
                return;
            }
        }

        System.out.println("Estado: 1. Activo  2. Suspendido  3. De baja");
        String textoEstado = pedir(sc, "Estado (número, Enter para todos): ");
        EstadoJugador estado = null;

        if (!textoEstado.isEmpty()) {
            estado = estadoDe(textoEstado);

            if (estado == null) {
                System.out.println(SELECCION_INVALIDA);
                return;
            }
        }

        String texto = pedir(sc, "Buscar por apellido o cédula (Enter para ver todos): ");

        // Jugadores del alcance elegido (un equipo o todos)
        List<Jugador> base = new ArrayList<>();
        for (Equipo e : campeonato.getEquipos()) {
            if (equipo == null || e == equipo) {
                base.addAll(e.getJugadores());
            }
        }

        if (base.isEmpty()) {
            System.out.println(equipo != null
                    ? "El equipo no tiene jugadores registrados."
                    : "No hay jugadores registrados.");
            return;
        }

        // Coincidencias de la búsqueda (nombre, apellido o cédula)
        List<Jugador> coincidencias = new ArrayList<>(campeonato.buscarJugador(texto));
        for (Jugador j : campeonato.buscarJugadoresCedula(texto)) {
            if (!coincidencias.contains(j)) {
                coincidencias.add(j);
            }
        }

        List<Jugador> encontrados = new ArrayList<>();
        for (Jugador j : base) {
            boolean coincideEstado = (estado == null || j.getEstado() == estado);

            if (coincideEstado && coincidencias.contains(j)) {
                encontrados.add(j);
            }
        }

        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron jugadores que coincidan con la búsqueda.");
            return;
        }

        System.out.println("Jugadores (" + encontrados.size() + "):");
        for (Jugador j : encontrados) {
            System.out.println("- [" + j.getCarne() + "] " + nombreCompleto(j)
                    + " | Cédula: " + j.getCedula()
                    + " | Edad: " + j.calcularEdad()
                    + " | Camiseta: " + j.getNumeroCamiseta()
                    + " | Equipo: " + j.getEquipo().getNombre()
                    + " | Estado: " + j.getEstado()
                    + " | Amarillas: " + j.getAmarillasAcumuladas());
        }
    }

    private void modificarJugador(Campeonato campeonato, Scanner sc) {
        Jugador jugador = elegirJugador(campeonato, sc, "Jugador a modificar (número): ");

        if (jugador == null) {
            return;
        }

        System.out.println("Datos actuales: " + nombreCompleto(jugador) + " | " + jugador.getCedula()
                + " | " + jugador.getFechaNacimiento().format(FORMATO_FECHA)
                + " | camiseta " + jugador.getNumeroCamiseta());

        String nombres = pedir(sc, "Nombres: ");
        String apellidos = pedir(sc, "Apellidos: ");
        String cedula = pedir(sc, "Cédula (10 dígitos): ");
        String textoFecha = pedir(sc, "Fecha de nacimiento (dd/MM/aaaa): ");
        String textoCamiseta = pedir(sc, "Número de camiseta (1 a 99): ");

        if (textoCamiseta.isEmpty()) {
            System.out.println(CAMPOS_INCOMPLETOS);
            return;
        }

        if (fechaConFormatoInvalido(textoFecha)) {
            System.out.println(FECHA_FORMATO);
            return;
        }

        LocalDate fecha = fechaDe(textoFecha);
        int camiseta = numero(textoCamiseta);

        String error = campeonato.validarModificacionJugador(jugador, nombres, apellidos, cedula, fecha, camiseta);

        if (error != null) {
            System.out.println(error);
            return;
        }

        campeonato.modificarJugador(jugador, nombres, apellidos, cedula, fecha, camiseta);
        System.out.println("Jugador actualizado correctamente.");
    }

    private void darDeBajaJugador(Campeonato campeonato, Scanner sc) {
        Jugador jugador = elegirJugador(campeonato, sc, "Jugador a dar de baja (número): ");

        if (jugador == null) {
            return;
        }

        String motivo = pedir(sc, "Motivo de la baja (5 a 100 caracteres): ");
        String error = jugador.validarBaja(motivo);

        if (error != null) {
            System.out.println(error);
            return;
        }

        String respuesta = pedir(sc, "¿Dar de baja a '" + nombreCompleto(jugador) + "'? (S/N): ");

        if (!esSi(respuesta)) {
            System.out.println("Baja cancelada.");
            return;
        }

        jugador.darDeBaja(motivo);
        System.out.println("Jugador '" + nombreCompleto(jugador) + "' dado de baja correctamente.");
    }

    // ======================================================================================
    // Apoyo
    // ======================================================================================

    // Pide el equipo y luego el jugador de su plantel; muestra el error y devuelve null si algo falla
    private Jugador elegirJugador(Campeonato campeonato, Scanner sc, String mensaje) {
        if (campeonato.getEquipos().isEmpty()) {
            System.out.println(SIN_EQUIPOS);
            return null;
        }

        listarEquipos(campeonato);
        Equipo equipo = equipoDe(campeonato, pedir(sc, "Equipo del jugador (número): "));

        if (equipo == null) {
            System.out.println(SIN_EQUIPO);
            return null;
        }

        if (equipo.getJugadores().isEmpty()) {
            System.out.println("El equipo no tiene jugadores registrados.");
            return null;
        }

        listarJugadores(equipo);
        Jugador jugador = jugadorDe(equipo.getJugadores(), pedir(sc, mensaje));

        if (jugador == null) {
            System.out.println(SIN_JUGADOR);
        }

        return jugador;
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

            System.out.println((i + 1) + ". [" + j.getCarne() + "] #" + j.getNumeroCamiseta()
                    + " " + nombreCompleto(j) + marca);
        }
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

    private EstadoJugador estadoDe(String texto) {
        switch (numero(texto)) {
            case 1:
                return EstadoJugador.ACTIVO;
            case 2:
                return EstadoJugador.SUSPENDIDO;
            case 3:
                return EstadoJugador.DE_BAJA;
            default:
                return null;
        }
    }

    private String nombreCompleto(Jugador j) {
        return j.getNombres() + " " + j.getApellidos();
    }

    // true si el usuario escribió algo y no es una fecha dd/MM/aaaa real
    private boolean fechaConFormatoInvalido(String texto) {
        return !texto.isEmpty() && !Validador.fechaValida(texto);
    }

    // null si el texto está vacío (el modelo lo informa como campo incompleto)
    private LocalDate fechaDe(String texto) {
        if (texto.isEmpty()) {
            return null;
        }

        return LocalDate.parse(texto, FORMATO_FECHA);
    }

    private boolean esSi(String texto) {
        return texto.equalsIgnoreCase("s")
                || texto.equalsIgnoreCase("si")
                || texto.equalsIgnoreCase("sí");
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
