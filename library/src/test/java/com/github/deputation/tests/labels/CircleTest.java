package com.github.deputation.tests.labels;

import com.github.deputation.labels.Circle;
import com.github.deputation.labels.ShapeType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CircleTest {
    private Circle circle;

    @BeforeEach
    public void setUp() {
        circle = new Circle("test", 0.0, 0.0, 1.0);
    }

    @Test
    public void testConstructor() {
        assertEquals("test", circle.getLabel());
        assertEquals(0.0, circle.getX());
        assertEquals(0.0, circle.getY());
        assertEquals(1.0, circle.getR());
        Assertions.assertEquals(ShapeType.CIRCLE, circle.getShapeType());
    }

    @Test
    public void testIsInside() {
        assertTrue(circle.isInside(0.0, 0.0));
        assertTrue(circle.isInside(0.5, 0.5));
        assertFalse(circle.isInside(1.0, 1.0));
    }

    @Test
    public void testGetCoordsInside() {
        double[] coords = circle.getCoordsInside();
        assertTrue(circle.isInside(coords[0], coords[1]));
    }
}