package de.itwerkstatt.pathfinder;

import de.itwerkstatt.pathfinder.entities.Area;
import de.itwerkstatt.pathfinder.entities.Point;
import de.itwerkstatt.pathfinder.entities.Triangle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author dsust
 */
public class PathFinderTest {

    record TriangleCreationTestcase(Area area, Triangle[] expectedTriangles) {

    }

    @Test
    public void testAreaTriangleCreation() {
        TriangleCreationTestcase[] testcases = new TriangleCreationTestcase[]{
            //Case 1
            // (0/10)  
            //   o----o (10/10)
            //   |   /
            //   |  /
            //   | /
            //   |/
            //   o
            // (0/0)
            new TriangleCreationTestcase(
            new Area(
            new Point(0, 0),
            new Point(0, 10),
            new Point(10, 10)),
            new Triangle[]{
                new Triangle(new Point(0, 0), new Point(0, 10), new Point(10, 10))
            }),
            //Case 2
            // (0/10)         (10/10)
            //   o---------------o
            //   |               |
            //   |               |
            //   |               |
            //   |               |
            //   o---------------o
            // (0/0)           (10/0)
            new TriangleCreationTestcase(
            new Area(
            new Point(0, 0),
            new Point(0, 10),
            new Point(10, 10),
            new Point(10, 0)),
            new Triangle[]{
                new Triangle(new Point(0, 0), new Point(0, 10), new Point(10, 10)),
                new Triangle(new Point(0, 0), new Point(10, 10), new Point(10, 0))
            }),
            //Case 3
            // (0/10)         (10/10)   (15/10)
            //   o---------------o---------o
            //   |                        /
            //   |                       /
            //   |                      /
            //   |                     /
            //   o--------------------o
            // (0/0)               (13/0)
            new TriangleCreationTestcase(
            new Area(
            new Point(0, 0),
            new Point(0, 10),
            new Point(10, 10),
            new Point(15, 10),
            new Point(13, 0)),
            new Triangle[]{
                new Triangle(new Point(0, 0), new Point(0, 10), new Point(10, 10)),
                new Triangle(new Point(0, 0), new Point(10, 10), new Point(15, 10)),
                new Triangle(new Point(0, 0), new Point(15, 10), new Point(13, 0))
            })
        };

        for (int caseNumber = 0; caseNumber < testcases.length; caseNumber++) {
            System.out.println("Test case #" + (caseNumber + 1));
            TriangleCreationTestcase testcase = testcases[caseNumber];
            PathFinder p = new PathFinder(testcase.area);
            assertEquals(testcase.expectedTriangles.length, p.getAreaTriangles().length);
            for (int i = 0; i < testcase.expectedTriangles.length; i++) {
                assertEquals(testcase.expectedTriangles[i], p.getAreaTriangles()[i]);
            }
        }
    }

    record FindPathTestcase(Area area, Triangle[] trianglesOfArea, Point start, Point end, Point[] expectedPath) {

    }

    @Test
    public void testFindPath() {

        FindPathTestcase[] testcases = new FindPathTestcase[]{
            // Case 1
            // 
            // (100,100)  (200,100) (300,100)  (400,100)
            //     o----------o         o----------o
            //     |           \       /           |
            //     |            \     /            |
            //     |     s       \   /      e      |
            //     | (150,160)    \ /   (350,160)  |
            //     |               o               |
            //     |           (250,200)           |
            //     |                               |
            //     o-------------------------------o
            // (100,300)                       (400,300)
            new FindPathTestcase(new Area(
            new Point(100, 100),
            new Point(200, 100),
            new Point(250, 200),
            new Point(300, 100),
            new Point(400, 100),
            new Point(400, 300),
            new Point(100, 300)
            ), new Triangle[]{
                new Triangle(new Point(100, 300), new Point(100, 100), new Point(200, 100)),
                new Triangle(new Point(100, 300), new Point(200, 100), new Point(250, 200)),
                new Triangle(new Point(100, 300), new Point(250, 200), new Point(400, 300)),
                new Triangle(new Point(400, 300), new Point(250, 200), new Point(300, 100)),
                new Triangle(new Point(400, 300), new Point(300, 100), new Point(400, 100))
            }, new Point(150, 160), new Point(350, 160), new Point[]{
                new Point(150, 160), new Point(250, 200),new Point(350, 160)
            })
        };

        for (int caseNumber = 0; caseNumber < testcases.length; caseNumber++) {
            System.out.println("Test case #" + (caseNumber + 1));
            FindPathTestcase testcase = testcases[caseNumber];
            PathFinder p = new PathFinder(testcase.area);
            p.setAreaTriangles(testcase.trianglesOfArea);
            p.setStartAndEndpoint(testcase.start, testcase.end);
            Point[] resultPath = p.findPath();
            assertEquals(testcase.expectedPath.length, resultPath.length);
            for (int i = 0; i < resultPath.length; i++) {
                assertEquals(testcase.expectedPath[i], resultPath[i]);
            }
        }
    }

}
