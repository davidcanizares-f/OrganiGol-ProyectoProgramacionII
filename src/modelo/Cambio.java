package modelo;

public class Cambio {

    //Atributos
    private Equipo equipo;
    private Jugador jugadorSale;
    private Jugador jugadorEntra;
    private int minuto;

    //Constructor
    public Cambio(Equipo equipo, Jugador jugadorSale, Jugador jugadorEntra, int minuto) {
        this.equipo = equipo;
        this.jugadorSale = jugadorSale;
        this.jugadorEntra = jugadorEntra;
        this.minuto = minuto;
    }

    //Getters y Setters
    public Equipo getEquipo() {
        return equipo;
    }

    public Jugador getJugadorSale() {
        return jugadorSale;
    }

    public Jugador getJugadorEntra() {
        return jugadorEntra;
    }

    public int getMinuto() {
        return minuto;
    }
}
