package modelo;

import java.util.List;

public class Alineacion {

    //Atributos
    private Equipo equipo;
    private List<Jugador> titulares;
    private List<Jugador> suplentes;
    private Jugador capitan;

    //Metodos
    public void validar() {

    }

    public boolean contiene(Jugador jugador) {
        return false;
    }

    public boolean esTitular(Jugador jugador) {
        return false;
    }

    public boolean esSuplente(Jugador jugador) {
        return false;
    }
}
