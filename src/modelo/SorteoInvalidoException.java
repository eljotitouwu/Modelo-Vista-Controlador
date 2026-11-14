package modelo;

/**
 * Excepción propia 2.
 * Se lanza desde el método recursivo del sorteo cuando se encuentra una
 * condición lógica imposible o un error en la asignación de oponentes.
 */
public class SorteoInvalidoException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje descripción del error, se pasa a la superclase Exception
     */
    public SorteoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
