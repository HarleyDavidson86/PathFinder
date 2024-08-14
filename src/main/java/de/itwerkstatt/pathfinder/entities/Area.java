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
    
    /**
     * Calculates the nearest point to the edge of the area
     * in direction to the destinationPoint
     * @param s
     * @param dest
     * @return 
     */
    public Point calculateDirectionalNearestPointToArea(Point s, Point dest) {
        //Find line which is being crossed by path from s to dest
        Line l = new Line(s, dest);
        for (int i = 0; i < points.length; i++) {
            Point p1 = points[i];
            Point p2 = points[0];
            if ((i+1) < points.length) {
                p2 = points[i+1];
            }
            Line tmpLine = new Line(p1,p2);
            System.out.println(l+" > "+tmpLine+" : "+l.doIntersect(tmpLine));
            //Lines intersect
            if (l.doIntersect(tmpLine)) {
                //Find shortest point on that edge of the area
                return tmpLine.getNearestPointToLine(s);
            }
        }
        return null;
    }
}
