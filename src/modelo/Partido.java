package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Partido {

    //Atributos
    private Equipo local;
    private Equipo visitante;
    private LocalDate fecha;
    private LocalTime hora;
    private String cancha;
    private EstadoPartido estado;
    private Alineacion alineacionLocal;
    private Alineacion alineacionVisitante;
    private List<Gol> goles;
    private List<Cambio> cambios;
    private List<Tarjeta> tarjetas;
    private ResultadoPartido resultado;
    private String observaciones;
    private Equipo equipoSuspendido;

    //Metodos
    public void abrirRegistro() {

    }

    public void finalizarRegistro() {

    }

    public Gol registrarGol(Jugador jugador, int minuto) {
        return null;
    }

    public Cambio registrarCambio(Equipo equipo, Jugador sale, Jugador entra, int minuto) {
        return null;
    }

    public Tarjeta registrarTarjeta(Jugador jugador, String tipo, int minuto, String detalle) {
        return null;
    }

    public boolean estaEnAlineacion(Jugador jugador) {
        return false;
    }

    public boolean estaExpulsado(Jugador jugador) {
        return false;
    }

    public int jugadoresEnCancha(Equipo equipo) {
        return 0;
    }

    public int golesDe(Equipo equipo) {
        return 0;
    }

    public String getMarcador() {
        return null;
    }

    public Alineacion getAlineacion(Equipo equipo) {
        return null;
    }

    public ResultadoPartido determinarResultado() {
        return null;
    }

    public void suspenderPorFaltaDeJugadores(Equipo equipo) {

    }

    public void cerrar(String observaciones) {

    }

    public boolean registrarAlineacion(Alineacion alineacion) {
        return false;
    }

    public boolean verificarMinimo(Equipo equipo) {
        return false;
    }

    public List<Tarjeta> filtrarTarjetas(Equipo equipo, Jugador jugador, String tipo) {
        return null;
    }

    public String generarEstadisticas() {
        return null;
    }

    public String generarActa(String nombreCampeonato) {
        return null;
    }
}
