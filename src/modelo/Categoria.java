package modelo;

public enum Categoria {

    INFANTIL(6, 12),
    FEMENINO(15, 99),
    PRIMERA(18, 99),
    MASTER(35, 99);

    private final int edadMinima;
    private final int edadMaxima;

    Categoria(int edadMinima, int edadMaxima) {
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
    }

    public boolean admiteEdad(int edad) {
        return edad >= edadMinima && edad <= edadMaxima;
    }

}
