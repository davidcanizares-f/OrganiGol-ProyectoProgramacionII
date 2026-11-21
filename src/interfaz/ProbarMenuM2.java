package interfaz;

import modelo.Campeonato;
import negocio.DatosDemo;

import java.util.Scanner;

/**
 * Prueba el menú del módulo 2 con un escenario de DatosDemo.
 * 1. Campeonato sin configurar (RF03)
 * 2. Campeonato con 4 equipos, sin partidos (RF04)
 * 3. Campeonato con 3 partidos programados (RF05)
 */
public class ProbarMenuM2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Escenario de prueba:");
        System.out.println("1. Campeonato sin configurar (RF03)");
        System.out.println("2. Campeonato con 4 equipos, sin partidos (RF04)");
        System.out.println("3. Campeonato con 3 partidos programados (RF05)");
        System.out.print("Escenario: ");

        String eleccion = sc.hasNextLine() ? sc.nextLine().trim() : "3";
        Campeonato campeonato;

        switch (eleccion) {
            case "1":
                campeonato = DatosDemo.cargarVacio();
                break;
            case "2":
                campeonato = DatosDemo.cargarBase();
                break;
            default:
                campeonato = DatosDemo.cargarConPartidos();
        }
        new MenuCampeonato().mostrar(campeonato, sc);
    }
}