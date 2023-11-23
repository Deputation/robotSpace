package com.github.deputation;

/**
 * Exception thrown when an error occurs during robot execution.
 */
public class RobotExecutionException extends Exception {
    /**
     * Constructs a RobotExecutionException with the specified error message.
     *
     * @param message The error message associated with the exception.
     */
    public RobotExecutionException(String message) {
        super(message);
    }
}
