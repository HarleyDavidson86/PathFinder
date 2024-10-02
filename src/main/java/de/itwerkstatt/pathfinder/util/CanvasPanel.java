package de.itwerkstatt.pathfinder.util;

import de.itwerkstatt.pathfinder.entities.Area;
import de.itwerkstatt.pathfinder.entities.Node;
import de.itwerkstatt.pathfinder.entities.Point;
import de.itwerkstatt.pathfinder.entities.Triangle;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author dsust
 */
public class CanvasPanel extends JPanel implements ItemListener {

    private Area area;
    private Triangle[] triangles;
    private Point startPoint;
    private Point endPoint;
    private Point[] path;
    private List<Node> nodes;

    private final int POINT_SIZE = 8;

    private int highlightedPathPoint = -1;

    public enum Filter {
        AREA(true),
        AREA_POINTS(false),
        TRIANGLES(false),
        TRIANGLE_POINTS(false),
        PATH(true),
        PATH_POINTS(false),
        START_END_POINTS(false),
        NODE_MESH(false);

        private Filter(boolean isVisible) {
            visible = isVisible;
        }

        private boolean visible;

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

    }

    //<editor-fold desc="Setter methods">
    public void setArea(Area area) {
        this.area = area;
    }

    public void setTriangles(Triangle[] triangles) {
        this.triangles = triangles;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public void setPath(Point[] path) {
        this.path = path;
    }

    void setHighlightedPathPointIndex(int newIndex) {
        highlightedPathPoint = newIndex;
        SwingUtilities.invokeLater(() -> repaint());
    }

    void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
    //</editor-fold>

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, getWidth(), getHeight());
        //Draw Area
        if (area != null && Filter.AREA.isVisible()) {
            g2d.setStroke(new BasicStroke(3));
            drawPolygon(g2d, area.points(), false);
            //Points of area
            if (Filter.AREA_POINTS.isVisible()) {
                for (Point point : area.points()) {
                    drawPoint(g2d, "", (int) point.x(), (int) point.y());
                }
            }
        }

        //Triangles
        if (triangles != null && Filter.TRIANGLES.isVisible()) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2));
            for (Triangle tri : triangles) {
                drawPolygon(g2d, new Point[]{tri.p1(), tri.p2(), tri.p3()}, true);
            }
            g2d.setColor(Color.RED);
            //Points of area
            if (Filter.TRIANGLE_POINTS.isVisible()) {
                for (Point point : Stream.of(triangles).flatMap(t -> Stream.of(t.p1(), t.p2(), t.p3())).toList()) {
                    drawPoint(g2d, "", (int) point.x(), (int) point.y());
                }
            }
        }

        g2d.setColor(Color.BLACK);
        if (startPoint != null && Filter.START_END_POINTS.isVisible()) {
            drawPoint(g2d, "Start", (int) startPoint.x(), (int) startPoint.y());
        }
        if (endPoint != null && Filter.START_END_POINTS.isVisible()) {
            drawPoint(g2d, "End", (int) endPoint.x(), (int) endPoint.y());
        }

        //Path
        if (path != null && Filter.PATH.isVisible()) {
            g2d.setColor(Color.BLUE);
            drawLine(g2d, path);
            if (Filter.PATH_POINTS.isVisible()) {
                for (Point point : path) {
                    drawPoint(g2d, "", (int) point.x(), (int) point.y());
                }
            }
            if (highlightedPathPoint > -1) {
                drawPoint(g2d, "", (int) path[highlightedPathPoint].x(), (int) path[highlightedPathPoint].y());
            }
        }

        //Nodes
        if (nodes != null && Filter.NODE_MESH.isVisible()) {
            g2d.setColor(Color.ORANGE);
            List<Point> alreadyPainted = new ArrayList<>();
            for (Node node : nodes) {
                if (!alreadyPainted.contains(node.getPoint())) {
                    alreadyPainted.add(node.getPoint());
                    for (Node.NeighbourNode directNeighbour : node.getDirectNeighbours()) {
                        if (!alreadyPainted.contains(directNeighbour.node().getPoint())){
                            drawLine(g2d, new Point[]{node.getPoint(), directNeighbour.node().getPoint()});
                        }
                    }
            
                }
            }
        }
    }

    private void drawLine(Graphics2D g2d, Point[] points) {
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            Point point = points[i];
            xPoints[i] = (int) point.x();
            yPoints[i] = (int) point.y();
        }
        g2d.drawPolyline(xPoints, yPoints, xPoints.length);
    }

    private void drawPolygon(Graphics2D g2d, Point[] points, boolean fillWithRandomColor) {
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            Point point = points[i];
            xPoints[i] = (int) point.x();
            yPoints[i] = (int) point.y();
        }
        if (fillWithRandomColor) {
            g2d.setColor(new Color(
                    ThreadLocalRandom.current().nextInt(255),
                    ThreadLocalRandom.current().nextInt(255),
                    ThreadLocalRandom.current().nextInt(255)));
            g2d.fillPolygon(xPoints, yPoints, xPoints.length);
        } else {
            g2d.drawPolygon(xPoints, yPoints, xPoints.length);
        }
    }

    private void drawPoint(Graphics2D g2d, String name, int x, int y) {
        g2d.fillOval(x - POINT_SIZE / 2, y - POINT_SIZE / 2, POINT_SIZE, POINT_SIZE);
        g2d.drawString(String.format("%s (%d/%d)", name, x, y), x + POINT_SIZE, y - POINT_SIZE);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        boolean selected = e.getStateChange() == ItemEvent.SELECTED;
        JCheckBox c = (JCheckBox) e.getItem();
        Filter.valueOf(c.getText()).setVisible(selected);
        SwingUtilities.invokeLater(() -> repaint());
    }

    public Point[] setPoint(Point p) {
        Point[] res = null;
        if (startPoint == null) {
            startPoint = p;
        } else if (startPoint != null && endPoint == null) {
            endPoint = p;
            res = new Point[2];
            res[0] = startPoint;
            res[1] = endPoint;
        } else {
            startPoint = p;
            endPoint = null;
        }
        SwingUtilities.invokeLater(() -> repaint());
        return res;
    }
}
