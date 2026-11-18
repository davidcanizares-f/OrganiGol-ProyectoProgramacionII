package modelo;

public abstract class Tarjeta {

    //Atributos
    private Jugador jugador;
    private Partido partido;
    private int minuto;
    private String detalle;

    //Metodos
    public abstract int fechasDeSancion();

    public abstract String getTipo();
}
