package interfaz;

import modelo.Campeonato;
import negocio.Validador;

import java.util.Scanner;

/*
 * Menú del módulo 1: equipos y jugadores (RF01 y RF02).
 * Uso: new MenuEquiposJugadores().mostrar(campeonato, scanner);
 */
public class MenuEquiposJugadores {

    public void mostrar(Campeonato campeonato, Scanner sc) {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== EQUIPOS Y JUGADORES ===");
            System.out.println("1. Gestionar equipos");
            System.out.println("2. Gestionar jugadores");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            // Si ya no hay más entrada (fin de archivo), se sale del menú
            opcion = sc.hasNextLine() ? numero(sc.nextLine().trim()) : 0;

            switch (opcion) {
                case 1:
                    new MenuEquipos().mostrar(campeonato, sc);
                    break;
                case 2:
                    new MenuJugadores().mostrar(campeonato, sc);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
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
