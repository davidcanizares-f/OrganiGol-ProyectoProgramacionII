package modelo;

/**
 * Resumen de la Clase pedido a una IA:
 * Tarjeta amarilla. No genera fechas por sí sola: cuenta en el acumulado del jugador (RF14). */
public class TarjetaAmarilla extends Tarjeta {

    private boolean esSegundaAmarilla;

    public TarjetaAmarilla(Jugador jugador, Partido partido, int minuto,
                           String detalle, boolean esSegundaAmarilla) {
        super(jugador, partido, minuto, detalle);
        this.esSegundaAmarilla = esSegundaAmarilla;
    }

    @Override
    public int fechasDeSancion() {
        return 0;
    }

    @Override
    public String getTipo() {
        return "AMARILLA";
    }

    public boolean isEsSegundaAmarilla() { return esSegundaAmarilla; }
    public void setEsSegundaAmarilla(boolean esSegundaAmarilla) { this.esSegundaAmarilla = esSegundaAmarilla; }
}
