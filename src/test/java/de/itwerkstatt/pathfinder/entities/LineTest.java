package de.itwerkstatt.pathfinder.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author dsust
 */
public class LineTest {

    @Test
    public void testIsValBetweenSegment() {
        Line l = new Line(new Point(0, 0), new Point(10, 0));
        assertTrue(l.isPointBetweenSegmentpoints(new Point(0, 0)));
        assertTrue(l.isPointBetweenSegmentpoints(new Point(5, 0)));
        assertTrue(l.isPointBetweenSegmentpoints(new Point(10, 0)));
        assertFalse(l.isPointBetweenSegmentpoints(new Point(0, 1)));
        assertFalse(l.isPointBetweenSegmentpoints(new Point(5, 1)));
        assertFalse(l.isPointBetweenSegmentpoints(new Point(10, 1)));
    }

    record NearestPointToLineTestcase(Line l, Point p, Point expectedResult) {

    }

    @Test
    public void testGetNearestPointToHorizontalLine() {
        Line l = new Line(new Point(0, 0), new Point(10, 0));
        NearestPointToLineTestcase[] testcases = new NearestPointToLineTestcase[]{
            // Case 1: Nearest point is intersection point P(2/0) of normal line
            //   (0/0)    (2/0)                      (10/0)
            //     o--------P--------------------------o
            //              |
            //              |
            //              x (2/5)
            new NearestPointToLineTestcase(l, new Point(2, 5), new Point(2, 0)),
            // Case 2: Nearest point is the second point of the line (10/0)
            //   (0/0)                          (10/0)
            //     o-----------------------------------o
            //                                          \
            //                                           \
            //                                            x (12/5)
            new NearestPointToLineTestcase(l, new Point(12, 5), new Point(10, 0)),
            // Case 3: Nearest point is the second point of the line (10/0)
            //   (0/0)                          (10/0)
            //     o-----------------------------------o---x (12/0)
            new NearestPointToLineTestcase(l, new Point(12, 0), new Point(10, 0)),
            // Case 4: Nearest point is intersection point P(2/0) of normal line
            //   (0/0)    (2/0)                      (10/0)
            //     o--------x--------------------------o
            new NearestPointToLineTestcase(l, new Point(2, 0), new Point(2, 0))};

        for (int caseNumber = 0; caseNumber < testcases.length; caseNumber++) {
            System.out.println("Test case #" + (caseNumber + 1));
            NearestPointToLineTestcase testcase = testcases[caseNumber];
            assertEquals(testcase.expectedResult, testcase.l.getNearestPointToLine(testcase.p));
        }
    }

    @Test
    public void testGetNearestPointToVerticalLine() {
        Line l = new Line(new Point(0, 0), new Point(0, 10));
        NearestPointToLineTestcase[] testcases = new NearestPointToLineTestcase[]{
            /*
            Case 1: Nearest point is intersection point P(0/2) of normal line
             (0/0)o
                  |
             (0/2)P-----x (5/2)
                  |
                  |
                  |
            (0/10)o     
             */
            new NearestPointToLineTestcase(l, new Point(5, 2), new Point(0, 2)),
            /*
            Case 2: Nearest point is the second point of the line (0/10)
             (0/0)o
                  |
                  |
                  |
                  |
                  |
            (0/10)o
                   \
                    \
                     x(5/12)
             */
            new NearestPointToLineTestcase(l, new Point(5, 12), new Point(0, 10)),
            /*
            Case 3: Nearest point is the second point of the line (0/10)
             (0/0)o
                  |
                  |
                  |
                  |
                  |
            (0/10)o
                  |
                  |
                  x(0/12)
             */
            new NearestPointToLineTestcase(l, new Point(0, 12), new Point(0, 10)),
            /*
            Case 4: Nearest point is intersection point P(0/2) of normal line
             (0/0)o
                  |
             (0/2)x
                  |
                  |
                  |
            (0/10)o     
             */
            new NearestPointToLineTestcase(l, new Point(0, 2), new Point(0, 2))
        };

        for (int caseNumber = 0; caseNumber < testcases.length; caseNumber++) {
            System.out.println("Test case #" + (caseNumber + 1));
            NearestPointToLineTestcase testcase = testcases[caseNumber];
            assertEquals(testcase.expectedResult, testcase.l.getNearestPointToLine(testcase.p));
        }
    }

    @Test
    public void testGetNearestPointToLine() {
        Line l = new Line(new Point(1, 2), new Point(5, 10));
        NearestPointToLineTestcase[] testcases = new NearestPointToLineTestcase[]{
            /*
            Case 1 Nearest point is intersection point of normal line
                (5/10)o
                     /
                    /
                   /   x(4/4)
                  /
            (1/2)o
             */
            new NearestPointToLineTestcase(l, new Point(4, 4), new Point(2.4, 4.8)),
            /*
            Case 2 Nearest point is the second point of the line (5/10)
                            x(9/14)

                (5/10)o
                     /
                    /
                   /
                  /
            (1/2)o
             */
            new NearestPointToLineTestcase(l, new Point(9, 14), new Point(5, 10)),
            /*
            Case 3 Nearest point is the second point of the line (5/10)
                        x(7/14)
                       /
                (5/10)o
                     /
                    /
                   /
                  /
            (1/2)o
             */
            new NearestPointToLineTestcase(l, new Point(7, 14), new Point(5, 10)),
            /*
            Case 4 Nearest point is intersection point P(4/8)
                  (5/10)o
                       /
                 (4/8)x
                     /
                    /
                   /
                  /
            (1/2)o
             */
            new NearestPointToLineTestcase(l, new Point(4, 8), new Point(4, 8))
        };

        for (int caseNumber = 0; caseNumber < testcases.length; caseNumber++) {
            System.out.println("Test case #" + (caseNumber + 1));
            NearestPointToLineTestcase testcase = testcases[caseNumber];
            assertEquals(testcase.expectedResult, testcase.l.getNearestPointToLine(testcase.p));
        }
    }

