package modelo;

import negocio.ReglasJuego;

public class TablaPosiciones {

    //Atributos
    private Equipo equipo;
    private int pj;
    private int pg;
    private int pe;
    private int pp;
    private int gf;
    private int gc;

    //Constructor
    public TablaPosiciones(Equipo equipo) {
        this.equipo = equipo;
    }

    //Metodos

    //Diferencia de goles
    public int dg() {
        return gf - gc;
    }

    public int puntos() {
        return pg * ReglasJuego.PUNTOS_VICTORIA
                + pe * ReglasJuego.PUNTOS_EMPATE
                + pp * ReglasJuego.PUNTOS_DERROTA;
    }

    //Getters y Setters
    public Equipo getEquipo() {
        return equipo;
    }

    public int getPj() {
        return pj;
    }

    public int getPg() {
        return pg;
    }

    public int getPe() {
        return pe;
    }

    public int getPp() {
        return pp;
    }

    public int getGf() {
        return gf;
    }

    public int getGc() {
        return gc;
    }

    public void setPj(int pj) {
        this.pj = pj;
    }

    public void setPg(int pg) {
        this.pg = pg;
    }

    public void setPe(int pe) {
        this.pe = pe;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public void setGf(int gf) {
        this.gf = gf;
    }

    public void setGc(int gc) {
        this.gc = gc;
    }
}