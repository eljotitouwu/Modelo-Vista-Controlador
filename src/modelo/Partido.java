package modelo;

import java.io.Serializable;

/** Enfrentamiento entre dos equipos dentro de una etapa. */
public class Partido implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Equipo local;
    private final Equipo visitante;
    private Equipo ganador; // null mientras no se juegue

    public Partido(Equipo local, Equipo visitante) {
        this.local = local;
        this.visitante = visitante;
    }

    public Equipo getLocal() {
        return local;
    }

    public Equipo getVisitante() {
        return visitante;
    }

    public Equipo getGanador() {
        return ganador;
    }

    public void setGanador(Equipo ganador) {
        this.ganador = ganador;
    }

    @Override
    public String toString() {
        return local + " vs " + visitante;
    }
}
