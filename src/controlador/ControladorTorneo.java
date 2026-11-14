package controlador;

import java.io.File;
import java.io.IOException;

import modelo.Etapa;
import modelo.NumeroEquiposInvalidoException;
import modelo.SorteoInvalidoException;
import modelo.Torneo;
import vista.VistaConsola;

/**
 * Controlador: recibe las acciones de la Vista, invoca al Modelo
 * (incluida la persistencia) y devuelve los resultados a la Vista.
 */
public class ControladorTorneo {

    private static final String ARCHIVO = "torneo.ser";

    private final VistaConsola vista;
    private Torneo torneo;

    public ControladorTorneo(VistaConsola vista) {
        this.vista = vista;
    }

    /** Bucle principal de la aplicación. */
    public void iniciar() {
        // Al inicio intenta cargar un torneo guardado previamente
        if (new File(ARCHIVO).exists()) {
            cargar();
        }

        int opcion;
        do {
            vista.mostrarMenu();
            opcion = vista.leerOpcion();
            switch (opcion) {
                case 1: nuevoTorneo(); break;
                case 2: sortearEtapa(); break;
                case 3: sortearTodo(); break;
                case 4: verTorneo(); break;
                case 5: guardar(); break;
                case 6: cargar(); break;
                case 0: vista.mostrarMensaje("¡Hasta luego!"); break;
                default: vista.mostrarError("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void nuevoTorneo() {
        try {
            torneo = new Torneo(Torneo.equiposIniciales());
            vista.mostrarMensaje("Torneo creado con " + torneo.getInscritos().size() + " equipos.");
        } catch (NumeroEquiposInvalidoException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private boolean hayTorneo() {
        if (torneo == null) {
            vista.mostrarError("Primero cree o cargue un torneo.");
            return false;
        }
        return true;
    }

    private void sortearEtapa() {
        if (!hayTorneo()) return;
        try {
            Etapa etapa = torneo.sortearSiguienteEtapa();
            vista.mostrarEtapa(etapa);
            if (torneo.isFinalizado()) {
                vista.mostrarMensaje("*** CAMPEÓN: " + torneo.getCampeon() + " ***");
            }
        } catch (NumeroEquiposInvalidoException | SorteoInvalidoException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void sortearTodo() {
        if (!hayTorneo()) return;
        while (!torneo.isFinalizado()) {
            try {
                vista.mostrarEtapa(torneo.sortearSiguienteEtapa());
            } catch (NumeroEquiposInvalidoException | SorteoInvalidoException e) {
                vista.mostrarError(e.getMessage());
                return;
            }
        }
        vista.mostrarMensaje("\n*** CAMPEÓN: " + torneo.getCampeon() + " ***");
    }

    private void verTorneo() {
        if (hayTorneo()) {
            vista.mostrarTorneo(torneo);
        }
    }

    private void guardar() {
        if (!hayTorneo()) return;
        try {
            torneo.guardar(ARCHIVO);
            vista.mostrarMensaje("Torneo guardado en " + ARCHIVO);
        } catch (IOException e) {
            vista.mostrarError("No se pudo guardar el torneo: " + e.getMessage());
        }
    }

    private void cargar() {
        try {
            torneo = Torneo.cargar(ARCHIVO);
            vista.mostrarMensaje("Torneo cargado desde " + ARCHIVO);
        } catch (IOException e) {
            vista.mostrarError("No se pudo leer el archivo: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            vista.mostrarError("El archivo no contiene un torneo válido: " + e.getMessage());
        }
    }
}
