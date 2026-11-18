package modelo;

public class TarjetaRoja extends Tarjeta {

    //Atributos
    private boolean porDobleAmarilla;

    //Constructor
    public TarjetaRoja(Jugador jugador, Partido partido, int minuto, String detalle, boolean porDobleAmarilla) {
        super(jugador, partido, minuto, detalle);
        this.porDobleAmarilla = porDobleAmarilla;
    }

    //Metodos
    @Override
    public int fechasDeSancion() {
        return 0;
    }

    @Override
    public String getTipo() {
        return null;
    }

    //Getters y Setters
    public boolean isPorDobleAmarilla() {
        return porDobleAmarilla;
    }
}
