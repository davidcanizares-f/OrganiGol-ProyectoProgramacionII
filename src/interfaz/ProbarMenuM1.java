package interfaz;

import modelo.Campeonato;
import negocio.DatosDemo;

import java.util.Scanner;

/**
 * Prueba el menú del módulo 1 con un escenario de DatosDemo.
 * 1. Campeonato sin configurar (RF01 debe pedir primero configurarlo)
 * 2. Campeonato con 4 equipos de 10 jugadores cada uno (RF01 y RF02)
 * 3. Campeonato con 3 partidos programados (los equipos con partidos no se pueden eliminar)
 */
public class ProbarMenuM1 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Escenario de prueba:");
        System.out.println("1. Campeonato sin configurar");
        System.out.println("2. Campeonato con 4 equipos y 10 jugadores por equipo");
        System.out.println("3. Campeonato con 3 partidos programados");
        System.out.print("Escenario: ");

        String eleccion = sc.hasNextLine() ? sc.nextLine().trim() : "2";
        Campeonato campeonato;

        switch (eleccion) {
            case "1":
                campeonato = DatosDemo.cargarVacio();
                break;
            case "3":
                campeonato = DatosDemo.cargarConPartidos();
                break;
            default:
                campeonato = DatosDemo.cargarBase();
        }
        new MenuEquiposJugadores().mostrar(campeonato, sc);
    }
}
