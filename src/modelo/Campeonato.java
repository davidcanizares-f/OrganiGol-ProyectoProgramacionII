package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Campeonato {

    //Atributos
    private String nombre;
    private Categoria categoria;
    private List<Equipo> equipos;
    private List<Partido> partidos;
    private int ultimoCarne;

    //Constructor
    public Campeonato() {
        this.equipos = new ArrayList<>();
        this.partidos = new ArrayList<>();
        this.ultimoCarne = 0;
    }

    //Metodos
    public void configurar(String nombre, Categoria categoria) {

    }

    public void modificar(String nombre, Categoria categoria) {

    }

    public boolean estaConfigurado() {
        return false;
    }

    public boolean estaListo() {
        return false;
    }

    public int equiposFaltantes() {
        return 0;
    }

    public void inscribirEquipo(Equipo equipo) {

    }

    public void modificarEquipo(Equipo equipo, String nombre, String barrio, String delegado, String telefono) {

    }

    public Equipo buscarEquipo(String nombre) {
        return null;
    }

    public List<Equipo> buscarEquipos(String texto) {
        return null;
    }

    public void eliminarEquipo(Equipo equipo) {

    }

    public boolean equipoTienePartidos(Equipo equipo) {
        return false;
    }

    public String siguienteCarne() {
        return null;
    }

    public Jugador inscribirJugador(Equipo equipo,
                                    String nombres,
                                    String apellidos,
                                    String cedula,
                                    LocalDate fechaNacimiento,
                                    int numeroCamiseta) {
        return null;
    }

    public void modificarJugador(Jugador jugador,
                                 String nombres,
                                 String apellidos,
                                 String cedula,
                                 LocalDate fechaNacimiento,
                                 int numeroCamiseta) {

    }

    public List<Jugador> buscarJugadores(Equipo equipo, EstadoJugador estado, String texto) {
        return null;
    }

    public void agregarPartido(Partido partido) {

    }

    public Partido crearPartido(Equipo local, Equipo visitante, LocalDate fecha, LocalTime hora, String cancha) {
        return null;
    }

    public boolean existeCruce(Equipo a, Equipo b) {
        return false;
    }

    public List<Partido> filtrarPartidos(Equipo equipo, EstadoPartido estado) {
        return null;
    }

    public List<Partido> partidosCerrados() {
        return null;
    }

    public List<TablaPosiciones> calcularTabla() {
        return null;
    }

    public List<TablaGoleadores> calcularGoleadores() {
        return null;
    }

    public List<Sancion> sancionesVigentes(Equipo equipo) {
        return null;
    }

    //Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public int getUltimoCarne() {
        return ultimoCarne;
    }
}
