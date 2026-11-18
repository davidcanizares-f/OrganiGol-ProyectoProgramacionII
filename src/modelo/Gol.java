package modelo;

public class Gol {

    //Atributos
    private Jugador jugador;
    private Equipo equipo;
    private int minuto;

    //Constructor
    public Gol(Jugador jugador, Equipo equipo, int minuto) {
        this.jugador = jugador;
        this.equipo = equipo;
        this.minuto = minuto;
    }

    //Getters y Setters
    public Jugador getJugador() {
        return jugador;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public int getMinuto() {
        return minuto;
    }
}
