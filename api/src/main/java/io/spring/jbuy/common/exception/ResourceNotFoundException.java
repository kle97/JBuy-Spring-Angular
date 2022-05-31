package io.spring.jbuy.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Class<?> clazz, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", clazz.getSimpleName(), fieldName, fieldValue));
    }

    public ResourceNotFoundException(Class<?> clazz, Object id) {
        super(String.format("%s not found with id: '%s'", clazz.getSimpleName(), id));
    }

    public ResourceNotFoundException(Class<?> clazz1, Class<?> clazz2, Object id) {
        super(String.format("%s not found with %s id: '%s'", clazz1.getSimpleName(), clazz2.getSimpleName(), id));
    }

    public ResourceNotFoundException(Class<?> clazz, Class<?> clazz1, Object id1, Class<?> clazz2, Object id2) {
        super(String.format("%s not found with %s id '%s' and %s id '%s'", clazz.getSimpleName(),
                            clazz1.getSimpleName(), id1, clazz2.getSimpleName(), id2));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
