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
        // (100,100)    (250,100)          (500,100)
        //     o------o     o------------------o
        //     |      |     |     (400,200)    |
        //     |      |     |    o----o        |
        //     |   s  |     | e  |    |        |
        //     |      |     o----o    |        |
        //     |      |               |        |
        //     |      o---------------o        |
        //     |  (200,400)       (400,400)    |
        //     o-------------------------------o
        // (100,500)                       (500,500)
        PathFinder p = new PathFinder(new Area(
                new Point(100, 100),
                new Point(200, 100),
                new Point(200, 400),
                new Point(400, 400),
                new Point(400, 200),
                new Point(350, 200),
                new Point(350, 350),
                new Point(250, 350),
                new Point(250, 100),
                new Point(500, 100),
                new Point(500, 500),
                new Point(100, 500)
        ));
        p.setAreaTriangles(
                new Triangle(new Point(100, 100), new Point(200, 100), new Point(200, 400)),
                new Triangle(new Point(100, 100), new Point(200, 400), new Point(100, 500)),
                new Triangle(new Point(100, 500), new Point(200, 400), new Point(400, 400)),
                new Triangle(new Point(100, 500), new Point(400, 400), new Point(500, 500)),
                new Triangle(new Point(500, 500), new Point(400, 400), new Point(400, 200)),
                new Triangle(new Point(500, 500), new Point(400, 200), new Point(500, 100)),
                new Triangle(new Point(500, 100), new Point(400, 200), new Point(350, 200)),
                new Triangle(new Point(500, 100), new Point(350, 200), new Point(250, 100)),
                new Triangle(new Point(250, 100), new Point(350, 200), new Point(350, 350)),
                new Triangle(new Point(250, 100), new Point(350, 350), new Point(250, 350))
        );
        Point s = new Point(150, 150);
        Point d = new Point(300, 300);
        p.setStartAndEndpoint(s, d);
        new TestGUI(p);
    }
}
