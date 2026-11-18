package modelo;

public class Sancion {

    //Atributos
    private Jugador jugador;
    private Partido partidoOrigen;
    private String motivo;
    private int fechasPendientes;

    //Constructor
    public Sancion(Jugador jugador, Partido partidoOrigen, String motivo, int fechasPendientes) {
        this.jugador = jugador;
        this.partidoOrigen = partidoOrigen;
        this.motivo = motivo;
        this.fechasPendientes = fechasPendientes;
    }

    //Metodos
    public void descontarFecha() {

    }

    public boolean estaCumplida() {
        return false;
    }

    //Getters y Setters
    public Jugador getJugador() {
        return jugador;
    }

    public Partido getPartidoOrigen() {
        return partidoOrigen;
    }

    public String getMotivo() {
        return motivo;
    }

    public int getFechasPendientes() {
        return fechasPendientes;
    }
}
