package modelo;

import java.util.ArrayList;
import java.util.List;

public class Equipo {

    //Atributos
    private String nombre;
    private String barrio;
    private String delegado;
    private String telefonoDelegado;
    private List<Jugador> jugadores;

    //Constructor
    public Equipo(String nombre, String barrio, String delegado, String telefonoDelegado) {
        //TODO: validar los datos con Validador
        this.nombre = nombre;
        this.barrio = barrio;
        this.delegado = delegado;
        this.telefonoDelegado = telefonoDelegado;
        this.jugadores = new ArrayList<>();
    }

    //Metodos
    public void agregarJugador(Jugador jugador) {

    }

    public boolean tieneCupo() {
        return false;
    }

    public boolean camisetaDisponible(int numero) {
        return false;
    }

    public int cantidadJugadores() {
        return 0;
    }

    public Jugador buscarJugador(String carne) {
        return null;
    }

    public void modificar(String nombre, String barrio, String delegado, String telefono) {

    }

    public int descontarFechas(Partido partido) {
        return 0;
    }

    //Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public String getBarrio() {
        return barrio;
    }

    public String getDelegado() {
        return delegado;
    }

    public String getTelefonoDelegado() {
        return telefonoDelegado;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }
}
