package com.github.deputation.instructions;

import com.github.deputation.RobotExecutionException;
import com.github.deputation.language.RobotContext;

public interface RobotInstruction {
    /**
     * Executes the instruction using the provided RobotContext.
     *
     * @param context The RobotContext on which the instruction should be executed.
     * @throws RobotExecutionException If an error occurs during the execution of the instruction.
     */
    void execute(RobotContext context) throws RobotExecutionException;

    /**
     * Retrieves the instruction type.
     *
     * @return The instruction type as a string.
     */
    String getInstructionType();
}
