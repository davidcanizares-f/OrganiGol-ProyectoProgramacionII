package modelo;

/**
 * Resumen de la Clase pedido a una IA:
 * Sanción de un jugador (RF14). Nace de una tarjeta en un partido de origen.
 * Las fechas nuevas de un jugador ya suspendido son OTRA Sancion; el total
 * se obtiene con Jugador.fechasPendientes().
 */
public class Sancion {

    // Textos del motivo (los usa Jugador, Partido y las pantallas)
    public static final String MOTIVO_ACUMULACION = "Acumulación de amarillas";
    public static final String MOTIVO_DOBLE_AMARILLA = "Doble amarilla";
    public static final String MOTIVO_ROJA_DIRECTA = "Roja directa";

    private Jugador jugador;
    private Partido partidoOrigen;
    private String motivo;
    private int fechasPendientes;

    public Sancion(Jugador jugador, Partido partidoOrigen, String motivo, int fechasPendientes) {
        this.jugador = jugador;
        this.partidoOrigen = partidoOrigen;
        this.motivo = motivo;
        this.fechasPendientes = fechasPendientes;
    }

    /** Resta una fecha (nunca baja de 0). */
    public void descontarFecha() {
        if (fechasPendientes > 0) {
            fechasPendientes--;
        }
    }

    public boolean estaCumplida() {
        return fechasPendientes == 0;
    }

    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }

    public Partido getPartidoOrigen() { return partidoOrigen; }
    public void setPartidoOrigen(Partido partidoOrigen) { this.partidoOrigen = partidoOrigen; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public int getFechasPendientes() { return fechasPendientes; }
    public void setFechasPendientes(int fechasPendientes) { this.fechasPendientes = fechasPendientes; }
}