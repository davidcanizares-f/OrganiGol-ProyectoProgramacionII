package modelo;

public class TablaGoleadores {

    //Atributos
    private Jugador jugador;
    private Equipo equipo;
    private int goles;

    //Constructor
    public TablaGoleadores(Jugador jugador, Equipo equipo, int goles) {
        this.jugador = jugador;
        this.equipo = equipo;
        this.goles = goles;
    }

    //Getters y Setters
    public Jugador getJugador() {
        return jugador;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public int getGoles() {
        return goles;
    }
}
