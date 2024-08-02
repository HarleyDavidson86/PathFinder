package de.itwerkstatt.pathfinder.entities;

/**
 * A line segment defined by two points
 * @author dsust
 */
public record Line(Point p1, Point p2) {

    /**
     * Checks if the given value is between the
     * x-coord value of p1 and p2
     * @param x
     * @return true, if x is between p1.x and p2.x 
     */
    boolean isValBetweenSegment(double x) {
        double maxX = p1.x() > p2.x() ? p1.x() : p2.x();
        double minX = p1.x() < p2.x() ? p1.x() : p2.x();
        return x > minX && x < maxX;
    }

    /**
     * Returns the point with the smallest distance between the 
     * given point and the line segment.
     *
     * @param p Starting point
     * @return Point
     */
    Point getNearestPointToLine(Point p) {
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

            // m x + c = m_normal x + c_normal
            // x = (c_normal - c) / (m - m_normal)
            double x_new = (c_normal - c) / (m - m_normal);
            double y_new = m_normal * x_new + c_normal;
            intersectionPoint = new Point(x_new, y_new);
        }

        // Check whether the point is within the segment
        if (isValBetweenSegment(intersectionPoint.x())) {
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
}
