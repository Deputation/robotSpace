package com.github.deputation.instructions;

import com.github.deputation.language.RobotContext;

public class SignalInstruction implements RobotInstruction {
    /**
     * Label to signal.
     */
    private final String label;

    /**
     * Constructs a new SignalInstruction with the specified label.
     *
     * @param label The label indicating the signal.
     */
    public SignalInstruction(String label) {
        this.label = label;
    }

    /**
     * Executes the SignalInstruction by invoking the `Signal` method on the provided RobotContext,
     * passing the label.
     *
     * @param context The RobotContext on which the Signal method should be invoked.
     */
    @Override
    public void execute(RobotContext context) {
        context.Signal(label);
    }

    /**
     * Retrieves the instruction type of this SignalInstruction.
     *
     * @return The instruction type, which is "SIGNAL".
     */
    @Override
    public String getInstructionType() {
        return "SIGNAL";
    }
}
