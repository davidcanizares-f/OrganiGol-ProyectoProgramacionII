package modelo;

import negocio.ReglasJuego;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
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

    //M2

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

    //M1
    public int equiposFaltantes() {
        int equipos_faltantes = 0;
        if(equipos.size() <4){
            equipos_faltantes = 4 - equipos.size();
        }
        return equipos_faltantes;
    }

    //AÑADIR METODO AL UML
    public String validarEquipo(Equipo equipo, String nombre, String barrio, String delegado, String telefono){

        // Nombre no repetido en el campeonato
        if (buscarEquipo(nombre) != null) {
            return "Error: ya existe un equipo con ese nombre en el campeonato.";
        }
        return null;
    }

    //CAMBIAR LA FIRMA
    public Equipo inscribirEquipo(Equipo equipo, String nombre, String barrio, String delegado, String telefono) {
        if(validarEquipo(equipo, nombre, barrio, delegado, telefono) == null){
            equipos.add(equipo);
            return equipo;
        }
        return null;
    }

    public void modificarEquipo(Equipo equipo, String nombre, String barrio, String delegado, String telefono) {
        equipo.modificar(nombre, barrio, delegado, telefono);
    }

    public Equipo buscarEquipo(String nombre) {
        for( Equipo e : equipos){
            if ( e.getNombre().equalsIgnoreCase(nombre)){
                return e;
            }
        }
        return null;
    }

    //Buscar coincidencias en el nombre
    public List<Equipo> buscarEquipos(String texto) {
        texto = texto.toLowerCase();
        List<Equipo> resultado = new ArrayList<>();
        for( Equipo e : equipos ){
            if(e.getNombre().toLowerCase().contains(texto)){
                resultado.add(e);
            }
        }
        return resultado;
    }

    //AGREGAR METODO AL UML

    public String validarEliminarEquipo(Equipo equipo){
        if (equipoTienePartidos(equipo)){
            return "No se ha podido eliminar. El equipo aun tiene partidos pendientes";
        }
        return null;
    }

    public void eliminarEquipo(Equipo equipo) {
        if (validarEliminarEquipo(equipo) == null){
            equipos.remove(equipo);
        }
    }

    public boolean equipoTienePartidos(Equipo equipo) {
        for (Partido p : partidos){
            if (p.getLocal() == equipo || p.getVisitante() == equipo){
                return true;
            }
        }
        return false;
    }

    public String siguienteCarne() {
        if (ultimoCarne < ReglasJuego.CARNE_MAXIMO){
            ultimoCarne++;
            return String.format("%04d", ultimoCarne);
        }
        return null;
    }

    //AÑADIR METODO AL UML
    public String validarJugador(Equipo equipo,
                                    String nombres,
                                    String apellidos,
                                    String cedula,
                                    LocalDate fechaNacimiento,
                                    int numeroCamiseta) {
        // Cedulas no repetidas
        for(Equipo e : equipos){
            for(Jugador j : e.getJugadores()){
                if(j.getCedula().equals(cedula)){
                    return "Error. La cedula esta repetida.";
                }
            }
        }

        //La edad corresponde a la categoria del campeonato
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if(!categoria.admiteEdad(edad)){
            return "Error. La edad de " + edad + " años no pertenece a la categoria.";
        }

        //Reglas del equipo
        if(!equipo.camisetaDisponible(numeroCamiseta)) {
            return "Error: el número de camiseta ya está en uso en este equipo.";
        }
        if(!equipo.tieneCupo()) {
            return "Error: el plantel del equipo ya tiene el máximo de 25 jugadores.";
        }

        //Carné disponible
        if(ultimoCarne >= ReglasJuego.CARNE_MAXIMO) {
            return "Error: se alcanzó el límite máximo de 9999 carnés.";
        }

        //Si todo esta bien
        return null;
    }

    public Jugador inscribirJugador(Equipo equipo,
                                 String nombres,
                                 String apellidos,
                                 String cedula,
                                 LocalDate fechaNacimiento,
                                 int numeroCamiseta){
        if(validarJugador(equipo, nombres, apellidos, cedula, fechaNacimiento, numeroCamiseta) == null){
            String carne = siguienteCarne();
            Jugador jugador = new Jugador(carne, nombres, apellidos, cedula, fechaNacimiento, numeroCamiseta, equipo);
            equipo.agregarJugador(jugador);
            return jugador;
        }
        return null;
    }

    public void modificarJugador(Jugador jugador,
                                 String nombres,
                                 String apellidos,
                                 String cedula,
                                 LocalDate fechaNacimiento,
                                 int numeroCamiseta) {
        jugador.setNombres(nombres);
        jugador.setApellidos(apellidos);
        jugador.setCedula(cedula);
        jugador.setFechaNacimiento(fechaNacimiento);
        jugador.setNumeroCamiseta(numeroCamiseta);
    }

    //Buscar jugador por nombre o apellido
    //VERIFICAR EN EL UML
    public List<Jugador> buscarJugador(String texto) {
        texto = texto.toLowerCase();
        List<Jugador> resultado = new ArrayList<>();
        for( Equipo e : equipos ){
            for( Jugador j : e.getJugadores() ){
                if(j.getNombres().toLowerCase().contains(texto) || j.getApellidos().toLowerCase().contains(texto)){
                    resultado.add(j);
                }
            }
        }
        return resultado;
    }

    //Buscar jugadores por cedula
    //AÑADIR EN EL UML
    public List<Jugador> buscarJugadoresCedula(String texto) {
        List<Jugador> resultado = new ArrayList<>();
        for( Equipo e : equipos ){
            for( Jugador j : e.getJugadores() ){
                if(j.getCedula().contains(texto)){
                    resultado.add(j);
                }
            }
        }
        return resultado;
    }

    //OTRO MODULO
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
