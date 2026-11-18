package modelo;

import java.time.LocalDate;
import java.util.List;

public class Jugador {

    //Atributos
    private String carne;
    private String nombres;
    private String apellidos;
    private String cedula;
    private LocalDate fechaNacimiento;
    private int numeroCamiseta;
    private EstadoJugador estado;
    private int amarillasAcumuladas;
    private String motivoBaja;
    private Equipo equipo;
    private List<Sancion> sanciones;

    //Metodos
    public int calcularEdad() {
        return 0;
    }

    public boolean estaHabilitado() {
        return false;
    }

    public void suspender() {

    }

    public void rehabilitar() {

    }

    public void darDeBaja(String motivo) {

    }

    public Sancion sumarAmarilla(Partido partido) {
        return null;
    }

    public Sancion aplicarSancion(Partido partidoOrigen, String motivo, int fechas) {
        return null;
    }

    public int fechasPendientes() {
        return 0;
    }

    public boolean descontarFechas(Partido partido) {
        return false;
    }
}
