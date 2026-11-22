
package negocio;

import java.time.YearMonth;

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

    // Convierte un texto en un entero sin lanzar errores.
    // Devuelve -1 si el texto está vacío, no son solo dígitos o tiene más de 9 dígitos.
    public static int aEntero(String texto) {
        if (!obligatorio(texto)) {
            return -1;
        }

        String limpio = texto.trim();

        if (limpio.length() > 9) {
            return -1;
        }

        for (int i = 0; i < limpio.length(); i++) {
            char digito = limpio.charAt(i);

            if (digito < '0' || digito > '9') {
                return -1;
            }
        }

        return Integer.parseInt(limpio);
    }

    // Comprueba una fecha con formato dd/MM/aaaa (día y mes de 2 dígitos, año de 4)
    public static boolean fechaValida(String texto) {
        if (texto == null || texto.length() != 10
                || texto.charAt(2) != '/' || texto.charAt(5) != '/') {
            return false;
        }

        String dia = texto.substring(0, 2);
        String mes = texto.substring(3, 5);
        String anio = texto.substring(6, 10);

        if (!soloDigitos(dia, 2) || !soloDigitos(mes, 2) || !soloDigitos(anio, 4)) {
            return false;
        }

        int d = aEntero(dia);
        int m = aEntero(mes);
        int a = aEntero(anio);

        if (a < 1 || m < 1 || m > 12 || d < 1) {
            return false;
        }

        // Días del mes (considera los años bisiestos)
        return d <= YearMonth.of(a, m).lengthOfMonth();
    }

    // Comprueba una hora con formato HH:mm
    public static boolean horaValida(String texto) {
        if (texto == null || texto.length() != 5 || texto.charAt(2) != ':') {
            return false;
        }

        String horas = texto.substring(0, 2);
        String minutos = texto.substring(3, 5);

        if (!soloDigitos(horas, 2) || !soloDigitos(minutos, 2)) {
            return false;
        }

        return rango(aEntero(horas), 0, 23) && rango(aEntero(minutos), 0, 59);
    }
}
