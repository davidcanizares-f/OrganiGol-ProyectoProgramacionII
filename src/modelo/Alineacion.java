package modelo;

import java.util.List;

public class Alineacion {

    //Atributos
    private Equipo equipo;
    private List<Jugador> titulares;
    private List<Jugador> suplentes;
    private Jugador capitan;

    //Constructor
    public Alineacion(Equipo equipo, List<Jugador> titulares, List<Jugador> suplentes, Jugador capitan) {
        this.equipo = equipo;
        this.titulares = titulares;
        this.suplentes = suplentes;
        this.capitan = capitan;
    }

    //Metodos
    public void validar() {

    }

    public boolean contiene(Jugador jugador) {
        return false;
    }

    public boolean esTitular(Jugador jugador) {
        return false;
    }

    public boolean esSuplente(Jugador jugador) {
        return false;
    }

    //Getters y Setters
    public Equipo getEquipo() {
        return equipo;
    }

    public List<Jugador> getTitulares() {
        return titulares;
    }

    public List<Jugador> getSuplentes() {
        return suplentes;
    }

    public Jugador getCapitan() {
        return capitan;
    }
}
