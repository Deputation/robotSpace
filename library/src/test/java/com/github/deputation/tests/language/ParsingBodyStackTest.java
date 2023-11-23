package com.github.deputation.tests.language;

import com.github.deputation.instructions.RobotInstruction;
import com.github.deputation.instructions.MoveInstruction;
import com.github.deputation.language.ParsingBodyStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParsingBodyStackTest {
    private ParsingBodyStack stack;

    @BeforeEach
    public void setUp() {
        stack = new ParsingBodyStack();
    }

    @Test
    public void testStartAndEndBody() {
        assertTrue(stack.isBodyPresent() == false);

        List<RobotInstruction> body1 = List.of(new MoveInstruction(new double[]{1.0, 2.0, 3.0}));
        stack.startBody(body1);
        assertTrue(stack.isBodyPresent());

        List<RobotInstruction> endedBody = stack.endBody();
        assertEquals(body1, endedBody);
        assertFalse(stack.isBodyPresent());
    }

    @Test
    public void testPeekBody() {
        List<RobotInstruction> body1 = List.of(new MoveInstruction(new double[]{1.0, 2.0, 3.0}));
        stack.startBody(body1);

        List<RobotInstruction> peekedBody = stack.peekBody();
        assertEquals(body1, peekedBody);

        // Ensure peeking doesn't remove the body
        assertTrue(stack.isBodyPresent());
    }

    @Test
    public void testClear() {
        List<RobotInstruction> body1 = List.of(new MoveInstruction(new double[]{1.0, 2.0, 3.0}));
        stack.startBody(body1);
        assertTrue(stack.isBodyPresent());

        stack.clear();
        assertFalse(stack.isBodyPresent());
    }

    @Test
    public void testEmptyStackException() {
        assertThrows(java.util.EmptyStackException.class, () -> stack.peekBody());
        assertThrows(java.util.EmptyStackException.class, () -> stack.endBody());
    }
}
