package interfaz;

import modelo.Campeonato;
import negocio.DatosDemo;

import java.util.Scanner;

public class ProbarMenuM4 {

    public static void main(String[] args) {
        Campeonato campeonato = DatosDemo.cargarParaMenuM4();
        Scanner sc = new Scanner(System.in);
        new MenuResumenActaTabla().mostrar(campeonato, sc);
    }
}