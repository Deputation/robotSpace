package com.github.deputation.instructions;

import com.github.deputation.language.RobotContext;

import java.util.Random;

public class MoveRandomInstruction implements RobotInstruction {
    /**
     * Move random instruction parameters.
     */
    private final double x1, y1, x2, y2, speed;
    /**
     * Random engine for extracting random data.
     */
    private static final Random rand = new Random();

    /**
     * Constructs a new MoveRandomInstruction with the specified arguments.
     *
     * @param args The array of arguments representing the coordinates and speed.
     */
    public MoveRandomInstruction(double[] args) {
        x1 = args[0];
        x2 = args[1];
        y1 = args[2];
        y2 = args[3];
        speed = args[4];
    }

    /**
     * Executes the MoveRandomInstruction by generating random target coordinates within the specified range
     * and invoking the `Move` method on the provided RobotContext, passing the target coordinates and speed.
     *
     * @param context The RobotContext on which the Move method should be invoked.
     */
    @Override
    public void execute(RobotContext context) {
        double targetX = getRandomCoordinate(x1, x2);
        double targetY = getRandomCoordinate(y1, y2);
        double[] arr = { targetX, targetY };
        context.Move(arr, speed);
    }

    /**
     * Generates a random coordinate within the specified range.
     *
     * @param min The minimum value of the coordinate range.
     * @param max The maximum value of the coordinate range.
     * @return A random coordinate within the specified range.
     */
    private double getRandomCoordinate(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }

    /**
     * Retrieves the instruction type of this MoveRandomInstruction.
     *
     * @return The instruction type, which is "MOVE".
     */
    @Override
    public String getInstructionType() {
        return "MOVE";
    }
}
