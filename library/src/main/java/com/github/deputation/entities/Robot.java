package com.github.deputation.entities;

import com.github.deputation.RobotExecutionException;
import com.github.deputation.instructions.*;
import com.github.deputation.language.LoopBodyStack;
import com.github.deputation.language.RobotContext;

import java.util.*;
import java.util.stream.Collectors;

public class Robot extends Entity implements RobotContext {

    /**
     * Represents a set of signals associated with the robot.
     */
    private Set<String> signals;

    /**
     * Represents a list of robots that are signaling to the current robot.
     */
    private List<Robot> signalingRobots;

    /**
     * Represents the program instructions for the robot.
     */
    private List<RobotInstruction> program;

    /**
     * Represents a stack for tracking loop bodies during program execution.
     */
    private final LoopBodyStack bodyTracker;

    /**
     * Indicates whether the end instruction has been executed for the robot.
     */
    private boolean endInstructionExecuted;

    /**
     * Represents the duration in milliseconds for continuing the execution of the robot.
     */
    private long continuingMillis;
    /**
     * An optional used to track the lastMove, so that the robot may keep moving and moves can be implemented
     * in both a continuous and blocking manner.
     * */
    private Optional<MoveInstruction> lastMove;
    /**
     * Constructs a Robot object.
     * */
    public Robot() {
        super();

        signals = new HashSet<>();
        signalingRobots = new ArrayList<>();
        program = new ArrayList<>();
        endInstructionExecuted = false;
        bodyTracker = new LoopBodyStack();
        lastMove = Optional.empty();
    }

    /**
     * This function lets you inform a robot of which other robots are signaling something.
     *
     * @param robots A list of robots with signals.
     */
    public void inputSignalingRobots(List<Robot> robots) {
        robots = robots.stream().filter(robot -> !robot.equals(this)).collect(Collectors.toList());

        signalingRobots.clear();
        signalingRobots.addAll(robots);
    }

    /**
     * This function returns all signals the robot is currently signaling.
     *
     * @return A set of strings representing the signals.
     */
    public Set<String> getSignals() {
        return signals;
    }

    /**
     * This function obtains a list of all the signaling robots the robot currently holds.
     *
     * @return A list of Robots that are signaling to the current robot.
     */
    public List<Robot> getSignalingRobots() {
        return signalingRobots;
    }

    /**
     * This function programs the Robot's processor and sets up the robot
     * state to ready-it-up for program execution.
     *
     * @param instructions A list of instructions representing the Robot's program.
     */
    public void program(List<RobotInstruction> instructions) {
        endInstructionExecuted = false;
        program.clear();
        lastMove = Optional.empty();

        program.addAll(instructions);
        bodyTracker.startBody(instructions, 0, new EndInstruction());
    }

    /**
     * This function checks whether the Robot is done executing the current program.
     *
     * @return True if it's done, false if it's not.
     */
    public boolean isRobotDone() {
        return endInstructionExecuted;
    }

    /**
     * Runs a tick of the processor and entity update tick.
     *
     * @param millis The amount of time that the processor should take to execute an instruction, used for the CONTINUE instruction.
     * @param simTime The amount of time that the physical simulation should advance by.
     * @throws RobotExecutionException in case of invalid instructions or instruction parameters.
     */
    public void tick(long millis, long simTime) throws RobotExecutionException {
        super.tick(simTime);
        processorTick(millis);
    }

    /**
     * Updates the continuing state of the robot, decreasing the amount of time that it still has to continue for.
     *
     * @param millis The amount of milliseconds to subtract from the continuingMillis internal field.
     */
    private void updateContinuingState(long millis) {
        continuingMillis -= millis;
    }

    /**
     * Checks whether Robot state updates are pending, and if so, executes them.
     *
     * @param millis The amount of milliseconds it takes to execute an instruction.
     * @return True if updates were pending and got executed, false if not.
     */
    private boolean areStateUpdatesPending(long millis) {
        if (endInstructionExecuted) {
            return true;
        }

        if (continuingMillis != 0) {
            updateContinuingState(millis);
            return true;
        }

        return false;
    }

