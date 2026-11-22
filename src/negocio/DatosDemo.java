package negocio;

import modelo.Alineacion;
import modelo.Campeonato;
import modelo.Categoria;
import modelo.Equipo;
import modelo.EstadoPartido;
import modelo.Gol;
import modelo.Jugador;
import modelo.Partido;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DatosDemo {

    private static final int JUGADORES_POR_EQUIPO = 10;

    private static final String[] NOMBRES = {
            "Carlos", "Luis", "Miguel", "Jorge", "Andrés",
            "Diego", "Pablo", "Mateo", "Santiago", "Daniel"
    };

    private static final String[] APELLIDOS = {
            "Mora", "Vera", "Paredes", "Salazar", "Torres",
            "Rojas", "Castro", "Herrera", "Ortiz", "Navarro"
    };

    //Cedulas de ejemplo con digito verificador valido (40 = 4 equipos x 10 jugadores)
    private static final String[] CEDULAS = {
            "1700010000", "1700010372", "1700010745", "1700011115", "1700011487",
            "1700011859", "1700012220", "1700012592", "1700012964", "1700013335",
            "1700013707", "1700014077", "1700014440", "1700014812", "1700015181",
            "1700015553", "1700015926", "1700016296", "1700016668", "1700017039",
            "1700017401", "1700017773", "1700018144", "1700018516", "1700018888",
            "1700019258", "1700019621", "1700019993", "1700020363", "1700020736",
            "1700021106", "1700021478", "1700021841", "1700022211", "1700022583",
            "1700022955", "1700023326", "1700023698", "1700024068", "1700024431"
    };

    //Escenarios

    //Campeonato sin configurar (RF03)
    public static Campeonato cargarVacio() {
        return new Campeonato();
    }

    //Campeonato configurado (Primera) con 4 equipos de 10 jugadores cada uno
    public static Campeonato cargarBase() {
        Campeonato campeonato = new Campeonato();
        campeonato.configurar("Copa Barrial 2026", Categoria.PRIMERA);
        crearEquipo(campeonato, "Barrio Unido", "La Floresta", "Pedro Morales", "0991111111", 0);
        crearEquipo(campeonato, "Los Pinos", "San Carlos", "Luis Andrade", "0992222222", 1);
        crearEquipo(campeonato, "Deportivo Sur", "Chillogallo", "Marco Vera", "0993333333", 2);
        crearEquipo(campeonato, "Atlético Norte", "Carcelén", "Jorge Salinas", "0994444444", 3);
        return campeonato;
    }

    //Base + 3 partidos PROGRAMADOS (RF05)
    public static Campeonato cargarConPartidos() {
        Campeonato campeonato = cargarBase();
        List<Equipo> equipos = campeonato.getEquipos();
        campeonato.getPartidos().add(crearPartido(equipos.get(0), equipos.get(1), 20));
        campeonato.getPartidos().add(crearPartido(equipos.get(2), equipos.get(3), 21));
        campeonato.getPartidos().add(crearPartido(equipos.get(0), equipos.get(2), 22));
        return campeonato;
    }

    //Con partidos + alineaciones aprobadas y el primer partido EN_REGISTRO (RF06 a RF08, RF12)
    public static Campeonato cargarConPartidoListo() {
        Campeonato campeonato = cargarConPartidos();
        Partido partido = campeonato.getPartidos().get(0);
        partido.setAlineacionLocal(crearAlineacion(partido.getLocal()));
        partido.setAlineacionVisitante(crearAlineacion(partido.getVisitante()));
        partido.setEstado(EstadoPartido.EN_REGISTRO);
        return campeonato;
    }

    //Partido listo + el primer jugador del local con 4 amarillas (RF14)
    //Usa Jugador.sumarAmarilla, que es de M5: hasta que se integre, el jugador queda con 0
    public static Campeonato cargarConAmarillas() {
        Campeonato campeonato = cargarConPartidoListo();
        Partido partido = campeonato.getPartidos().get(0);
        Jugador jugador = partido.getLocal().getJugadores().get(0);
        for (int i = 0; i < 4; i++) {
            jugador.sumarAmarilla(partido);
        }
        return campeonato;
    }

    //Base + 3 partidos CERRADOS con goles (RF11)
    //Tabla esperada: Atlético Norte 4, Barrio Unido 3, Deportivo Sur 1, Los Pinos 0
    public static Campeonato cargarConCerrados() {
        Campeonato campeonato = cargarBase();
        List<Equipo> equipos = campeonato.getEquipos();
        Equipo barrioUnido = equipos.get(0);
        Equipo losPinos = equipos.get(1);
        Equipo deportivoSur = equipos.get(2);
        Equipo atleticoNorte = equipos.get(3);
        agregarPartidoCerrado(campeonato, barrioUnido, losPinos, 2, 1);
        agregarPartidoCerrado(campeonato, deportivoSur, atleticoNorte, 1, 1);
        agregarPartidoCerrado(campeonato, atleticoNorte, losPinos, 3, 0);
        return campeonato;
    }

    //Para probar el menu de M4: partidos cerrados + uno FINALIZADO (se puede cerrar),
    //uno EN_REGISTRO (estadisticas provisionales) y uno PROGRAMADO
    public static Campeonato cargarParaMenuM4() {
        Campeonato campeonato = cargarConCerrados();
        List<Equipo> equipos = campeonato.getEquipos();
        agregarPartido(campeonato, equipos.get(0), equipos.get(2), 2, 2, EstadoPartido.FINALIZADO);
        agregarPartido(campeonato, equipos.get(0), equipos.get(3), 1, 0, EstadoPartido.EN_REGISTRO);
        campeonato.getPartidos().add(crearPartido(equipos.get(2), equipos.get(1), 30));
        return campeonato;
    }

    //Armado de partidos (tambien los usa PruebaM4)

    //Agrega un partido con alineaciones y goles en el estado indicado, sin pasar por las reglas de registro.
    //Los goles se reparten entre los 3 primeros jugadores de cada equipo.
    public static Partido agregarPartido(Campeonato campeonato, Equipo local, Equipo visitante,
                                         int golesLocal, int golesVisitante, EstadoPartido estado) {
        Partido partido = crearPartido(local, visitante, 1 + campeonato.getPartidos().size());
        partido.setAlineacionLocal(crearAlineacion(local));
        partido.setAlineacionVisitante(crearAlineacion(visitante));
        agregarGoles(partido, local, golesLocal);
        agregarGoles(partido, visitante, golesVisitante);
        partido.setEstado(estado);
        campeonato.getPartidos().add(partido);
        return partido;
    }

    //Igual que agregarPartido, pero ademas lo cierra con la logica de M4 (Partido.cerrar)
    public static Partido agregarPartidoCerrado(Campeonato campeonato, Equipo local, Equipo visitante,
                                                int golesLocal, int golesVisitante) {
        Partido partido = agregarPartido(campeonato, local, visitante, golesLocal, golesVisitante,
                EstadoPartido.FINALIZADO);
        partido.cerrar(null);
        return partido;
    }

    //Metodos de apoyo

    private static void crearEquipo(Campeonato campeonato, String nombre, String barrio,
                                    String delegado, String telefono, int numeroEquipo) {
        Equipo equipo = new Equipo(nombre, barrio, delegado, telefono);
        for (int i = 0; i < JUGADORES_POR_EQUIPO; i++) {
            String carne = campeonato.siguienteCarne();
            String cedula = CEDULAS[numeroEquipo * JUGADORES_POR_EQUIPO + i];
            LocalDate nacimiento = LocalDate.of(1998 + (i % 5), i + 1, 10 + i);
            Jugador jugador = new Jugador(carne, NOMBRES[i], APELLIDOS[(i + numeroEquipo) % 10],
                    cedula, nacimiento, i + 1, equipo);
            equipo.getJugadores().add(jugador);
        }
        campeonato.getEquipos().add(equipo);
    }

    private static Partido crearPartido(Equipo local, Equipo visitante, int dia) {
        return new Partido(local, visitante, LocalDate.of(2026, 11, dia), LocalTime.of(16, 0), "Cancha Central");
    }

    //7 titulares, el resto suplentes y capitan el primer titular
    private static Alineacion crearAlineacion(Equipo equipo) {
        List<Jugador> titulares = new ArrayList<>();
        List<Jugador> suplentes = new ArrayList<>();
        for (int i = 0; i < equipo.getJugadores().size(); i++) {
            if (i < ReglasJuego.TITULARES_MIN) {
                titulares.add(equipo.getJugadores().get(i));
            } else {
                suplentes.add(equipo.getJugadores().get(i));
            }
        }
        return new Alineacion(equipo, titulares, suplentes, titulares.get(0));
    }

    private static void agregarGoles(Partido partido, Equipo equipo, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            Jugador anotador = equipo.getJugadores().get(i % 3);
            int minuto = 10 + i * 10;
            partido.getGoles().add(new Gol(anotador, equipo, minuto));
        }
    }
}
