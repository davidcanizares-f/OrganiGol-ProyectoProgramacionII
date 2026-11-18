package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private List<Sancion> sanciones;
    private Equipo equipo;

    //Constructor
    public Jugador(String carne,
                   String nombres,
                   String apellidos,
                   String cedula,
                   LocalDate fechaNacimiento,
                   int numeroCamiseta,
                   Equipo equipo) {
        //TODO: validar los datos con Validador
        this.carne = carne;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.fechaNacimiento = fechaNacimiento;
        this.numeroCamiseta = numeroCamiseta;
        this.equipo = equipo;
        this.estado = EstadoJugador.ACTIVO;
        this.amarillasAcumuladas = 0;
        this.sanciones = new ArrayList<>();
    }

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

    //Getters y Setters
    public String getCarne() {
        return carne;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCedula() {
        return cedula;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public int getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public EstadoJugador getEstado() {
        return estado;
    }

    public int getAmarillasAcumuladas() {
        return amarillasAcumuladas;
    }

    public String getMotivoBaja() {
        return motivoBaja;
    }

    public List<Sancion> getSanciones() {
        return sanciones;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setNumeroCamiseta(int numeroCamiseta) {
        this.numeroCamiseta = numeroCamiseta;
    }
}
