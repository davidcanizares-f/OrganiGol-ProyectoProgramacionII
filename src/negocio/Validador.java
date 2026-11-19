package negocio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class Validador {

    public static void obligatorio(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()){
            throw new IllegalArgumentException(
                    "El campo " + campo+ " es obligatorio." );
        }
    }

    public static void longitud(String texto, int min, int max, String campo) {
        obligatorio(texto, campo);

        if (texto.length() < min || texto.length() > max) {
            throw new IllegalArgumentException(
                    "El campo " + campo + " debe tener entre " + min + " y " + max + " caracteres.");
        }
    }

    public static void soloLetras(String texto, String campo) {
        obligatorio(texto, campo);

        if (!texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+")) {
            throw new IllegalArgumentException(
                    "El campo " + campo + " solo debe contener letras.");
        }
    }

    public static void soloDigitos(String texto, int cantidad, String campo) {
        obligatorio(texto, campo);

        if (!texto.matches("[0-9]+") || texto.length() != cantidad) {
            throw new IllegalArgumentException(
                    "El campo " + campo + " debe contener exactamente "
                            + cantidad + " dígitos.");
        }
    }

    public static void rango(int valor, int min, int max, String campo) {
        if (valor < min || valor > max) {
            throw new IllegalArgumentException(
                    "El campo " + campo + " debe estar entre "
                            + min + " y " + max + ".");
        }
    }

    public static boolean cedulaValida(String cedula) {
        if (cedula == null || !cedula.matches("[0-9]{10}")) {
            return false;
        }

        for (int i = 1; i < cedula.length(); i++) {
            if (cedula.charAt(i) != cedula.charAt(0)) {
                return true;
            }
        }
        return false;
    }

    public static void minutoValido(int minuto) {
        if (minuto < 1 || minuto > 80) {
            throw new IllegalArgumentException(
                    "El minuto debe estar entre 1 y 80.");
        }
    }

    public static LocalDate fechaValida(String texto) {
        obligatorio(texto, "fecha");


        DateTimeFormatter formato = DateTimeFormatter
                .ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        return LocalDate.parse(texto, formato);
    }

    public static LocalTime horaValida(String texto) {
        obligatorio(texto, "hora");

        DateTimeFormatter formato = DateTimeFormatter
                .ofPattern("HH:mm")
                .withResolverStyle(ResolverStyle.STRICT);

        return LocalTime.parse(texto, formato);
    }
}
