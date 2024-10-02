package de.itwerkstatt.pathfinder.util;

import de.itwerkstatt.pathfinder.entities.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Implements the A* algorithm that calculates the shortest path to all areas of
 * the map.
 *
 * @author dsust
 */
public class AStarUtil {

    public static List<Node> aStar(Node startNode, Node destinationNode) {

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<Node> closedSet = new HashSet<>();

        startNode.setDistanceFromStart(0);
        startNode.setTotalDistance(startNode.getHeuristicDistanceToGoal());
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(destinationNode)) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            for (Node.NeighbourNode nn : current.getDirectNeighbours()) {
                Double distance = nn.distance();
                Node neighbor = nn.node();
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double tentativeG = current.getDistanceFromStart() + distance;

                if (tentativeG < neighbor.getDistanceFromStart()) {
                    neighbor.setParent(current);
                    neighbor.setDistanceFromStart(tentativeG);
                    neighbor.setTotalDistance(neighbor.getDistanceFromStart() + neighbor.getHeuristicDistanceToGoal());

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
        return new ArrayList<>(); // Return empty path if no path found
    }

    private static List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;
    }

}
