
package negocio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class Validador {

    // Comprueba que el texto no esté vacío
    public static boolean obligatorio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    // Comprueba la longitud de un texto
    public static boolean longitud(String texto, int min, int max) {
        return obligatorio(texto)
                && texto.trim().length() >= min
                && texto.trim().length() <= max;
    }

    // Comprueba que contenga solamente letras y espacios
    public static boolean soloLetras(String texto) {
        if (!obligatorio(texto)) {
            return false;
        }

        int i = 0;

        while (i < texto.length()) {
            char letra = texto.charAt(i);

            if (!Character.isLetter(letra) && letra != ' ') {
                return false;
            }

            i++;
        }

        return true;
    }

    // Comprueba que contenga exactamente cierta cantidad de dígitos
    public static boolean soloDigitos(String texto, int cantidad) {
        if (!obligatorio(texto) || texto.length() != cantidad) {
            return false;
        }

        int i = 0;

        while (i < texto.length()) {
            char digito = texto.charAt(i);

            if (digito < '0' || digito > '9') {
                return false;
            }

            i++;
        }

        return true;
    }

    // Comprueba que un número esté dentro de un rango
    public static boolean rango(int valor, int min, int max) {
        return valor >= min && valor <= max;
    }

    // Comprueba el formato básico de una cédula
    public static boolean cedulaValida(String cedula) {
        if (!soloDigitos(cedula, 10)) {
            return false;
        }

        int i = 1;

        while (i < cedula.length()) {
            if (cedula.charAt(i) != cedula.charAt(0)) {
                return true;
            }

            i++;
        }

        return false;
    }

    // Comprueba que el minuto esté entre 1 y 80
    public static boolean minutoValido(int minuto) {
        return minuto >= 1 && minuto <= 80;
    }

    // Comprueba una fecha con formato dd/MM/uuuu
    public static boolean fechaValida(String texto) {
        if (!obligatorio(texto)) {
            return false;
        }

        try {
            DateTimeFormatter formato = DateTimeFormatter
                    .ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

            LocalDate.parse(texto, formato);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // Comprueba una hora con formato HH:mm
    public static boolean horaValida(String texto) {
        if (!obligatorio(texto)) {
            return false;
        }

        try {
            DateTimeFormatter formato = DateTimeFormatter
                    .ofPattern("HH:mm")
                    .withResolverStyle(ResolverStyle.STRICT);

            LocalTime.parse(texto, formato);
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
