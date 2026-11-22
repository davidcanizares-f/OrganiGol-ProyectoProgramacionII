package interfaz;

import modelo.Alineacion;
import modelo.Cambio;
import modelo.Campeonato;
import modelo.Equipo;
import modelo.EstadoPartido;
import modelo.Gol;
import modelo.Jugador;
import modelo.Partido;
import negocio.ReglasJuego;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Menú del módulo 3: registro del partido (RF06, RF07 y RF08).
 * Uso: new MenuRegistroPartido().mostrar(campeonato, scanner);
 */
public class MenuRegistroPartido {

    public void mostrar(Campeonato campeonato, Scanner sc) {
        if (sc == null) {
            return;
        }
        int opcion;
        do {
            imprimirMenu();
            opcion = leerEntero(sc, "Opción: ");
            System.out.println();

            switch (opcion) {
                case 1:
                    abrirRegistro(campeonato, sc);
                    break;
                case 2:
                    finalizarRegistro(campeonato, sc);
                    break;
                case 3:
                    consultarEstado(campeonato, sc);
                    break;
                case 4:
                    registrarGol(campeonato, sc);
                    break;
                case 5:
                    consultarGoles(campeonato, sc);
                    break;
                case 6:
                    registrarCambio(campeonato, sc);
                    break;
                case 7:
                    consultarCambios(campeonato, sc);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void imprimirMenu() {
        System.out.println();
        System.out.println("=== REGISTRO DEL PARTIDO ===");
        System.out.println("1. Abrir registro");
        System.out.println("2. Finalizar registro");
        System.out.println("3. Consultar estado y marcador");
        System.out.println("4. Registrar gol");
        System.out.println("5. Consultar goles");
        System.out.println("6. Registrar cambio");
        System.out.println("7. Consultar cambios");
        System.out.println("0. Volver");
    }

    // ======================================================================================
    // RF06: Abrir y finalizar el registro del partido
    // ======================================================================================

    private void abrirRegistro(Campeonato campeonato, Scanner sc) {
        Partido partido = seleccionarPartido(campeonato, sc);
        if (partido == null) {
            return;
        }

        String error = partido.validarAbrirRegistro();
        if (error != null) {
            System.out.println(error);
            return;
        }

        partido.abrirRegistro();
        System.out.println("Registro abierto: " + partido.getLocal().getNombre() + " vs "
                + partido.getVisitante().getNombre() + ". Ya puede anotar goles, cambios y tarjetas.");
    }

    private void finalizarRegistro(Campeonato campeonato, Scanner sc) {
        Partido partido = seleccionarPartido(campeonato, sc);
        if (partido == null) {
            return;
        }

        String error = partido.validarFinalizarRegistro();
        if (error != null) {
            System.out.println(error);
            return;
        }

        String confirmacion = leerTexto(sc, "¿Confirmar la finalización del registro? (S/N): ");
        System.out.println();
        if (!confirmacion.equalsIgnoreCase("S")) {
            System.out.println("Finalización cancelada.");
            return;
        }

        partido.finalizarRegistro();
        System.out.println("Registro finalizado. Marcador final: " + partido.getMarcador() + ".");
    }

    private void consultarEstado(Campeonato campeonato, Scanner sc) {
        Partido partido = seleccionarPartido(campeonato, sc);
        if (partido == null) {
            return;
        }

        System.out.println("Estado del partido: " + partido.getEstado() + ". Marcador: "
                + partido.getMarcador() + ".");
    }

    // ======================================================================================
    // RF07: Registrar y consultar goles
    // ======================================================================================

    private void registrarGol(Campeonato campeonato, Scanner sc) {
        Partido partido = seleccionarPartido(campeonato, sc);
        if (partido == null) {
            return;
        }

        if (partido.getEstado() != EstadoPartido.EN_REGISTRO) {
            System.out.println("Error: el partido no está en registro.");
            return;
        }

        List<Jugador> jugadoresDisponibles = obtenerJugadoresAlineacion(partido);
        if (jugadoresDisponibles.isEmpty()) {
            System.out.println("Error: debe seleccionar un jugador.");
            return;
        }

        System.out.println("Jugadores:");
        for (int i = 0; i < jugadoresDisponibles.size(); i++) {
            Jugador j = jugadoresDisponibles.get(i);
            System.out.println((i + 1) + ". " + j.getNombres() + " " + j.getApellidos()
                    + " (" + j.getEquipo().getNombre() + ")");
        }

        String textoOpcion = leerTexto(sc, "Jugador que anotó: ");
        System.out.println();
        if (textoOpcion.isEmpty()) {
            System.out.println("Error: campos obligatorios incompletos.");
            return;
        }
        int indice;
        try {
            indice = Integer.parseInt(textoOpcion);
        } catch (NumberFormatException e) {
            System.out.println("Error: debe seleccionar un jugador.");
            return;
        }
        if (indice < 1 || indice > jugadoresDisponibles.size()) {
            System.out.println("Error: debe seleccionar un jugador.");
            return;
        }
        Jugador jugador = jugadoresDisponibles.get(indice - 1);

        String textoMinuto = leerTexto(sc, "Minuto (1 a 80): ");
        System.out.println();
        if (textoMinuto.isEmpty()) {
            System.out.println("Error: campos obligatorios incompletos.");
            return;
        }
        int minuto;
        try {
            minuto = Integer.parseInt(textoMinuto);
        } catch (NumberFormatException e) {
            System.out.println("Error: el minuto debe estar entre 1 y 80.");
            return;
        }

        String error = partido.validarGol(jugador, minuto);
        if (error != null) {
            System.out.println(error);
            return;
        }

        Gol gol = partido.registrarGol(jugador, minuto);
        if (gol != null) {
            System.out.println("Gol de " + jugador.getNombres() + " " + jugador.getApellidos()
                    + " (" + gol.getEquipo().getNombre() + ") en el minuto " + minuto
                    + ". Marcador: " + partido.getMarcador() + ".");
        }
    }

    private void consultarGoles(Campeonato campeonato, Scanner sc) {
        Partido partido = seleccionarPartido(campeonato, sc);
        if (partido == null) {
            return;
        }

        if (partido.getGoles().isEmpty()) {
            System.out.println("Todavía no hay goles registrados en este partido.");
            return;
        }

        System.out.println("Marcador: " + partido.getMarcador());
        List<Gol> ordenados = new ArrayList<>(partido.getGoles());
        ordenados.sort((a, b) -> Integer.compare(a.getMinuto(), b.getMinuto()));
        for (Gol g : ordenados) {
            System.out.println("Minuto " + g.getMinuto() + " - " + g.getJugador().getNombres()
                    + " " + g.getJugador().getApellidos() + " (" + g.getEquipo().getNombre() + ")");
        }
    }

    // ======================================================================================
    // RF08: Registrar y consultar cambios
    // ======================================================================================

    private void registrarCambio(Campeonato campeonato, Scanner sc) {
        Partido partido = seleccionarPartido(campeonato, sc);
        if (partido == null) {
            return;
        }

        if (partido.getEstado() != EstadoPartido.EN_REGISTRO) {
            System.out.println("Error: el partido no está en registro.");
            return;
        }

        System.out.println("Equipo: 1. " + partido.getLocal().getNombre()
                + "  2. " + partido.getVisitante().getNombre());
        String textoEquipo = leerTexto(sc, "Opción: ");
        System.out.println();
        if (textoEquipo.isEmpty()) {
            System.out.println("Error: campos obligatorios incompletos.");
            return;
        }
        int opcionEquipo;
        try {
            opcionEquipo = Integer.parseInt(textoEquipo);
        } catch (NumberFormatException e) {
            System.out.println("Error: el equipo seleccionado no juega en este partido.");
            return;
        }
        Equipo equipo = (opcionEquipo == 1) ? partido.getLocal()
                : (opcionEquipo == 2) ? partido.getVisitante() : null;
        if (equipo == null) {
            System.out.println("Error: el equipo seleccionado no juega en este partido.");
            return;
        }

        if (contarCambios(partido, equipo) >= ReglasJuego.CAMBIOS_MAXIMOS) {
            System.out.println("Error: el equipo ya usó los 5 cambios permitidos.");
            return;
        }

        Alineacion alineacion = partido.getAlineacion(equipo);
        if (alineacion == null) {
            System.out.println("Error: campos obligatorios incompletos.");
            return;
        }

        // Listar jugadores del equipo (titulares y suplentes que ya entraron)
        List<Jugador> jugadoresEnCancha = obtenerJugadoresEnCancha(partido, alineacion);
        if (jugadoresEnCancha.isEmpty()) {
            System.out.println("Error: campos obligatorios incompletos.");
            return;
        }
        System.out.println("Jugador que sale:");
        for (int i = 0; i < jugadoresEnCancha.size(); i++) {
            Jugador j = jugadoresEnCancha.get(i);
            System.out.println((i + 1) + ". #" + j.getNumeroCamiseta() + " " + j.getNombres() + " " + j.getApellidos());
        }
        String textoSale = leerTexto(sc, "Opción: ");
        System.out.println();
        if (textoSale.isEmpty()) {
            System.out.println("Error: campos obligatorios incompletos.");
            return;
        }
        int indiceSale;
        try {
            indiceSale = Integer.parseInt(textoSale);
        } catch (NumberFormatException e) {
            System.out.println("Error: campos obligatorios incompletos.");
            return;
        }
        if (indiceSale < 1 || indiceSale > jugadoresEnCancha.size()) {
            System.out.println("Error: campos obligatorios incompletos.");
            return;
        }
        Jugador sale = jugadoresEnCancha.get(indiceSale - 1);

        // Listar suplentes disponibles del equipo
        List<Jugador> suplentesDisponibles = obtenerSuplentesDisponibles(partido, alineacion);
        if (suplentesDisponibles.isEmpty()) {
            System.out.println("Error: campos obligatorios incompletos.");
            return;
        }
        System.out.println("Jugador que entra (suplente):");
        for (int i = 0; i < suplentesDisponibles.size(); i++) {
            Jugador j = suplentesDisponibles.get(i);
            System.out.println((i + 1) + ". #" + j.getNumeroCamiseta() + " " + j.getNombres() + " " + j.getApellidos());
        }
        String textoEntra = leerTexto(sc, "Opción: ");
        System.out.println();
        if (textoEntra.isEmpty()) {
            System.out.println("Error: campos obligatorios incompletos.");
            return;
        }
        int indiceEntra;
        try {
            indiceEntra = Integer.parseInt(textoEntra);
        } catch (NumberFormatException e) {
            System.out.println("Error: campos obligatorios incompletos.");
            return;
        }
        if (indiceEntra < 1 || indiceEntra > suplentesDisponibles.size()) {
            System.out.println("Error: campos obligatorios incompletos.");
            return;
        }
        Jugador entra = suplentesDisponibles.get(indiceEntra - 1);

        String textoMinuto = leerTexto(sc, "Minuto (1 a 80): ");
        System.out.println();
        if (textoMinuto.isEmpty()) {
            System.out.println("Error: campos obligatorios incompletos.");
            return;
        }
        int minuto;
        try {
            minuto = Integer.parseInt(textoMinuto);
        } catch (NumberFormatException e) {
            System.out.println("Error: el minuto debe estar entre 1 y 80.");
            return;
        }

        String error = partido.validarCambio(equipo, sale, entra, minuto);
        if (error != null) {
            System.out.println(error);
            return;
        }

        Cambio cambio = partido.registrarCambio(equipo, sale, entra, minuto);
        if (cambio != null) {
            int usados = contarCambios(partido, equipo);
            System.out.println("Cambio registrado en el minuto " + minuto + ": sale "
                    + sale.getNombres() + " " + sale.getApellidos() + ", entra "
                    + entra.getNombres() + " " + entra.getApellidos() + " (" + equipo.getNombre()
                    + "). Cambios usados: " + usados + " de " + ReglasJuego.CAMBIOS_MAXIMOS + ".");
        }
    }

    private void consultarCambios(Campeonato campeonato, Scanner sc) {
        Partido partido = seleccionarPartido(campeonato, sc);
        if (partido == null) {
            return;
        }

        if (partido.getCambios().isEmpty()) {
            System.out.println("Todavía no hay cambios registrados en este partido.");
            return;
        }

        List<Cambio> ordenados = new ArrayList<>(partido.getCambios());
        ordenados.sort((a, b) -> Integer.compare(a.getMinuto(), b.getMinuto()));
        for (Cambio c : ordenados) {
            System.out.println("Minuto " + c.getMinuto() + " - " + c.getEquipo().getNombre()
                    + ": sale " + c.getJugadorSale().getNombres() + " " + c.getJugadorSale().getApellidos()
                    + ", entra " + c.getJugadorEntra().getNombres() + " " + c.getJugadorEntra().getApellidos());
        }
    }

    // ======================================================================================
    // Métodos auxiliares de selección y lectura
    // ======================================================================================

    private Partido seleccionarPartido(Campeonato campeonato, Scanner sc) {
        if (campeonato == null || campeonato.getPartidos().isEmpty()) {
            System.out.println("El campeonato no tiene partidos programados.");
            return null;
        }
        List<Partido> partidos = campeonato.getPartidos();
        System.out.println("Partidos:");
        for (int i = 0; i < partidos.size(); i++) {
            Partido p = partidos.get(i);
            System.out.println((i + 1) + ". " + p.getLocal().getNombre() + " vs "
                    + p.getVisitante().getNombre() + " - " + p.getEstado());
        }
        int opcion = leerEntero(sc, "Partido: ");
        System.out.println();
        if (opcion < 1 || opcion > partidos.size()) {
            System.out.println("Error: debe seleccionar un partido.");
            return null;
        }
        return partidos.get(opcion - 1);
    }

    private List<Jugador> obtenerJugadoresAlineacion(Partido partido) {
        List<Jugador> lista = new ArrayList<>();
        Alineacion local = partido.getAlineacion(partido.getLocal());
        if (local != null) {
            lista.addAll(local.getTitulares());
            lista.addAll(local.getSuplentes());
        }
        Alineacion visitante = partido.getAlineacion(partido.getVisitante());
        if (visitante != null) {
            lista.addAll(visitante.getTitulares());
            lista.addAll(visitante.getSuplentes());
        }
        return lista;
    }

    private List<Jugador> obtenerJugadoresEnCancha(Partido partido, Alineacion alineacion) {
        List<Jugador> enCancha = new ArrayList<>();
        Equipo equipo = alineacion.getEquipo();

        for (Jugador t : alineacion.getTitulares()) {
            boolean salio = false;
            for (Cambio c : partido.getCambios()) {
                if (c.getEquipo() == equipo && c.getJugadorSale() == t) {
                    salio = true;
                    break;
                }
            }
            if (!salio && !partido.estaExpulsado(t)) {
                enCancha.add(t);
            }
        }

        for (Jugador s : alineacion.getSuplentes()) {
            boolean entro = false;
            boolean salio = false;
            for (Cambio c : partido.getCambios()) {
                if (c.getEquipo() == equipo && c.getJugadorEntra() == s) {
                    entro = true;
                }
                if (c.getEquipo() == equipo && c.getJugadorSale() == s) {
                    salio = true;
                }
            }
            if (entro && !salio && !partido.estaExpulsado(s)) {
                enCancha.add(s);
            }
        }

        return enCancha;
    }

    private List<Jugador> obtenerSuplentesDisponibles(Partido partido, Alineacion alineacion) {
        List<Jugador> disponibles = new ArrayList<>();
        Equipo equipo = alineacion.getEquipo();

        for (Jugador s : alineacion.getSuplentes()) {
            boolean yaJugo = false;
            for (Cambio c : partido.getCambios()) {
                if (c.getEquipo() == equipo && (c.getJugadorEntra() == s || c.getJugadorSale() == s)) {
                    yaJugo = true;
                    break;
                }
            }
            if (!yaJugo && !partido.estaExpulsado(s)) {
                disponibles.add(s);
            }
        }

        return disponibles;
    }

    private int contarCambios(Partido partido, Equipo equipo) {
        int cantidad = 0;
        for (Cambio c : partido.getCambios()) {
            if (c.getEquipo() == equipo) {
                cantidad++;
            }
        }
        return cantidad;
    }

    private String leerTexto(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        if (!sc.hasNextLine()) {
            return "";
        }
        return sc.nextLine().trim();
    }

    private int leerEntero(Scanner sc, String mensaje) {
        String texto = leerTexto(sc, mensaje);
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
