package de.itwerkstatt.pathfinder.util;

import de.itwerkstatt.pathfinder.entities.Area;
import de.itwerkstatt.pathfinder.entities.Point;
import de.itwerkstatt.pathfinder.entities.Triangle;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    private final int POINT_SIZE = 8;

    public enum Filter {
        AREA, TRIANGLES, PATH, POINTS;

        private Filter() {
            visible = true;
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
    //</editor-fold>

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, getWidth(), getHeight());
        //Draw Area
        if (area != null && Filter.AREA.isVisible()) {
            g2d.setStroke(new BasicStroke(3));
            drawPointArray(g2d, area.points(), false);
            //Points of area
            if (Filter.POINTS.isVisible()) {
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
                drawPointArray(g2d, new Point[]{tri.p1(), tri.p2(), tri.p3()}, false);
            }
            //Points of area
            if (Filter.POINTS.isVisible()) {
                for (Point point : area.points()) {
                    drawPoint(g2d, "", (int) point.x(), (int) point.y());
                }
            }
        }

        g2d.setColor(Color.BLACK);
        if (startPoint != null && Filter.POINTS.isVisible()) {
            drawPoint(g2d, "Start", (int) startPoint.x(), (int) startPoint.y());
        }
        if (endPoint != null && Filter.POINTS.isVisible()) {
            drawPoint(g2d, "End", (int) endPoint.x(), (int) endPoint.y());
        }

        //Path
        if (path != null) {
            g2d.setColor(Color.BLUE);
            drawPointArray(g2d, path, true);
            if (Filter.POINTS.isVisible()) {
                for (Point point : path) {
                    drawPoint(g2d, "", (int) point.x(), (int) point.y());
                }
            }
        }
    }

    private void drawPointArray(Graphics2D g2d, Point[] points, boolean asLine) {
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            Point point = points[i];
            xPoints[i] = (int) point.x();
            yPoints[i] = (int) point.y();
        }
        if (asLine) {
            g2d.drawPolyline(xPoints, yPoints, xPoints.length);
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
