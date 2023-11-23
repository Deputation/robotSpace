package com.github.deputation.instructions;

import com.github.deputation.language.RobotContext;

public class MoveInstruction implements RobotInstruction {
    /**
     * Coordinates the Robot will start moving towards.
     */
    private final double[] coordinates;
    /**
     * The speed (in meters per second) the Robot will start moving at.
     */
    private final double speed;

    /**
     * Constructs a new MoveInstruction with the specified arguments.
     *
     * @param args The array of arguments representing the coordinates and speed.
     */
    public MoveInstruction(double[] args) {
        this.coordinates = new double[]{args[0], args[1]};
        this.speed = args[2];
    }

    /**
     * Constructs a new MoveInstruction with the specified arguments.
     *
     * @param coordinates The relative coordinates that the move instruction should move the Robot to.
     * @param speed The speed at which the Robot should move.
     */
    public MoveInstruction(double[] coordinates, double speed) {
        this.coordinates = coordinates;
        this.speed = speed;
    }

    /**
     * Executes the MoveInstruction by invoking the `Move` method on the provided RobotContext,
     * passing the coordinates and speed.
     *
     * @param context The RobotContext on which the Move method should be invoked.
     */
    @Override
    public void execute(RobotContext context) {
        context.Move(coordinates, speed);
    }

    /**
     * Retrieves the instruction type of this MoveInstruction.
     *
     * @return The instruction type, which is "MOVE".
     */
    @Override
    public String getInstructionType() {
        return "MOVE";
    }
}
