package negocio;

import java.time.LocalDate;
import java.time.LocalTime;

public class Validador {

    public static void obligatorio(String texto, String campo) {

    }

    public static void longitud(String texto, int min, int max, String campo) {

    }

    public static void soloLetras(String texto, String campo) {

    }

    public static void soloDigitos(String texto, int cantidad, String campo) {

    }

    public static void rango(int valor, int min, int max, String campo) {

    }

    public static boolean cedulaValida(String cedula) {
        return false;
    }

    public static void minutoValido(int minuto) {

    }

    public static LocalDate fechaValida(String texto) {
        return null;
    }

    public static LocalTime horaValida(String texto) {
        return null;
    }
}
