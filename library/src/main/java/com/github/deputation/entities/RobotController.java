package com.github.deputation.entities;

import com.github.deputation.RobotExecutionException;
import com.github.deputation.instructions.RobotInstruction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RobotController {
    /**
     * List representing the robot swarm.
     */
    private final List<Robot> swarm;

    /**
     * Constructs a RobotController with the specified number of robots.
     *
     * @param robots the number of robots to create in the swarm
     */
    public RobotController(int robots) {

        swarm = new ArrayList<>();

        for (int i = 0; i < robots; i++) {
            swarm.add(new Robot());
        }
    }

    /**
     * Retrieves the list of robots in the swarm.
     *
     * @return the list of robots in the swarm
     */
    public List<Robot> getSwarm() {
        return swarm;
    }

    /**
     * Programs all the robots in the swarm with the specified instructions.
     *
     * @param instructions the list of instructions to program the robots with
     * @throws RobotExecutionException if an error occurs during execution
     */
    public void programSwarm(List<RobotInstruction> instructions) throws RobotExecutionException {
        swarm.forEach(r -> r.program(instructions));
    }

    /**
     * Checks if all robots in the swarm have finished their execution.
     *
     * @return true if all robots in the swarm have finished execution, false otherwise
     */
    public boolean isSwarmDone() {
        return swarm.stream().allMatch(Robot::isRobotDone);
    }

    /**
     * Performs a sensor update by collecting signaling robots in the swarm
     * and updating each robot's input signaling robots.
     */
    public void sensorUpdate() {
        List<Robot> signalingRobots = swarm.stream()
                .filter(r -> r.getSignals().size() > 0)
                .collect(Collectors.toList());

        swarm.forEach(r -> {
            r.inputSignalingRobots(signalingRobots);
        });
    }

    /**
     * Updates the sensors of all robots in the swarm and performs a tick operation.
     *
     * @param millis   the number of milliseconds elapsed since the last tick
     * @param simTime  the current simulation time
     * @throws RuntimeException if a RobotExecutionException occurs during the tick operation
     */
    private void updateSensorsAndTick(long millis, long simTime) {
        sensorUpdate();

        swarm.forEach(r -> {
            try {
                r.tick(millis, simTime);
            } catch (RobotExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Performs a tick operation by updating sensors and executing instructions for all robots in the swarm.
     *
     * @param millis  the number of milliseconds elapsed since the last tick
     * @param simTime the current simulation time
     */
    public void tick(long millis, long simTime) {
        updateSensorsAndTick(millis, simTime);
    }
}
