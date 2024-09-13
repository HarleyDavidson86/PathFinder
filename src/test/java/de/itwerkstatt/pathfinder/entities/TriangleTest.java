package de.itwerkstatt.pathfinder.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author dsust
 */
public class TriangleTest {

    record PointInTriangleTestcase(Triangle triangle, Point p, boolean expectedResult) {

    }

    @Test
    public void testIsPointInTriangle() {
        Triangle tri = new Triangle(new Point(0, 0), new Point(0, 50), new Point(10, 50));
        PointInTriangleTestcase[] testcases = new PointInTriangleTestcase[]{
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
            new PointInTriangleTestcase(tri, new Point(5, 30), true),
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
            new PointInTriangleTestcase(tri, new Point(0, 30), true),
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
            new PointInTriangleTestcase(tri, new Point(9, 30), false)
        };

        for (int caseNumber = 0; caseNumber < testcases.length; caseNumber++) {
            System.out.println("Test case #" + (caseNumber + 1));
            PointInTriangleTestcase testcase = testcases[caseNumber];
            assertEquals(testcase.expectedResult, testcase.triangle.isPointInTriangle(testcase.p));
        }
    }
}
