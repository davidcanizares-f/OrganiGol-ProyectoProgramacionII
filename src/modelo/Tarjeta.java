package modelo;

public abstract class Tarjeta {

    //Atributos
    private Jugador jugador;
    private Partido partido;
    private int minuto;
    private String detalle;

    //Constructor
    protected Tarjeta(Jugador jugador, Partido partido, int minuto, String detalle) {
        this.jugador = jugador;
        this.partido = partido;
        this.minuto = minuto;
        this.detalle = detalle;
    }

    //Metodos
    public abstract int fechasDeSancion();

    public abstract String getTipo();

    //Getters y Setters
    public Jugador getJugador() {
        return jugador;
    }

    public Partido getPartido() {
        return partido;
    }

    public int getMinuto() {
        return minuto;
    }

    public String getDetalle() {
        return detalle;
    }
}
