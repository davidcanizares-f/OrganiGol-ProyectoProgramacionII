package modelo;

import java.util.List;

public class Equipo {

    //Atributos
    private String nombre;
    private String barrio;
    private String delegado;
    private String telefonoDelegado;
    private List<Jugador> jugadores;

    //Metodos
    public void agregarJugador(Jugador jugador) {

    }

    public boolean tieneCupo() {
        return false;
    }

    public boolean camisetaDisponible(int numero) {
        return false;
    }

    public int cantidadJugadores() {
        return 0;
    }

    public Jugador buscarJugador(String carne) {
        return null;
    }

    public void modificar(String nombre, String barrio, String delegado, String telefono) {

    }

    public int descontarFechas(Partido partido) {
        return 0;
    }
}
