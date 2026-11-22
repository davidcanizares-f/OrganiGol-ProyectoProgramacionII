package interfaz;

import modelo.Campeonato;
import negocio.DatosDemo;

import java.util.Scanner;

/**
 * Programa principal de OrganiGol (gestión de una liga deportiva barrial).
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Bienvenido a OrganiGol");
        System.out.println("1. Empezar con un campeonato nuevo");
        System.out.println("2. Cargar datos de demostración (4 equipos y 3 partidos)");
        System.out.print("Opción: ");

        String eleccion = sc.hasNextLine() ? sc.nextLine().trim() : "1";
        Campeonato campeonato;

        if (eleccion.equals("2")) {
            campeonato = DatosDemo.cargarConPartidos();
        } else {
            campeonato = new Campeonato();
        }

        new MenuPrincipal().mostrar(campeonato, sc);
        sc.close();
    }
}
