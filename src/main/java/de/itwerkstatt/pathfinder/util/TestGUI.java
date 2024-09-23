package de.itwerkstatt.pathfinder.util;

import de.itwerkstatt.pathfinder.PathFinder;
import de.itwerkstatt.pathfinder.entities.Point;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.stream.Stream;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Small TestGUI to check generated area and triangles Start and end point can
 * be set by clicking on the frame
 *
 * @author dsust
 */
public class TestGUI {

    private final PathFinder pathFinder;

    private final JFrame frame;

    private final CanvasPanel canvas;
    private JList<String> pathPointList;

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
        canvas.setStartPoint(p.getStartPoint());
        canvas.setEndPoint(p.getEndPoint());
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point[] points = canvas.setPoint(new Point(e.getX(), e.getY()));
                if (points != null) {
                    p.setStartAndEndpoint(points[0], points[1]);
                    Point[] path = p.findPath();
                    canvas.setPath(path);
                    populatePathPointList(path);
                } else {
                    canvas.setPath(null);
                    ((DefaultListModel) pathPointList.getModel()).removeAllElements();
                }
            }
        });

        frame.add(canvas, BorderLayout.CENTER);
        frame.add(setupFilterCheckboxes(), BorderLayout.EAST);
        frame.add(setupPathList(), BorderLayout.WEST);

        try {
            Point[] path = p.findPath();
            canvas.setPath(path);
            populatePathPointList(path);
        } catch (IllegalArgumentException ignore) {
            //No start and endpoint available yet
        }

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void populatePathPointList(Point[] path) {
        DefaultListModel listModel = (DefaultListModel) pathPointList.getModel();
        listModel.removeAllElements();
        for (int i = 0; i < path.length; i++) {
            listModel.addElement(String.format("%d: (%s/%s)", i + 1, Double.toString(path[i].x()), Double.toString(path[i].y())));
        }
    }

    private JPanel setupFilterCheckboxes() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        for (CanvasPanel.Filter f : CanvasPanel.Filter.values()) {
            JCheckBox c = new JCheckBox(f.name());
            c.addItemListener(canvas);
            c.setSelected(f.isVisible());
            p.add(c);
        }
        return p;
    }

    private Component setupPathList() {
        JPanel p = new JPanel();
        pathPointList = new JList<>(new DefaultListModel());
        pathPointList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                canvas.setHighlightedPathPointIndex(pathPointList.getSelectedIndex());
            }
        });
        pathPointList.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                    ke.consume();
                    if (pathPointList.getSelectedIndex() == pathPointList.getModel().getSize() - 1) {
                        pathPointList.setSelectedIndex(0);
                    } else {
                        pathPointList.setSelectedIndex(pathPointList.getSelectedIndex() + 1);
                    }
                    canvas.setHighlightedPathPointIndex(pathPointList.getSelectedIndex());
                }
                if (ke.getKeyCode() == KeyEvent.VK_UP) {
                    ke.consume();
                    if (pathPointList.getSelectedIndex() == 0) {
                        pathPointList.setSelectedIndex(pathPointList.getModel().getSize() - 1);
                    } else {
                        pathPointList.setSelectedIndex(pathPointList.getSelectedIndex() - 1);
                    }
                    canvas.setHighlightedPathPointIndex(pathPointList.getSelectedIndex());
                }
            }
        });
        p.setPreferredSize(new Dimension(100, 500));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JScrollPane(pathPointList));
        return p;
    }

}
