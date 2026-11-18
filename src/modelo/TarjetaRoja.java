package modelo;

public class TarjetaRoja extends Tarjeta {

    //Atributos
    private boolean porDobleAmarilla;

    //Metodos
    @Override
    public int fechasDeSancion() {
        return 0;
    }

    @Override
    public String getTipo() {
        return null;
    }
}
