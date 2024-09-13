package de.itwerkstatt.pathfinder;

import de.itwerkstatt.pathfinder.entities.Area;
import de.itwerkstatt.pathfinder.entities.Line;
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
    private Line[] areaLines;

    private Point startPoint;
    private Point endPoint;

    /**
     * Takes the given area and calculates triangles of all points of the
     * area.<br>
     * This array can be overwritten with null null     {@link #setAreaTriangles(de.itwerkstatt.pathfinder.entities.Triangle[]) 
     * setAreaTriangles} method which can be necessary if the calculation of
     * triangles did not work. So please examine the calculated triangles with
     * {@link #getAreaTriangles()}.
     *
     * @param a
     */
    public PathFinder(Area a) {
        this.area = a;
        calculateTrianglesOfArea();
        calculateAreaLines();
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

    public boolean isPointInArea(Point p) {
        return Stream.of(areaTriangles).anyMatch(t -> t.isPointInTriangle(p));
    }

    /**
     * Tries to find the shortest possible path from startpoint to endpoint. If
     * the startPoint is outside of the area, first point in the result array
     * will be the point on the border of the area.<br>
     * If the endPoint is outside of the area, last point in the result array
     * will be the point on the border of the area. If there is no possible path
     * from start to end, an empty array will be returned.
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
        if (isPointInArea(startPoint)) {
            result.add(startPoint);
        } else {
            //Alternatively: NearestPointToArea regardless of direction?
            startPoint = area.calculateDirectionalNearestPointToArea(startPoint, endPoint);
            result.add(startPoint);
        }
        if (!isPointInArea(endPoint)) {
            //Alternatively: NearestPointToArea regardless of direction?
            endPoint = area.calculateDirectionalNearestPointToArea(endPoint, startPoint);
        }

        //We now have start and end point inside of area.
        List<Line> crossedEdges = Stream.of(areaLines).filter(l -> l.doIntersect(new Line(startPoint, endPoint))).toList();
        //Direct path from start to end crosses some area edges
        if (!crossedEdges.isEmpty()) {
            Line areaLine = getLineWithShortestPathFromStartpoint(crossedEdges);

            //Check point 1
            List<Point> subPath1 = new ArrayList<>();
            double distancePath1 = 0;
            //Go along points till direct path without crossings 
            for (Point p : area.getAllPointsBeginningWith(areaLine.p1())) {
                subPath1.add(p);
                Line line = new Line(p, endPoint);
                distancePath1 += line.length();
                if (Stream.of(areaLines).noneMatch(l -> l.doIntersect(line))) {
                    //No intersection, we found a path to destination!
                    break;
                }
            }
            //Check point 2
            List<Point> subPath2 = new ArrayList<>();
            double distancePath2 = 0;
            //Go along points till direct path without crossings 
            for (Point p : area.getAllPointsBeginningWith(areaLine.p2())) {
                subPath2.add(p);
                Line line = new Line(p, endPoint);
                distancePath2 += line.length();
                if (Stream.of(areaLines).noneMatch(l -> l.doIntersect(line))) {
                    //No intersection, we found a path to destination!
                    break;
                }
            }
            if (distancePath1 < distancePath2) {
                result.addAll(subPath1);
            } else {
                result.addAll(subPath2);
            }
        }
        //Add endpoint
        result.add(endPoint);
        return result.toArray(Point[]::new);
    }

    private Line getLineWithShortestPathFromStartpoint(List<Line> crossedAreaLines) {
        double minDistance = Double.MAX_VALUE;
        Line areaLineWithShortestPath = null;
        for (Line areaLine : crossedAreaLines) {
            double distance = new Line(startPoint, areaLine.getNearestPointToLine(startPoint)).length();
            if (distance < minDistance) {
                minDistance = distance;
                areaLineWithShortestPath = areaLine;
            }
        }
        return areaLineWithShortestPath;
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

    /**
     * Calculates all edge-lines of the area by the points.
     */
    private void calculateAreaLines() {
        areaLines = new Line[area.points().length];
        for (int i = 0; i < area.points().length; i++) {
            Point p1 = area.points()[i];
            Point p2 = area.points()[0];
            if ((i + 1) < area.points().length) {
                p2 = area.points()[i + 1];
            }
            areaLines[i] = new Line(p1, p2);
        }
    }

    public Area getArea() {
        return area;
    }

    public Triangle[] getAreaTriangles() {
        return areaTriangles;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    /**
     * Sets the array of triangles that should cover in sum the complete area.
     *
     * @param areaTriangles
     */
    public void setAreaTriangles(Triangle... areaTriangles) {
        this.areaTriangles = areaTriangles;
    }
}
