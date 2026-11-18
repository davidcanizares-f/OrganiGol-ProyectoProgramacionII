package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Campeonato {

    //Atributos
    private String nombre;
    private Categoria categoria;
    private List<Equipo> equipos;
    private List<Partido> partidos;
    private int ultimoCarne;

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

    public Equipo buscarEquipo(String nombre) {
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

    public void agregarPartido(Partido partido) {

    }

    public boolean existeCruce(Equipo a, Equipo b) {
        return false;
    }

    public List<Partido> partidosCerrados() {
        return null;
    }

    public List<FilaTabla> calcularTabla() {
        return null;
    }

    public List<FilaGoleador> calcularGoleadores() {
        return null;
    }

    public void modificarEquipo(Equipo equipo, String nombre, String barrio, String delegado, String telefono) {

    }

    public List<Equipo> buscarEquipos(String texto) {
        return null;
    }

    public Jugador inscribirJugador(Equipo equipo, String nombres, String apellidos, String cedula,
                                    LocalDate fechaNacimiento, int numeroCamiseta) {
        return null;
    }

    public void modificarJugador(Jugador jugador, String nombres, String apellidos, String cedula,
                                 LocalDate fechaNacimiento, int numeroCamiseta) {

    }

    public List<Jugador> buscarJugadores(Equipo equipo, EstadoJugador estado, String texto) {
        return null;
    }

    public Partido crearPartido(Equipo local, Equipo visitante, LocalDate fecha, LocalTime hora, String cancha) {
        return null;
    }

    public List<Partido> filtrarPartidos(Equipo equipo, EstadoPartido estado) {
        return null;
    }

    public List<Sancion> sancionesVigentes(Equipo equipo) {
        return null;
    }
}
