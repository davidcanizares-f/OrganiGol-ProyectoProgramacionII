package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import negocio.ReglasJuego;
import java.time.format.DateTimeFormatter;

public class Partido {

    //Atributos
    private Equipo local;
    private Equipo visitante;
    private LocalDate fecha;
    private LocalTime hora;
    private String cancha;
    private EstadoPartido estado;
    private Alineacion alineacionLocal;
    private Alineacion alineacionVisitante;
    private List<Gol> goles;
    private List<Cambio> cambios;
    private List<Tarjeta> tarjetas;
    private ResultadoPartido resultado;
    private String observaciones;
    private Equipo equipoSuspendido;

    //Constructor
    public Partido(Equipo local, Equipo visitante, LocalDate fecha, LocalTime hora, String cancha) {
        this.local = local;
        this.visitante = visitante;
        this.fecha = fecha;
        this.hora = hora;
        this.cancha = cancha;
        this.estado = EstadoPartido.PROGRAMADO;
        this.goles = new ArrayList<>();
        this.cambios = new ArrayList<>();
        this.tarjetas = new ArrayList<>();
    }

    //Metodos

    // ===== M2: RF05 =====

    public String validarAlineacion(Alineacion alineacion) {

        if (alineacion == null) {
            return "Error: campos obligatorios incompletos.";
        }

        if (estado != EstadoPartido.PROGRAMADO) {
            return "Error: el partido no está en estado programado.";
        }

        Equipo equipo = alineacion.getEquipo();

        if (equipo == null) {
            return "Error: campos obligatorios incompletos.";
        }

        if (equipo != local && equipo != visitante) {
            return "Error: el equipo seleccionado no juega en este partido.";
        }

        return alineacion.validar();
    }

    public boolean registrarAlineacion(Alineacion alineacion) {

        if (validarAlineacion(alineacion) != null) {
            return false;
        }

        Equipo equipo = alineacion.getEquipo();
        boolean reemplazada;

        if (equipo == local) {
            reemplazada = alineacionLocal != null;
            alineacionLocal = alineacion;
        } else {
            reemplazada = alineacionVisitante != null;
            alineacionVisitante = alineacion;
        }

        return reemplazada;
    }

    public Alineacion getAlineacion(Equipo equipo) {

        if (equipo == null) {
            return null;
        }

        if (equipo == local) {
            return alineacionLocal;
        }

        if (equipo == visitante) {
            return alineacionVisitante;
        }

        return null;
    }


    public void abrirRegistro() {

    }

    public void finalizarRegistro() {

    }

    public Gol registrarGol(Jugador jugador, int minuto) {
        return null;
    }

    public Cambio registrarCambio(Equipo equipo, Jugador sale, Jugador entra, int minuto) {
        return null;
    }

    public boolean estaEnAlineacion(Jugador jugador) {
        return false;
    }

    public int golesDe(Equipo equipo) {
        return 0;
    }

    public String getMarcador() {
        return null;
    }


    // ===== M5 =====
    public String validarPartidoEnRegistro() {
        if (estado != EstadoPartido.EN_REGISTRO) {
            return "Error: el partido no está en registro.";
        }
        return null;
    }

    public String validarTarjeta(Jugador jugador, String tipo, int minuto, String detalle) {
        // Obligatorios
        if (jugador == null) {
            return "Error: debe seleccionar un jugador.";
        }
        if (tipo == null || !(tipo.equalsIgnoreCase("AMARILLA") || tipo.equalsIgnoreCase("ROJA"))) {
            return "Error: debe seleccionar el tipo de tarjeta (amarilla o roja).";
        }
        // Formato
        if (minuto < ReglasJuego.MINUTO_MINIMO || minuto > ReglasJuego.MINUTO_MAXIMO) {
            return "Error: el minuto debe estar entre 1 y 80.";
        }
        if (detalle != null && !detalle.trim().isEmpty()) {
            int largo = detalle.trim().length();
            if (largo < ReglasJuego.OBSERVACIONES_MIN || largo > ReglasJuego.OBSERVACIONES_MAX) {
                return "Error: el detalle del informe debe tener entre 5 y 100 caracteres.";
            }
        }
        // Reglas de negocio
        String errorEstado = validarPartidoEnRegistro();
        if (errorEstado != null) {
            return errorEstado;
        }
        if (!jugadorEnAlineacion(jugador)) {
            return "Error: el jugador '" + jugador.getNombres() + " " + jugador.getApellidos()
                    + "' no está en la alineación de este partido.";
        }
        if (estaExpulsado(jugador)) {
            return "Error: el jugador ya fue expulsado en este partido.";
        }
        return null;
    }

