package com.github.deputation.tests;

import com.github.deputation.RobotExecutionException;
import com.github.deputation.RobotSpaceService;
import it.unicam.cs.followme.utilities.FollowMeParserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RobotSpaceServiceTest {
    private RobotSpaceService robotSpaceService;

    @BeforeEach
    public void setUp() {
        int robots = 5;
        robotSpaceService = new RobotSpaceService(robots);
    }

    @Test
    public void testCompileRobotProgram() throws IOException, FollowMeParserException {
        File sourceFile = new File("src/test/resources/robot_program.txt");
        robotSpaceService.compileRobotProgram(sourceFile);
    }

    @Test
    public void testCompileRobotProgramWithPath() throws IOException, FollowMeParserException {
        Path sourcePath = Paths.get("src/test/resources/robot_program.txt");
        robotSpaceService.compileRobotProgram(sourcePath);
    }

    @Test
    public void testCompileRobotProgramWithString() throws FollowMeParserException {
        String sourceCode = """
                        SIGNAL Z2
                        SIGNAL Z1
                        REPEAT 2
                        MOVE 1 1 3
                        DONE
                        UNSIGNAL Z1
                        MOVE 1 1 3
                        """;
        robotSpaceService.compileRobotProgram(sourceCode);
    }

    @Test
    public void testCompileEnvironment() throws IOException, FollowMeParserException {
        File sourceFile = new File("src/test/resources/environment.txt");
        robotSpaceService.compileEnvironment(sourceFile);
    }

    @Test
    public void testExecute() throws FollowMeParserException, RobotExecutionException {
        // Reducing clock time to 100 ms per instruction to speed tests up.
        long millis = 100L;
        robotSpaceService.compileRobotProgram("""
                        SIGNAL Z2
                        SIGNAL Z1
                        REPEAT 2
                        MOVE 1 1 3
                        DONE
                        UNSIGNAL Z1
                        MOVE 1 1 3
                        """);
        robotSpaceService.execute(millis, millis);
    }

    @Test
    public void compileError() throws FollowMeParserException {
        assertThrows(FollowMeParserException.class, () -> {
            robotSpaceService.compileRobotProgram("Exception test");
        });
    }
}