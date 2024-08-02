package de.itwerkstatt.pathfinder.entities;

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

    @Test
    public void testGetNearestPointToHorizontalLine() {
        Line line = new Line(new Point(0, 0), new Point(10, 0));
        // Case 1: Nearest point is intersection point P(2/0) of normal line
        //   (0/0)    (2/0)                      (10/0)
        //     o--------P--------------------------o
        //              |
        //              |
        //              x (2/5)
        System.out.println("Case 1 : testGetNearestPointToHorizontalLine");
        assertEquals(new Point(2, 0), line.getNearestPointToLine(new Point(2, 5)));

        // Case 2: Nearest point is the second point of the line (10/0)
        //   (0/0)                          (10/0)
        //     o-----------------------------------o
        //                                          \
        //                                           \
        //                                            x (12/5)
        System.out.println("Case 2 : testGetNearestPointToHorizontalLine");
        assertEquals(new Point(10, 0), line.getNearestPointToLine(new Point(12, 5)));

        // Case 3: Nearest point is the second point of the line (10/0)
        //   (0/0)                          (10/0)
        //     o-----------------------------------o---x (12/0)
        System.out.println("Case 3 : testGetNearestPointToHorizontalLine");
        assertEquals(new Point(10, 0), line.getNearestPointToLine(new Point(12, 0)));

        // Case 4: Nearest point is intersection point P(2/0) of normal line
        //   (0/0)    (2/0)                      (10/0)
        //     o--------x--------------------------o
        System.out.println("Case 4 : testGetNearestPointToHorizontalLine");
        assertEquals(new Point(2, 0), line.getNearestPointToLine(new Point(2, 0)));
    }

    @Test
    public void testGetNearestPointToVerticalLine() {
        Line line = new Line(new Point(0, 0), new Point(0, 10));
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
        System.out.println("Case 1 : testGetNearestPointToVerticalLine");
        assertEquals(new Point(0, 2), line.getNearestPointToLine(new Point(5, 2)));
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
        System.out.println("Case 2 : testGetNearestPointToVerticalLine");
        assertEquals(new Point(0, 10), line.getNearestPointToLine(new Point(5, 12)));
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
        System.out.println("Case 3 : testGetNearestPointToVerticalLine");
        assertEquals(new Point(0, 10), line.getNearestPointToLine(new Point(0, 12)));
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
        System.out.println("Case 4 : testGetNearestPointToVerticalLine");
        assertEquals(new Point(0, 2), line.getNearestPointToLine(new Point(0, 2)));
    }

    @Test
    public void testGetNearestPointToLine() {
        Line line = new Line(new Point(1, 2), new Point(5, 10));
        /*
        Case 1 Nearest point is intersection point of normal line
            (5/10)o
                 /
                /
               /   x(4/4)
              /
        (1/2)o
         */
        System.out.println("Case 1 : testGetNearestPointToLine");
        assertEquals(new Point(2.4, 4.8), line.getNearestPointToLine(new Point(4, 4)));
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
        System.out.println("Case 2 : testGetNearestPointToLine");
        assertEquals(new Point(5, 10), line.getNearestPointToLine(new Point(9, 14)));
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
        System.out.println("Case 3 : testGetNearestPointToLine");
        assertEquals(new Point(5, 10), line.getNearestPointToLine(new Point(7, 14)));
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
        System.out.println("Case 4 : testGetNearestPointToLine");
        assertEquals(new Point(4, 8), line.getNearestPointToLine(new Point(4, 8)));
    }

}
