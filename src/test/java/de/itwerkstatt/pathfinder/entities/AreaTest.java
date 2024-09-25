package de.itwerkstatt.pathfinder.entities;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author dsust
 */
public class AreaTest {

    record NearestPointToAreaTestcase(Area area, Point p, Point expectedResult) {

    }

    @Test
    public void testCalculateNearestPointToArea() {
        Area a = new Area(
                new Point(0, 0),
                new Point(0, 10),
                new Point(10, 10),
                new Point(10, 0)
        );

        NearestPointToAreaTestcase[] testcases = new NearestPointToAreaTestcase[]{
            //Case 1
            // (0/10)         (10/10)
            //   o---------------o
            //   |               |
            //   |               |      X(15/6)
            //   |               |
            //   |               |
            //   o---------------o
            // (0/0)           (10/0)
            new NearestPointToAreaTestcase(a, new Point(15, 6), new Point(10, 6)),
            //Case 2
            // (0/10)         (10/10)   X(15/12)
            //   o---------------o
            //   |               |
            //   |               |      
            //   |               |
            //   |               |
            //   o---------------o
            // (0/0)           (10/0)
            new NearestPointToAreaTestcase(a, new Point(15, 12), new Point(10, 10)),
            //Case 3
            // (0/10)         (10/10)
            //   o---------------o
            //   |               |
            //   |               X(10/8)   
            //   |               |
            //   |               |
            //   o---------------o
            // (0/0)           (10/0)
            new NearestPointToAreaTestcase(a, new Point(10, 8), new Point(10, 8)),
            //Case 4
            // (0/10)         (10/10)
            //   o---------------o
            //   |               |
            //   | X(1/6)        |   
            //   |               |
            //   |               |
            //   o---------------o
            // (0/0)           (10/0)
            new NearestPointToAreaTestcase(a, new Point(1, 6), new Point(0, 6))
        };

        for (int caseNumber = 0; caseNumber < testcases.length; caseNumber++) {
            System.out.println("Test case #" + (caseNumber + 1));
            NearestPointToAreaTestcase testcase = testcases[caseNumber];
            assertEquals(testcase.expectedResult, testcase.area.calculateNearestPointToArea(testcase.p));
        }
    }

    @Test
    public void testCalculateDirectionalNearestPointToArea() {
        Area a = new Area(
                new Point(100, 100),
                new Point(200, 100),
                new Point(250, 200),
                new Point(300, 100),
                new Point(400, 100),
                new Point(400, 300),
                new Point(100, 300)
        );

        // Case 1
        // 
        // (100,100)  (200,100) (300,100)  (400,100)
        //     o----------o         o----------o
        //     |           \    s  /           |
        //     |            \     /            |
        //     |             \   /             |
        //     |              \ /              |
        //     |    d          o               |
        //     |           (250,200)           |
        //     |                               |
        //     o-------------------------------o
        // (100,300)                       (400,300)
        Point s = new Point(260, 120);
        Point d = new Point(140, 240);
        assertEquals(new Point(220, 140), a.calculateDirectionalNearestPointToArea(s, d));
    }

    record ShiftPointsArrayTestcase(Point startpoint, Point[] expectedResult) {

    }

    @Test
    public void testGetAllPointsBeginningWith() {
        Area a = new Area(new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(4, 4), new Point(5, 5));
        ShiftPointsArrayTestcase[] testcases = new ShiftPointsArrayTestcase[]{
            new ShiftPointsArrayTestcase(new Point(1, 1), new Point[]{new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(4, 4), new Point(5, 5)}),
            new ShiftPointsArrayTestcase(new Point(2, 2), new Point[]{new Point(2, 2), new Point(3, 3), new Point(4, 4), new Point(5, 5), new Point(1, 1)}),
            new ShiftPointsArrayTestcase(new Point(3, 3), new Point[]{new Point(3, 3), new Point(4, 4), new Point(5, 5), new Point(1, 1), new Point(2, 2)}),
            new ShiftPointsArrayTestcase(new Point(4, 4), new Point[]{new Point(4, 4), new Point(5, 5), new Point(1, 1), new Point(2, 2), new Point(3, 3)}),
            new ShiftPointsArrayTestcase(new Point(5, 5), new Point[]{new Point(5, 5), new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(4, 4)}),
            new ShiftPointsArrayTestcase(new Point(6, 6), null),};

        for (int caseNumber = 0; caseNumber < testcases.length; caseNumber++) {
            System.out.println("Test case #" + (caseNumber + 1));
            ShiftPointsArrayTestcase testcase = testcases[caseNumber];
            if (testcase.expectedResult != null) {
                assertEquals(Arrays.asList(testcase.expectedResult), Arrays.asList(a.getAllPointsBeginningWith(testcase.startpoint, false)));
            } else {
                assertThrows(IllegalArgumentException.class, () -> a.getAllPointsBeginningWith(testcase.startpoint, false));
            }
        }
    }

    @Test
    public void testGetAllPointsBeginningWithReverse() {
        Area a = new Area(new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(4, 4), new Point(5, 5));
        ShiftPointsArrayTestcase[] testcases = new ShiftPointsArrayTestcase[]{
            new ShiftPointsArrayTestcase(new Point(1, 1), new Point[]{new Point(1, 1), new Point(5, 5), new Point(4, 4), new Point(3, 3), new Point(2, 2)}),
            new ShiftPointsArrayTestcase(new Point(2, 2), new Point[]{new Point(2, 2), new Point(1, 1), new Point(5, 5), new Point(4, 4), new Point(3, 3)}),
            new ShiftPointsArrayTestcase(new Point(3, 3), new Point[]{new Point(3, 3), new Point(2, 2), new Point(1, 1), new Point(5, 5), new Point(4, 4)}),
            new ShiftPointsArrayTestcase(new Point(4, 4), new Point[]{new Point(4, 4), new Point(3, 3), new Point(2, 2), new Point(1, 1), new Point(5, 5)}),
            new ShiftPointsArrayTestcase(new Point(5, 5), new Point[]{new Point(5, 5), new Point(4, 4), new Point(3, 3), new Point(2, 2), new Point(1, 1)}),
            new ShiftPointsArrayTestcase(new Point(6, 6), null),};

        for (int caseNumber = 0; caseNumber < testcases.length; caseNumber++) {
            System.out.println("Test case #" + (caseNumber + 1));
            ShiftPointsArrayTestcase testcase = testcases[caseNumber];
            if (testcase.expectedResult != null) {
                assertEquals(Arrays.asList(testcase.expectedResult), Arrays.asList(a.getAllPointsBeginningWith(testcase.startpoint, true)));
            } else {
                assertThrows(IllegalArgumentException.class, () -> a.getAllPointsBeginningWith(testcase.startpoint, true));
            }
        }
    }
}
