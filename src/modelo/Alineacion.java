
package modelo;

import negocio.ReglasJuego;
import java.util.ArrayList;
import java.util.List;

public class Alineacion {

    private Equipo equipo;
    private List<Jugador> titulares;
    private List<Jugador> suplentes;
    private Jugador capitan;

    public Alineacion(Equipo equipo, List<Jugador> titulares,
                      List<Jugador> suplentes, Jugador capitan) {
        this.equipo = equipo;
        this.titulares = titulares == null
                ? new ArrayList<>() : new ArrayList<>(titulares);
        this.suplentes = suplentes == null
                ? new ArrayList<>() : new ArrayList<>(suplentes);
        this.capitan = capitan;
    }

    // ===== M2: RF05 =====

    public String validar() {

        if (equipo == null || titulares == null
                || suplentes == null || capitan == null) {
            return "Error: campos obligatorios incompletos.";
        }

        if (titulares.size() < ReglasJuego.TITULARES_MIN
                || titulares.size() > ReglasJuego.TITULARES_MAX) {
            return "Error: debe presentar entre 7 y 11 titulares.";
        }

        if (suplentes.size() > ReglasJuego.SUPLENTES_MAX) {
            return "Error: puede presentar como máximo 7 suplentes.";
        }

        // Validar titulares
        for (Jugador jugador : titulares) {
            String error = validarJugador(jugador);
            if (error != null) {
                return error;
            }
        }

        // Validar suplentes
        for (Jugador jugador : suplentes) {
            String error = validarJugador(jugador);
            if (error != null) {
                return error;
            }
        }

        // El capitán debe estar entre los titulares
        if (!esTitular(capitan)) {
            return "Error: el capitán debe ser uno de los titulares.";
        }

        return null;
    }

    private String validarJugador(Jugador jugador) {

        if (jugador == null) {
            return "Error: campos obligatorios incompletos.";
        }

        String nombre = jugador.getNombres() + " "
                + jugador.getApellidos();

        if (jugador.getEquipo() != equipo) {
            return "Error: el jugador '" + nombre
                    + "' no pertenece al equipo '"
                    + equipo.getNombre() + "'.";
        }

        if (jugador.getEstado() == EstadoJugador.SUSPENDIDO) {
            return "Error: el jugador '" + nombre
                    + "' está suspendido ("
                    + jugador.fechasPendientes()
                    + " fecha(s) pendiente(s)).";
        }

        if (jugador.getEstado() == EstadoJugador.DE_BAJA) {
            return "Error: el jugador '" + nombre
                    + "' está dado de baja.";
        }

        if (contarRepeticiones(jugador) > 1) {
            return "Error: el jugador '" + nombre
                    + "' está repetido en la lista.";
        }

        return null;
    }

    private int contarRepeticiones(Jugador jugador) {
        int cantidad = 0;

        for (Jugador titular : titulares) {
            if (titular == jugador) {
                cantidad++;
            }
        }

        for (Jugador suplente : suplentes) {
            if (suplente == jugador) {
                cantidad++;
            }
        }

        return cantidad;
    }

    public boolean contiene(Jugador jugador) {
        return esTitular(jugador) || esSuplente(jugador);
    }

    public boolean esTitular(Jugador jugador) {
        return titulares.contains(jugador);
    }

    public boolean esSuplente(Jugador jugador) {
        return suplentes.contains(jugador);
    }

    // Getters

    public Equipo getEquipo() {
        return equipo;
    }

    public List<Jugador> getTitulares() {
        return titulares;
    }

    public List<Jugador> getSuplentes() {
        return suplentes;
    }

    public Jugador getCapitan() {
        return capitan;
    }
}
