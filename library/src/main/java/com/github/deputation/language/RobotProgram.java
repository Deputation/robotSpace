package com.github.deputation.language;

import com.github.deputation.instructions.*;
import com.github.deputation.labels.Circle;
import com.github.deputation.labels.Rectangle;
import com.github.deputation.labels.Shape;
import com.github.deputation.labels.ShapeType;
import it.unicam.cs.followme.utilities.FollowMeParserHandler;
import it.unicam.cs.followme.utilities.ShapeData;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent the Robot space's program which is a sequence of instructions for the robot + shapes representing regions.
 *
 * This class's data will be fed into the RobotContext which will then take over Robots and control them in the Space.
 */
public class RobotProgram implements FollowMeParserHandler {
    /**
     * List to hold all the instructions for the robot program.
     */
    private final List<RobotInstruction> programInstructions;

    /**
     * List to hold all the shapes involved in the robot program.
     */
    private final List<Shape> programShapes;

    /**
     * The ParsingBodyStack object used to track the bodies of loops during parsing.
     */
    private final ParsingBodyStack parsingBodyStack;

    /**
     * Constructor for RobotProgram.
     * Initializes the program instructions and shapes list.
     * Initializes the body tracker to start tracking the main body.
     */
    public RobotProgram() {
        programInstructions = new ArrayList<>();
        programShapes = new ArrayList<>();
        parsingBodyStack = new ParsingBodyStack();
    }

    /**
     * Loads shape data into the program by adding shapes to the list of program shapes.
     * Each shape data object in the given list is processed and converted to the corresponding shape type.
     *
     * @param data The list of shape data objects to be loaded.
     * @throws IllegalArgumentException If the shape type is unsupported.
     */
    public void loadShapeData(List<ShapeData> data) {
        programShapes.clear();

        for (ShapeData shapeData : data) {
            ShapeType type = ShapeType.valueOf(shapeData.shape().toUpperCase());
            switch (type) {
                case CIRCLE -> addCircle(shapeData);
                case RECTANGLE -> addRectangle(shapeData);
                default -> throw new IllegalArgumentException("Unsupported shape type: " + type);
            }
        }
    }

    /**
     * Adds a circle shape to the list of program shapes based on the provided shape data.
     * The shape data must have the correct number of arguments (3) for a circle.
     *
     * @param shapeData The shape data object containing the necessary information for the circle shape.
     * @throws IllegalArgumentException If the shape data has an incorrect number of arguments for a circle.
     */
    private void addCircle(ShapeData shapeData) {
        double[] args = shapeData.args();
        if (args.length == 3) {
            programShapes.add(new Circle(shapeData.label(), args[0], args[1], args[2]));
        } else {
            throw new IllegalArgumentException("Incorrect argument count for CIRCLE.");
        }
    }

    /**
     * Adds a rectangle shape to the list of program shapes based on the provided shape data.
     * The shape data must have the correct number of arguments (4) for a rectangle.
     *
     * @param shapeData The shape data object containing the necessary information for the rectangle shape.
     * @throws IllegalArgumentException If the shape data has an incorrect number of arguments for a rectangle.
     */
    private void addRectangle(ShapeData shapeData) {
        double[] args = shapeData.args();
        if (args.length == 4) {
            programShapes.add(new Rectangle(shapeData.label(), args[0], args[1], args[2], args[3]));
        } else {
            throw new IllegalArgumentException("Incorrect argument count for RECTANGLE.");
        }
    }

    /**
     * This method is the method that is invoked ad the beginning of the parse procedure.
     *
     * It sets up the parsingBodyStack and the programInstructions.
     */
    @Override
    public void parsingStarted() {
        parsingBodyStack.clear();
        programInstructions.clear();

        parsingBodyStack.startBody(programInstructions);
    }

    /**
     * This method is the method that is invoked ad the end of the parse procedure.
     *
     * It ends the last body of the parsingBodyStack and makes sure there are no orphaned bodies.
     */
    @Override
    public void parsingDone() {
        parsingBodyStack.endBody();

        if (parsingBodyStack.isBodyPresent()) {
            throw new IllegalStateException("Body tracker detected orphaned body.");
        }
    }

    /**
     * Retrieves the current instruction stream from the parsing body stack without removing it.
     *
     * @return the current instruction stream
     */
    private List<RobotInstruction> getCurrentInstructionStream() {
        return parsingBodyStack.peekBody();
    }

