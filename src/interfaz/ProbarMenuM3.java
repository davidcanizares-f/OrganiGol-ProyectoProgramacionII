package interfaz;

import modelo.Campeonato;
import negocio.DatosDemo;

import java.util.Scanner;

/*
 * Prueba el menú del módulo 3 con el escenario de DatosDemo:
 * Campeonato con partidos, alineaciones aprobadas y el primer partido en EN_REGISTRO.
 */
public class ProbarMenuM3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Campeonato campeonato = DatosDemo.cargarConPartidoListo();
        new MenuRegistroPartido().mostrar(campeonato, sc);
    }
}
