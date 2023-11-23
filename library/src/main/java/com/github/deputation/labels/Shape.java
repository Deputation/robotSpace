package com.github.deputation.labels;

/**
 * Represents a shape with a label and a shape type.
 */
public interface Shape {
    /**
     * Retrieves the label of the shape.
     *
     * @return The label of the shape.
     */
    String getLabel();

    /**
     * Retrieves the shape type of the shape.
     *
     * @return The shape type of the shape.
     */
    ShapeType getShapeType();

    /**
     * Checks if the given coordinates (x, y) are inside the object.
     *
     * @param x the X-coordinate to check
     * @param y the Y-coordinate to check
     * @return true if the coordinates are inside the object, false otherwise
     */
    boolean isInside(double x, double y);

    /**
     * Retrieves the coordinates inside the object as an array.
     *
     * @return an array representing the coordinates inside the object
     */
    double[] getCoordsInside();
}
