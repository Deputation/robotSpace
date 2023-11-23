package com.github.deputation.language;

import com.github.deputation.instructions.RobotInstruction;

import java.util.List;
import java.util.Stack;

/**
 * The ParsingBodyStack class is used for tracking and managing the bodies of entities
 * during the parsing process of the programming language.
 */
public class ParsingBodyStack {
    /**
     * Stack of instruction bodies.
     */
    private Stack<List<RobotInstruction>> bodyStack;

    /**
     * Initializes a new instance of the ParsingBodyStack class, setting up an empty stack
     * to hold the entity bodies.
     */
    public ParsingBodyStack() {
        bodyStack = new Stack<>();
    }

    /**
     * Begins a new body by pushing its instruction list onto the stack.
     *
     * @param body A list of instructions that represents the new body.
     */
    public void startBody(List<RobotInstruction> body) {
        bodyStack.push(body);
    }

    /**
     * Completes the current body by popping its instruction list from the stack.
     *
     * @return A list of instructions that represents the ended body.
     */
    public List<RobotInstruction> endBody() {
        return bodyStack.pop();
    }

    /**
     * Returns the instruction list of the top body from the stack without popping it.
     *
     * @return A list of instructions that represents the top body on the stack.
     * @throws java.util.EmptyStackException if there are no bodies on the stack.
     */
    public List<RobotInstruction> peekBody() {
        return bodyStack.peek();
    }

    /**
     * Clears all bodies from the stack.
     */
    public void clear() {
        bodyStack.clear();
    }

    /**
     * Determines whether the parser is currently handling a body.
     *
     * @return true if there is at least one body on the stack, false otherwise.
     */
    public boolean isBodyPresent() {
        return !bodyStack.isEmpty();
    }
}