    // Si la validación falla no cambia nada y devuelve null.
    // Si es la segunda amarilla, devuelve la roja por doble amarilla.
    public Tarjeta registrarTarjeta(Jugador jugador, String tipo, int minuto, String detalle) {
        if (validarTarjeta(jugador, tipo, minuto, detalle) != null) {
            return null;
        }
        String texto = (detalle == null || detalle.trim().isEmpty()) ? null : detalle.trim();
        Equipo equipo = jugador.getEquipo();

        if (tipo.equalsIgnoreCase("AMARILLA")) {
            if (contarAmarillas(jugador) == 1) {
                // Segunda amarilla: se guarda y se convierte en roja; las 2 no suman al acumulado
                tarjetas.add(new TarjetaAmarilla(jugador, this, minuto, texto, true));
                jugador.restarAmarilla();
                Tarjeta roja = new TarjetaRoja(jugador, this, minuto, texto, true);
                tarjetas.add(roja);
                jugador.aplicarSancion(this, Sancion.MOTIVO_DOBLE_AMARILLA, roja.fechasDeSancion());
                verificarMinimo(equipo);
                return roja;
            }
            Tarjeta amarilla = new TarjetaAmarilla(jugador, this, minuto, texto, false);
            tarjetas.add(amarilla);
            jugador.sumarAmarilla(this);
            return amarilla;
        }

        Tarjeta roja = new TarjetaRoja(jugador, this, minuto, texto, false);
        tarjetas.add(roja);
        jugador.aplicarSancion(this, Sancion.MOTIVO_ROJA_DIRECTA, roja.fechasDeSancion());
        verificarMinimo(equipo);
        return roja;
    }

    private boolean jugadorEnAlineacion(Jugador jugador) {
        Alineacion alineacion = getAlineacion(jugador.getEquipo());
        return alineacion != null && alineacion.contiene(jugador);
    }

    private int contarAmarillas(Jugador jugador) {
        int total = 0;
        for (Tarjeta t : tarjetas) {
            if (t instanceof TarjetaAmarilla && t.getJugador() == jugador) {
                total++;
            }
        }
        return total;
    }

    public boolean estaExpulsado(Jugador jugador) {
        for (Tarjeta t : tarjetas) {
            if (t instanceof TarjetaRoja && t.getJugador() == jugador) {
                return true;
            }
        }
        return false;
    }

    // Titulares del equipo menos los expulsados (los cambios no modifican el conteo)
    public int jugadoresEnCancha(Equipo equipo) {
        Alineacion alineacion = getAlineacion(equipo);
        if (alineacion == null) {
            return 0;
        }
        int total = alineacion.getTitulares().size();
        for (Tarjeta t : tarjetas) {
            if (t instanceof TarjetaRoja && t.getJugador().getEquipo() == equipo) {
                total--;
            }
        }
        return total;
    }

    public boolean verificarMinimo(Equipo equipo) {
        if (jugadoresEnCancha(equipo) < ReglasJuego.MINIMO_EN_CANCHA) {
            suspenderPorFaltaDeJugadores(equipo);
            return true;
        }
        return false;
    }

    public void suspenderPorFaltaDeJugadores(Equipo equipo) {
        this.estado = EstadoPartido.SUSPENDIDO;
        this.equipoSuspendido = equipo;
    }

    // Filtros opcionales (null = sin filtro). Nunca devuelve null; ordena por minuto.
    public List<Tarjeta> filtrarTarjetas(Equipo equipo, Jugador jugador, String tipo) {
        List<Tarjeta> resultado = new ArrayList<>();
        for (Tarjeta t : tarjetas) {
            if (equipo != null && t.getJugador().getEquipo() != equipo) {
                continue;
            }
            if (jugador != null && t.getJugador() != jugador) {
                continue;
            }
            if (tipo != null && !t.getTipo().equalsIgnoreCase(tipo)) {
                continue;
            }
            resultado.add(t);
        }
        resultado.sort(Comparator.comparingInt(Tarjeta::getMinuto));
        return resultado;
    }

