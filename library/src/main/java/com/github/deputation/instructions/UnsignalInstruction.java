package com.github.deputation.instructions;

import com.github.deputation.RobotExecutionException;
import com.github.deputation.language.RobotContext;

public class UnsignalInstruction implements RobotInstruction {
    /**
     * Label to unsignal.
     */
    private final String label;

    /**
     * Constructs a new UnsignalInstruction with the specified label.
     *
     * @param label The label indicating the signal to unsignal.
     */
    public UnsignalInstruction(String label) {
        this.label = label;
    }

    /**
     * Executes the UnsignalInstruction by invoking the `Unsignal` method on the provided RobotContext,
     * passing the label.
     *
     * @param context The RobotContext on which the Unsignal method should be invoked.
     */
    @Override
    public void execute(RobotContext context) throws RobotExecutionException {
        context.Unsignal(label);
    }

    /**
     * Retrieves the instruction type of this UnsignalInstruction.
     *
     * @return The instruction type, which is "UNSIGNAL".
     */
    @Override
    public String getInstructionType() {
        return "UNSIGNAL";
    }
}
