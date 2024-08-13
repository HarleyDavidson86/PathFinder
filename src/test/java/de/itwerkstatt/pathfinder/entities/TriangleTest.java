package de.itwerkstatt.pathfinder.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author dsust
 */
public class TriangleTest {

    @Test
    public void testIsPointInTriangle() {
        Triangle tri = new Triangle(new Point(0, 0), new Point(0, 50), new Point(10, 50));
        // Case 1
        // (0/50)      (10/50)
        // o-----------o
        // |          /
        // |  (5/30) /
        // |   X    /
        // |       /
        // |      /
        // |     /   
        // |    /
        // |   /
        // |  /
        // | /
        // o(0/0)
        assertTrue(tri.isPointInTriangle(new Point(5,30)));
        
        // Case 2
        // (0/50)      (10/50)
        // o-----------o
        // |          /
        // |         /
        // X(0/30)  /
        // |       /
        // |      /
        // |     /   
        // |    /
        // |   /
        // |  /
        // | /
        // o(0/0)
        assertTrue(tri.isPointInTriangle(new Point(0,30)));
        
        // Case 3
        // (0/50)      (10/50)
        // o-----------o
        // |          /
        // |         /
        // |        /
        // |       /
        // |      /   X(9/30)
        // |     /   
        // |    /
        // |   /
        // |  /
        // | /
        // o(0/0)
        assertFalse(tri.isPointInTriangle(new Point(9,30)));
    }
}
