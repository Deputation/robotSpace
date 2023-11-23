package com.github.deputation.instructions;

import com.github.deputation.language.RobotContext;

public class EndInstruction implements RobotInstruction {
    /**
     * Executes the EndInstruction by invoking the `terminate` method on the provided RobotContext.
     *
     * @param context The RobotContext on which the terminate method should be invoked.
     */
    @Override
    public void execute(RobotContext context) {
        context.Terminate();
    }

    /**
     * Retrieves the instruction type of this EndInstruction.
     *
     * @return The instruction type, which is "END".
     */
    @Override
    public String getInstructionType() {
        return "END";
    }
}
