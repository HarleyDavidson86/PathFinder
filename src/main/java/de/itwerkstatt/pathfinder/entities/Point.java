package de.itwerkstatt.pathfinder.entities;

/**
 * A single location defined by a x- and y-coordinate
 * @author dsust
 */
public record Point(double x, double y) {
    
    /**
     * Calculates the distance (Euclidean distance) 
     * using the Pythagorean theorem.
     *
     * @param p
     * @return The distance between the two points
     */
    double distanceTo(Point p) {
        return Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2));
    }
}
