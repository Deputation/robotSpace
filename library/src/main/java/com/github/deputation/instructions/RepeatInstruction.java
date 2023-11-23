package com.github.deputation.instructions;

import com.github.deputation.RobotExecutionException;
import com.github.deputation.language.RobotContext;

import java.util.List;

public class RepeatInstruction implements RobotInstruction {
    /**
     * Amount of times instructions should be repeated.
     */
    private final int times;
    /**
     * Instructions that should be repeated.
     */
    private final List<RobotInstruction> instructions;

    /**
     * Constructs a new RepeatInstruction with the specified number of times and list of instructions.
     *
     * @param times        The number of times the instructions should be repeated.
     * @param instructions The list of RobotInstruction objects representing the instructions to be repeated.
     */
    public RepeatInstruction(int times, List<RobotInstruction> instructions) {
        this.times = times;
        this.instructions = instructions;
    }

    /**
     * Executes the RepeatInstruction by invoking the `Repeat` method on the provided RobotContext,
     * passing the number of times and the list of instructions.
     *
     * @param context The RobotContext on which the Repeat method should be invoked.
     */
    @Override
    public void execute(RobotContext context) throws RobotExecutionException {
        context.Repeat(times, instructions);
    }

    /**
     * Retrieves the instruction type of this RepeatInstruction.
     *
     * @return The instruction type, which is "REPEAT".
     */
    @Override
    public String getInstructionType() {
        return "REPEAT";
    }
}
