package modelo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Clase principal del Modelo: guarda el estado completo del torneo
 * (equipos, etapas sorteadas y campeón). Implementa Serializable para
 * poder guardarse y cargarse desde un archivo binario (torneo.ser).
 */
public class Torneo implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Equipo> inscritos;
    private List<Equipo> clasificados;          // equipos que juegan la próxima etapa
    private final List<Etapa> etapas = new ArrayList<>();
    private Equipo campeon;
    private final Random azar = new Random();

    /**
     * @param equipos equipos participantes
     * @throws NumeroEquiposInvalidoException si no son una potencia de 2
     */
    public Torneo(List<Equipo> equipos) throws NumeroEquiposInvalidoException {
        validarCantidad(equipos.size());
        this.inscritos = new ArrayList<>(equipos);
        this.clasificados = new ArrayList<>(equipos);
    }

    /** Valida que la cantidad sea potencia de 2 (2, 4, 8, 16...). */
    private static void validarCantidad(int n) throws NumeroEquiposInvalidoException {
        if (n < 2 || (n & (n - 1)) != 0) {
            throw new NumeroEquiposInvalidoException(
                    "Número de equipos inválido (" + n + "). Debe ser una potencia de 2: 16, 8, 4, 2.");
        }
    }

    /** Devuelve el nombre de la etapa según cuántos equipos la juegan. */
    private static String nombreEtapa(int equipos) {
        switch (equipos) {
            case 16: return "OCTAVOS DE FINAL";
            case 8:  return "CUARTOS DE FINAL";
            case 4:  return "SEMIFINALES";
            case 2:  return "FINAL";
            default: return "RONDA DE " + equipos;
        }
    }

    /**
     * Sortea la siguiente etapa usando el algoritmo recursivo, simula los
     * ganadores y los deja clasificados para la etapa siguiente.
     *
     * @return la etapa recién sorteada
     */
    public Etapa sortearSiguienteEtapa() throws NumeroEquiposInvalidoException, SorteoInvalidoException {
        if (campeon != null) {
            throw new SorteoInvalidoException("El torneo ya terminó; el campeón es " + campeon + ".");
        }
        validarCantidad(clasificados.size());

        List<Equipo> pendientes = new ArrayList<>(clasificados);
        Collections.shuffle(pendientes, azar);
        List<Partido> partidos = new ArrayList<>();

        // Sorteo recursivo
        Sorteo.emparejar(pendientes, partidos, azar);

        // Simulación de resultados: el ganador pasa a la siguiente etapa
        List<Equipo> ganadores = new ArrayList<>();
        for (Partido p : partidos) {
            Equipo g = azar.nextBoolean() ? p.getLocal() : p.getVisitante();
            p.setGanador(g);
            ganadores.add(g);
        }

        Etapa etapa = new Etapa(nombreEtapa(clasificados.size()), partidos);
        etapas.add(etapa);
        clasificados = ganadores;

        if (clasificados.size() == 1) {
            campeon = clasificados.get(0);
        }
        return etapa;
    }

    public boolean isFinalizado() {
        return campeon != null;
    }

    public Equipo getCampeon() {
        return campeon;
    }

    public List<Etapa> getEtapas() {
        return Collections.unmodifiableList(etapas);
    }

    public List<Equipo> getInscritos() {
        return Collections.unmodifiableList(inscritos);
    }

    // ------------------------------------------------------------------
    // Persistencia (serialización / deserialización)
    // ------------------------------------------------------------------

    /** Guarda este torneo en un archivo binario. */
    public void guardar(String ruta) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ruta))) {
            out.writeObject(this);
        }
    }

    /** Reconstruye un torneo desde un archivo binario. */
    public static Torneo cargar(String ruta) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ruta))) {
            return (Torneo) in.readObject();
        }
    }

    /** Crea los 16 equipos iniciales de la liga. */
    public static List<Equipo> equiposIniciales() {
        String[] nombres = {
            "Liga de Quito", "Barcelona SC", "Emelec", "Independiente del Valle",
            "Aucas", "El Nacional", "Delfín SC", "Orense SC",
            "Macará", "Universidad Católica", "Técnico Universitario", "Mushuc Runa",
            "Libertad FC", "Deportivo Cuenca", "Guayaquil City", "Imbabura SC"
        };
        List<Equipo> lista = new ArrayList<>();
        for (int i = 0; i < nombres.length; i++) {
            lista.add(new Equipo(i + 1, nombres[i]));
        }
        return lista;
    }
}
