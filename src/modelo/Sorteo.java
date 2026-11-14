package modelo;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Algoritmo de sorteo RECURSIVO.
 * Empareja aleatoriamente a los equipos de una etapa garantizando que:
 *  - ningún equipo juegue dos veces en la misma etapa,
 *  - ningún equipo sea competidor y oponente a la vez,
 *  - no se repita un mismo partido dentro de la etapa.
 */
public class Sorteo {

    private Sorteo() {
        // Clase utilitaria: no se instancia
    }

    /**
     * Método recursivo. En cada llamada saca dos equipos al azar de la lista de
     * pendientes, forma un partido y se llama a sí mismo con los que quedan.
     *
     * Caso base: no quedan equipos pendientes.
     *
     * @param pendientes equipos que aún no tienen partido (se va vaciando)
     * @param partidos   lista donde se acumulan los partidos sorteados
     * @param usados     claves de los partidos ya creados (control de repetidos)
     * @param azar       generador de números aleatorios
     * @throws SorteoInvalidoException si se detecta una condición imposible
     */
    public static void emparejar(List<Equipo> pendientes, List<Partido> partidos,
                                 Set<String> usados, Random azar) throws SorteoInvalidoException {
        // Caso base
        if (pendientes.isEmpty()) {
            return;
        }
        // Condición imposible: queda un equipo sin oponente
        if (pendientes.size() == 1) {
            throw new SorteoInvalidoException(
                    "Sorteo imposible: el equipo " + pendientes.get(0) + " se quedó sin oponente.");
        }

        // Paso recursivo: elegir competidor y oponente al azar
        Equipo competidor = pendientes.remove(azar.nextInt(pendientes.size()));
        Equipo oponente = pendientes.remove(azar.nextInt(pendientes.size()));

        // Validaciones lógicas
        if (competidor.equals(oponente)) {
            throw new SorteoInvalidoException(
                    "Un equipo no puede ser competidor y oponente a la vez: " + competidor);
        }
        String clave = Math.min(competidor.getId(), oponente.getId()) + "-"
                + Math.max(competidor.getId(), oponente.getId());
        if (!usados.add(clave)) {
            throw new SorteoInvalidoException(
                    "Partido repetido en la misma etapa: " + competidor + " vs " + oponente);
        }

        partidos.add(new Partido(competidor, oponente));

        // Llamada recursiva con los equipos restantes
        emparejar(pendientes, partidos, usados, azar);
    }

    /** Atajo que prepara las estructuras auxiliares y lanza la recursión. */
    public static void emparejar(List<Equipo> pendientes, List<Partido> partidos, Random azar)
            throws SorteoInvalidoException {
        emparejar(pendientes, partidos, new HashSet<>(), azar);
    }
}
