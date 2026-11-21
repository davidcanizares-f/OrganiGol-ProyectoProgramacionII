package negocio;

import modelo.Partido;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ActaArchivo {

    //Atributos
    private String carpeta = "actas";

    //Constructor
    public ActaArchivo() {

    }

    //Metodos

    //Guarda el acta en actas/[local]_vs_[visitante].txt y devuelve la ruta del archivo.
    //Si no se pudo guardar devuelve null
    public String guardar(Partido partido, String texto) {
        File archivo = new File(carpeta, nombreArchivo(partido));
        try {
            File directorio = new File(carpeta);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }
            PrintWriter escritor = new PrintWriter(archivo, "UTF-8");
            escritor.print(texto);
            escritor.close();
        } catch (IOException e) {
            return null;
        }
        return archivo.getPath();
    }

    //Indica si el partido ya tiene su acta guardada
    public boolean existeActa(Partido partido) {
        File archivo = new File(carpeta, nombreArchivo(partido));
        return archivo.exists();
    }

    //Lee el acta de un partido cerrado. Si no existe devuelve null
    public String leer(Partido partido) {
        File archivo = new File(carpeta, nombreArchivo(partido));
        if (!archivo.exists()) {
            return null;
        }
        String texto = "";
        try {
            Scanner lector = new Scanner(archivo, "UTF-8");
            while (lector.hasNextLine()) {
                texto = texto + lector.nextLine() + "\n";
            }
            lector.close();
        } catch (FileNotFoundException e) {
            return null;
        }
        return texto;
    }

    //El nombre de los equipos puede tener espacios o simbolos que no sirven en un archivo
    private String nombreArchivo(Partido partido) {
        String nombre = partido.getLocal().getNombre() + "_vs_" + partido.getVisitante().getNombre();
        String limpio = "";
        for (int i = 0; i < nombre.length(); i++) {
            char letra = nombre.charAt(i);
            if (Character.isLetterOrDigit(letra)) {
                limpio = limpio + letra;
            } else {
                limpio = limpio + "_";
            }
        }
        return limpio + ".txt";
    }


}
