package modelo;

import negocio.ReglasJuego;

import java.util.ArrayList;
import java.util.List;

public class Equipo {

    //Atributos
    private String nombre;
    private String barrio;
    private String delegado;
    private String telefonoDelegado;
    private final List<Jugador> jugadores;

    //Constructor
    public Equipo(String nombre, String barrio, String delegado, String telefonoDelegado) {
        this.nombre = nombre;
        this.barrio = barrio;
        this.delegado = delegado;
        this.telefonoDelegado = telefonoDelegado;
        this.jugadores = new ArrayList<>();
    }

    //Metodos

    //M1

    public void agregarJugador(Jugador jugador) {
        jugadores.add(jugador);
    }

    public boolean tieneCupo() {
        return jugadores.size() < ReglasJuego.PLANTEL_MAXIMO;
    }

    public boolean camisetaDisponible(int numero) {
        for(Jugador j : jugadores){
            if(j.getNumeroCamiseta() == numero){
                return false;
            }
        }
        return true;
    }

    public int cantidadJugadores() {
        return jugadores.size();
    }

    public Jugador buscarJugador(String carne) {
        for(Jugador j : jugadores){
            if(j.getCarne().equals(carne)){
                return j;
            }
        }
        return null;
    }

    public void modificar(String nombre, String barrio, String delegado, String telefono) {
        this.nombre = nombre;
        this.barrio = barrio;
        this.delegado = delegado;
        this.telefonoDelegado = telefono;
    }

    //OTRO MODULO

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
