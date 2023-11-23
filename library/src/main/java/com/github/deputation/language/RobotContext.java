package com.github.deputation.language;

import com.github.deputation.RobotExecutionException;
import com.github.deputation.entities.RestrictedRobotContext;
import com.github.deputation.instructions.RobotInstruction;

import java.util.List;

public interface RobotContext extends RestrictedRobotContext {

    /**
     * Makes the robot follow the target with the specified label and parameters.
     *
     * @param label      The label of the target to follow.
     * @param parameters The parameters associated with the follow instruction.
     */
    void Follow(String label, double[] parameters) throws RobotExecutionException;

    /**
     * Repeats the specified instructions for the given number of times.
     *
     * @param times        The number of times to repeat the instructions.
     * @param instructions The list of instructions to repeat.
     */
    void Repeat(int times, List<RobotInstruction> instructions) throws RobotExecutionException;

    /**
     * Executes the instructions until the specified label is signaled.
     *
     * @param label        The label indicating the condition to continue executing the instructions.
     * @param instructions The list of instructions to execute until the condition is met.
     */
    void Until(String label, List<RobotInstruction> instructions);

    /**
     * Executes the instructions indefinitely in a loop.
     *
     * @param instructions The list of instructions to execute in the loop.
     */
    void DoForever(List<RobotInstruction> instructions);

    /**
     * That's all, folks.
     * */
    void Terminate();
}