package de.itwerkstatt.pathfinder.entities;

/**
 * A line segment defined by two points
 *
 * @author dsust
 */
public record Line(Point p1, Point p2) {

    /**
     * Checks if the given point p is between the linesegment by checking the
     * coordinate-values
     *
     * @param p
     * @return true, if p is between p1 and p2
     */
    boolean isPointBetweenSegmentpoints(Point p) {
        double maxY = p1.y() > p2.y() ? p1.y() : p2.y();
        double minY = p1.y() < p2.y() ? p1.y() : p2.y();
        double maxX = p1.x() > p2.x() ? p1.x() : p2.x();
        double minX = p1.x() < p2.x() ? p1.x() : p2.x();
        return p.y() >= minY && p.y() <= maxY && p.x() >= minX && p.x() <= maxX;
    }

    /**
     * Returns the point with the smallest distance between the given point and
     * the line segment.
     *
     * @param p Starting point
     * @return Point
     */
    public Point getNearestPointToLine(Point p) {
        System.out.println("Starting point: " + p + ", Line: " + this);
        Point intersectionPoint;
        boolean isHorizontal = p1.y() == p2.y();
        boolean isVertical = p1.x() == p2.x();
        if (isHorizontal) {
            System.out.println("Line is horizontal.");
            intersectionPoint = new Point(p.x(), p1.y());
        } else if (isVertical) {
            System.out.println("Line is vertical.");
            intersectionPoint = new Point(p1.x(), p.y());
        } else {
            //TODO We have to test this
            // Calculate linear equation y = mx + c
            // Gradient 
            double m = (p2.y() - p1.y()) / (p2.x() - p1.x());
            System.out.println("Gradient m : " + m);
            // Offset c = y - mx
            double c = p1.y() - (m * p1.x());
            System.out.println("Offset c : " + c);
            System.out.println("Linear equation y = " + m + "x + " + c);

            //Normal through point P
            //Gradient of normal
            double m_normal = -1.0d / m;

            System.out.println("Gradient normal : " + m_normal);
            double c_normal = p.y() - (m_normal * p.x());
            System.out.println("Offset normal : " + c_normal);
            System.out.println("Linear equation normal y = " + m_normal + "x + " + c_normal);
            // m x + c = m_normal x + c_normal
            // x = (c_normal - c) / (m - m_normal)
            double x_new = (c_normal - c) / (m - m_normal);
            double y_new = m_normal * x_new + c_normal;
            intersectionPoint = new Point(x_new, y_new);
        }

        System.out.println("Intersection point : " + intersectionPoint);
        if (isPointBetweenSegmentpoints(intersectionPoint)) {
            return intersectionPoint;
        }
        // Point is not within the segment. 
        // Return the point of the line with the 
        // smallest distance to the starting point
        double distanceToP1 = p.distanceTo(p1);
        if (p.distanceTo(p2) < distanceToP1) {
            return p2;
        }
        return p1;
    }

    /**
     * Calculates the orientation of the three points line.p1, line.p2, p
     *
     * @param p
     * @return 0 = collinear, 1 = clock wise, 2 = counter clock wise
     */
    int orientation(Point p) {
        var result = (p2.y() - p1.y()) * (p.x() - p2.x()) - (p2.x() - p1.x()) * (p.y() - p2.y());
        if (result == 0) {
            return 0;
        }
        return (result > 0) ? 1 : 2;
    }

    /**
     * Checks if the line intersects with the other
     *
     * @param other
     * @return true if line intersects
     */
    public boolean doIntersect(Line other) {
        int o1 = orientation(other.p1);
        int o2 = orientation(other.p2);
        int o3 = other.orientation(p1);
        int o4 = other.orientation(p2);

        //Lines have different orientations
        if (o1 != o2 && o3 != o4) {
            //Return only true if they do not share a point.
            return !(p1.equals(other.p1) || p2.equals(other.p1) || p1.equals(other.p2) || p2.equals(other.p2));
        }
        //Line is on other line
        if (o1 == 0 && o2 == 0 && o3 == 0 && o4 == 0) {
            return false;
        }

        if (o1 == 0 && isPointBetweenSegmentpoints(other.p1)) {
            return true;
        }
        if (o2 == 0 && isPointBetweenSegmentpoints(other.p2)) {
            return true;
        }
        if (o3 == 0 && other.isPointBetweenSegmentpoints(p1)) {
            return true;
        }
        return o4 == 0 && other.isPointBetweenSegmentpoints(p2);
    }

    /**
     * Calculates the length of this line by Pythagorean theorem
     *
     * @return
     */
    public double length() {
        return Math.sqrt(Math.pow(p2.x() - p1.x(), 2) + Math.pow(p2.y() - p1.y(), 2));
    }

    /**
     * Returns the center point of this line
     *
     * @return
     */
    public Point getCenterPoint() {
        return new Point((p1.x() + p2.x()) / 2, (p1.y() + p2.y()) / 2);
    }
}
