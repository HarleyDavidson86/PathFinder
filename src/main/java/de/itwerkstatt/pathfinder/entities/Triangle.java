package de.itwerkstatt.pathfinder.entities;

/**
 * A triangle area defined by three points
 * @author dsust
 */
public record Triangle(Point p1, Point p2, Point p3) {

    /**
     * Calculates the area of this triangle
     * @return double
     */
    private double calculateArea() {
        return 0.5d * ((p1.x() - p3.x()) * (p2.y() - p3.y()) - (p2.x() - p3.x()) * (p1.y() - p3.y()));
    }

    /**
     * Checks whether the point is located in the triangle.<br>
     * The method calculates the area of the three 
     * triangles (p,tri.p1,tri.p2),(p,tri.p2,tri.p3),(p,tri.p3,tri.p1) 
     * If the areas of the triangles are all 
     * positive or all negative, the point is located inside the 
     * triangle or on an edge of it.
     *
     * @param p
     * @return true, if point is located inside the triangle
     */
    public boolean isPointInTriangle(Point p) {
        System.out.println("Startpunkt: " + p + ", Dreieck: " + this);

        // The order of the points is important and must always 
        // have the same direction.
        double areaTri1 = new Triangle(p, p1, p2).calculateArea();
        double areaTri2 = new Triangle(p, p2, p3).calculateArea();
        double areaTri3 = new Triangle(p, p3, p1).calculateArea();
        System.out.printf("Areas: %f %f %f\n", areaTri1, areaTri2, areaTri3);

        // Check if positive and negative areas present
        boolean has_neg_areas = (areaTri1 < 0d) || (areaTri2 < 0d) || (areaTri3 < 0d);
        boolean has_pos_areas = (areaTri1 > 0d) || (areaTri2 > 0d) || (areaTri3 > 0d);
        return !(has_neg_areas && has_pos_areas);
    }

}