    record OrientationTestcase(Line l, Point p, int expectedOrientation) {

    }

    @Test
    public void testOrientation() {
        Line l = new Line(new Point(1, 1), new Point(10, 1));
        OrientationTestcase[] testcases = new OrientationTestcase[]{
            //Case 1
            //(1/1)     X(5/5)             (10/1)
            // o-----------------------------o
            new OrientationTestcase(l, new Point(5, 5), 2),
            //Case 2
            //(1/1)                        (10/1)
            // o-----------------------------o
            //          X(5/-5)
            new OrientationTestcase(l, new Point(5, -5), 1),
            //Case 3
            //(1/1)                        (10/1)
            // o--------X(5/1)---------------o
            new OrientationTestcase(l, new Point(5, 1), 0)
        };

        for (int caseNumber = 0; caseNumber < testcases.length; caseNumber++) {
            System.out.println("Test case #" + (caseNumber + 1));
            OrientationTestcase testcase = testcases[caseNumber];
            assertEquals(testcase.expectedOrientation, testcase.l.orientation(testcase.p));
        }
    }

    record IntersectTestcase(Line line1, Line line2, boolean expectedResult) {

    }

    @Test
    public void testDoIntersect() {
        Line l = new Line(new Point(1, 1), new Point(10, 1));

        IntersectTestcase[] testcases = new IntersectTestcase[]{
//            //Case 1
//            //(1/1)                        (10/1)
//            // o-----------------------------o
//            // X-----------------------------X
//            //(1/0)                        (10/0)
//            new IntersectTestcase(l, new Line(new Point(1, 0), new Point(10, 0)), false),
//            //Case 2
//            //                  X (8/10)
//            //                 /
//            //                /
//            //(1/1)          X (6/5)       (10/1)
//            // o-----------------------------o
//            new IntersectTestcase(l, new Line(new Point(6, 5), new Point(8, 10)), false),
//            //Case 3
//            //                X (6/5)
//            //(1/1)          /             (10/1)
//            // o-----------------------------o
//            //             /
//            //            X (4/-1)
//            new IntersectTestcase(l, new Line(new Point(4, -1), new Point(6, 5)), true),
//            //Case 4
//            //(1/1)                        (10/1)
//            // o---------X------X------------o
//            //         (6/1)  (4/1)
//            new IntersectTestcase(l, new Line(new Point(4, 1), new Point(6, 1)), false),
//            //Case 5
//            //(1/1)                        (10/1)
//            // o---------X-------------------o--------X
//            //         (6/1)                        (14/1)
//            new IntersectTestcase(l, new Line(new Point(6, 1), new Point(14, 1)), false),
//            //Case 6
//            //(1/1)                        (10/1)
//            // o-----------------------------o--------X
//            //                            (10/1)   (14/1)
//            new IntersectTestcase(l, new Line(new Point(10, 1), new Point(14, 1)), false),
            //Case 7
            //(1/1)                        (10/1)
            // o-----------------------------x
            //                         (10/1) \
            //                                 \
            //                                  o(14/5)
            new IntersectTestcase(l, new Line(new Point(10, 1), new Point(14, 5)), false)
        };

        for (int caseNumber = 0; caseNumber < testcases.length; caseNumber++) {
            System.out.println("Test case #" + (caseNumber + 1));
            IntersectTestcase testcase = testcases[caseNumber];
            assertEquals(testcase.expectedResult, testcase.line1.doIntersect(testcase.line2));
            assertEquals(testcase.expectedResult, testcase.line2.doIntersect(testcase.line1));
        }
    }

    record LengthTestcase(Point p1, Point p2, double expectedResult) {

    }

    @Test
    public void testLength() {
        LengthTestcase[] testcases = new LengthTestcase[]{
            // Case 1:
            //   (0/0)                               (10/0)
            //     o-----------------------------------o
            new LengthTestcase(new Point(0, 0), new Point(10, 0), 10),
            // Case 2
            //   (5/10)o
            //        /
            //       /
            // (1/2)o
            new LengthTestcase(new Point(1, 2), new Point(5, 10), 8.94),
            // Case 3:
            // (0/0)o
            //      |
            //      |
            // (0/5)o
            new LengthTestcase(new Point(0, 5), new Point(0, 0), 5),
            // Case 4: Zero length
            new LengthTestcase(new Point(0, 5), new Point(0, 5), 0)
        };

        for (int caseNumber = 0; caseNumber < testcases.length; caseNumber++) {
            System.out.println("Test case #" + (caseNumber + 1));
            LengthTestcase testcase = testcases[caseNumber];
            assertEquals(testcase.expectedResult, round(new Line(testcase.p1, testcase.p2).length()));
            assertEquals(testcase.expectedResult, round(new Line(testcase.p2, testcase.p1).length()));
        }
    }

    /**
     * Round double value to 2 decimal places
     * @param value
     * @return 
     */
    double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
