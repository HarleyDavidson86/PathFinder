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

    record IntersectTestcase(String description, Line line1, Line line2, boolean expectedResult) {

    }

    @Test
    public void testDoIntersect() {
        IntersectTestcase[] testcases = new IntersectTestcase[]{
            // Case 1
            //  o-----------o
            //  o-----------o
            new IntersectTestcase(
            "Both lines horizonal, always false",
            new Line(new Point(1, 1), new Point(10, 1)),
            new Line(new Point(1, 5), new Point(10, 5)),
            false
            ),
            //Case 2
            //        o
            //        |
            //  o-----+-----o
            //        |
            //        o
            new IntersectTestcase(
            "Line is horizonal, other is vertical, intersecting",
            new Line(new Point(1, 1), new Point(10, 1)),
            new Line(new Point(5, -3), new Point(5, 5)),
            true
            ),
            //Case 3
            //        o
            //        |
            //        |
            //        o
            //
            //  o-----------o
            new IntersectTestcase(
            "Line is horizonal, other is vertical, not intersecting",
            new Line(new Point(1, 1), new Point(10, 1)),
            new Line(new Point(5, -10), new Point(5, -3)),
            false
            ),
            //Case 4
            //           o
            //          /
            //         /
            //        o
            //
            //  o-----------o
            new IntersectTestcase(
            "Line is horizonal, other has positive gradient, not intersecting",
            new Line(new Point(1, 1), new Point(10, 1)),
            new Line(new Point(5, -10), new Point(10, -5)),
            false
            ),
            //Case 5
            //           o
            //          /
            //         /
            //  o-----------o
            //       /
            //      o
            new IntersectTestcase(
            "Line is horizonal, other has positive gradient, intersecting",
            new Line(new Point(1, 1), new Point(10, 1)),
            new Line(new Point(5, -10), new Point(10, 5)),
            true
            ),
            //Case 6
            // o o
            // | |
            // | |
            // | |
            // o o
            new IntersectTestcase(
            "Both lines vertical, always false",
            new Line(new Point(1, 1), new Point(1, 10)),
            new Line(new Point(3, 3), new Point(3, 10)),
            false
            ),
            //Case 7
            // o     o
            // |    /
            // |   /
            // |  /
            // o o
            new IntersectTestcase(
            "Line is vertical, other has positive gradient, not intersecting",
            new Line(new Point(1, 1), new Point(1, 10)),
            new Line(new Point(3, 10), new Point(10, 1)),
            false
            ),
            //Case 8
            //   o o
            //   |/
            //   |
            //  /|
            // o o
            new IntersectTestcase(
            "Line is vertical, other has positive gradient, intersecting",
            new Line(new Point(1, 1), new Point(1, 10)),
            new Line(new Point(-4, 10), new Point(10, 1)),
            true
            ),
            //Case 9
            //     o o
            //    / /
            //   / /
            //  / /
            // o o
            new IntersectTestcase(
            "Both lines have positive gradient and are parallel",
            new Line(new Point(1, 1), new Point(5, 10)),
            new Line(new Point(2, 1), new Point(6, 10)),
            false
            ),
            //Case 10
            //  o  o  
            //   \/  
            //   /\ 
            //  /  \
            // o    o
            new IntersectTestcase(
            "Both lines has gradients",
            new Line(new Point(1, 1), new Point(5, 10)),
            new Line(new Point(2, 10), new Point(6, 1)),
            true
            ),
            //Case 11
            //        o
            //        |
            //  o-----o
            new IntersectTestcase(
            "Line is horizonal, other is vertical, only touching",
            new Line(new Point(1, 1), new Point(10, 1)),
            new Line(new Point(10, 1), new Point(10, 5)),
            false
            ),
            //Case 12
            //     o  
            //    / \  
            //   /   \ 
            //  /     \
            // o       o
            new IntersectTestcase(
            "Both lines has gradients, only touching",
            new Line(new Point(1, 1), new Point(5, 10)),
            new Line(new Point(5, 10), new Point(3, 6)),
            false
            ),
            //Case 13
            //            o
            //           /
            //          /
            // o-------o
            new IntersectTestcase(
            "One line is horizontal, other has gradients, only touching",
            new Line(new Point(1, 1), new Point(5, 1)),
            new Line(new Point(5, 1), new Point(7, 6)),
            false
            ),
            //Case 14
            // o   o
            // |  /
            // | /
            // |/
            // o
            new IntersectTestcase(
            "One line is vertical, other has gradients, only touching",
            new Line(new Point(1, 1), new Point(1, 5)),
            new Line(new Point(1, 5), new Point(4, 1)),
            false
            )
        };

        for (int caseNumber = 0; caseNumber < testcases.length; caseNumber++) {
            IntersectTestcase testcase = testcases[caseNumber];
            String caseName = "Test case #" + (caseNumber + 1) + ": " + testcase.description;
            System.out.println(caseName);
            assertEquals(testcase.expectedResult, testcase.line1.doIntersect(testcase.line2), "Error in " + caseName);
            System.out.println(caseName + " (inverted)");
            assertEquals(testcase.expectedResult, testcase.line2.doIntersect(testcase.line1), "Error in " + caseName + " (inverted)");
        }
    }

    @Test
    public void tempTest() {
//Checking Line[p1=Point[x=200.0, y=200.0], p2=Point[x=150.0, y=150.0]]
//Intersecting with Area Line Line[p1=Point[x=400.0, y=200.0], p2=Point[x=500.0, y=200.0]]
        Line l1 = new Line(new Point(200,200), new Point(150,150));
        Line l2 = new Line(new Point(400,200), new Point(500,200));
        System.out.println(l1.doIntersect(l2));
        System.out.println(l2.doIntersect(l1));
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
     *
     * @param value
     * @return
     */
    double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    record CenterTestcase(Point p1, Point p2, Point expectedResult) {

    }

    @Test
    public void testCenter() {
        CenterTestcase[] testcases = new CenterTestcase[]{
            new CenterTestcase(new Point(0, 0), new Point(10, 10), new Point(5, 5)),
            new CenterTestcase(new Point(10, 10), new Point(5, 5), new Point(7.5, 7.5)),
            new CenterTestcase(new Point(10, 10), new Point(10, 10), new Point(10, 10))
        };

        for (int caseNumber = 0; caseNumber < testcases.length; caseNumber++) {
            System.out.println("Test case #" + (caseNumber + 1));
            CenterTestcase testcase = testcases[caseNumber];
            assertEquals(testcase.expectedResult, new Line(testcase.p1, testcase.p2).getCenterPoint());
            assertEquals(testcase.expectedResult, new Line(testcase.p2, testcase.p1).getCenterPoint());
        }
    }
}
