package com.github.deputation.labels;

import java.util.Random;

/**
 * Represents a circle shape.
 * Implements the Shape interface.
 */
public class Circle implements Shape {
    /**
     * Label of the circle.
     */
    private final String label;
    /**
     * X coordinate on the X-axis of the circle's center.
     */
    private final double x;
    /**
     * Y coordinate on the Y-axis of the circle's center.
     */
    private final double y;
    /**
     * The circle's radius.
     */
    private final double r;

    /**
     * Constructs a Circle object with the specified label, center coordinates, and radius.
     *
     * @param label The label of the circle.
     * @param x     The x-coordinate of the center of the circle.
     * @param y     The y-coordinate of the center of the circle.
     * @param r     The radius of the circle.
     */
    public Circle(String label, double x, double y, double r) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    /**
     * Retrieves the label of the circle.
     *
     * @return The label of the circle.
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Retrieves the shape type of the circle.
     *
     * @return The shape type of the circle (CIRCLE).
     */
    @Override
    public ShapeType getShapeType() {
        return ShapeType.CIRCLE;
    }

    /**
     * Retrieves the x-coordinate of the center of the circle.
     *
     * @return The x-coordinate of the center.
     */
    public double getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of the center of the circle.
     *
     * @return The y-coordinate of the center.
     */
    public double getY() {
        return y;
    }

    /**
     * Retrieves the radius of the circle.
     *
     * @return The radius of the circle.
     */
    public double getR() {
        return r;
    }

    /**
     * Checks if the specified coordinates are inside the circle.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return true if the coordinates are inside the circle, false otherwise.
     */
    public boolean isInside(double x, double y) {
        double distance = Math.sqrt(Math.pow((x - this.x), 2) + Math.pow((y - this.y), 2));
        return distance <= r;
    }

    /**
     * @return A random pair of coordinates inside the Shape.
     */
    @Override
    public double[] getCoordsInside() {
        Random random = new Random();
        double angle = random.nextDouble() * 2 * Math.PI;
        double distance = random.nextDouble() * r;
        double xInside = x + distance * Math.cos(angle);
        double yInside = y + distance * Math.sin(angle);
        return new double[]{xInside, yInside};
    }
}