    /**
     * Verifies the arguments for the MOVE command.
     *
     * @param args the array of arguments for the MOVE command
     * @throws IllegalArgumentException if the arguments are invalid
     */
    private void verifyMoveCommandArguments(double[] args) {
        double x = args[0];
        double y = args[1];
        double speed = args[2];
        if (x < -1 || x > 1 || y < -1 || y > 1 || speed <= 0) {
            throw new IllegalArgumentException("Invalid MOVE command arguments.");
        }
    }

    /**
     * Verifies the arguments for the MOVE RANDOM command.
     *
     * @param args the array of arguments for the MOVE RANDOM command
     * @throws IllegalArgumentException if the arguments are invalid
     */
    private void verifyMoveRandomCommandArguments(double[] args) {
        double x1 = args[0];
        double x2 = args[1];
        double y1 = args[2];
        double y2 = args[3];
        double speed = args[4];
        if (x1 > x2 || y1 > y2 || speed <= 0) {
            throw new IllegalArgumentException("Invalid MOVE RANDOM command arguments.");
        }
    }

    /**
     * Method invoked when a command "MOVE" is parsed.
     *
     * @param args command arguments.
     */
    @Override
    public void moveCommand(double[] args) {
        verifyMoveCommandArguments(args);

        getCurrentInstructionStream().add(new MoveInstruction(args));
    }

    /**
     * Method invoked when a command "MOVE RANDOM" is parsed.
     *
     * @param args command arguments.
     */
    @Override
    public void moveRandomCommand(double[] args) {
        verifyMoveRandomCommandArguments(args);

        getCurrentInstructionStream().add(new MoveRandomInstruction(args));
    }

    /**
     * Method invoked when a command "SIGNAL" is parsed.
     *
     * @param label label to signal
     */
    @Override
    public void signalCommand(String label) {
        getCurrentInstructionStream().add(new SignalInstruction(label));
    }

    /**
     * Method invoked when a command "UNSIGNAL" is parsed.
     *
     * @param label label to unsignal
     */
    @Override
    public void unsignalCommand(String label) {
        getCurrentInstructionStream().add(new UnsignalInstruction(label));
    }

    /**
     * Method invoked when a command "FOLLOW" is parsed.
     *
     * @param label label to follow
     * @param args  command arguments
     */
    @Override
    public void followCommand(String label, double[] args) {
        getCurrentInstructionStream().add(new FollowInstruction(label, args));
    }

    /**
     * Method invoked when a command "STOP" is parsed.
     */
    @Override
    public void stopCommand() {
        getCurrentInstructionStream().add(new StopInstruction());
    }

    /**
     * Method invoked when a command "WAIT" is parsed.
     *
     * @param s number of seconds;
     */
    @Override
    public void continueCommand(int s) {
        getCurrentInstructionStream().add(new ContinueInstruction(s));
    }

    /**
     * Sets up the loop state with the given loop instruction and instruction stream.
     *
     * @param loopInstruction The RobotInstruction representing the loop instruction.
     * @param list The list of RobotInstruction objects representing the loop body.
     * @throws IllegalStateException If an error occurs while setting up the loop state.
     */
    private void trackLoopState(RobotInstruction loopInstruction, List<RobotInstruction> list) {
        getCurrentInstructionStream().add(loopInstruction);
        parsingBodyStack.startBody(list);
    }

    /**
     * Method invoked when a command "REPEAT" is parsed.
     *
     * @param n number of iterations.
     */
    @Override
    public void repeatCommandStart(int n) {
        List<RobotInstruction> list = new ArrayList<>();
        var object = new RepeatInstruction(n, list);
        trackLoopState(object, list);
    }

    /**
     * Method invoked when a command "UNTIL" is parsed.
     *
     * @param label name of a label
     */
    @Override
    public void untilCommandStart(String label) {
        List<RobotInstruction> list = new ArrayList<>();
        var object = new UntilInstruction(label, list);
        trackLoopState(object, list);
    }

    /**
     * Method invoked when a command "DO FOREVER" is parsed.
     */
    @Override
    public void doForeverStart() {
        List<RobotInstruction> list = new ArrayList<>();
        var object = new DoForeverInstruction(list);
        trackLoopState(object, list);
    }

    /**
     * Method invoked when a command "DONE" is parsed.
     */
    @Override
    public void doneCommand() {
        parsingBodyStack.endBody();
    }

    /**
     * Retrieves a copy of the compiled program instructions.
     *
     * @return a copy of the compiled program instructions
     */
    public List<RobotInstruction> getCompiledProgram() {
        return new ArrayList<>(programInstructions);
    }

    /**
     * Retrieves a copy of the environmental data shapes.
     *
     * @return a copy of the environmental data shapes
     */
    public List<Shape> getEnvironmentalData() {
        return new ArrayList<>(programShapes);
    }
}
