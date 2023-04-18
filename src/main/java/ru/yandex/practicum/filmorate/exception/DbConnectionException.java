package ru.yandex.practicum.filmorate.exception;

public class DbConnectionException extends RuntimeException {
    public DbConnectionException() {
        super();
    }

    public DbConnectionException(String message) {
        super(message);
    }

    public DbConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbConnectionException(Throwable cause) {
        super(cause);
    }
}
