package interfaz;

import modelo.Campeonato;
import negocio.DatosDemo;

import java.util.Scanner;

public class ProbarMenuM5 {

    public static void main(String[] args) {
        Campeonato campeonato = DatosDemo.cargarConAmarillas();
        Scanner sc = new Scanner(System.in);
        new MenuTarjetas().mostrar(campeonato, sc);
    }
}
