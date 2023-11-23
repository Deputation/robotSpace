package com.github.deputation.instructions;

import com.github.deputation.language.RobotContext;

public class ContinueInstruction implements RobotInstruction {
    /**
     * Seconds the continuation of movement should happen for.
     */
    private final int seconds;

    /**
     * Constructs a new ContinueInstruction with the specified duration in seconds.
     *
     * @param seconds The duration in seconds for which the robot should continue.
     */
    public ContinueInstruction(int seconds) {
        this.seconds = seconds;
    }

    /**
     * Executes the ContinueInstruction by invoking the `ContinueFor` method on the provided RobotContext,
     * passing the duration in seconds.
     *
     * @param context The RobotContext on which the ContinueFor method should be invoked.
     */
    @Override
    public void execute(RobotContext context) {
        context.ContinueFor(seconds);
    }

    /**
     * Retrieves the instruction type of this ContinueInstruction.
     *
     * @return The instruction type, which is "CONTINUE".
     */
    @Override
    public String getInstructionType() {
        return "CONTINUE";
    }
}