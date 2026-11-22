package modelo;

import negocio.ReglasJuego;

import java.time.LocalDate;
import java.time.Period;
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

    //M1

    public int calcularEdad() {
        int edad;
        edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        return edad;
    }

    //VERIFICAR MODULOS

    public boolean estaHabilitado() {
        return estado == EstadoJugador.ACTIVO && fechasPendientes() == 0;
    }

    // ===== M5 =====
    public void suspender() {
        // Un jugador dado de baja no cambia de estado
        if (estado == EstadoJugador.ACTIVO) {
            estado = EstadoJugador.SUSPENDIDO;
        }
    }

    public void rehabilitar() {
        if (estado == EstadoJugador.SUSPENDIDO) {
            estado = EstadoJugador.ACTIVO;
        }
    }
    // ===== fin M5 =====

    public void darDeBaja(String motivo) {
        this.estado = EstadoJugador.DE_BAJA;
        this.motivoBaja = motivo;
    }

    // ===== M5 =====
    public Sancion sumarAmarilla(Partido partido) {
        amarillasAcumuladas++;
        if (amarillasAcumuladas >= ReglasJuego.AMARILLAS_PARA_SUSPENDER) {
            amarillasAcumuladas = 0;
            return aplicarSancion(partido, Sancion.MOTIVO_ACUMULACION, ReglasJuego.FECHAS_AMARILLAS);
        }
        return null;
    }

    // Se usa cuando una segunda amarilla se convierte en roja: las amarillas del partido no suman
    public void restarAmarilla() {
        if (amarillasAcumuladas > 0) {
            amarillasAcumuladas--;
        }
    }

    public Sancion aplicarSancion(Partido partidoOrigen, String motivo, int fechas) {
        Sancion sancion = new Sancion(this, partidoOrigen, motivo, fechas);
        sanciones.add(sancion);
        suspender();
        return sancion;
    }

    public int fechasPendientes() {
        int total = 0;
        for (Sancion s : sanciones) {
            total += s.getFechasPendientes();
        }
        return total;
    }

    // Resta 1 fecha a la primera sanción pendiente que NO nació en este partido.
    // Devuelve true si el jugador queda sin fechas y vuelve a ACTIVO.
    public boolean descontarFechas(Partido partido) {
        for (Sancion s : sanciones) {
            if (s.getPartidoOrigen() != partido && !s.estaCumplida()) {
                s.descontarFecha();
                if (fechasPendientes() == 0) {
                    rehabilitar();
                    return true;
                }
                return false;
            }
        }
        return false;
    }
    // ===== fin M5 =====

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
