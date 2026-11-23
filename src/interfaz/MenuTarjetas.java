package interfaz;

import modelo.Alineacion;
import modelo.Campeonato;
import modelo.Equipo;
import modelo.EstadoPartido;
import modelo.Jugador;
import modelo.Partido;
import modelo.Sancion;
import modelo.Tarjeta;
import modelo.TarjetaAmarilla;
import modelo.TarjetaRoja;
import negocio.Validador;
import negocio.ReglasJuego;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Menú del módulo 5: tarjetas y sanciones (RF12, RF13 y RF14).
 * Uso: new MenuTarjetas().mostrar(campeonato, scanner);
 */
public class MenuTarjetas {

    private Campeonato campeonato;
    private Scanner scanner;

    public void mostrar(Campeonato campeonato, Scanner scanner) {
        this.campeonato = campeonato;
        this.scanner = scanner;
        int opcion;
        do {
            System.out.println();
            System.out.println("=== TARJETAS Y SANCIONES ===");
            System.out.println("1. Registrar tarjeta");
            System.out.println("2. Consultar tarjetas de un partido");
            System.out.println("3. Consultar jugadores en cancha");
            System.out.println("4. Consultar sanciones vigentes");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1: registrarTarjeta(); break;
                case 2: consultarTarjetas(); break;
                case 3: consultarEnCancha(); break;
                case 4: consultarSanciones(); break;
                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    // ------------------------------------------------------------------ RF12 registrar
    private void registrarTarjeta() {
        Partido partido = seleccionarPartido();
        if (partido == null) return;
        String errorEstado = partido.validarPartidoEnRegistro();
        if (errorEstado != null) {
            System.out.println(errorEstado);
            return;
        }
        Jugador jugador = seleccionarJugador(partido, null, false);
        if (jugador == null) return;

        System.out.println("Tipo de tarjeta: 1. Amarilla  2. Roja");
        int t = leerEntero("Opción: ");
        String tipo = (t == 1) ? "AMARILLA" : (t == 2) ? "ROJA" : null;

        String textoMinuto = leerTexto("Minuto (1 a 80): ");
        if (textoMinuto.isEmpty()) {
            System.out.println("Error: campos obligatorios incompletos.");
            return;
        }
        int minuto = Validador.aEntero(textoMinuto);
        String detalle = leerTexto("Detalle del informe (opcional, 5 a 100 caracteres): ");

        // Estado antes de registrar, para saber qué mensajes mostrar
        Sancion ultimaAntes = ultimaSancion(jugador);
        int pendientesAntes = jugador.fechasPendientes();
        boolean estabaSuspendidoElPartido = partido.getEstado() == EstadoPartido.SUSPENDIDO;

        String error = partido.validarTarjeta(jugador, tipo, minuto, detalle);
        if (error != null) {
            System.out.println(error);
            return;
        }
        Tarjeta tarjeta = partido.registrarTarjeta(jugador, tipo, minuto, detalle);
        String nombre = nombreCompleto(jugador);

        // --- RF12: mensaje de la tarjeta ---
        if (tarjeta instanceof TarjetaRoja) {
            TarjetaRoja roja = (TarjetaRoja) tarjeta;
            if (roja.isPorDobleAmarilla()) {
                System.out.println("Segunda amarilla: " + nombre
                        + " queda expulsado (roja por doble amarilla) en el minuto " + minuto + ".");
            } else {
                System.out.println("Tarjeta roja registrada para " + nombre
                        + " en el minuto " + minuto + ". El jugador queda expulsado.");
            }
        } else {
            System.out.println("Tarjeta amarilla registrada para " + nombre + " en el minuto " + minuto + ".");
            if (ultimaSancion(jugador) != ultimaAntes) {
                System.out.println("El jugador " + nombre
                        + " alcanzó 5 amarillas: se aplicará una suspensión y el contador se reinicia.");
            } else {
                System.out.println("El jugador " + nombre + " acumula " + jugador.getAmarillasAcumuladas()
                        + " de " + ReglasJuego.AMARILLAS_PARA_SUSPENDER + " amarillas.");
            }
        }

        // --- RF14: mensajes de la sanción generada ---
        Sancion nueva = ultimaSancion(jugador);
        if (nueva != ultimaAntes) {
            String causa;
            if (Sancion.MOTIVO_ACUMULACION.equals(nueva.getMotivo())) {
                causa = "por acumulación de " + ReglasJuego.AMARILLAS_PARA_SUSPENDER + " tarjetas amarillas";
            } else if (Sancion.MOTIVO_DOBLE_AMARILLA.equals(nueva.getMotivo())) {
                causa = "por doble amarilla";
            } else {
                causa = "por tarjeta roja";
            }
            if (pendientesAntes > 0) {
                System.out.println("Se sumaron " + nueva.getFechasPendientes() + " fecha(s) a las pendientes de '"
                        + nombre + "' (total: " + jugador.fechasPendientes() + ").");
            } else {
                System.out.println("Jugador '" + nombre + "' suspendido por " + nueva.getFechasPendientes()
                        + (nueva.getFechasPendientes() == 1 ? " fecha " : " fechas ") + causa + ".");
            }
        }

        // --- RF13: ¿el partido quedó suspendido? ---
        if (!estabaSuspendidoElPartido && partido.getEstado() == EstadoPartido.SUSPENDIDO) {
            Equipo equipo = jugador.getEquipo();
            Equipo rival = (equipo == partido.getLocal()) ? partido.getVisitante() : partido.getLocal();
            System.out.println("El equipo '" + equipo.getNombre() + "' quedó con "
                    + partido.jugadoresEnCancha(equipo) + " jugadores (mínimo "
                    + ReglasJuego.MINIMO_EN_CANCHA + "): partido suspendido. Victoria 3 - 0 para '"
                    + rival.getNombre() + "'.");
        }
    }

    // ------------------------------------------------------------------ RF12 consultar
    private void consultarTarjetas() {
        Partido partido = seleccionarPartido();
        if (partido == null) return;

        Equipo equipo = seleccionarEquipoDelPartido(partido, "Filtrar por equipo");
        Jugador jugador = null;
        if (partido.getAlineacion(partido.getLocal()) != null || partido.getAlineacion(partido.getVisitante()) != null) {
            jugador = seleccionarJugador(partido, equipo, true);
        }

        System.out.println("Tipo: 1. Todas  2. Amarilla  3. Roja");
        int t = leerEntero("Opción: ");
        String tipo = (t == 2) ? "AMARILLA" : (t == 3) ? "ROJA" : null;

        List<Tarjeta> lista = partido.filtrarTarjetas(equipo, jugador, tipo);
        if (lista.isEmpty()) {
            System.out.println("No hay tarjetas registradas con los filtros seleccionados.");
            return;
        }
        System.out.printf("%-6s %-26s %-20s %-10s %s%n", "Min", "Jugador", "Equipo", "Tipo", "Detalle");
        for (Tarjeta tj : lista) {
            String tipoTxt = tj.getTipo();
            if (tj instanceof TarjetaAmarilla && ((TarjetaAmarilla) tj).isEsSegundaAmarilla()) {
                tipoTxt += " (2.ª)";
            } else if (tj instanceof TarjetaRoja && ((TarjetaRoja) tj).isPorDobleAmarilla()) {
                tipoTxt += " (doble)";
            }
            System.out.printf("%-6d %-26s %-20s %-10s %s%n", tj.getMinuto(),
                    nombreCompleto(tj.getJugador()), tj.getJugador().getEquipo().getNombre(), tipoTxt,
                    tj.getDetalle() == null ? "-" : tj.getDetalle());
        }
    }

    // ------------------------------------------------------------------ RF13 consultar
    private void consultarEnCancha() {
        Partido partido = seleccionarPartido();
        if (partido == null) return;
        Equipo equipo = seleccionarEquipoDelPartido(partido, "Equipo a consultar");
        String error = partido.validarConsultaEnCancha(equipo);
        if (error != null) {
            System.out.println(error);
            return;
        }
        List<Equipo> equipos = new ArrayList<>();
        if (equipo == null) {
            equipos.add(partido.getLocal());
            equipos.add(partido.getVisitante());
        } else {
            equipos.add(equipo);
        }
        for (Equipo e : equipos) {
            System.out.println("El equipo '" + e.getNombre() + "' tiene " + partido.jugadoresEnCancha(e)
                    + " jugadores en cancha. Mínimo permitido: " + ReglasJuego.MINIMO_EN_CANCHA + ".");
        }
    }

    // ------------------------------------------------------------------ RF14 consultar
    private void consultarSanciones() {
        Equipo equipo = null;
        List<Equipo> equipos = campeonato.getEquipos();
        if (!equipos.isEmpty()) {
            System.out.println("Filtrar por equipo (0 = todos):");
            for (int i = 0; i < equipos.size(); i++) {
                System.out.println((i + 1) + ". " + equipos.get(i).getNombre());
            }
            int op = leerEntero("Opción: ");
            if (op >= 1 && op <= equipos.size()) {
                equipo = equipos.get(op - 1);
            }
        }
        List<Sancion> lista = campeonato.sancionesVigentes(equipo);
        if (lista.isEmpty()) {
            System.out.println("No hay jugadores suspendidos.");
            return;
        }
        System.out.printf("%-26s %-20s %-8s %-26s %s%n", "Jugador", "Equipo", "Fechas", "Motivo", "Partido de origen");
        for (Sancion s : lista) {
            Partido o = s.getPartidoOrigen();
            System.out.printf("%-26s %-20s %-8d %-26s %s%n", nombreCompleto(s.getJugador()),
                    s.getJugador().getEquipo().getNombre(), s.getFechasPendientes(), s.getMotivo(),
                    o.getLocal().getNombre() + " vs " + o.getVisitante().getNombre());
        }
    }

    // ------------------------------------------------------------------ selección
    private Partido seleccionarPartido() {
        List<Partido> partidos = campeonato.getPartidos();
        if (partidos.isEmpty()) {
            System.out.println("El campeonato no tiene partidos programados.");
            return null;
        }
        System.out.println("Partidos:");
        for (int i = 0; i < partidos.size(); i++) {
            Partido p = partidos.get(i);
            System.out.println((i + 1) + ". " + p.getLocal().getNombre() + " vs "
                    + p.getVisitante().getNombre() + " - " + p.getEstado());
        }
        int op = leerEntero("Partido: ");
        if (op < 1 || op > partidos.size()) {
            System.out.println("Error: debe seleccionar un partido.");
            return null;
        }
        return partidos.get(op - 1);
    }

    /* Devuelve null si el usuario elige "todos". */
    private Equipo seleccionarEquipoDelPartido(Partido partido, String titulo) {
        System.out.println(titulo + ": 0. Todos  1. " + partido.getLocal().getNombre()
                + "  2. " + partido.getVisitante().getNombre());
        int op = leerEntero("Opción: ");
        if (op == 1) return partido.getLocal();
        if (op == 2) return partido.getVisitante();
        return null;
    }

    /*
     * Lista los jugadores de las alineaciones del partido (filtrando por equipo si se indica).
     * Si opcional es true, 0 significa "sin filtro" y devuelve null.
     */
    private Jugador seleccionarJugador(Partido partido, Equipo equipo, boolean opcional) {
        List<Jugador> candidatos = new ArrayList<>();
        for (Equipo e : new Equipo[]{partido.getLocal(), partido.getVisitante()}) {
            if (equipo != null && e != equipo) continue;
            Alineacion a = partido.getAlineacion(e);
            if (a != null) {
                candidatos.addAll(a.getTitulares());
                candidatos.addAll(a.getSuplentes());
            }
        }
        if (candidatos.isEmpty()) {
            if (!opcional) {
                System.out.println("Error: debe seleccionar un jugador.");
            }
            return null;
        }
        System.out.println("Jugadores de la alineación" + (opcional ? " (0 = todos)" : "") + ":");
        for (int i = 0; i < candidatos.size(); i++) {
            Jugador j = candidatos.get(i);
            System.out.println((i + 1) + ". #" + j.getNumeroCamiseta() + " " + nombreCompleto(j)
                    + " (" + j.getEquipo().getNombre() + ")");
        }
        int op = leerEntero("Jugador: ");
        if (op < 1 || op > candidatos.size()) {
            if (!opcional) {
                System.out.println("Error: debe seleccionar un jugador.");
            }
            return null;
        }
        return candidatos.get(op - 1);
    }

    // ------------------------------------------------------------------ utilidades
    private Sancion ultimaSancion(Jugador j) {
        List<Sancion> lista = j.getSanciones();
        return lista.isEmpty() ? null : lista.get(lista.size() - 1);
    }

    private String nombreCompleto(Jugador j) {
        return j.getNombres() + " " + j.getApellidos();
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        String texto = leerTexto(mensaje);
        return Validador.aEntero(texto);
    }
}
