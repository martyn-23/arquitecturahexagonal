package com.example.demo.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra una entidad o recurso solicitado.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public RecursoNoEncontradoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
