package com.github.deputation.language;

import com.github.deputation.instructions.RobotInstruction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * The LoopBodyStack class provides functionality to track bodies with associated instruction pointers
 * during execution of the programming language.
 */
public class LoopBodyStack {
    /**
     * A stack to store bodies of instructions.
     */
    private Stack<List<RobotInstruction>> bodyStack;

    /**
     * A map to associate bodies of instructions with instruction pointers.
     */
    private Map<List<RobotInstruction>, Integer> bodyToInstructionPointerMap;

    /**
     * A map to associate bodies of instructions with loop instructions.
     */
    private Map<List<RobotInstruction>, RobotInstruction> bodyToLoopInstructionMap;

    /**
     * Constructs a new LoopBodyStack object.
     * Initializes an empty stack and map to store bodies and their associated instruction pointers.
     */
    public LoopBodyStack() {
        bodyStack = new Stack<>();
        bodyToInstructionPointerMap = new HashMap<>();
        bodyToLoopInstructionMap = new HashMap<>();
    }

    /**
     * Starts a new body of instructions with the specified parameters.
     *
     * @param body           the body of instructions to start
     * @param intValue       the initial instruction pointer value for the body
     * @param loopInstruction the loop instruction associated with the body
     */
    public void startBody(List<RobotInstruction> body, int intValue, RobotInstruction loopInstruction) {
        bodyStack.push(body);
        bodyToInstructionPointerMap.put(body, intValue);
        bodyToLoopInstructionMap.put(body, loopInstruction);
    }

    /**
     * Ends the current body of instructions and retrieves the ended body.
     *
     * @return the ended body of instructions
     */
    public List<RobotInstruction> endBody() {
        List<RobotInstruction> body = bodyStack.pop();
        bodyToInstructionPointerMap.remove(body);
        bodyToLoopInstructionMap.remove(body);

        return body;
    }

    /**
     * Retrieves the body of instructions at the top of the body stack without removing it.
     *
     * @return the body of instructions at the top of the body stack
     * @throws java.util.EmptyStackException if the body stack is empty.
     */
    public List<RobotInstruction> peekBody() {
        return bodyStack.peek();
    }

    /**
     * Retrieves the instruction pointer value of the current body of instructions at the top of the body stack.
     *
     * @return the instruction pointer value of the current body of instructions
     */
    public int peekInstructionPointer() {
        List<RobotInstruction> body = peekBody();
        return bodyToInstructionPointerMap.get(body);
    }

    /**
     * Retrieves the instruction pointer value of the specified body of instructions.
     *
     * @param body the body of instructions to retrieve the instruction pointer value from
     * @return the instruction pointer value of the specified body of instructions
     */
    public int peekInstructionPointer(List<RobotInstruction> body) {
        return bodyToInstructionPointerMap.get(body);
    }

    /**
     * Retrieves the loop instruction associated with the current body of instructions at the top of the body stack.
     *
     * @return the loop instruction associated with the current body of instructions
     */
    public RobotInstruction peekLoopInstruction() {
        List<RobotInstruction> body = peekBody();
        return bodyToLoopInstructionMap.get(body);
    }

    /**
     * Retrieves the loop instruction associated with the specified body of instructions.
     *
     * @param body the body of instructions to retrieve the loop instruction from
     * @return the loop instruction associated with the specified body of instructions
     */
    public RobotInstruction peekLoopInstruction(List<RobotInstruction> body) {
        return bodyToLoopInstructionMap.get(body);
    }

    /**
     * Increases the instruction pointer value of the specified body of instructions by 1.
     *
     * @param body the body of instructions to increase the instruction pointer value for
     */
    public void increaseIntegerValue(List<RobotInstruction> body) {
        var instructionPointer = peekInstructionPointer(body);
        bodyToInstructionPointerMap.replace(body, instructionPointer, instructionPointer + 1);
    }

    /**
     * Increases the instruction pointer value of the current body of instructions by 1.
     *
     * @throws IllegalStateException if the instruction pointer is already at the end of the body
     */
    public void increaseIntegerValue() {
        var body = peekBody();
        var instructionPointer = peekInstructionPointer(body);

        if (instructionPointer >= body.size()) {
            throw new IllegalStateException("Increasing IP when unnecessary.");
        }

        bodyToInstructionPointerMap.replace(body, instructionPointer, instructionPointer + 1);
    }

    /**
     * Restarts the current body of instructions by resetting the instruction pointer to 0.
     */
    public void restartBody() {
        var body = peekBody();
        var instructionPointer = peekInstructionPointer(body);
        bodyToInstructionPointerMap.replace(body, instructionPointer, 0);
    }

    /**
     * Clears all the stored bodies of instructions, instruction pointers, and loop instructions.
     * Resets the body stack, instruction pointer map, and loop instruction map.
     */
    public void clear() {
        bodyStack.clear();
        bodyToInstructionPointerMap.clear();
        bodyToLoopInstructionMap.clear();
    }

    /**
     * Checks if the tracker is currently inside a body.
     *
     * @return true if the tracker is inside a body, false otherwise.
     */
    public boolean isBodyPresent() {
        return !bodyStack.isEmpty();
    }
}