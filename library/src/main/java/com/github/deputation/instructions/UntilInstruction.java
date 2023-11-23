package com.github.deputation.instructions;

import com.github.deputation.language.RobotContext;

import java.util.List;

public class UntilInstruction implements RobotInstruction {
    /**
     * The label to check for.
     */
    private final String label;
    /**
     * The instructions to execute.
     */
    private final List<RobotInstruction> instructions;

    /**
     * Constructs a new UntilInstruction with the specified label and list of instructions.
     *
     * @param label       The label indicating the condition for the until loop.
     * @param instructions The list of RobotInstruction objects representing the instructions within the until loop.
     */
    public UntilInstruction(String label, List<RobotInstruction> instructions) {
        this.label = label;
        this.instructions = instructions;
    }

    /**
     * Executes the UntilInstruction by invoking the `Until` method on the provided RobotContext,
     * passing the label and the list of instructions.
     *
     * @param context The RobotContext on which the Until method should be invoked.
     */
    @Override
    public void execute(RobotContext context) {
        context.Until(label, instructions);
    }

    /**
     * Retrieves the instruction type of this UntilInstruction.
     *
     * @return The instruction type, which is "UNTIL".
     */
    @Override
    public String getInstructionType() {
        return "UNTIL";
    }
}