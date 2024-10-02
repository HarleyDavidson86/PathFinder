package de.itwerkstatt.pathfinder.entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dsust
 */
public class Node implements Comparable<Node> {
    
    public record NeighbourNode(Node node, Double distance) {}

    private final Point point;
    private final List<NeighbourNode> directNeighbours;
    private double distanceFromStart; //g
    private double heuristicDistanceToGoal; //h
    private double totalDistance; //f
    
    private Node parent;

    public Node(Point p) {
        this.point = p;
        this.directNeighbours = new ArrayList<>();
        this.distanceFromStart = Double.MAX_VALUE;
        this.heuristicDistanceToGoal = 0;
        this.totalDistance = Double.MAX_VALUE;
    }

    public Point getPoint() {
        return point;
    }

    public void setDistanceFromStart(double distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
    }

    public void setHeuristicDistanceToGoal(double heuristicDistanceToGoal) {
        this.heuristicDistanceToGoal = heuristicDistanceToGoal;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public List<NeighbourNode> getDirectNeighbours() {
        return directNeighbours;
    }

    public double getDistanceFromStart() {
        return distanceFromStart;
    }

    public double getHeuristicDistanceToGoal() {
        return heuristicDistanceToGoal;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
    
    public void resetNode() {
        parent = null;
        distanceFromStart = Double.MAX_VALUE;
        heuristicDistanceToGoal = 0;
        totalDistance = Double.MAX_VALUE;
    }
    
    public void addNeighbour(Node n, Double distance) {
        directNeighbours.add(new NeighbourNode(n, distance));
    }

    @Override
    public String toString() {
        return "Node{" + "point=" + point + ", directNeighbours=" + directNeighbours.stream().map(e -> e.node().getPoint()+" : "+e.distance()).toList() + '}';
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.totalDistance, o.getTotalDistance());
    }
   
}
