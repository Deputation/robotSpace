package com.github.deputation.entities;

import com.github.deputation.RobotExecutionException;

public interface RestrictedRobotContext {

    /**
     * Moves the robot to the specified coordinates with the given speed.
     *
     * @param coordinates The coordinates to move the robot to.
     * @param speed       The speed at which the robot should move.
     */
    void Move(double[] coordinates, double speed);

    /**
     * Sends a signal with the specified label.
     *
     * @param label The label of the signal to send.
     */
    void Signal(String label);

    /**
     * Unsignals the signal with the specified label.
     *
     * @param label The label of the signal to unsignal.
     */
    void Unsignal(String label) throws RobotExecutionException;

    /**
     * Stops the robot.
     */
    void Stop();

    /**
     * Continues the robot's movement for a specified duration.
     *
     * @param seconds The number of seconds for which the robot should continue moving in the current heading.
     */
    void ContinueFor(int seconds);
}
