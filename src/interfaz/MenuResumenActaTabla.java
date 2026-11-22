package interfaz;

import modelo.Campeonato;
import modelo.Partido;
import modelo.TablaGoleadores;
import modelo.TablaPosiciones;
import negocio.Validador;
import negocio.ActaArchivo;

import java.util.List;
import java.util.Scanner;


public class MenuResumenActaTabla {

    public void mostrar(Campeonato campeonato, Scanner sc) {
        int opcion;
        do {
            System.out.println();
            System.out.println("=== RESUMEN, ACTA Y TABLA ===");
            System.out.println("1. Estadísticas de un partido");
            System.out.println("2. Cerrar partido y generar acta");
            System.out.println("3. Consultar acta de un partido cerrado");
            System.out.println("4. Tabla de posiciones");
            System.out.println("5. Goleadores");
            System.out.println("0. Volver");
            opcion = leerEntero(sc, "Opción: ");
            switch (opcion) {
                case 1: verEstadisticas(campeonato, sc); break;
                case 2: cerrarPartido(campeonato, sc); break;
                case 3: consultarActa(campeonato, sc); break;
                case 4: verTabla(campeonato); break;
                case 5: verGoleadores(campeonato, sc); break;
                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    // ------------------------------------------------------------------ RF09
    private void verEstadisticas(Campeonato campeonato, Scanner sc) {
        Partido partido = seleccionarPartido(campeonato, sc);
        if (partido == null) return;
        String error = partido.validarEstadisticas();
        if (error != null) {
            System.out.println(error);
            return;
        }
        System.out.println();
        System.out.println(partido.generarEstadisticas());
    }

    // ------------------------------------------------------------------ RF10
    private void cerrarPartido(Campeonato campeonato, Scanner sc) {
        Partido partido = seleccionarPartido(campeonato, sc);
        if (partido == null) return;

        //Primero se revisa el estado del partido y despues las observaciones
        String error = partido.validarCierre(null);
        if (error != null) {
            System.out.println(error);
            return;
        }
        String observaciones = leerTexto(sc, "Observaciones (opcional, 5 a 100 caracteres): ");
        error = partido.validarCierre(observaciones);
        if (error != null) {
            System.out.println(error);
            return;
        }
        String confirmacion = leerTexto(sc, "¿Confirmar el cierre? Una vez cerrado ya no se puede modificar (S/N): ");
        if (!confirmacion.equalsIgnoreCase("S")) {
            System.out.println("Cierre cancelado.");
            return;
        }

        partido.cerrar(observaciones);

        String marcador = partido.getLocal().getNombre() + " " + partido.golesDe(partido.getLocal())
                + " - " + partido.golesDe(partido.getVisitante()) + " " + partido.getVisitante().getNombre();
        System.out.println("Partido cerrado: " + marcador + ". Resultado: " + textoResultado(partido) + ".");

        String acta = partido.generarActa(campeonato.getNombre());
        String ruta = new ActaArchivo().guardar(partido, acta);
        if (ruta == null) {
            System.out.println("Error: no se pudo guardar el archivo del acta.");
            return;
        }
        System.out.println("Acta generada: " + ruta.replace("\\", "/"));
    }

    private void consultarActa(Campeonato campeonato, Scanner sc) {
        Partido partido = seleccionarPartido(campeonato, sc);
        if (partido == null) return;
        String acta = new ActaArchivo().leer(partido);
        if (acta == null) {
            System.out.println("Error: el partido no tiene acta cerrada.");
            return;
        }
        System.out.println();
        System.out.println(acta);
    }

    private String textoResultado(Partido partido) {
        switch (partido.getResultado()) {
            case VICTORIA_LOCAL: return "victoria de '" + partido.getLocal().getNombre() + "'";
            case VICTORIA_VISITANTE: return "victoria de '" + partido.getVisitante().getNombre() + "'";
            default: return "empate";
        }
    }

    // ------------------------------------------------------------------ RF11
    private void verTabla(Campeonato campeonato) {
        String error = campeonato.validarConsultaTabla();
        if (error != null) {
            System.out.println(error);
            return;
        }
        List<TablaPosiciones> tabla = campeonato.calcularTabla();
        if (tabla.isEmpty()) {
            System.out.println("El campeonato no tiene equipos inscritos.");
            return;
        }
        if (campeonato.partidosCerrados().isEmpty()) {
            System.out.println("El campeonato aún no tiene partidos cerrados. Todos los equipos tienen 0 puntos.");
        }
        System.out.println();
        System.out.printf("%-4s %-20s %3s %3s %3s %3s %3s %3s %4s %4s%n",
                "Pos", "Equipo", "PJ", "PG", "PE", "PP", "GF", "GC", "DG", "Pts");
        for (int i = 0; i < tabla.size(); i++) {
            TablaPosiciones f = tabla.get(i);
            System.out.printf("%-4d %-20s %3d %3d %3d %3d %3d %3d %4d %4d%n", i + 1, f.getEquipo().getNombre(),
                    f.getPj(), f.getPg(), f.getPe(), f.getPp(), f.getGf(), f.getGc(), f.dg(), f.puntos());
        }
    }

    private void verGoleadores(Campeonato campeonato, Scanner sc) {
        String error = campeonato.validarConsultaTabla();
        if (error != null) {
            System.out.println(error);
            return;
        }
        System.out.println("Cantidad a mostrar: 1. Los 5 primeros  2. Los 10 primeros  3. Todos");
        int opcion = leerEntero(sc, "Opción: ");
        int cantidad;
        if (opcion == 1) {
            cantidad = 5;
        } else if (opcion == 2) {
            cantidad = 10;
        } else {
            cantidad = Integer.MAX_VALUE;
        }

        List<TablaGoleadores> goleadores = campeonato.calcularGoleadores();
        if (goleadores.isEmpty()) {
            System.out.println("Aún no se han registrado goles en este campeonato.");
            return;
        }
        System.out.println();
        System.out.printf("%-7s %-26s %-20s %s%n", "Puesto", "Jugador", "Equipo", "Goles");
        for (int i = 0; i < goleadores.size() && i < cantidad; i++) {
            TablaGoleadores g = goleadores.get(i);
            System.out.printf("%-7d %-26s %-20s %d%n", i + 1,
                    g.getJugador().getNombres() + " " + g.getJugador().getApellidos(),
                    g.getEquipo().getNombre(), g.getGoles());
        }
    }

    // ------------------------------------------------------------------ seleccion y lectura
    private Partido seleccionarPartido(Campeonato campeonato, Scanner sc) {
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
        int opcion = leerEntero(sc, "Partido: ");
        if (opcion < 1 || opcion > partidos.size()) {
            System.out.println("Error: debe seleccionar un partido.");
            return null;
        }
        return partidos.get(opcion - 1);
    }

    private String leerTexto(Scanner sc, String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private int leerEntero(Scanner sc, String mensaje) {
        String texto = leerTexto(sc, mensaje);
        return Validador.aEntero(texto);
    }
}