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
        return cantidadJugadores() < ReglasJuego.PLANTEL_MAXIMO;
    }

    public boolean camisetaDisponible(int numero) {
        return camisetaDisponible(numero, null);
    }

    // AÑADIR METODO AL UML
    // excepto: jugador que se esta modificando (para que no choque consigo mismo)
    public boolean camisetaDisponible(int numero, Jugador excepto) {
        for(Jugador j : jugadores){
            if(j != excepto
                    && j.getEstado() != EstadoJugador.DE_BAJA
                    && j.getNumeroCamiseta() == numero){
                return false;
            }
        }
        return true;
    }

    // Los jugadores dados de baja no ocupan cupo
    public int cantidadJugadores() {
        int cantidad = 0;
        for(Jugador j : jugadores){
            if(j.getEstado() != EstadoJugador.DE_BAJA){
                cantidad++;
            }
        }
        return cantidad;
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

    // ===== M5 =====
    // Devuelve a cuántos jugadores se les descontó una fecha
    public int descontarFechas(Partido partido) {
        int descontados = 0;
        for (Jugador j : jugadores) {
            int antes = j.fechasPendientes();
            j.descontarFechas(partido);
            if (j.fechasPendientes() < antes) {
                descontados++;
            }
        }
        return descontados;
    }
    // ===== fin M5 =====

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
