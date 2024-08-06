package de.itwerkstatt.pathfinder;

import de.itwerkstatt.pathfinder.entities.Area;
import de.itwerkstatt.pathfinder.entities.Point;
import de.itwerkstatt.pathfinder.entities.Triangle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Provides functionality to calculate the shortest possible path in a given
 * area from a defined start and destination. If the given points are outside of
 * the area, at first the shortest way to the border of the area is being
 * calculated.
 *
 * @author dsust
 */
public class PathFinder {

    private final Area area;
    private Triangle[] areaTriangles;

    private Point startPoint;
    private Point endPoint;

    /**
     * Takes the given area and calculates triangles of all points of the
     * area.<br>
     * This array can be overwritten with      {@link #setAreaTriangles(de.itwerkstatt.pathfinder.entities.Triangle[]) 
     * setAreaTriangles} method which can be necessary if the calculation of
     * triangles did not work. So please examine the calculated triangles with
     * {@link #getAreaTriangles()}.
     *
     * @param a
     */
    public PathFinder(Area a) {
        this.area = a;
        calculateTrianglesOfArea();
    }

    /**
     * Sets the new start and endpoints for the path. Parameters can also be set
     * individually, old values will not be deleted.
     *
     * @param start
     * @param end
     */
    public void setStartAndEndpoint(Point start, Point end) {
        if (start != null) {
            startPoint = start;
        }
        if (end != null) {
            endPoint = end;
        }
    }

    public boolean isStartpointInArea() {
        return Stream.of(areaTriangles).anyMatch(t -> t.isPointInTriangle(startPoint));
    }

    public boolean isEndpointInArea() {
        return Stream.of(areaTriangles).anyMatch(t -> t.isPointInTriangle(endPoint));
    }

    /**
     * Tries to find the shortest possible path from startpoint to endpoint. If
     * the startPoint is outside of the area, first point in the result array
     * will be the point on the border of the area.<br>
     * If the endPoint is outside of the area, last point in the result
     * array will be the point on the border of the area. If there is no
     * possible path from start to end, an empty array will be returned.
     *
     * @return an array of points
     * @throws IllegalArgumentException if start or endpoint is null
     */
    public Point[] findPath() {
        if (startPoint == null || endPoint == null) {
            throw new IllegalArgumentException("Start- and endpoint must not be null.");
        }
        List<Point> result = new ArrayList<>();
        //Check if startPoint is in area
        if (isStartpointInArea()) {
            result.add(startPoint);
        } else {
            result.add(area.calculateNearestPointToArea(startPoint));
        }
        if (isEndpointInArea()) {
            result.add(endPoint);
        } else {
            result.add(area.calculateNearestPointToArea(endPoint));
        }
        return result.toArray(Point[]::new);
    }

    /**
     * Tries to calculate triangles from the points of the area automatically.
     */
    private void calculateTrianglesOfArea() {
        areaTriangles = new Triangle[area.points().length - 2];
        Point p1 = area.points()[0];
        for (int i = 2; i < area.points().length; i++) {
            Point p2 = area.points()[i - 1];
            Point p3 = area.points()[i];
            areaTriangles[i - 2] = new Triangle(p1, p2, p3);
        }
    }

    public Area getArea() {
        return area;
    }

    public Triangle[] getAreaTriangles() {
        return areaTriangles;
    }

    /**
     * Sets the array of triangles that should cover in sum the complete area.
     *
     * @param areaTriangles
     */
    public void setAreaTriangles(Triangle[] areaTriangles) {
        this.areaTriangles = areaTriangles;
    }
}