    /**
     * Executes the last move if present.
     */
    private void executeLastMove() {
        if (!lastMove.isEmpty()) {
            lastMove.get().execute(this);
        }
    }

    /**
     * Executes a single processor tick.
     *
     * @param millis the amount of time that passes in processor time for every instruction
     * @throws RobotExecutionException if an error occurs during execution
     */
    private void processorTick(long millis) throws RobotExecutionException {
        if (areStateUpdatesPending(millis)) {
            // Keep it moving.
            executeLastMove();
            return;
        }

        var currentBody = bodyTracker.peekBody();
        var currentInstructionPointer = bodyTracker.peekInstructionPointer();

        if (currentBody.size() - 1 < currentInstructionPointer) {
            handleLoopEndLogic();
            return;
        }

        executeInstruction(currentInstructionPointer);
    }

    /**
     * Retrieves the state of the robot as a formatted string.
     *
     * @return the robot state information
     */
    public String getState() {
        StringBuilder stateBuilder = new StringBuilder();

        stateBuilder.append("Robot State for Robot ID: ").append(this).append("\n");
        stateBuilder.append(getEntityState());
        stateBuilder.append("Signals: ").append(getSignals()).append("\n");
        stateBuilder.append("Signaling Robots: ").append(getSignalingRobots()).append("\n");
        stateBuilder.append("Continuing: ").append(continuingMillis != 0 ? "Yes" : "No").append("\n");
        stateBuilder.append("Continuing for: ").append(continuingMillis).append("ms\n");

        stateBuilder.append("-----\n");

        return stateBuilder.toString();
    }

    /**
     * Executes the instruction at the given instruction pointer.
     *
     * Also makes sure the move state is preserved and executed properly.
     *
     * @param currentInstructionPointer the current instruction pointer in the relevant loop body
     * @throws RobotExecutionException if an error occurs during execution
     */
    private void executeInstruction(int currentInstructionPointer) throws RobotExecutionException {
        var currentInstruction = bodyTracker.peekBody().get(currentInstructionPointer);

        if (!Objects.equals(currentInstruction.getInstructionType(), "MOVE")) {
            executeLastMove();
        }

        executeAndIncreaseIp(currentInstruction);
    }

    /**
     * Handles the logic when reaching the end of a loop.
     *
     * @throws RobotExecutionException if an error occurs during execution
     */
    private void handleLoopEndLogic() throws RobotExecutionException {
        var loopInstruction = bodyTracker.peekLoopInstruction();
        bodyTracker.endBody();

        switch (loopInstruction.getInstructionType()) {
            case "REPEAT" -> {
                return;
            }
            case "END" -> {
                loopInstruction.execute(this);
                return;
            }
        }

        executeAndIncreaseIp(loopInstruction);
    }

    /**
     * Executes the given instruction and increases the instruction pointer.
     *
     * @param instruction the instruction to execute
     * @throws RobotExecutionException if an error occurs during execution
     */
    private void executeAndIncreaseIp(RobotInstruction instruction) throws RobotExecutionException {
        instruction.execute(this);
        bodyTracker.increaseIntegerValue();
    }

    /**
     * Updates the last move state for the Robot, so that it may be remembered in the following processor tick.
     *
     * @param coordinates Coordinates of the last move.
     * @param speed Speed of the last move.
     */
    private void updateLastMoveState(double[] coordinates, double speed) {
        lastMove = Optional.of(new MoveInstruction(coordinates, speed));
    }

    /**
     * Moves the robot to the specified relative coordinates with the given speed.
     *
     * @param coordinates The relative coordinates to move the robot to.
     * @param speed       The speed at which the robot should move.
     */
    @Override
    public void Move(double[] coordinates, double speed) {
        targetX = getX() + coordinates[0];
        targetY = getY() + coordinates[1];
        this.speed = speed;

        updateLastMoveState(coordinates, speed);
    }

