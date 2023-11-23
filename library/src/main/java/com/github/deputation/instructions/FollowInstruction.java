package com.github.deputation.instructions;

import com.github.deputation.RobotExecutionException;
import com.github.deputation.language.RobotContext;

public class FollowInstruction implements RobotInstruction {
    /**
     * Label to follow.
     */
    private final String label;
    /**
     * Follow instruction parameters, parsed by the RobotContext.
     */
    private final double[] parameters;

    /**
     * Constructs a new FollowInstruction with the specified label and parameters.
     *
     * @param label      The label indicating the target to follow.
     * @param parameters The parameters associated with the follow instruction.
     */
    public FollowInstruction(String label, double[] parameters) {
        this.label = label;
        this.parameters = parameters;
    }

    /**
     * Executes the FollowInstruction by invoking the `Follow` method on the provided RobotContext,
     * passing the label and parameters.
     *
     * @param context The RobotContext on which the Follow method should be invoked.
     */
    @Override
    public void execute(RobotContext context) throws RobotExecutionException {
        context.Follow(label, parameters);
    }

    /**
     * Retrieves the instruction type of this FollowInstruction.
     *
     * @return The instruction type, which is "FOLLOW".
     */
    @Override
    public String getInstructionType() {
        return "FOLLOW";
    }
}
