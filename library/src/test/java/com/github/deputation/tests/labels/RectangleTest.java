package com.github.deputation.tests.labels;

import com.github.deputation.labels.Rectangle;
import com.github.deputation.labels.ShapeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RectangleTest {
    private Rectangle rectangle;

    @BeforeEach
    public void setUp() {
        rectangle = new Rectangle("test", 0.0, 0.0, 2.0, 2.0);
    }

    @Test
    public void testConstructor() {
        assertEquals("test", rectangle.getLabel());
        assertEquals(0.0, rectangle.getCenterX());
        assertEquals(0.0, rectangle.getCenterY());
        assertEquals(2.0, rectangle.getWidth());
        assertEquals(2.0, rectangle.getHeight());
        assertEquals(ShapeType.RECTANGLE, rectangle.getShapeType());
    }

    @Test
    public void testIsInside() {
        assertTrue(rectangle.isInside(0.0, 0.0));
        assertTrue(rectangle.isInside(0.5, 0.5));
        assertFalse(rectangle.isInside(1.5, 1.5));
    }

    @Test
    public void testGetCoordsInside() {
        double[] coords = rectangle.getCoordsInside();
        assertTrue(rectangle.isInside(coords[0], coords[1]));
    }
}
