
package modelo;

import negocio.ReglasJuego;
import negocio.Validador;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Campeonato {

    // Atributos
    private String nombre;
    private Categoria categoria;
    private List<Equipo> equipos;
    private List<Partido> partidos;
    private int ultimoCarne;

    // Constructor
    public Campeonato() {
        this.equipos = new ArrayList<>();
        this.partidos = new ArrayList<>();
        this.ultimoCarne = 0;
    }

    // Metodos

    // ======================================================================================
    // MODULO 2
    // ======================================================================================

    // RF03: valida los datos para configurar el campeonato
    public String validarConfiguracion(String nombre, Categoria categoria) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: el nombre del campeonato es obligatorio.";
        }

        if (categoria == null) {
            return "Error: debe seleccionar una categoría.";
        }

        if (estaConfigurado()) {
            return "Error: el campeonato ya está configurado.";
        }

        return null;
    }

    // RF03: configura el campeonato por primera vez
    public void configurar(String nombre, Categoria categoria) {
        if (validarConfiguracion(nombre, categoria) != null) {
            return;
        }

        this.nombre = nombre.trim();
        this.categoria = categoria;
    }

    // RF03: comprueba si el campeonato ya fue configurado
    public boolean estaConfigurado() {
        return nombre != null
                && !nombre.trim().isEmpty()
                && categoria != null;
    }

    // RF03: valida la modificación del campeonato
    public String validarModificacion(String nombre, Categoria categoria) {
        if (!estaConfigurado()) {
            return "Error: primero debe configurar el campeonato.";
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: el nombre del campeonato es obligatorio.";
        }

        if (categoria == null) {
            return "Error: debe seleccionar una categoría.";
        }

        if (this.categoria != categoria && !equipos.isEmpty()) {
            return "Error: no se puede cambiar la categoría porque ya existen equipos inscritos.";
        }

        return null;
    }

    // RF03: modifica los datos del campeonato
    public void modificar(String nombre, Categoria categoria) {
        if (validarModificacion(nombre, categoria) != null) {
            return;
        }

        this.nombre = nombre.trim();
        this.categoria = categoria;
    }

    // RF03: comprueba si el campeonato está listo para crear partidos
    public boolean estaListo() {
        return estaConfigurado() && equiposFaltantes() == 0;
    }

    // RF04: agrega un partido a la lista del campeonato
    public void agregarPartido(Partido partido) {
        if (partido != null && !partidos.contains(partido)) {
            partidos.add(partido);
        }
    }

    // RF04: valida los datos necesarios para crear un partido
    public String validarCrearPartido(
            Equipo local,
            Equipo visitante,
            LocalDate fecha,
            LocalTime hora,
            String cancha) {

        if (!estaConfigurado()) {
            return "Error: primero debe configurar el campeonato.";
        }

        if (!estaListo()) {
            return "Error: el campeonato necesita al menos 4 equipos para crear partidos.";
        }

        if (local == null || visitante == null || fecha == null
                || hora == null || cancha == null || cancha.trim().isEmpty()) {
            return "Error: todos los campos del partido son obligatorios.";
        }

        if (local == visitante) {
            return "Error: el equipo local y el visitante deben ser distintos.";
        }

        if (!equipos.contains(local) || !equipos.contains(visitante)) {
            return "Error: ambos equipos deben estar inscritos en el campeonato.";
        }

        if (existeCruce(local, visitante)) {
            return "Error: estos equipos ya tienen un partido programado.";
        }

        return null;
    }

    // RF04: crea un partido y lo agrega al campeonato
    public Partido crearPartido(
            Equipo local,
            Equipo visitante,
            LocalDate fecha,
            LocalTime hora,
            String cancha) {

        if (validarCrearPartido(local, visitante, fecha, hora, cancha) != null) {
            return null;
        }

        Partido partido = new Partido(
                local,
                visitante,
                fecha,
                hora,
                cancha.trim()
        );

        agregarPartido(partido);

        return partido;
    }

    // RF04: comprueba si dos equipos ya tienen un cruce registrado
    // Se comprueba en ambos sentidos: A contra B y B contra A
    public boolean existeCruce(Equipo a, Equipo b) {
        if (a == null || b == null) {
            return false;
        }

        for (Partido partido : partidos) {
            boolean mismoCruce =
                    (partido.getLocal() == a && partido.getVisitante() == b)
                            || (partido.getLocal() == b && partido.getVisitante() == a);

            if (mismoCruce) {
                return true;
            }
        }

        return false;
    }

    // RF04: filtra partidos por equipo y estado
    // Si equipo es null, no filtra por equipo.
    // Si estado es null, no filtra por estado.
    public List<Partido> filtrarPartidos(Equipo equipo, EstadoPartido estado) {
        List<Partido> resultado = new ArrayList<>();

        for (Partido partido : partidos) {
            boolean coincideEquipo =
                    equipo == null
                            || partido.getLocal() == equipo
                            || partido.getVisitante() == equipo;

            boolean coincideEstado =
                    estado == null || partido.getEstado() == estado;

            if (coincideEquipo && coincideEstado) {
                resultado.add(partido);
            }
        }

        // Ordenar por fecha y después por hora
        resultado.sort((a, b) -> {
            int comparacionFecha = a.getFecha().compareTo(b.getFecha());

            if (comparacionFecha != 0) {
                return comparacionFecha;
            }

            return a.getHora().compareTo(b.getHora());
        });

        return resultado;
    }

    // ======================================================================================
    // MODULO 1
    // ======================================================================================

    public int equiposFaltantes() {
        int equipos_faltantes = 0;

        if (equipos.size() < 4) {
            equipos_faltantes = 4 - equipos.size();
        }

        return equipos_faltantes;
    }

    // AÑADIR METODO AL UML
    public String validarEquipo(
            Equipo equipo,
            String nombre,
            String barrio,
            String delegado,
            String telefono) {

        if (!estaConfigurado()) {
            return "Error: primero debe configurar el campeonato.";
        }

        // Obligatorios
        if (!Validador.obligatorio(nombre)
                || !Validador.obligatorio(barrio)
                || !Validador.obligatorio(delegado)
                || !Validador.obligatorio(telefono)) {
            return "Error: campos obligatorios incompletos.";
        }

        // Formato
        if (!Validador.longitud(nombre, 3, 40)) {
            return "Error: el nombre del equipo debe tener entre 3 y 40 caracteres.";
        }

        if (!Validador.longitud(barrio, 1, 40)) {
            return "Error: el barrio no puede superar los 40 caracteres.";
        }

        if (!Validador.soloLetras(delegado.trim())) {
            return "Error: el nombre del delegado solo puede contener letras y espacios.";
        }

        if (!Validador.longitud(delegado, 3, 60)) {
            return "Error: el nombre del delegado debe tener entre 3 y 60 caracteres.";
        }

        if (!Validador.soloDigitos(telefono.trim(), 10)) {
            return "Error: el teléfono debe tener 10 dígitos numéricos.";
        }

        // Nombre no repetido en el campeonato (el propio equipo no cuenta, sirve al modificar)
        Equipo existente = buscarEquipo(nombre);
        if (existente != null && existente != equipo) {
            return "Error: ya existe un equipo con ese nombre en el campeonato.";
        }

        return null;
    }

    // CAMBIAR LA FIRMA
    public Equipo inscribirEquipo(
            Equipo equipo,
            String nombre,
            String barrio,
            String delegado,
            String telefono) {

        if (validarEquipo(equipo, nombre, barrio, delegado, telefono) == null) {
            equipos.add(equipo);
            return equipo;
        }

        return null;
    }

    public void modificarEquipo(
            Equipo equipo,
            String nombre,
            String barrio,
            String delegado,
            String telefono) {

        if (equipo == null
                || validarEquipo(equipo, nombre, barrio, delegado, telefono) != null) {
            return;
        }

        equipo.modificar(nombre.trim(), barrio.trim(), delegado.trim(), telefono.trim());
    }

    public Equipo buscarEquipo(String nombre) {
        if (nombre == null) {
            return null;
        }

        for (Equipo e : equipos) {
            if (e.getNombre().equalsIgnoreCase(nombre.trim())) {
                return e;
            }
        }

        return null;
    }

    // Buscar coincidencias en el nombre
    public List<Equipo> buscarEquipos(String texto) {
        texto = (texto == null) ? "" : texto.trim().toLowerCase();

        List<Equipo> resultado = new ArrayList<>();

        for (Equipo e : equipos) {
            if (e.getNombre().toLowerCase().contains(texto)) {
                resultado.add(e);
            }
        }

        return resultado;
    }

    // AGREGAR METODO AL UML
    public String validarEliminarEquipo(Equipo equipo) {
        if (equipo == null) {
            return "Error: debe seleccionar un equipo.";
        }

        if (equipoTienePartidos(equipo)) {
            return "Error: el equipo tiene partidos y no puede eliminarse.";
        }

        return null;
    }

    public void eliminarEquipo(Equipo equipo) {
        if (validarEliminarEquipo(equipo) == null) {
            equipos.remove(equipo);
        }
    }

    public boolean equipoTienePartidos(Equipo equipo) {
        for (Partido p : partidos) {
            if (p.getLocal() == equipo || p.getVisitante() == equipo) {
                return true;
            }
        }

        return false;
    }

    public String siguienteCarne() {
        if (ultimoCarne < ReglasJuego.CARNE_MAXIMO) {
            ultimoCarne++;
            return String.format("%04d", ultimoCarne);
        }

        return null;
    }

    // AÑADIR METODO AL UML
    public String validarJugador(
            Equipo equipo,
            String nombres,
            String apellidos,
            String cedula,
            LocalDate fechaNacimiento,
            int numeroCamiseta) {

        if (!estaConfigurado()) {
            return "Error: primero debe configurar el campeonato.";
        }

        if (equipo == null) {
            return "Error: campos obligatorios incompletos.";
        }

        // Obligatorios y formato
        String error = validarDatosJugador(nombres, apellidos, cedula, fechaNacimiento, numeroCamiseta);
        if (error != null) {
            return error;
        }

        // La edad corresponde a la categoria del campeonato
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

        if (!categoria.admiteEdad(edad)) {
            return "Error: la edad del jugador (" + edad + " años) no corresponde a la categoría "
                    + categoria + " del campeonato.";
        }

        // Cedulas no repetidas
        if (cedulaRepetida(cedula.trim(), null)) {
            return "Error: la cédula ya está inscrita en otro equipo del campeonato.";
        }

        // Reglas del equipo
        if (!equipo.camisetaDisponible(numeroCamiseta)) {
            return "Error: el número de camiseta ya está en uso en este equipo.";
        }

        if (!equipo.tieneCupo()) {
            return "Error: el plantel del equipo ya tiene el máximo de " + ReglasJuego.PLANTEL_MAXIMO + " jugadores.";
        }

        // Carné disponible
        if (ultimoCarne >= ReglasJuego.CARNE_MAXIMO) {
            return "Error: se alcanzó el límite máximo de " + ReglasJuego.CARNE_MAXIMO + " carnés.";
        }

        // Si todo esta bien
        return null;
    }

    // AÑADIR METODO AL UML (apoyo privado)
    // Valida obligatorios y formato de los datos de un jugador
    private String validarDatosJugador(
            String nombres,
            String apellidos,
            String cedula,
            LocalDate fechaNacimiento,
            int numeroCamiseta) {

        if (!Validador.obligatorio(nombres)
                || !Validador.obligatorio(apellidos)
                || !Validador.obligatorio(cedula)
                || fechaNacimiento == null) {
            return "Error: campos obligatorios incompletos.";
        }

        if (!Validador.soloLetras(nombres.trim()) || !Validador.soloLetras(apellidos.trim())) {
            return "Error: los nombres y apellidos solo pueden contener letras y espacios.";
        }

        if (!Validador.longitud(nombres, 2, 40) || !Validador.longitud(apellidos, 2, 40)) {
            return "Error: los nombres y apellidos deben tener entre 2 y 40 caracteres.";
        }

        if (!Validador.soloDigitos(cedula.trim(), 10)) {
            return "Error: la cédula debe tener 10 dígitos numéricos.";
        }

        if (!Validador.cedulaValida(cedula.trim())) {
            return "Error: la cédula ingresada no es válida.";
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            return "Error: la fecha de nacimiento no puede ser posterior a la fecha actual.";
        }

        if (!Validador.rango(numeroCamiseta, 1, 99)) {
            return "Error: el número de camiseta debe estar entre 1 y 99.";
        }

        return null;
    }

    // excepto: el jugador que se modifica (null al inscribir)
    private boolean cedulaRepetida(String cedula, Jugador excepto) {
        for (Equipo e : equipos) {
            for (Jugador j : e.getJugadores()) {
                if (j != excepto && j.getCedula().equals(cedula)) {
                    return true;
                }
            }
        }

        return false;
    }

    public Jugador inscribirJugador(
            Equipo equipo,
            String nombres,
            String apellidos,
            String cedula,
            LocalDate fechaNacimiento,
            int numeroCamiseta) {

        if (validarJugador(
                equipo,
                nombres,
                apellidos,
                cedula,
                fechaNacimiento,
                numeroCamiseta) == null) {

            String carne = siguienteCarne();

            Jugador jugador = new Jugador(
                    carne,
                    nombres.trim(),
                    apellidos.trim(),
                    cedula.trim(),
                    fechaNacimiento,
                    numeroCamiseta,
                    equipo
            );

            equipo.agregarJugador(jugador);

            return jugador;
        }

        return null;
    }

    // AÑADIR METODO AL UML
    public String validarModificacionJugador(
            Jugador jugador,
            String nombres,
            String apellidos,
            String cedula,
            LocalDate fechaNacimiento,
            int numeroCamiseta) {

        if (jugador == null) {
            return "Error: debe seleccionar un jugador.";
        }

        if (jugador.getEstado() == EstadoJugador.DE_BAJA) {
            return "Error: el jugador está dado de baja y no puede modificarse.";
        }

        String error = validarDatosJugador(nombres, apellidos, cedula, fechaNacimiento, numeroCamiseta);
        if (error != null) {
            return error;
        }

        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

        if (!categoria.admiteEdad(edad)) {
            return "Error: la edad del jugador (" + edad + " años) no corresponde a la categoría "
                    + categoria + " del campeonato.";
        }

        if (cedulaRepetida(cedula.trim(), jugador)) {
            return "Error: la cédula ya está inscrita en otro equipo del campeonato.";
        }

        if (!jugador.getEquipo().camisetaDisponible(numeroCamiseta, jugador)) {
            return "Error: el número de camiseta ya está en uso en este equipo.";
        }

        return null;
    }

    public void modificarJugador(
            Jugador jugador,
            String nombres,
            String apellidos,
            String cedula,
            LocalDate fechaNacimiento,
            int numeroCamiseta) {

        if (validarModificacionJugador(
                jugador,
                nombres,
                apellidos,
                cedula,
                fechaNacimiento,
                numeroCamiseta) != null) {
            return;
        }

        jugador.setNombres(nombres.trim());
        jugador.setApellidos(apellidos.trim());
        jugador.setCedula(cedula.trim());
        jugador.setFechaNacimiento(fechaNacimiento);
        jugador.setNumeroCamiseta(numeroCamiseta);
    }

    // Buscar jugador por nombre o apellido
    // VERIFICAR EN EL UML
    public List<Jugador> buscarJugador(String texto) {
        texto = (texto == null) ? "" : texto.trim().toLowerCase();

        List<Jugador> resultado = new ArrayList<>();

        for (Equipo e : equipos) {
            for (Jugador j : e.getJugadores()) {
                if (j.getNombres().toLowerCase().contains(texto)
                        || j.getApellidos().toLowerCase().contains(texto)) {

                    resultado.add(j);
                }
            }
        }

        return resultado;
    }

    // Buscar jugadores por cedula
    // AÑADIR EN EL UML
    public List<Jugador> buscarJugadoresCedula(String texto) {
        texto = (texto == null) ? "" : texto.trim();

        List<Jugador> resultado = new ArrayList<>();

        for (Equipo e : equipos) {
            for (Jugador j : e.getJugadores()) {
                if (j.getCedula().contains(texto)) {
                    resultado.add(j);
                }
            }
        }

        return resultado;
    }

    // OTRO MODULO
    public List<Sancion> sancionesVigentes(Equipo equipo) {
        return null;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public int getUltimoCarne() {
        return ultimoCarne;
    }

    // ======================================================================================
    // MODULO 4
    // ======================================================================================

    // RF11: solo los partidos cerrados cuentan para la tabla
    public List<Partido> partidosCerrados() {
        List<Partido> cerrados = new ArrayList<>();

        for (Partido p : partidos) {
            if (p.getEstado() == EstadoPartido.CERRADO) {
                cerrados.add(p);
            }
        }

        return cerrados;
    }

    // RF11: devuelve el mensaje de error si no se puede consultar la tabla, o null si se puede
    public String validarConsultaTabla() {
        if (!estaConfigurado()) {
            return "Error: primero debe configurar el campeonato.";
        }

        return null;
    }

    // RF11: la tabla no se guarda, se calcula cada vez que se consulta.
    // Si el campeonato no esta configurado devuelve una lista vacia
    public List<TablaPosiciones> calcularTabla() {
        if (validarConsultaTabla() != null) {
            return new ArrayList<>();
        }

        // Todos los equipos aparecen, aunque no hayan jugado
        List<TablaPosiciones> tabla = new ArrayList<>();

        for (Equipo e : equipos) {
            tabla.add(new TablaPosiciones(e));
        }

        // golesDe ya devuelve 3 - 0 cuando el partido se suspendio por falta de jugadores
        for (Partido p : partidosCerrados()) {
            int golesLocal = p.golesDe(p.getLocal());
            int golesVisitante = p.golesDe(p.getVisitante());

            TablaPosiciones filaLocal = buscarFilaTabla(tabla, p.getLocal());
            TablaPosiciones filaVisitante = buscarFilaTabla(tabla, p.getVisitante());

            if (filaLocal != null) {
                sumarResultado(filaLocal, golesLocal, golesVisitante);
            }

            if (filaVisitante != null) {
                sumarResultado(filaVisitante, golesVisitante, golesLocal);
            }
        }

        // Orden de burbuja: se intercambian dos filas vecinas si la de abajo debe ir antes
        for (int i = 0; i < tabla.size() - 1; i++) {
            for (int j = 0; j < tabla.size() - 1 - i; j++) {
                if (filaDebeIrAntes(tabla.get(j + 1), tabla.get(j))) {
                    TablaPosiciones auxiliar = tabla.get(j);
                    tabla.set(j, tabla.get(j + 1));
                    tabla.set(j + 1, auxiliar);
                }
            }
        }

        return tabla;
    }

    // RF11: suma los goles de cada jugador en los partidos cerrados
    public List<TablaGoleadores> calcularGoleadores() {
        if (validarConsultaTabla() != null) {
            return new ArrayList<>();
        }

        List<TablaGoleadores> goleadores = new ArrayList<>();

        for (Partido p : partidosCerrados()) {
            // Si el partido se suspendio por falta de jugadores, su marcador no vale (se cuenta 3 - 0),
            // asi que sus goles tampoco se suman a los goleadores
            if (p.getEquipoSuspendido() == null) {
                for (Gol g : p.getGoles()) {
                    TablaGoleadores fila = buscarFilaGoleador(goleadores, g.getJugador());

                    if (fila == null) {
                        goleadores.add(new TablaGoleadores(g.getJugador(), g.getEquipo(), 1));
                    } else {
                        fila.setGoles(fila.getGoles() + 1);
                    }
                }
            }
        }

        // Orden de burbuja: mas goles primero y, si empatan, por apellido
        for (int i = 0; i < goleadores.size() - 1; i++) {
            for (int j = 0; j < goleadores.size() - 1 - i; j++) {
                if (goleadorDebeIrAntes(goleadores.get(j + 1), goleadores.get(j))) {
                    TablaGoleadores auxiliar = goleadores.get(j);
                    goleadores.set(j, goleadores.get(j + 1));
                    goleadores.set(j + 1, auxiliar);
                }
            }
        }

        return goleadores;
    }

    // Metodos de apoyo de M4

    private TablaPosiciones buscarFilaTabla(List<TablaPosiciones> tabla, Equipo equipo) {
        for (TablaPosiciones fila : tabla) {
            if (fila.getEquipo() == equipo) {
                return fila;
            }
        }

        return null;
    }

    private TablaGoleadores buscarFilaGoleador(
            List<TablaGoleadores> goleadores,
            Jugador jugador) {

        for (TablaGoleadores fila : goleadores) {
            if (fila.getJugador() == jugador) {
                return fila;
            }
        }

        return null;
    }

    // Actualiza una fila con el resultado de un partido
    private void sumarResultado(TablaPosiciones fila, int golesFavor, int golesContra) {
        fila.setPj(fila.getPj() + 1);
        fila.setGf(fila.getGf() + golesFavor);
        fila.setGc(fila.getGc() + golesContra);

        if (golesFavor > golesContra) {
            fila.setPg(fila.getPg() + 1);
        } else if (golesFavor == golesContra) {
            fila.setPe(fila.getPe() + 1);
        } else {
            fila.setPp(fila.getPp() + 1);
        }
    }

    // Orden de la tabla: puntos, diferencia de goles, goles a favor y nombre del equipo
    private boolean filaDebeIrAntes(TablaPosiciones a, TablaPosiciones b) {
        if (a.puntos() != b.puntos()) {
            return a.puntos() > b.puntos();
        }

        if (a.dg() != b.dg()) {
            return a.dg() > b.dg();
        }

        if (a.getGf() != b.getGf()) {
            return a.getGf() > b.getGf();
        }

        return a.getEquipo().getNombre()
                .compareToIgnoreCase(b.getEquipo().getNombre()) < 0;
    }

    // Orden de goleadores: goles de mayor a menor y, con igual cantidad, por apellido
    private boolean goleadorDebeIrAntes(TablaGoleadores a, TablaGoleadores b) {
        if (a.getGoles() != b.getGoles()) {
            return a.getGoles() > b.getGoles();
        }

        int comparacion = a.getJugador().getApellidos()
                .compareToIgnoreCase(b.getJugador().getApellidos());

        if (comparacion == 0) {
            comparacion = a.getJugador().getNombres()
                    .compareToIgnoreCase(b.getJugador().getNombres());
        }

        return comparacion < 0;
    }
}
