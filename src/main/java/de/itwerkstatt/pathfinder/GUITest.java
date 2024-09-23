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
        PathFinder p = new PathFinder(new Area(
                new Point(100, 100),
                new Point(200, 100),
                new Point(250, 200),
                new Point(300, 100),
                new Point(400, 100),
                new Point(400, 300),
                new Point(100, 300)
        ));
        p.setAreaTriangles(
                new Triangle(new Point(100, 300), new Point(100, 100), new Point(200, 100)),
                new Triangle(new Point(100, 300), new Point(200, 100), new Point(250, 200)),
                new Triangle(new Point(100, 300), new Point(250, 200), new Point(400, 300)),
                new Triangle(new Point(400, 300), new Point(250, 200), new Point(300, 100)),
                new Triangle(new Point(400, 300), new Point(300, 100), new Point(400, 100))
        );
        Point s = new Point(150,160);
        Point d = new Point(350,160);
        p.setStartAndEndpoint(s, d);
        new TestGUI(p);
    }
}
