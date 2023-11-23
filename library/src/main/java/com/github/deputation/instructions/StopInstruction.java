package com.github.deputation.instructions;

import com.github.deputation.language.RobotContext;

public class StopInstruction implements RobotInstruction {
    /**
     * Executes the StopInstruction by invoking the `Stop` method on the provided RobotContext.
     *
     * @param context The RobotContext on which the Stop method should be invoked.
     */
    @Override
    public void execute(RobotContext context) {
        context.Stop();
    }

    /**
     * Retrieves the instruction type of this StopInstruction.
     *
     * @return The instruction type, which is "STOP".
     */
    @Override
    public String getInstructionType() {
        return "STOP";
    }
}
