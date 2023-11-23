package com.github.deputation.entities;

import com.github.deputation.labels.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 The Entity class represents an entity in a robotic system.
 It encapsulates the position, target position, heading, speed,
 and environmental data of the entity.
 */
public class Entity {
    /**
     * Positional information.
     */
    protected double x, y;
    /**
     * Target position towards which the entity is moving.
     */
    protected double targetX, targetY;
    /**
     * Entity's heading in degrees (0 through 360)
     */
    protected double heading;
    /**
     * Entity's speed.
     */
    protected double speed;
    /**
     * Entity's speed before they stopped last time.
     */
    protected double lastSpeed;
    /**
     * Environmental data regarding the shapes in the environment the entity is in.
     */
    private List<Shape> environmentalData;

    /**
     * Entity constructor, initializes most class fields to 0 or empty lists.
     */
    public Entity() {
        environmentalData = new ArrayList<>();
        x = 0;
        y = 0;
        targetX = 0;
        targetY = 0;
        heading = 0;
        speed = 0;
    }

    /**
     * Lets the entity simulate its own movement in the environment.
     *
     * @param millis How many milliseconds to simulate the environment for.
     */
    protected void tick(long millis) {
        double epsilon = 0.0001;

        if (Math.abs(x - targetX) < epsilon && Math.abs(y - targetY) < epsilon) {
            // stop
            if (speed != 0) {
                lastSpeed = speed;
            }

            speed = 0;
            return;
        }

        updateHeading();
        updatePosition(millis);
    }

    /**
     * Updates the entity's heading depending on its target position.
     */
    public void updateHeading() {
        double dx = targetX - x;
        double dy = targetY - y;
        heading = Math.toDegrees(Math.atan2(dy, dx));

        if (heading < 0) {
            heading += 360;
        }
    }

    /**
     * Gathers the entity's state.
     *
     * @return The entity's state as a string.
     */
    public String getEntityState() {
        StringBuilder stateBuilder = new StringBuilder();

        stateBuilder.append("Position: (").append(getX()).append(", ").append(getY()).append(")\n");
        stateBuilder.append("Target Position: (").append(getTargetX()).append(", ").append(getTargetY()).append(")\n");
        stateBuilder.append("Heading: ").append(getHeading()).append("\n");
        stateBuilder.append("Speed: ").append(getSpeed()).append("\n");

        return stateBuilder.toString();
    }

    /**
     * Updates the entity's position based on its speed.
     * Internally takes 20 small steps to ensure no overshooting and precise movements.
     *
     * @param millis How many milliseconds to simulate time for.
     */
    public void updatePosition(long millis) {
        double seconds = millis / 1000.0;
        var its = 20;

        var quantum = (seconds / its);
        for (int i = 1; i <= its; i++) {
            double distance = speed * quantum;

            double radianHeading = Math.toRadians(heading);
            double dx = distance * Math.cos(radianHeading);
            double dy = distance * Math.sin(radianHeading);

            updateCoordinates(dx, dy);

            if (speed == 0) {
                return;
            }
        }
    }

    /**
     * Safely updates coordinates (stops if the destination is reached) by a given dx and dy.
     *
     * @param dx How many meters per second to change the X axis coordinates by.
     * @param dy How many meters per second to change the Y axis coordinates by.
     */
    public void updateCoordinates(double dx, double dy) {
        var newX = x + dx;
        var newY = y + dy;

        safeUpdateCoords(newX, newY);
    }

    /**
     * Safely updates the entity's position keeping into account targetX and targetY.
     *
     * @param newX The newX to set.
     * @param newY The newY to set.
     */
    private void safeUpdateCoords(double newX, double newY) {
        var dist = calculateDistance(newX, newY, targetX, targetY);

        if (dist < 0.1) {
            handleAcceptablyClose();

            return;
        }

        x = newX;
        y = newY;
    }


    /**
     * Stops the entity, stores the last speed and wraps the position to targetX and targetY.
     */
    private void handleAcceptablyClose() {
        if (speed != 0) {
            lastSpeed = speed;
        }
        speed = 0;
        x = targetX;
        y = targetY;
    }

    /**
     * Calculates the distance between two points (x1, y1) and (x2, y2)
     * @param x1 X coords of the first point.
     * @param y1 Y coords of the first point.
     * @param x2 X coords of the second point.
     * @param y2 Y coords of the second point.
     * @return A double representing the distance in meters.
     */
    public double calculateDistance(double x1, double y1, double x2, double y2) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        return distance;
    }

    /**
     * Randomly places an entity in the space according to environmental data.
     *
     * @param random Random class used to generate random numbers.
     */
    private void placeEntityAccordingToEnvironmentalData(Random random) {
        int randomIndex = random.nextInt(environmentalData.size());
        Shape randomShape = environmentalData.get(randomIndex);
        double[] randomCoords = randomShape.getCoordsInside();

        this.x = randomCoords[0];
        this.y = randomCoords[1];
    }

    /**
     * Sets the environmental data for the Entity.
     *
     * @param environmentalData The list of Shape objects representing the environmental data.
     */
    public void setEnvironmentalData(List<Shape> environmentalData) {
        Random random = new Random();

        this.environmentalData = environmentalData;

        if (environmentalData != null && !environmentalData.isEmpty()) {
            placeEntityAccordingToEnvironmentalData(random);
            return;
        }

        x = random.nextFloat(-10, 10);
        y = random.nextFloat(-10, 10);
    }

    /**
     * Returns the x-coordinate of the Entity.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the Entity.
     *
     * @param x The new x-coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns the y-coordinate of the Entity.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the target y-coordinate of the Entity.
     *
     * @return The target y-coordinate.
     */
    public double getTargetY() {
        return targetY;
    }

    /**
     * Returns the target x-coordinate of the Entity.
     *
     * @return The target x-coordinate.
     */
    public double getTargetX() {
        return targetX;
    }

    /**
     * Returns the heading of the Entity.
     *
     * @return The heading.
     */
    public double getHeading() {
        return heading;
    }

    /**
     * Returns the speed of the Entity.
     *
     * @return The speed.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the y-coordinate of the Entity.
     *
     * @param y The new y-coordinate.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns an optional environmental label based on the Entity's position.
     *
     * @return An Optional containing the environmental label if found, or an empty Optional if not found.
     */
    public Optional<String> getEnvironmentalLabel() {
        for (var s : environmentalData) {
            if (s.isInside(x, y)) {
                return Optional.of(s.getLabel());
            }
        }

        return Optional.empty();
    }
}
