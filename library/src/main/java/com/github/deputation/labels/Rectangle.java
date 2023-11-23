package com.github.deputation.labels;

import java.util.Random;

/**
 * Represents a rectangle shape.
 * Implements the Shape interface.
 */
public class Rectangle implements Shape {
    /**
     * The label associated with the object.
     */
    private final String label;

    /**
     * The X-coordinate of the center of the object.
     */
    private final double centerX;

    /**
     * The Y-coordinate of the center of the object.
     */
    private final double centerY;

    /**
     * The width of the object.
     */
    private final double width;

    /**
     * The height of the object.
     */
    private final double height;

    /**
     * Constructs a Rectangle object with the specified label, position, width, and height.
     *
     * @param label   The label of the rectangle.
     * @param centerX The x-coordinate of the center of the rectangle.
     * @param centerY The y-coordinate of the center of the rectangle.
     * @param width   The width of the rectangle.
     * @param height  The height of the rectangle.
     */
    public Rectangle(String label, double centerX, double centerY, double width, double height) {
        this.label = label;
        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;
    }

    /**
     * Retrieves the label of the rectangle.
     *
     * @return The label of the rectangle.
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Retrieves the shape type of the rectangle.
     *
     * @return The shape type of the rectangle (RECTANGLE).
     */
    @Override
    public ShapeType getShapeType() {
        return ShapeType.RECTANGLE;
    }

    /**
     * Retrieves the x-coordinate of the center of the rectangle.
     *
     * @return The x-coordinate of the center.
     */
    public double getCenterX() {
        return centerX;
    }

    /**
     * Retrieves the y-coordinate of the center of the rectangle.
     *
     * @return The y-coordinate of the center.
     */
    public double getCenterY() {
        return centerY;
    }

    /**
     * Retrieves the width of the rectangle.
     *
     * @return The width of the rectangle.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Retrieves the height of the rectangle.
     *
     * @return The height of the rectangle.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Checks if the specified coordinates are inside the rectangle.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return true if the coordinates are inside the rectangle, false otherwise.
     */
    public boolean isInside(double x, double y) {
        double xDist = Math.abs(x - this.centerX);
        double yDist = Math.abs(y - this.centerY);
        return (xDist <= width / 2) && (yDist <= height / 2);
    }

    /**
     * @return
     */
    @Override
    public double[] getCoordsInside() {
        Random random = new Random();

        double xInside = centerX - width / 2 + random.nextDouble() * width;
        double yInside = centerY - height / 2 + random.nextDouble() * height;

        return new double[]{xInside, yInside};
    }
}