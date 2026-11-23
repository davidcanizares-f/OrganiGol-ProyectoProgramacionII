package interfaz;

import modelo.Campeonato;
import modelo.Equipo;
import negocio.Validador;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Menú del módulo 1: gestión de equipos (RF01).
 * Uso: new MenuEquipos().mostrar(campeonato, scanner);
 *
 * Toda la validación vive en el modelo (Campeonato, Validador):
 * este menú solo pide datos, llama al modelo y muestra el texto que devuelve.
 */
public class MenuEquipos {

    private static final String SIN_EQUIPOS = "No hay equipos registrados.";
    private static final String SIN_COINCIDENCIAS = "No se encontraron equipos que coincidan con la búsqueda.";
    private static final String SIN_SELECCION = "Error: debe seleccionar un equipo.";

    public void mostrar(Campeonato campeonato, Scanner sc) {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== EQUIPOS ===");
            System.out.println("1. Registrar equipo");
            System.out.println("2. Consultar equipos");
            System.out.println("3. Modificar equipo");
            System.out.println("4. Eliminar equipo");
            System.out.println("0. Volver");
            opcion = numero(pedir(sc, "Opción: "));

            System.out.println();

            switch (opcion) {
                case 1:
                    registrarEquipo(campeonato, sc);
                    break;
                case 2:
                    consultarEquipos(campeonato, sc);
                    break;
                case 3:
                    modificarEquipo(campeonato, sc);
                    break;
                case 4:
                    eliminarEquipo(campeonato, sc);
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
    // RF01: equipos
    // ======================================================================================

    private void registrarEquipo(Campeonato campeonato, Scanner sc) {
        String nombre = pedir(sc, "Nombre del equipo (3 a 40 caracteres): ");
        String barrio = pedir(sc, "Barrio (máximo 40 caracteres): ");
        String delegado = pedir(sc, "Nombre del delegado: ");
        String telefono = pedir(sc, "Teléfono del delegado (10 dígitos): ");

        String error = campeonato.validarEquipo(null, nombre, barrio, delegado, telefono);

        if (error != null) {
            System.out.println(error);
            return;
        }

        Equipo equipo = new Equipo(nombre, barrio, delegado, telefono);
        campeonato.inscribirEquipo(equipo, nombre, barrio, delegado, telefono);

        System.out.println("Equipo '" + equipo.getNombre() + "' registrado correctamente en el campeonato '"
                + campeonato.getNombre() + "'.");
    }

    private void consultarEquipos(Campeonato campeonato, Scanner sc) {
        if (campeonato.getEquipos().isEmpty()) {
            System.out.println(SIN_EQUIPOS);
            return;
        }

        String texto = pedir(sc, "Buscar por nombre (Enter para ver todos): ");

        // Se ordena una copia, para no alterar el orden de inscripción del campeonato
        List<Equipo> encontrados = new ArrayList<>(campeonato.buscarEquipos(texto));
        encontrados.sort((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));

        if (encontrados.isEmpty()) {
            System.out.println(SIN_COINCIDENCIAS);
            return;
        }

        System.out.println("Equipos (" + encontrados.size() + "):");
        for (Equipo e : encontrados) {
            System.out.println("- " + e.getNombre()
                    + " | Barrio: " + e.getBarrio()
                    + " | Delegado: " + e.getDelegado()
                    + " | Teléfono: " + e.getTelefonoDelegado()
                    + " | Jugadores: " + e.cantidadJugadores());
        }
    }

    private void modificarEquipo(Campeonato campeonato, Scanner sc) {
        if (campeonato.getEquipos().isEmpty()) {
            System.out.println(SIN_EQUIPOS);
            return;
        }

        listarEquipos(campeonato);
        Equipo equipo = equipoDe(campeonato, pedir(sc, "Equipo a modificar (número): "));

        if (equipo == null) {
            System.out.println(SIN_SELECCION);
            return;
        }

        System.out.println("Datos actuales: " + equipo.getNombre() + " | " + equipo.getBarrio()
                + " | " + equipo.getDelegado() + " | " + equipo.getTelefonoDelegado());

        String nombre = pedir(sc, "Nombre del equipo (3 a 40 caracteres): ");
        String barrio = pedir(sc, "Barrio (máximo 40 caracteres): ");
        String delegado = pedir(sc, "Nombre del delegado: ");
        String telefono = pedir(sc, "Teléfono del delegado (10 dígitos): ");

        String error = campeonato.validarEquipo(equipo, nombre, barrio, delegado, telefono);

        if (error != null) {
            System.out.println(error);
            return;
        }

        campeonato.modificarEquipo(equipo, nombre, barrio, delegado, telefono);
        System.out.println("Equipo actualizado correctamente.");
    }

    private void eliminarEquipo(Campeonato campeonato, Scanner sc) {
        if (campeonato.getEquipos().isEmpty()) {
            System.out.println(SIN_EQUIPOS);
            return;
        }

        listarEquipos(campeonato);
        Equipo equipo = equipoDe(campeonato, pedir(sc, "Equipo a eliminar (número): "));

        String error = campeonato.validarEliminarEquipo(equipo);

        if (error != null) {
            System.out.println(error);
            return;
        }

        int jugadores = equipo.getJugadores().size();
        String respuesta = pedir(sc, "¿Eliminar el equipo '" + equipo.getNombre()
                + "' junto con sus " + jugadores + " jugadores? (S/N): ");

        if (!esSi(respuesta)) {
            System.out.println("Eliminación cancelada.");
            return;
        }

        String nombre = equipo.getNombre();
        campeonato.eliminarEquipo(equipo);
        System.out.println("Equipo '" + nombre + "' eliminado correctamente junto con sus "
                + jugadores + " jugadores.");
    }

    // ======================================================================================
    // Apoyo
    // ======================================================================================

    private void listarEquipos(Campeonato campeonato) {
        List<Equipo> equipos = campeonato.getEquipos();

        for (int i = 0; i < equipos.size(); i++) {
            System.out.println((i + 1) + ". " + equipos.get(i).getNombre());
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
