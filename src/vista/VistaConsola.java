package vista;

import java.util.Scanner;

import modelo.Etapa;
import modelo.Partido;
import modelo.Torneo;

/**
 * Vista: maneja toda la entrada/salida por consola.
 * No contiene lógica de negocio.
 */
public class VistaConsola {

    private final Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        System.out.println();
        System.out.println("===== SORTEO LIGA PROFESIONAL =====");
        System.out.println("1. Nuevo torneo (16 equipos)");
        System.out.println("2. Sortear siguiente etapa");
        System.out.println("3. Sortear todas las etapas");
        System.out.println("4. Ver torneo");
        System.out.println("5. Guardar torneo (torneo.ser)");
        System.out.println("6. Cargar torneo (torneo.ser)");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
    }

    /** Lee una opción numérica; devuelve -1 si lo ingresado no es un número. */
    public int leerOpcion() {
        String linea = scanner.hasNextLine() ? scanner.nextLine().trim() : "0";
        try {
            return Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("[ERROR] " + mensaje);
    }

    public void mostrarEtapa(Etapa etapa) {
        System.out.println();
        System.out.println("--- " + etapa.getNombre() + " ---");
        int n = 1;
        for (Partido p : etapa.getPartidos()) {
            System.out.printf("Partido %d: %s  vs  %s   -> Pasa: %s%n",
                    n++, p.getLocal(), p.getVisitante(), p.getGanador());
        }
    }

    public void mostrarTorneo(Torneo torneo) {
        if (torneo.getEtapas().isEmpty()) {
            System.out.println("Aún no se ha sorteado ninguna etapa.");
        }
        for (Etapa e : torneo.getEtapas()) {
            mostrarEtapa(e);
        }
        if (torneo.isFinalizado()) {
            System.out.println();
            System.out.println("*** CAMPEÓN: " + torneo.getCampeon() + " ***");
        }
    }
}
