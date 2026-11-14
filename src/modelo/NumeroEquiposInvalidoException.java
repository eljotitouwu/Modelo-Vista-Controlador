package modelo;

/**
 * Excepción propia 1.
 * Se lanza cuando el número de equipos de una etapa no es válido para un
 * torneo de eliminación directa (debe ser una potencia de 2: 16, 8, 4, 2).
 */
public class NumeroEquiposInvalidoException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje descripción del error, se pasa a la superclase Exception
     */
    public NumeroEquiposInvalidoException(String mensaje) {
        super(mensaje);
    }
}
