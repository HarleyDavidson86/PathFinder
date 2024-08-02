

package de.itwerkstatt.pathfinder;

import de.itwerkstatt.pathfinder.entities.Area;
import de.itwerkstatt.pathfinder.entities.Point;
import de.itwerkstatt.pathfinder.entities.Triangle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Provides functionality to calculate the shortest possible 
 * path in a given area from a defined start and destination.
 * If the given points are outside of the area, at first the shortest way
 * to the border of the area is being calculated.
 * @author dsust
 */
public class PathFinder {
    private final Area area;
    private Triangle[] areaTriangles;

    /**
     * Takes the given area and calculates triangles of 
     * all points of the area.<br>
     * This array can be overwritten with 
     * {@link #setAreaTriangles(de.itwerkstatt.pathfinder.entities.Triangle[]) 
     * setAreaTriangles} method which can be necessary if the calculation of 
     * triangles did not work. So please examine the calculated triangles with
     * {@link #getAreaTriangles()}.
     * @param a
     */
    public PathFinder(Area a) {
        this.area = a;
        calculateTrianglesOfArea();
    }
    
    /**
     * Tries to find the shortest possible path from startpoint to endpoint.
     * If the startPoint is outside of the area, second point in the result array
     * will be the point on the border of the area.<br>
     * If the endPoint is outside of the area, second last point in the result 
     * array will be the point on the border of the area.
     * If there is no possible path from start to end, an empty array will be
     * returned.
     * @param startPoint
     * @param endPoint
     * @return an array of points
     */
    public Point[] findPath(Point startPoint, Point endPoint) {
        List<Point> result = new ArrayList<>();
        result.add(startPoint);
        //Check if startPoint is in area
        if (Stream.of(areaTriangles).noneMatch(t -> t.isPointInTriangle(startPoint))) {
            result.add(area.calculateNearestPointToArea(startPoint));
        }
        if (Stream.of(areaTriangles).noneMatch(t -> t.isPointInTriangle(endPoint))) {
            result.add(area.calculateNearestPointToArea(endPoint));
        }
        result.add(endPoint);
        return result.toArray(Point[]::new);
    }
    
    /**
     * Tries to calculate triangles from the points of the area
     * automatically.
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
     * @param areaTriangles 
     */
    public void setAreaTriangles(Triangle[] areaTriangles) {
        this.areaTriangles = areaTriangles;
    }
}