    /**
     * Sends a signal with the specified label.
     *
     * @param label The label of the signal to send.
     */
    @Override
    public void Signal(String label) {
        signals.add(label);
    }

    /**
     * Unsignals the signal with the specified label.
     *
     * @param label The label of the signal to unsignal.
     */
    @Override
    public void Unsignal(String label) throws RobotExecutionException {
        if (!signals.contains(label)) {
            throw new RobotExecutionException("Tried unsignaling a label that's not there!");
        }
        
        signals.remove(label);
    }

    /**
     * Stops the robot.
     */
    @Override
    public void Stop() {
        lastSpeed = speed;
        lastMove = Optional.empty();
        speed = 0;
    }

    /**
     * Continues the robot's movement for the specified duration in seconds.
     *
     * @param seconds the duration in seconds to continue the movement
     */
    @Override
    public void ContinueFor(int seconds) {
        continuingMillis = seconds * 1000L;
    }

    /**
     * Follows the specified label and parameters to determine the robot's movement.
     *
     * @param label      the label to follow
     * @param parameters the parameters for following
     * @throws RobotExecutionException if an error occurs during execution
     */
    @Override
    public void Follow(String label, double[] parameters) throws RobotExecutionException {
        List<Robot> validRobots = getValidRobots(label, parameters[0]);

        if (!validRobots.isEmpty()) {
            double[] direction = calculateDirection(validRobots);
            double[] targetCoordinates = calculateTargetCoordinates(direction, parameters[0]);
            Move(targetCoordinates, parameters[1]);

            return;
        }

        moveRandomlyWithinRange(parameters[0], parameters[1]);
    }

