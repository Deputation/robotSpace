package com.github.deputation.tests.entities;

import com.github.deputation.RobotExecutionException;
import com.github.deputation.entities.Robot;
import com.github.deputation.instructions.MoveInstruction;
import com.github.deputation.instructions.RobotInstruction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

    private Robot robot;

    @BeforeEach
    void setUp() {
        robot = new Robot();
    }

    @Test
    void inputSignalingRobots() {
        Robot r1 = new Robot();
        Robot r2 = new Robot();
        r1.Signal("Z1");
        r2.Signal("Z2");
        List<Robot> swarm = new ArrayList<>();
        swarm.add(r1);
        swarm.add(r2);

        robot.inputSignalingRobots(swarm);

        var size = robot.getSignalingRobots().size();

        assertTrue(  size == 2);
    }

    @Test
    void getSignals() {
        robot.Signal("signal1");
        robot.Signal("signal2");

        assertEquals(new HashSet<>(Arrays.asList("signal1", "signal2")), robot.getSignals());
    }

    @Test
    void program() {
        RobotInstruction instruction = new MoveInstruction(new double[] { 1, 2, 3});
        robot.program(Arrays.asList(instruction, instruction));

        assertFalse(robot.isRobotDone());
    }

    @Test
    void tick() throws RobotExecutionException {
        RobotInstruction instruction = new MoveInstruction(new double[] { 1, 2, 3});
        List<RobotInstruction> prog = new ArrayList<>();
        prog.add(instruction);
        robot.program(prog);

        robot.tick(1, 10000);
        robot.tick(1, 10000);

        assertTrue(robot.isRobotDone());
    }

    @Test
    void Move() {
        robot.setX(5);
        robot.setY(5);
        robot.Move(new double[] {10.0, 20.0}, 5.0);

        assertEquals(robot.getX() + 10.0, robot.getTargetX());
        assertEquals(robot.getY() + 20.0, robot.getTargetY());
        assertEquals(5.0, robot.getSpeed());
    }

    @Test
    void Signal() {
        robot.Signal("signal1");

        assertTrue(robot.getSignals().contains("signal1"));
    }

    @Test
    void Unsignal() throws RobotExecutionException {
        robot.Signal("signal1");
        robot.Unsignal("signal1");

        assertFalse(robot.getSignals().contains("signal1"));
        assertThrows(RobotExecutionException.class, () -> {
            robot.Unsignal("signal1");
        });
    }

    @Test
    void Stop() {
        robot.Move(new double[] {10.0, 20.0}, 5.0);
        robot.Stop();

        assertEquals(0, robot.getSpeed());
    }

    @Test
    void ContinueFor() {
        robot.ContinueFor(10);
    }

    @Test
    void Terminate() {
        robot.Terminate();

        assertTrue(robot.isRobotDone());
    }
}
