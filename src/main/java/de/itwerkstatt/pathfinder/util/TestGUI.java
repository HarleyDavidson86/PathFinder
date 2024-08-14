package de.itwerkstatt.pathfinder.util;

import de.itwerkstatt.pathfinder.PathFinder;
import de.itwerkstatt.pathfinder.entities.Point;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.stream.Stream;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Small TestGUI to check generated area and triangles
 * Start and end point can be set by clicking on the frame
 * @author dsust
 */
public class TestGUI {

    private final PathFinder pathFinder;

    private final JFrame frame;

    private final CanvasPanel canvas;

    public TestGUI(PathFinder p) {
        this.pathFinder = p;

        frame = new JFrame("PathFinder Test GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        canvas = new CanvasPanel();
        canvas.setMinimumSize(new Dimension(
                Stream.of(pathFinder.getArea().points()).mapToInt(point -> (int) point.x()).max().getAsInt() + 100,
                Stream.of(pathFinder.getArea().points()).mapToInt(point -> (int) point.y()).max().getAsInt() + 100
        ));
        canvas.setPreferredSize(canvas.getMinimumSize());
        canvas.setArea(p.getArea());
        canvas.setTriangles(p.getAreaTriangles());
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point[] points = canvas.setPoint(new Point(e.getX(), e.getY()));
                if (points != null) {
                    p.setStartAndEndpoint(points[0], points[1]);
                    canvas.setPath(p.findPath());
                } else {
                    canvas.setPath(null);
                }
            }
        });
        
        try {
            canvas.setPath(p.findPath());
        } catch (IllegalArgumentException ignore) {
            //No start and endpoint available yet
        }

        frame.add(canvas, BorderLayout.CENTER);
        frame.add(setupFilterCheckboxes(), BorderLayout.EAST);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel setupFilterCheckboxes() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        for (CanvasPanel.Filter f : CanvasPanel.Filter.values()) {
            JCheckBox c = new JCheckBox(f.name());
            c.addItemListener(canvas);
            c.setSelected(true);
            p.add(c);
        }
        return p;
    }

}
