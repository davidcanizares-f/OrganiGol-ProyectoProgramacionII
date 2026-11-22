package modelo;

public enum Categoria {

    INFANTIL("Infantil", 6, 12),
    FEMENINO("Femenino", 15, 99),
    PRIMERA("Primera", 18, 99),
    MASTER("Máster", 35, 99);

    private final String etiqueta;
    private final int edadMinima;
    private final int edadMaxima;

    Categoria(String etiqueta, int edadMinima, int edadMaxima) {
        this.etiqueta = etiqueta;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
    }

    public boolean admiteEdad(int edad) {
        return edad >= edadMinima && edad <= edadMaxima;
    }

    // Texto para mostrar en pantalla (con tilde): Infantil, Femenino, Primera, Máster
    @Override
    public String toString() {
        return etiqueta;
    }

}