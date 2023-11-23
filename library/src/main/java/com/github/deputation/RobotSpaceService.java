package com.github.deputation;

import com.github.deputation.entities.Robot;
import com.github.deputation.entities.RobotController;
import com.github.deputation.language.RobotProgram;
import it.unicam.cs.followme.utilities.FollowMeParser;
import it.unicam.cs.followme.utilities.FollowMeParserException;
import it.unicam.cs.followme.utilities.ShapeData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class RobotSpaceService {
    /**
     * Robot program.
     */
    private final RobotProgram robotProgram;
    /**
     * Parser.
     */
    private final FollowMeParser followMeParser;
    /**
     * Robot controller.
     */
    private RobotController robotController;
    /**
     * Initializes a new instance of the RobotSpaceService class.
     * @param robots the number of robots to be controlled by the service.
     */
    public RobotSpaceService(int robots) {
        robotProgram = new RobotProgram();
        followMeParser = new FollowMeParser(robotProgram);
        robotController = new RobotController(robots);
    }

    /**
     * Compiles a robot program from a source file.
     * @param sourceFile the file to be parsed into a robot program.
     */
    public void compileRobotProgram(File sourceFile) throws IOException, FollowMeParserException {
        followMeParser.parseRobotProgram(sourceFile);
    }

    /**
     * Compiles a robot program from a path.
     * @param path the path to be parsed into a robot program.
     */
    public void compileRobotProgram(Path path) throws IOException, FollowMeParserException {
        followMeParser.parseRobotProgram(path);
    }

    /**
     * Compiles a robot program from a string of code.
     * @param code the string to be parsed into a robot program.
     */
    public void compileRobotProgram(String code) throws FollowMeParserException {
        followMeParser.parseRobotProgram(code);
    }

    /**
     * Parses environment data from a file.
     * @param file the file to be parsed into environment data.
     */
    private List<ShapeData> parseEnvironment(File file) throws IOException, FollowMeParserException {
        return followMeParser.parseEnvironment(file);
    }

    /**
     * Parses environment data from a string.
     * @param data the string to be parsed into environment data.
     */
    private List<ShapeData> parseEnvironment(String data) throws FollowMeParserException {
        return followMeParser.parseEnvironment(data);
    }

    /**
     * Parses environment data from a path.
     * @param path the path to be parsed into environment data.
     */
    private List<ShapeData> parseEnvironment(Path path) throws IOException, FollowMeParserException {
        return followMeParser.parseEnvironment(path);
    }

    /**
     * Compiles environment data from a file and loads it into the robot program.
     * @param file the file to be parsed into environment data.
     */
    public void compileEnvironment(File file) throws IOException, FollowMeParserException {
        robotProgram.loadShapeData(parseEnvironment(file));
    }

    /**
     * Compiles environment data from a string and loads it into the robot program.
     * @param data the string to be parsed into environment data.
     */
    public void compileEnvironment(String data) throws FollowMeParserException {
        robotProgram.loadShapeData(parseEnvironment(data));
    }

    /**
     * Compiles environment data from a path and loads it into the robot program.
     * @param path the path to be parsed into environment data.
     */
    public void compileEnvironment(Path path) throws IOException, FollowMeParserException {
        robotProgram.loadShapeData(parseEnvironment(path));
    }

    /**
     * Makes the robots aware of the environmental data by setting it in each robot in the swarm.
     */
    private void makeRobotsEnvironmentallyAware() {
        robotController.getSwarm().forEach(r -> r.setEnvironmentalData(robotProgram.getEnvironmentalData()));
    }

    /**
     * Retrieves the swarm of robots controlled by this service.
     * @return a list of robots in the swarm.
     */
    public List<Robot> getSwarm() {
        return robotController.getSwarm();
    }

    /**
     * Programs the swarm of robots with the compiled robot program.
     */
    private void programTheSwarm() throws RobotExecutionException {
        robotController.programSwarm(robotProgram.getCompiledProgram());
    }

    /**
     * Puts the thread to sleep for the time it takes to execute an instruction.
     *
     * This is purely artificial for the purposes of adhering to the spec, it is very much not needed.
     * */
    private void waitInstructionTime(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Runs a simulation tick, making robots aware of the environment, programming them and checking
     * if their tasks are done. Optionally runs a provided action.
     * @param millis the real time in milliseconds for the simulation tick.
     * @param simTime the simulated time for the simulation tick.
     * @param action an optional action to be run after each tick.
     */
    private void controllerTick(long millis, long simTime, Runnable action) throws RobotExecutionException {
        makeRobotsEnvironmentallyAware();
        programTheSwarm();

        while (!robotController.isSwarmDone()) {
            robotController.tick(millis, simTime);

            if (action != null) {
                action.run();
            }

            waitInstructionTime(millis);
        }
    }

    /**
     * Executes the compiled robot program with a specified simulation tick rate.
     * @param millis the real time in milliseconds for the processor tick.
     * @param simTime the simulated time for the simulation tick.
     */
    public void execute(long millis, long simTime) throws RobotExecutionException {
        controllerTick(millis, simTime, null);
    }

    /**
     * Executes the compiled robot program with a specified simulation tick rate and a custom action
     * to be executed after each tick.
     * @param millis the real time in milliseconds for the processor tick.
     * @param simTime the simulated time for the simulation tick.
     * @param action an action to be run after each tick.
     */
    public void execute(long millis, long simTime, Runnable action) throws RobotExecutionException {
        controllerTick(millis, simTime, action);
    }
}
