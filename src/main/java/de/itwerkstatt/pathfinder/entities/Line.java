package de.itwerkstatt.pathfinder.entities;

import java.util.Optional;

/**
 * A line segment defined by two points
 *
 * @author dsust
 */
public record Line(Point p1, Point p2) {

    /**
     * Returns the y value for the given x. If this point is not between this
     * segment empty will be returned.
     *
     * @param x
     * @return the corresponding y value or empty if outside of segment
     */
    private Optional<Double> getYforGivenX(double x) {
        if (isHorizontal()) {
            return (x >= getMinX() && x <= getMaxX()) ? Optional.of(p1.y()) : Optional.empty();
        } else if (isVertical()) {
            return (x == p1.x()) ? Optional.of(p1.y()) : Optional.empty();
        } else {
            // y = m*x + c
            double y = calculateGradient() * x + calculateOffset();
            return (y >= getMinY() && y <= getMaxY()) ? Optional.of(y) : Optional.empty();
        }
    }
    
    /**
     * Returns the x value for the given y. If this point is not between this
     * segment empty will be returned.
     *
     * @param y
     * @return the corresponding x value or empty if outside of segment
     */
    private Optional<Double> getXforGivenY(double y) {
        if (isHorizontal()) {
            return (y == p1.y()) ? Optional.of(p1.x()) : Optional.empty();
        } else if (isVertical()) {
            return (y >= getMinY() && y <= getMaxY()) ? Optional.of(p1.x()) : Optional.empty();
        } else {
            // y = m*x + c
            // x = (y-c)/m
            double x = (y - calculateOffset())/calculateGradient();
            return (x >= getMinX() && x <= getMaxX()) ? Optional.of(x) : Optional.empty();
        }
    }

    /**
     * Returns true if the line is horizontal
     * @return 
     */
    private boolean isHorizontal() {
        return p1.y() == p2.y();
    }

    /**
     * Returns true if the line is vertical
     * @return 
     */
    private boolean isVertical() {
        return p1.x() == p2.x();
    }

    /**
     * Calculates the gradient of the line
     *
     * @return the gradient
     * @throws ArithmeticException when line is vertical
     */
    private double calculateGradient() {
        return (p2.y() - p1.y()) / (p2.x() - p1.x());
    }

    /**
     * Calculates the offset of the line 
     * @return 
     * @throws ArithmeticException when line is vertical
     */
    private double calculateOffset() {
        return p1.y() - (calculateGradient() * p1.x());
    }

    /**
     * Checks if the given point p is between the linesegment by checking the
     * coordinate-values
     *
     * @param p
     * @return true, if p is between p1 and p2
     */
    private boolean isPointBetweenSegmentpoints(Point p) {
        return p.y() >= getMinY() && p.y() <= getMaxY()
                && p.x() >= getMinX() && p.x() <= getMaxX();
    }

    private double getMaxX() {
        return p1.x() > p2.x() ? p1.x() : p2.x();
    }

    private double getMinX() {
        return p1.x() < p2.x() ? p1.x() : p2.x();
    }

    private double getMaxY() {
        return p1.y() > p2.y() ? p1.y() : p2.y();
    }

    private double getMinY() {
        return p1.y() < p2.y() ? p1.y() : p2.y();
    }

