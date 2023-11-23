package com.github.deputation.instructions;

import com.github.deputation.language.RobotContext;

import java.util.List;

public class DoForeverInstruction implements RobotInstruction {
    /**
     * Lists of instructions that should be executed forever.
     */
    private final List<RobotInstruction> instructions;

    /**
     * Constructs a new DoForeverInstruction with the specified list of instructions.
     *
     * @param instructions The list of RobotInstruction objects representing the instructions to be executed.
     */
    public DoForeverInstruction(List<RobotInstruction> instructions) {
        this.instructions = instructions;
    }

    /**
     * Executes the DoForeverInstruction by invoking the `DoForever` method on the provided RobotContext,
     * passing the list of instructions.
     *
     * @param context The RobotContext on which the DoForever method should be invoked.
     */
    @Override
    public void execute(RobotContext context) {
        context.DoForever(instructions);
    }

    /**
     * Retrieves the instruction type of this DoForeverInstruction.
     *
     * @return The instruction type, which is "DOFOREVER".
     */
    @Override
    public String getInstructionType() {
        return "DOFOREVER";
    }
}
