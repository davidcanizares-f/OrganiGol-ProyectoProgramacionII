package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    //Constructor
    public Partido(Equipo local, Equipo visitante, LocalDate fecha, LocalTime hora, String cancha) {
        this.local = local;
        this.visitante = visitante;
        this.fecha = fecha;
        this.hora = hora;
        this.cancha = cancha;
        this.estado = EstadoPartido.PROGRAMADO;
        this.goles = new ArrayList<>();
        this.cambios = new ArrayList<>();
        this.tarjetas = new ArrayList<>();
    }

    //Metodos
    public boolean registrarAlineacion(Alineacion alineacion) {
        return false;
    }

    public Alineacion getAlineacion(Equipo equipo) {
        return null;
    }

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

    public boolean verificarMinimo(Equipo equipo) {
        return false;
    }

    public void suspenderPorFaltaDeJugadores(Equipo equipo) {

    }

    public int golesDe(Equipo equipo) {
        return 0;
    }

    public String getMarcador() {
        return null;
    }

    public ResultadoPartido determinarResultado() {
        return null;
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

    public void cerrar(String observaciones) {

    }

    //Getters y Setters
    public Equipo getLocal() {
        return local;
    }

    public Equipo getVisitante() {
        return visitante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getCancha() {
        return cancha;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    public Alineacion getAlineacionLocal() {
        return alineacionLocal;
    }

    public Alineacion getAlineacionVisitante() {
        return alineacionVisitante;
    }

    public List<Gol> getGoles() {
        return goles;
    }

    public List<Cambio> getCambios() {
        return cambios;
    }

    public List<Tarjeta> getTarjetas() {
        return tarjetas;
    }

    public ResultadoPartido getResultado() {
        return resultado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Equipo getEquipoSuspendido() {
        return equipoSuspendido;
    }
}
