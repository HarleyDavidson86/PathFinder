package de.itwerkstatt.pathfinder.entities;

/**
 * An area defined by at least three points
 * @author dsust
 */
public record Area(Point... points) {

    /**
     * Constructor for a new Area Object.
     * @param points 
     * @throws IllegalArgumentException if less than three points were passed
     */
    public Area(Point... points) {
        if (points.length < 3) {
            throw new IllegalArgumentException("Area must be defined by at least three points.");
        }
        this.points = points;
    }
    
    /**
     * Calculates the nearest point to the edge of the area
     * @param p
     * @return 
     */
    public Point calculateNearestPointToArea(Point p) {
        //Find Point with minimal distance
        double minDistance = Double.MAX_VALUE;
        Line candidate1 = null;
        Line candidate2 = null;
        for (int i = 0; i < points.length; i++) {
            double distanceTo = points[i].distanceTo(p);
            if (distanceTo < minDistance) {
                //Set new minDistance
                minDistance = distanceTo;
                //Get Prev and Next Point of current
                Point prevPoint = (i == 0) ? points[points.length - 1] : points[i - 1];
                Point nextPoint = (i == points.length - 1) ? points[0] : points[i + 1];
                candidate1 = new Line(prevPoint, points[i]);
                candidate2 = new Line(nextPoint, points[i]);
            }
        }
        assert candidate1 != null && candidate2 != null;
        Point nearestPointCandid1 = candidate1.getNearestPointToLine(p);
        Point nearestPointCandid2 = candidate2.getNearestPointToLine(p);

        if (nearestPointCandid1.distanceTo(p) < nearestPointCandid2.distanceTo(p)) {
            return nearestPointCandid1;
        }
        return nearestPointCandid2;
    }
}
