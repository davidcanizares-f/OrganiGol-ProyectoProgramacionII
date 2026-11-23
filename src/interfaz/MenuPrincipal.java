package interfaz;

import modelo.Campeonato;
import negocio.Validador;

import java.util.Scanner;

/*
 * Menú principal de OrganiGol: reúne los menús de los cinco módulos.
 * Uso: new MenuPrincipal().mostrar(campeonato, scanner);
 */
public class MenuPrincipal {

    public void mostrar(Campeonato campeonato, Scanner sc) {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== ORGANIGOL ===");
            System.out.println(estadoCampeonato(campeonato));
            System.out.println("1. Campeonato y partidos");
            System.out.println("2. Equipos y jugadores");
            System.out.println("3. Registro del partido");
            System.out.println("4. Tarjetas y sanciones");
            System.out.println("5. Resumen, acta y tabla");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            // Si ya no hay más entrada (fin de archivo), se sale del menú
            opcion = sc.hasNextLine() ? numero(sc.nextLine().trim()) : 0;

            switch (opcion) {
                case 1:
                    new MenuCampeonato().mostrar(campeonato, sc);
                    break;
                case 2:
                    new MenuEquiposJugadores().mostrar(campeonato, sc);
                    break;
                case 3:
                    new MenuRegistroPartido().mostrar(campeonato, sc);
                    break;
                case 4:
                    new MenuTarjetas().mostrar(campeonato, sc);
                    break;
                case 5:
                    new MenuResumenActaTabla().mostrar(campeonato, sc);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);

        System.out.println("Hasta pronto.");
    }

    // Línea con el estado del campeonato, para saber por dónde empezar
    private String estadoCampeonato(Campeonato campeonato) {
        if (!campeonato.estaConfigurado()) {
            return "Campeonato sin configurar (empiece por la opción 1).";
        }

        return "Campeonato '" + campeonato.getNombre() + "' - categoría " + campeonato.getCategoria()
                + " - " + campeonato.getEquipos().size() + " equipo(s), "
                + campeonato.getPartidos().size() + " partido(s).";
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
