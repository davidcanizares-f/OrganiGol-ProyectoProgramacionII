package modelo;

import negocio.ReglasJuego;

/*Tarjeta roja. Por doble amarilla = 1 fecha; directa = 2 fechas (un solo nivel, sin gravedad).
 */
public class TarjetaRoja extends Tarjeta {

    private boolean porDobleAmarilla;

    public TarjetaRoja(Jugador jugador, Partido partido, int minuto,
                       String detalle, boolean porDobleAmarilla) {
        super(jugador, partido, minuto, detalle);
        this.porDobleAmarilla = porDobleAmarilla;
    }

    @Override
    public int fechasDeSancion() {
        return porDobleAmarilla ? ReglasJuego.FECHAS_DOBLE_AMARILLA : ReglasJuego.FECHAS_ROJA_DIRECTA;
    }

    @Override
    public String getTipo() {
        return "ROJA";
    }

    public boolean isPorDobleAmarilla() { return porDobleAmarilla; }
    public void setPorDobleAmarilla(boolean porDobleAmarilla) { this.porDobleAmarilla = porDobleAmarilla; }
}