    /**
     * Calculates the distance between this robot and the specified robot.
     *
     * @param robot the robot to calculate the distance to
     * @return the distance between the two robots
     */
    private double calculateDistance(Robot robot) {
        double deltaX = robot.getX() - getX();
        double deltaY = robot.getY() - getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Retrieves a list of valid robots based on the specified label and distance criteria.
     *
     * @param label    the label to match
     * @param distance the maximum distance to consider
     * @return a list of valid robots that satisfy the criteria
     */
    private List<Robot> getValidRobots(String label, double distance) {
        return signalingRobots.stream()
                .filter(robot -> robot.getSignals().contains(label))
                .filter(robot -> calculateDistance(robot) <= distance)
                .toList();
    }

    /**
     * Calculates the direction based on the list of valid robots.
     *
     * @param validRobots the list of valid robots to calculate the direction from
     * @return an array representing the direction in the X and Y axes
     * @throws RobotExecutionException if an error occurs during execution
     */
    private double[] calculateDirection(List<Robot> validRobots) throws RobotExecutionException {
        try {
            double avgX = calculateAverageX(validRobots);
            double avgY = calculateAverageY(validRobots);
            double directionX = calculateDirectionX(avgX);
            double directionY = calculateDirectionY(avgY);
            return normalizeDirection(directionX, directionY);
        } catch (NoSuchElementException ex) {
            throw new RobotExecutionException("Direction normalization error.");
        }
    }

    /**
     * Calculates the average X-coordinate value among the list of valid robots.
     *
     * @param validRobots the list of valid robots to calculate the average X-coordinate from
     * @return the average X-coordinate value
     * @throws NoSuchElementException if the list of valid robots is empty
     */
    private double calculateAverageX(List<Robot> validRobots) {
        return validRobots.stream()
                .mapToDouble(Robot::getX)
                .average()
                .orElseThrow();
    }

    /**
     * Calculates the average Y-coordinate value among the list of valid robots.
     *
     * @param validRobots the list of valid robots to calculate the average Y-coordinate from
     * @return the average Y-coordinate value
     * @throws NoSuchElementException if the list of valid robots is empty
     */
    private double calculateAverageY(List<Robot> validRobots) {
        return validRobots.stream()
                .mapToDouble(Robot::getY)
                .average()
                .orElseThrow();
    }

    /**
     * Calculates the direction in the X-axis based on the average X-coordinate value.
     *
     * @param averageX the average X-coordinate value
     * @return the direction in the X-axis
     */
    private double calculateDirectionX(double averageX) {
        return averageX - getX();
    }

    /**
     * Calculates the direction in the Y-axis based on the average Y-coordinate value.
     *
     * @param averageY the average Y-coordinate value
     * @return the direction in the Y-axis
     */
    private double calculateDirectionY(double averageY) {
        return averageY - getY();
    }

    /**
     * Normalizes the direction represented by the given X and Y components.
     *
     * @param directionX the X component of the direction
     * @param directionY the Y component of the direction
     * @return an array representing the normalized direction in the X and Y axes
     */
    private double[] normalizeDirection(double directionX, double directionY) {
        double norm = Math.sqrt(directionX * directionX + directionY * directionY);

        if (norm != 0) {
            directionX /= norm;
            directionY /= norm;
        }

        return new double[]{directionX, directionY};
    }

    /**
     * Calculates the target coordinates based on the given direction and distance.
     *
     * @param direction the direction in the X and Y axes
     * @param distance  the distance from the current position to the target position
     * @return an array representing the target coordinates in the X and Y axes
     */
    private double[] calculateTargetCoordinates(double[] direction, double distance) {
        double targetX = getX() + distance * direction[0];
        double targetY = getY() + distance * direction[1];
        return new double[]{targetX, targetY};
    }

    /**
     * Moves the robot randomly within the specified range at the given speed.
     *
     * @param range the range within which the robot can move randomly
     * @param speed the speed at which the robot should move
     */
    private void moveRandomlyWithinRange(double range, double speed) {
        double randomX = getX() + (Math.random() * 2 - 1) * range;
        double randomY = getY() + (Math.random() * 2 - 1) * range;
        Move(new double[]{randomX, randomY}, speed);
    }

    /**
     * Repeats the specified instructions for the given number of times.
     *
     * @param times       the number of times to repeat the instructions
     * @param instructions the list of instructions to repeat
     */
    @Override
    public void Repeat(int times, List<RobotInstruction> instructions) {
        bodyTracker.increaseIntegerValue();
        List<RobotInstruction> buffer = new ArrayList<>();

        for (int i = 0; i < times; i++) {
            buffer.addAll(instructions);
        }

        bodyTracker.startBody(buffer, -1, new RepeatInstruction(times, instructions));
    }

    /**
     * Checks if the environmental label matches the specified label.
     *
     * @param label the label to check against the environmental label
     * @return true if the environmental label matches the specified label, false otherwise
     */
    private boolean checkUntilCondition(String label) {
        Optional<String> environmentalLabel = getEnvironmentalLabel();
        return environmentalLabel.isPresent() && environmentalLabel.get().equals(label);
    }

    /**
     * Executes the specified instructions until the condition, indicated by the label, is met.
     *
     * @param label       the label indicating the condition to be met
     * @param instructions the list of instructions to execute
     */
    @Override
    public void Until(String label, List<RobotInstruction> instructions) {
        bodyTracker.increaseIntegerValue();

        boolean conditionMet = checkUntilCondition(label); // Implement your condition check logic.
        if (!conditionMet) {
            bodyTracker.startBody(instructions, -1, new UntilInstruction(label, instructions));
        }
    }

    /**
     * Executes the specified instructions indefinitely in a loop.
     *
     * @param instructions the list of instructions to execute
     */
    @Override
    public void DoForever(List<RobotInstruction> instructions) {
        bodyTracker.startBody(instructions, -1, new DoForeverInstruction(instructions));
    }

    /**
     * Terminates the execution of the robot's instructions.
     * This method sets the flag to indicate that the end instruction has been executed.
     */
    @Override
    public void Terminate() {
        endInstructionExecuted = true;
    }
}