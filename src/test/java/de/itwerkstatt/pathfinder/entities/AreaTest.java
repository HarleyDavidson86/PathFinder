package de.itwerkstatt.pathfinder.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author dsust
 */
public class AreaTest {

    @Test
    public void testCalculateNearestPointToArea() {
        Area a = new Area(
                new Point(0, 0),
                new Point(0, 10),
                new Point(10, 10),
                new Point(10, 0)
        );
        //Case 1
        // (0/10)         (10/10)
        //   o---------------o
        //   |               |
        //   |               |      X(15/6)
        //   |               |
        //   |               |
        //   o---------------o
        // (0/0)           (10/0)
        assertEquals(new Point(10, 6), a.calculateNearestPointToArea(new Point(15, 6)));

        //Case 2
        // (0/10)         (10/10)   X(15/12)
        //   o---------------o
        //   |               |
        //   |               |      
        //   |               |
        //   |               |
        //   o---------------o
        // (0/0)           (10/0)
        assertEquals(new Point(10, 10), a.calculateNearestPointToArea(new Point(15, 12)));

        //Case 3
        // (0/10)         (10/10)
        //   o---------------o
        //   |               |
        //   |               X(10/8)   
        //   |               |
        //   |               |
        //   o---------------o
        // (0/0)           (10/0)
        assertEquals(new Point(10, 8), a.calculateNearestPointToArea(new Point(10, 8)));

        //Case 4
        // (0/10)         (10/10)
        //   o---------------o
        //   |               |
        //   | X(1/6)        |
        //   |               |
        //   |               |
        //   o---------------o
        // (0/0)           (10/0)
        assertEquals(new Point(0, 6), a.calculateNearestPointToArea(new Point(1, 6)));
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
        Point s = new Point(260,120);
        Point d = new Point(140,240);
        assertEquals(new Point(220,140), a.calculateDirectionalNearestPointToArea(s, d));
    }
}
