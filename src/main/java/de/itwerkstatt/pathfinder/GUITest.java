package de.itwerkstatt.pathfinder;

import de.itwerkstatt.pathfinder.entities.Area;
import de.itwerkstatt.pathfinder.entities.Point;
import de.itwerkstatt.pathfinder.entities.Triangle;
import de.itwerkstatt.pathfinder.util.TestGUI;
import java.io.IOException;

/**
 *
 * @author dsust
 */
public class GUITest {
    public static void main(String[] args) throws InterruptedException, IOException {
        // (100,100) (200,100) (300,100) (400,100) (500,100) (600,100)
        //     o---------o         o---------o         o---------o
        //     |         |         |         |         |         |
        //     |    s    |         |         |         |    e    |
        //     |(150,150)|         |         |         |(550,150)|
        //     |         |         |         |         |         |
        //     |         o---------o         o---------o         |
        //     |     (200,200) (300,200) (400,200) (500,200)     |
        //     |                                                 |
        //     o-------------------------------------------------o
        // (100,300)                                         (600,300)
        PathFinder p = new PathFinder(new Area(
                new Point(100, 100),
                new Point(200, 100),
                new Point(200, 200),
                new Point(300, 200),
                new Point(300, 100),
                new Point(400, 100),
                new Point(400, 200),
                new Point(500, 200),
                new Point(500, 100),
                new Point(600, 100),
                new Point(600, 300),
                new Point(100, 300)
        ));
        p.setAreaTriangles(
                new Triangle(new Point(100, 300), new Point(100, 100), new Point(200, 100)),
                new Triangle(new Point(100, 300), new Point(200, 100), new Point(200, 200)),
                new Triangle(new Point(100, 300), new Point(200, 200), new Point(300, 200)),
                new Triangle(new Point(100, 300), new Point(300, 200), new Point(400, 200)),
                new Triangle(new Point(100, 300), new Point(400, 200), new Point(500, 200)),
                new Triangle(new Point(100, 300), new Point(500, 200), new Point(600, 300)),
                new Triangle(new Point(300, 200), new Point(300, 100), new Point(400, 100)),
                new Triangle(new Point(300, 200), new Point(400, 100), new Point(400, 200)),
                new Triangle(new Point(500, 200), new Point(500, 100), new Point(600, 100)),
                new Triangle(new Point(500, 200), new Point(600, 100), new Point(600, 300))
        );
        Point s = new Point(150,150);
        Point d = new Point(550,150);
        p.setStartAndEndpoint(s, d);
        new TestGUI(p);
    }
}
