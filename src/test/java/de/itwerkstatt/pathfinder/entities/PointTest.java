package de.itwerkstatt.pathfinder.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PointTest {
    
    @Test
    public void testDistanceTo() {
        Point start = new Point(0, 0);
        assertEquals(1, start.distanceTo(new Point(1, 0)));
        assertEquals(1, start.distanceTo(new Point(-1, 0)));
        assertEquals(1, start.distanceTo(new Point(0, 1)));
        assertEquals(1, start.distanceTo(new Point(0, -1)));
        assertEquals(0, start.distanceTo(new Point(0, 0)));
    }
    
}
