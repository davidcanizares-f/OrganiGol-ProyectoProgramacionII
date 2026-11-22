package modelo;

import negocio.ReglasJuego;
import negocio.Validador;

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
        if (estado == EstadoJugador.ACTIVO) {
            estado = EstadoJugador.SUSPENDIDO;
        }
    }

    public void rehabilitar() {
        if (estado == EstadoJugador.SUSPENDIDO) {
            estado = EstadoJugador.ACTIVO;
        }
    }

    // AÑADIR METODO AL UML
    public String validarBaja(String motivo) {
        if (!Validador.longitud(motivo, 5, 100)) {
            return "Error: el motivo de la baja es obligatorio y debe tener entre 5 y 100 caracteres.";
        }

        if (estado == EstadoJugador.DE_BAJA) {
            return "Error: el jugador ya se encuentra dado de baja.";
        }

        if (estado == EstadoJugador.SUSPENDIDO) {
            return "Error: el jugador está suspendido y no puede darse de baja hasta cumplir su sanción.";
        }

        return null;
    }

    public void darDeBaja(String motivo) {
        if (validarBaja(motivo) != null) {
            return;
        }

        this.estado = EstadoJugador.DE_BAJA;
        this.motivoBaja = motivo.trim();
    }

    // Suma 1 amarilla. Al llegar a 5 aplica 1 fecha, reinicia el contador y devuelve la sanción;
    // si no llega, devuelve null.
    public Sancion sumarAmarilla(Partido partido) {
        amarillasAcumuladas++;
        if (amarillasAcumuladas >= ReglasJuego.AMARILLAS_PARA_SUSPENDER) {
            amarillasAcumuladas = 0;
            return aplicarSancion(partido, Sancion.MOTIVO_ACUMULACION, ReglasJuego.FECHAS_AMARILLAS);
        }
        return null;
    }

    // Deshace el efecto de la amarilla que se contó en ese partido (para la roja por doble amarilla).
    // Si esa amarilla fue la quinta, también quita la sanción por acumulación que generó.
    //AÑADIR METODO AL UML
    public void revertirAmarilla(Partido partido) {
        for (Sancion s : sanciones) {
            if (s.getPartidoOrigen() == partido && Sancion.MOTIVO_ACUMULACION.equals(s.getMotivo())) {
                sanciones.remove(s);
                amarillasAcumuladas = ReglasJuego.AMARILLAS_PARA_SUSPENDER - 1;
                if (fechasPendientes() == 0) {
                    rehabilitar();
                }
                return;
            }
        }
        if (amarillasAcumuladas > 0) {
            amarillasAcumuladas--;
        }
    }

    // Crea la sanción (las fechas nuevas son otra Sancion) y pasa al jugador a SUSPENDIDO.
    // Devuelve null si no hay partido de origen o las fechas no son positivas.
    public Sancion aplicarSancion(Partido partidoOrigen, String motivo, int fechas) {
        if (partidoOrigen == null || fechas <= 0) {
            return null;
        }
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

    // Resta 1 fecha a la primera sanción pendiente cuyo partido de origen no sea "partido"
    // (no se descuenta en el partido donde nació la sanción).
    // Devuelve true si el jugador queda sin fechas y vuelve a ACTIVO.
    public boolean descontarFechas(Partido partido) {
        for (Sancion s : sanciones) {
            if (!s.estaCumplida() && s.getPartidoOrigen() != partido) {
                s.descontarFecha();
                if (fechasPendientes() == 0 && estado == EstadoJugador.SUSPENDIDO) {
                    rehabilitar();
                    return true;
                }
                return false;
            }
        }
        return false;
    }
    // ===== fin M5 =====
}
