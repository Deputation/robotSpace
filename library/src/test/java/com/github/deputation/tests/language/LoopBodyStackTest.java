package com.github.deputation.tests.language;

import com.github.deputation.instructions.RobotInstruction;
import com.github.deputation.instructions.MoveInstruction;
import com.github.deputation.language.LoopBodyStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoopBodyStackTest {
    private LoopBodyStack loopBodyStack;
    private List<RobotInstruction> body;
    private RobotInstruction instruction;

    @BeforeEach
    public void setUp() {
        loopBodyStack = new LoopBodyStack();
        body = new ArrayList<>();
        instruction = new MoveInstruction(new double[]{1.0, 1.0, 1.0});
        body.add(instruction);
    }

    @Test
    public void testStartBody() {
        loopBodyStack.startBody(body, 0, instruction);
        assertEquals(body, loopBodyStack.peekBody());
        assertEquals(0, loopBodyStack.peekInstructionPointer());
    }

    @Test
    public void testEndBody() {
        loopBodyStack.startBody(body, 0, instruction);
        assertEquals(body, loopBodyStack.endBody());
        assertFalse(loopBodyStack.isBodyPresent());
    }

    @Test
    public void testPeekBody() {
        loopBodyStack.startBody(body, 0, instruction);
        assertEquals(body, loopBodyStack.peekBody());
    }

    @Test
    public void testPeekInstructionPointer() {
        loopBodyStack.startBody(body, 0, instruction);
        assertEquals(0, loopBodyStack.peekInstructionPointer());
    }

    @Test
    public void testIncreaseIntegerValue() {
        loopBodyStack.startBody(body, 0, instruction);
        loopBodyStack.increaseIntegerValue();
        assertEquals(1, loopBodyStack.peekInstructionPointer());
    }

    @Test
    public void testClear() {
        loopBodyStack.startBody(body, 0, instruction);
        loopBodyStack.clear();
        assertFalse(loopBodyStack.isBodyPresent());
    }

    @Test
    public void testIsBodyPresent() {
        assertFalse(loopBodyStack.isBodyPresent());
        loopBodyStack.startBody(body, 0, instruction);
        assertTrue(loopBodyStack.isBodyPresent());
    }
}
