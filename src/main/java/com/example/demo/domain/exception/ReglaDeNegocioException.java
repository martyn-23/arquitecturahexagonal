package com.example.demo.domain.exception;

/**
 * Excepción lanzada cuando una operación viola una regla de negocio del dominio.
 */
public class ReglaDeNegocioException extends RuntimeException {

    public ReglaDeNegocioException(String mensaje) {
        super(mensaje);
    }

    public ReglaDeNegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
