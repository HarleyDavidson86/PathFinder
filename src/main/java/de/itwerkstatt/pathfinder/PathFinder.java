package de.itwerkstatt.pathfinder;

import de.itwerkstatt.pathfinder.entities.Area;
import de.itwerkstatt.pathfinder.entities.Line;
import de.itwerkstatt.pathfinder.entities.Point;
import de.itwerkstatt.pathfinder.entities.Triangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * This array can be overwritten with null null null null null     {@link #setAreaTriangles(de.itwerkstatt.pathfinder.entities.Triangle[]) 
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

            //Key: Distance, Val: Subpath
            Map<Double, List<Point>> subPathCandidates = new HashMap<>();

            //Clock wise
            calculateSubPathAndDistance(subPathCandidates, area.getAllPointsBeginningWith(areaLine.p1(), false));
            calculateSubPathAndDistance(subPathCandidates, area.getAllPointsBeginningWith(areaLine.p2(), false));

            //Counter clock wise
            calculateSubPathAndDistance(subPathCandidates, area.getAllPointsBeginningWith(areaLine.p1(), true));
            calculateSubPathAndDistance(subPathCandidates, area.getAllPointsBeginningWith(areaLine.p2(), true));

            result.addAll(subPathCandidates.get(subPathCandidates.keySet().stream().min(Double::compare).get()));
        }
        //Add endpoint
        result.add(endPoint);

        List<Point> simplifiedPath = simplifyPath(result);

        return simplifiedPath.toArray(Point[]::new);
    }

    /**
     * Simplify the given path regarding the area
     *
     * @param totalPath
     * @return a simplified path from start to end
     */
    private List<Point> simplifyPath(List<Point> totalPath) {
        // Begin from startpoint, find the last point in path which the current point
        // can see directly without crossing an edge
        List<Point> simplifiedPath = new ArrayList<>();
        System.out.println("Begin simplifying path with size " + totalPath);
        for (int i = 0; i < totalPath.size(); i++) {
            if (i < 0) {
                break;
            }
            Point currentPoint = totalPath.get(i);
            System.out.println("Current start point with index #" + i + ", " + currentPoint);
            simplifiedPath.add(currentPoint);
            int nextPointIndex = -1;
            for (int j = i + 1; j < totalPath.size(); j++) {
                Point nextPoint = totalPath.get(j);
                Line line = new Line(currentPoint, nextPoint);
                //Check if line crosses no area edges
                boolean crossAreaEdge = Stream.of(areaLines).noneMatch(l -> l.doIntersect(line));
                //Check if center point of line is in area
                //Maybe there is a better way but for now this works
                boolean centerIsInArea = isPointInArea(line.getCenterPoint());
                if (crossAreaEdge && centerIsInArea) {
                    //line between currentPoint and nextPoint crosses no edge and center is in area
                    System.out.println("Next point with index #" + j + " crosses no edge: " + nextPoint);
                    nextPointIndex = j;
                } else {
                    //edge was crossed
                    System.out.println("Next point with index #" + j + " crosses edge: " + nextPoint + ".");
                }
            }
            i = nextPointIndex - 1;
        }
        return simplifiedPath;
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

    /**
     * Calculates the distance from areaEdgePoints array to the endpoint and
     * puts the result in the map
     *
     * @param subPathCandidates
     * @param startPoint
     */
    private void calculateSubPathAndDistance(Map<Double, List<Point>> subPathCandidates, Point[] areaEdgePoints) {
        List<Point> subPath = new ArrayList<>();
        double distance = 0;
        //Go along points till direct path without crossings 
        for (Point p : areaEdgePoints) {
            subPath.add(p);
            Line line = new Line(p, endPoint);
            distance += line.length();
            if (Stream.of(areaLines).noneMatch(l -> l.doIntersect(line))) {
                //No intersection, we found a path to destination!
                break;
            } else {
            }
        }
        subPathCandidates.put(distance, subPath);
    }
}
