package modelo;

public class TarjetaAmarilla extends Tarjeta {

    //Atributos
    private boolean esSegundaAmarilla;

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
