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
        // (100,100) (200,100) (300,100)                     (600,100)
        //     o---------o         o-----------------------------o
        //     |         |         |                             |
        //     |    s    |         |     (400,200) (500,200)     |
        //     |(150,150)|         |         o---------o         |
        //     |         |         |         |         |         |
        //     |         o---------o         |         |         |
        //     |     (200,400) (300,400)     |         |    e    |
        //     |                             |         |(550,450)|
        //     o-----------------------------o         o---------o
        // (100,500)                     (400,500) (500,500) (600,500)
        PathFinder p = new PathFinder(new Area(
                new Point(100, 100),
                new Point(200, 100),
                new Point(200, 400),
                new Point(300, 400),
                new Point(300, 100),
                new Point(600, 100),
                new Point(600, 500),
                new Point(500, 500),
                new Point(500, 200),
                new Point(400, 200),
                new Point(400, 500),
                new Point(100, 500)
        ));
        p.setAreaTriangles(
                new Triangle(new Point(100, 500), new Point(100, 100), new Point(200, 100)),
                new Triangle(new Point(100, 500), new Point(200, 100), new Point(200, 400)),
                new Triangle(new Point(100, 500), new Point(200, 400), new Point(300, 400)),
                new Triangle(new Point(100, 500), new Point(300, 400), new Point(400, 500)),
                new Triangle(new Point(300, 400), new Point(300, 100), new Point(400, 200)),
                new Triangle(new Point(300, 400), new Point(400, 200), new Point(400, 500)),
                new Triangle(new Point(300, 100), new Point(600, 100), new Point(500, 200)),
                new Triangle(new Point(300, 100), new Point(500, 200), new Point(400, 200)),
                new Triangle(new Point(600, 100), new Point(600, 500), new Point(500, 500)),
                new Triangle(new Point(600, 100), new Point(500, 500), new Point(500, 200))
        );
        Point s = new Point(150, 150);
        Point d = new Point(550, 450);
        p.setStartAndEndpoint(s, d);
        new TestGUI(p);
    }
}
