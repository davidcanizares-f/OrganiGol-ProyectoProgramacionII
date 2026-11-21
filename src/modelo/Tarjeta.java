package modelo;

/**
 * Resumen de la Clase pedido a una IA:
 * Tarjeta de un partido (RF12). Clase abstracta: TarjetaAmarilla y TarjetaRoja
 * deciden cuántas fechas de sanción genera cada una y cuál es su tipo.
 */
public abstract class Tarjeta {

    private Jugador jugador;
    private Partido partido;
    private int minuto;
    private String detalle;

    protected Tarjeta(Jugador jugador, Partido partido, int minuto, String detalle) {
        if (jugador == null) {
            throw new IllegalArgumentException("Error: debe seleccionar un jugador.");
        }
        if (partido == null) {
            throw new IllegalArgumentException("Error: debe seleccionar un partido.");
        }
        this.jugador = jugador;
        this.partido = partido;
        this.minuto = minuto;
        this.detalle = detalle;
    }

    /** Fechas de suspensión que genera esta tarjeta. */
    public abstract int fechasDeSancion();

    /** "AMARILLA" o "ROJA". */
    public abstract String getTipo();

    // ---------- getters y setters (no se dibujan en el UML) ----------
    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }

    public Partido getPartido() { return partido; }
    public void setPartido(Partido partido) { this.partido = partido; }

    public int getMinuto() { return minuto; }
    public void setMinuto(int minuto) { this.minuto = minuto; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
}