    /**
     * Returns the point with the smallest distance between the given point and
     * the line segment.
     *
     * @param p Starting point
     * @return Point
     */
    public Point getNearestPointToLine(Point p) {
        Point intersectionPoint;
        if (isHorizontal()) {
            intersectionPoint = new Point(p.x(), p1.y());
        } else if (isVertical()) {
            intersectionPoint = new Point(p1.x(), p.y());
        } else {
            // Calculate linear equation y = mx + c
            // Gradient 
            double m = calculateGradient();
            // Offset c = y - mx
            double c = calculateOffset();
            //Normal through point P
            //Gradient of normal
            double m_normal = -1.0d / m;
            double c_normal = p.y() - (m_normal * p.x());
            // m x + c = m_normal x + c_normal
            // x = (c_normal - c) / (m - m_normal)
            double x_new = (c_normal - c) / (m - m_normal);
            double y_new = m_normal * x_new + c_normal;
            intersectionPoint = new Point(x_new, y_new);
        }
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
     * Checks if the line segments intersect
     *
     * @param other
     * @return
     */
    public boolean doIntersect(Line other) {
        //Case 1 - line is horizontal
        if (isHorizontal()) {
            //Other line is horizonal
            if (other.isHorizontal()) {
                return false;
            }
            //Other line is vertical
            if (other.isVertical()) {
                return other.p1.x() >= getMinX() && other.p1.x() <= getMaxX()
                        && p1.y() >= other.getMinY() && p1.y() <= other.getMaxY()
                        && !touch(other);
            }
            //Other line has specific gradient
            Optional<Double> optXVal = other.getXforGivenY(p1.y());
            if (optXVal.isPresent()) {
                double intersectAtXVal = optXVal.get();
                return p1.x() != intersectAtXVal && p2.x() != intersectAtXVal
                        && intersectAtXVal >= getMinX() && intersectAtXVal <= getMaxX();
            }
            return false;
        }

        //Case 2 - line is vertical
        if (isVertical()) {
            //Other line is horizonal
            if (other.isHorizontal()) {
                return other.p1.y() >= getMinY() && other.p1.y() <= getMaxY()
                        && p1.x() >= other.getMinX() && p1.x() <= other.getMaxX()
                        && !touch(other);
            }
            //Other line is vertical
            if (other.isVertical()) {
                return false;
            }
            //Other line has specific gradient
            Optional<Double> optYVal = other.getYforGivenX(p1.x());
            if (optYVal.isPresent()) {
                double intersectAtYVal = optYVal.get();
                return p1.y() != intersectAtYVal && p2.y() != intersectAtYVal
                        && intersectAtYVal >= getMinY() && intersectAtYVal <= getMaxY();
            }
            return false;
        }
        //Line has a specific gradient
        //Other line is horizonal or vertical
        if (other.isHorizontal() || other.isVertical()) {
            return other.doIntersect(this);
        }
        //Both lines have a specific gradient, calculacte intersection point
        //Same gradient, so the lines are parallel
        if (calculateGradient() == other.calculateGradient()) {
            return false; 
        }

        // x-Koordinate des Schnittpunkts berechnen
        double x = (other.calculateOffset() - calculateOffset()) / 
                (calculateGradient() - other.calculateGradient());

        // y-Koordinate des Schnittpunkts berechnen
        double y = calculateGradient() * x + calculateOffset();
        Point intersectionPoint = new Point(x,y);
        return isPointBetweenSegmentpoints(intersectionPoint) && !intersectionPoint.equals(p1) && !intersectionPoint.equals(p2);
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
     * Splits the line in steps and return the cut points (exclusive start and end
     * points of the line)
     * @param steps how many points 
     * @return array with points
     */
    public Point[] splitLineInPoints(int steps) {
        Point[] result = new Point[steps];
        double step = (getMaxX()-getMinX()) / ((double) steps+1.0);
        if (isVertical()) {
            step = (getMaxY()-getMinY()) / ((double) steps+1.0);
        }
        for (int i = 1; i < steps+1; i++) {
            double x,y;
            if (isVertical()) {
                y = getMinY()+step*i;
                x = getXforGivenY(y).get();
            } else {
                x = getMinX()+step*i;
                y = getYforGivenX(x).get();
            }
            result[i-1] = new Point(x,y);
        }
        return result;
    }

    /**
     * Checks if at least one point of the one line 
     * lay on the line segment of the other one
     * @param other
     * @return 
     */
    public boolean touch(Line other) {
        if (isHorizontal()) {
            return other.p1.y() == p1.y() || other.p2.y() == p1.y();
        }
        if (isVertical()) {
            return other.p1.x() == p1.x() || other.p2.x() == p1.x();
        }
        //Line has specific gradient
        // y = mx + c
        boolean p1_onSegment = other.p1.y() == calculateGradient() * other.p1.x() + calculateOffset();
        boolean p2_onSegment = other.p2.y() == calculateGradient() * other.p2.x() + calculateOffset();
        return p1_onSegment || p2_onSegment;
    }
}
