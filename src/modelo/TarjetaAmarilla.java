package modelo;

public class TarjetaAmarilla extends Tarjeta {

    //Atributos
    private boolean esSegundaAmarilla;

    //Constructor
    public TarjetaAmarilla(Jugador jugador, Partido partido, int minuto, String detalle, boolean esSegundaAmarilla) {
        super(jugador, partido, minuto, detalle);
        this.esSegundaAmarilla = esSegundaAmarilla;
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
    public boolean isEsSegundaAmarilla() {
        return esSegundaAmarilla;
    }
}