    public String validarConsultaEnCancha(Equipo equipo) {
        if (estado == EstadoPartido.PROGRAMADO) {
            return "Error: el partido todavía no tiene el registro abierto.";
        }
        if (equipo != null && equipo != local && equipo != visitante) {
            return "Error: el equipo seleccionado no juega en este partido.";
        }
        return null;
    }
    // ===== fin M5 =====

    //Getters y Setters
    public Equipo getLocal() {
        return local;
    }

    public Equipo getVisitante() {
        return visitante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getCancha() {
        return cancha;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    public Alineacion getAlineacionLocal() {
        return alineacionLocal;
    }

    public Alineacion getAlineacionVisitante() {
        return alineacionVisitante;
    }

    public List<Gol> getGoles() {
        return goles;
    }

    public List<Cambio> getCambios() {
        return cambios;
    }

    public List<Tarjeta> getTarjetas() {
        return tarjetas;
    }

    public ResultadoPartido getResultado() {
        return resultado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Equipo getEquipoSuspendido() {
        return equipoSuspendido;
    }

    //Setters que usa DatosDemo para armar partidos de prueba
    public void setEstado(EstadoPartido estado) {
        this.estado = estado;
    }

    public void setAlineacionLocal(Alineacion alineacionLocal) {
        this.alineacionLocal = alineacionLocal;
    }

    public void setAlineacionVisitante(Alineacion alineacionVisitante) {
        this.alineacionVisitante = alineacionVisitante;
    }

    public void setEquipoSuspendido(Equipo equipoSuspendido) {
        this.equipoSuspendido = equipoSuspendido;
    }

    // ======================================================================================
    // M O D U L O  4
    // =====================================================================================

    //RF10: compara los goles de los dos equipos (golesDe ya considera el 3-0 por suspension)
    public ResultadoPartido determinarResultado() {
        int golesLocal = golesDe(local);
        int golesVisitante = golesDe(visitante);
        if (golesLocal > golesVisitante) {
            return ResultadoPartido.VICTORIA_LOCAL;
        }
        if (golesVisitante > golesLocal) {
            return ResultadoPartido.VICTORIA_VISITANTE;
        }
        return ResultadoPartido.EMPATE;
    }

    //RF10: devuelve el mensaje de error si el partido no se puede cerrar, o null si se puede
    public String validarCierre(String observaciones) {
        if (estado == EstadoPartido.CERRADO) {
            return "Error: el partido ya está cerrado.";
        }
        if (estado != EstadoPartido.FINALIZADO && estado != EstadoPartido.SUSPENDIDO) {
            return "Error: el partido todavía no ha finalizado.";
        }
        //Las observaciones son opcionales, pero si se escriben deben tener de 5 a 100 caracteres
        if (observaciones != null && !observaciones.trim().isEmpty()) {
            int largo = observaciones.trim().length();
            if (largo < ReglasJuego.OBSERVACIONES_MIN || largo > ReglasJuego.OBSERVACIONES_MAX) {
                return "Error: las observaciones deben tener entre 5 y 100 caracteres.";
            }
        }
        return null;
    }

    //RF10: cierra el partido y descuenta las fechas de los jugadores suspendidos.
    //Si no se puede cerrar no hace nada
    public void cerrar(String observaciones) {
        if (validarCierre(observaciones) != null) {
            return;
        }

        String textoObservaciones = null;
        if (observaciones != null && !observaciones.trim().isEmpty()) {
            textoObservaciones = observaciones.trim();
        }

        this.resultado = determinarResultado();
        this.observaciones = textoObservaciones;
        this.estado = EstadoPartido.CERRADO;

        //Se descuenta una fecha a los suspendidos de los dos equipos.
        //No se descuenta la sancion que nacio en este mismo partido (eso lo controla Jugador.descontarFechas)
        local.descontarFechas(this);
        visitante.descontarFechas(this);
    }

    //RF09: devuelve el mensaje de error si todavia no se puede ver el resumen, o null si se puede
    public String validarEstadisticas() {
        if (estado == EstadoPartido.PROGRAMADO) {
            return "Error: el partido todavía no tiene el registro abierto.";
        }
        return null;
    }

    //RF09: resumen del partido (sirve mientras esta en registro y cuando ya termino).
    //Si todavia no se puede ver, devuelve null
    public String generarEstadisticas() {
        if (validarEstadisticas() != null) {
            return null;
        }

        //Mientras el partido no este cerrado el resultado es provisional
        String tipoResultado = "provisional";
        if (estado == EstadoPartido.CERRADO) {
            tipoResultado = "final";
        }

        StringBuilder texto = new StringBuilder();
        texto.append("=== ESTADÍSTICAS DEL PARTIDO ===\n");
        texto.append(local.getNombre()).append(" vs ").append(visitante.getNombre()).append("\n");
        texto.append("Estado: ").append(estado).append("\n");
        texto.append("Marcador: ").append(marcadorParaActa()).append("\n");
        texto.append(textoResultado(tipoResultado)).append("\n");
        if (equipoSuspendido != null) {
            texto.append(textoSuspension()).append("\n");
        }

        if (goles.isEmpty() && tarjetas.isEmpty()) {
            texto.append("El partido no tiene goles ni tarjetas registrados.\n");
        }

        //Goles por tiempo: primer tiempo del 1 al 40, segundo tiempo del 41 al 80
        int finPrimerTiempo = ReglasJuego.MINUTOS_POR_TIEMPO;
        int finSegundoTiempo = ReglasJuego.MINUTO_MAXIMO;
        texto.append("\nGOLES POR TIEMPO\n");
        texto.append("Primer tiempo (1 a ").append(finPrimerTiempo).append("): ")
                .append(local.getNombre()).append(" ")
                .append(contarGoles(local, ReglasJuego.MINUTO_MINIMO, finPrimerTiempo)).append(" - ")
                .append(contarGoles(visitante, ReglasJuego.MINUTO_MINIMO, finPrimerTiempo)).append(" ")
                .append(visitante.getNombre()).append("\n");
        texto.append("Segundo tiempo (").append(finPrimerTiempo + 1).append(" a ").append(finSegundoTiempo).append("): ")
                .append(local.getNombre()).append(" ")
                .append(contarGoles(local, finPrimerTiempo + 1, finSegundoTiempo)).append(" - ")
                .append(contarGoles(visitante, finPrimerTiempo + 1, finSegundoTiempo)).append(" ")
                .append(visitante.getNombre()).append("\n");
        if (equipoSuspendido != null) {
            texto.append("(Son los goles registrados antes de la suspensión; el marcador oficial es 3 - 0.)\n");
        }

        texto.append("\nGOLEADORES\n");
        texto.append(listaGoles());

        texto.append("\nTARJETAS\n");
        texto.append(local.getNombre()).append(": ")
                .append(contarTarjetas(local, "AMARILLA")).append(" amarilla(s), ")
                .append(contarTarjetas(local, "ROJA")).append(" roja(s)\n");
        texto.append(visitante.getNombre()).append(": ")
                .append(contarTarjetas(visitante, "AMARILLA")).append(" amarilla(s), ")
                .append(contarTarjetas(visitante, "ROJA")).append(" roja(s)\n");

        texto.append("\nCAMBIOS USADOS\n");
        texto.append(local.getNombre()).append(": ")
                .append(contarCambios(local)).append(" de ").append(ReglasJuego.CAMBIOS_MAXIMOS).append("\n");
        texto.append(visitante.getNombre()).append(": ")
                .append(contarCambios(visitante)).append(" de ").append(ReglasJuego.CAMBIOS_MAXIMOS).append("\n");

        texto.append("\nEXPULSADOS\n");
        texto.append(listaExpulsados());

        return texto.toString();
    }

    //RF10: arma el texto del acta final (el archivo lo guarda ActaArchivo)
    public String generarActa(String nombreCampeonato) {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        StringBuilder texto = new StringBuilder();
        texto.append("==================================================\n");
        texto.append("ACTA FINAL DEL PARTIDO\n");
        texto.append("==================================================\n");
        texto.append("Campeonato: ").append(nombreCampeonato).append("\n");
        texto.append("Fecha: ").append(fecha.format(formatoFecha)).append("\n");
        texto.append("Hora: ").append(hora.format(formatoHora)).append("\n");
        texto.append("Cancha: ").append(cancha).append("\n");
        texto.append("Local: ").append(local.getNombre()).append("\n");
        texto.append("Visitante: ").append(visitante.getNombre()).append("\n");
        texto.append("Marcador final: ").append(marcadorParaActa()).append("\n");
        texto.append(textoResultado("final")).append("\n");
        if (equipoSuspendido != null) {
            texto.append(textoSuspension()).append("\n");
        }

        texto.append("\nALINEACIONES\n");
        texto.append(textoAlineacion(local));
        texto.append(textoAlineacion(visitante));

        texto.append("\nGOLES\n");
        texto.append(listaGoles());

        texto.append("\nCAMBIOS\n");
        if (cambios.isEmpty()) {
            texto.append("No hubo cambios.\n");
        } else {
            List<Cambio> ordenados = new ArrayList<>(cambios);
            ordenados.sort((a, b) -> a.getMinuto() - b.getMinuto());
            for (Cambio c : ordenados) {
                texto.append("Minuto ").append(c.getMinuto()).append(" - ").append(c.getEquipo().getNombre())
                        .append(": sale ").append(nombreParaActa(c.getJugadorSale()))
                        .append(", entra ").append(nombreParaActa(c.getJugadorEntra())).append("\n");
            }
        }

        texto.append("\nTARJETAS\n");
        if (tarjetas.isEmpty()) {
            texto.append("No hubo tarjetas.\n");
        } else {
            List<Tarjeta> ordenadas = new ArrayList<>(tarjetas);
            ordenadas.sort((a, b) -> a.getMinuto() - b.getMinuto());
            for (Tarjeta t : ordenadas) {
                texto.append("Minuto ").append(t.getMinuto()).append(" - ").append(t.getTipo())
                        .append(" - ").append(nombreParaActa(t.getJugador()))
                        .append(" (").append(t.getJugador().getEquipo().getNombre()).append(")");
                if (t.getDetalle() != null && !t.getDetalle().isEmpty()) {
                    texto.append(" - ").append(t.getDetalle());
                }
                texto.append("\n");
            }
        }

        texto.append("\nEXPULSADOS\n");
        texto.append(listaExpulsados());

        //Sanciones que nacieron en este partido, de los jugadores de ambos equipos
        texto.append("\nSANCIONES GENERADAS\n");
        boolean haySanciones = false;
        Equipo[] equipos = {local, visitante};
        for (Equipo equipo : equipos) {
            for (Jugador j : equipo.getJugadores()) {
                for (Sancion s : j.getSanciones()) {
                    if (s.getPartidoOrigen() == this) {
                        texto.append(nombreParaActa(j)).append(" (").append(equipo.getNombre()).append("): ")
                                .append(s.getFechasPendientes()).append(" fecha(s) - ")
                                .append(s.getMotivo()).append("\n");
                        haySanciones = true;
                    }
                }
            }
        }
        if (!haySanciones) {
            texto.append("No se generaron sanciones.\n");
        }

        texto.append("\nOBSERVACIONES\n");
        if (observaciones == null || observaciones.isEmpty()) {
            texto.append("Sin observaciones.\n");
        } else {
            texto.append(observaciones).append("\n");
        }

        return texto.toString();
    }

    //Metodos de apoyo de M4 (solo los usan generarEstadisticas y generarActa)

    private String marcadorParaActa() {
        return local.getNombre() + " " + golesDe(local) + " - " + golesDe(visitante) + " " + visitante.getNombre();
    }

    private String nombreParaActa(Jugador jugador) {
        return jugador.getNombres() + " " + jugador.getApellidos();
    }

    private String textoResultado(String tipo) {
        int golesLocal = golesDe(local);
        int golesVisitante = golesDe(visitante);
        ResultadoPartido resultadoActual = determinarResultado();
        if (resultadoActual == ResultadoPartido.VICTORIA_LOCAL) {
            return "Resultado " + tipo + ": victoria de '" + local.getNombre() + "' "
                    + golesLocal + " - " + golesVisitante + ".";
        }
        if (resultadoActual == ResultadoPartido.VICTORIA_VISITANTE) {
            return "Resultado " + tipo + ": victoria de '" + visitante.getNombre() + "' "
                    + golesVisitante + " - " + golesLocal + ".";
        }
        return "Resultado " + tipo + ": empate " + golesLocal + " - " + golesVisitante + ".";
    }
    private String textoSuspension() {
        Equipo rival = local;
        if (equipoSuspendido == local) {
            rival = visitante;
        }
        return "Partido suspendido por falta de jugadores: '" + equipoSuspendido.getNombre()
                + "' quedó con menos de " + ReglasJuego.MINIMO_EN_CANCHA + ". Victoria "
                + ReglasJuego.GOLES_VICTORIA_ADMIN + " - 0 para '" + rival.getNombre() + "'.";
    }

    //Cuenta los goles registrados de un equipo entre dos minutos (incluidos)
    private int contarGoles(Equipo equipo, int minutoDesde, int minutoHasta) {
        int cantidad = 0;
        for (Gol g : goles) {
            if (g.getEquipo() == equipo && g.getMinuto() >= minutoDesde && g.getMinuto() <= minutoHasta) {
                cantidad++;
            }
        }
        return cantidad;
    }

    //tipo: "AMARILLA" o "ROJA"
    private int contarTarjetas(Equipo equipo, String tipo) {
        int cantidad = 0;
        for (Tarjeta t : tarjetas) {
            if (t.getJugador().getEquipo() == equipo && t.getTipo().equals(tipo)) {
                cantidad++;
            }
        }
        return cantidad;
    }

    private int contarCambios(Equipo equipo) {
        int cantidad = 0;
        for (Cambio c : cambios) {
            if (c.getEquipo() == equipo) {
                cantidad++;
            }
        }
        return cantidad;
    }

    //Un goleador por linea, ordenado por minuto
    private String listaGoles() {
        if (goles.isEmpty()) {
            return "Todavía no hay goles registrados.\n";
        }
        List<Gol> ordenados = new ArrayList<>(goles);
        ordenados.sort((a, b) -> a.getMinuto() - b.getMinuto());
        String texto = "";
        for (Gol g : ordenados) {
            texto = texto + "Minuto " + g.getMinuto() + " - " + nombreParaActa(g.getJugador())
                    + " (" + g.getEquipo().getNombre() + ")\n";
        }
        return texto;
    }
    //Los expulsados son los que tienen una tarjeta roja en este partido
    private String listaExpulsados() {
        String texto = "";
        for (Tarjeta t : tarjetas) {
            if (t instanceof TarjetaRoja) {
                TarjetaRoja roja = (TarjetaRoja) t;
                String causa = "roja directa";
                if (roja.isPorDobleAmarilla()) {
                    causa = "doble amarilla";
                }
                texto = texto + nombreParaActa(t.getJugador()) + " (" + t.getJugador().getEquipo().getNombre()
                        + "), minuto " + t.getMinuto() + ", " + causa + "\n";
            }
        }
        if (texto.isEmpty()) {
            return "No hubo expulsados.\n";
        }
        return texto;
    }

    private String textoAlineacion(Equipo equipo) {
        Alineacion alineacion = alineacionLocal;
        if (equipo == visitante) {
            alineacion = alineacionVisitante;
        }
        String texto = equipo.getNombre() + "\n";
        if (alineacion == null) {
            return texto + "  Sin alineación registrada.\n";
        }
        texto = texto + "  Titulares:\n";
        for (Jugador j : alineacion.getTitulares()) {
            texto = texto + "    #" + j.getNumeroCamiseta() + " " + nombreParaActa(j)
                    + " (carné " + j.getCarne() + ")\n";
        }
        texto = texto + "  Suplentes:\n";
        if (alineacion.getSuplentes().isEmpty()) {
            texto = texto + "    Ninguno\n";
        }
        for (Jugador j : alineacion.getSuplentes()) {
            texto = texto + "    #" + j.getNumeroCamiseta() + " " + nombreParaActa(j)
                    + " (carné " + j.getCarne() + ")\n";
        }
        if (alineacion.getCapitan() != null) {
            texto = texto + "  Capitán: " + nombreParaActa(alineacion.getCapitan()) + "\n";
        }
        return texto;
    }
}
