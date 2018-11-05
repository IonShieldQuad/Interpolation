package graphics;

import math.InterpolationException;
import math.Interpolator;
import math.PointDouble;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GraphDisplay extends JPanel {
    private static final int MARGIN_X = 50;
    private static final int MARGIN_Y = 50;
    private static final double EXTRA_AMOUNT = 0.2;
    private static final Color GRID_COLOR = Color.GRAY;
    private static final Color GRAPH_COLOR = new Color(0x5bcefa);
    private static final Color POINT_COLOR = Color.YELLOW;
    private static final int POINT_SIZE = 5;
    
    private Interpolator interpolator;
    private List<PointDouble> points = new ArrayList<>();
    
    public GraphDisplay() {
        super();
    }
    
    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }
    
    public void setPoints(List<PointDouble> points) {
        this.points = new ArrayList<>();
        points.forEach(p -> this.points.add(new PointDouble(p.getX(), p.getY())));
        this.points.sort(Comparator.comparing(PointDouble::getX));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (interpolator != null) {
            drawGraph(g);
        }
        drawPoints(g);
        drawGrid(g);
    }
    
    private void drawPoints(Graphics g) {
        g.setColor(POINT_COLOR);
        points.stream().map(this::valueToGraph).forEach(p -> g.drawOval((int)Math.round(p.getX()) - POINT_SIZE / 2, (int)Math.round(p.getY()) - POINT_SIZE / 2, POINT_SIZE, POINT_SIZE));
    }
    
    private void drawGraph(Graphics g) {
        g.setColor(GRAPH_COLOR);
        int prev = 0;
        for (int i = 0; i < graphWidth(); i++) {
            try {
                PointDouble val = graphToValue(new PointDouble(i + MARGIN_X, 0));
                val = new PointDouble(val.getX(), interpolator.evaluate(val.getX()));
                val = valueToGraph(val);
                if (i != 0) {
                    g.drawLine(MARGIN_X + i - 1, prev, (int)Math.round(val.getX()), (int)Math.round(val.getY()));
                }
                prev = (int)Math.round(val.getY());
            } catch (InterpolationException ignored) {}
        }
    }
    
    private void drawGrid(Graphics g) {
        g.setColor(GRID_COLOR);
        g.drawLine(MARGIN_X, getHeight() - MARGIN_Y, getWidth() - MARGIN_X, getHeight() - MARGIN_Y);
        g.drawLine(MARGIN_X, MARGIN_Y + (int)(graphHeight() * (1 - EXTRA_AMOUNT)), getWidth() - MARGIN_X, MARGIN_Y + (int)(graphHeight() * (1 - EXTRA_AMOUNT)));
        g.drawLine(MARGIN_X, MARGIN_Y + (int)(graphHeight() * EXTRA_AMOUNT), getWidth() - MARGIN_X, MARGIN_Y + (int)(graphHeight() * EXTRA_AMOUNT));
        
        g.drawLine(MARGIN_X, getHeight() - MARGIN_Y, MARGIN_X, MARGIN_Y);
        g.drawLine(MARGIN_X + (int)(graphWidth() * EXTRA_AMOUNT), getHeight() - MARGIN_Y, MARGIN_X + (int)(graphWidth() * EXTRA_AMOUNT), MARGIN_Y);
        g.drawLine(MARGIN_X + (int)(graphWidth() * (1 - EXTRA_AMOUNT)), getHeight() - MARGIN_Y, MARGIN_X + (int)(graphWidth() * (1 - EXTRA_AMOUNT)), MARGIN_Y);
        
        g.drawString(Double.toString(lowerX()), MARGIN_X + (int)(graphWidth() * EXTRA_AMOUNT), getHeight() - MARGIN_Y / 2);
        g.drawString(Double.toString(upperX()), MARGIN_X + (int)(graphWidth() * (1 - EXTRA_AMOUNT)), getHeight() - MARGIN_Y / 2);
        g.drawString(Double.toString(lowerY()), MARGIN_X / 4, MARGIN_Y + (int)(graphHeight() * (1 - EXTRA_AMOUNT)));
        g.drawString(Double.toString(upperY()), MARGIN_X / 4, MARGIN_Y + (int)(graphHeight() * EXTRA_AMOUNT));
    }
    
    private int graphWidth() {
        return getWidth() - 2 * MARGIN_X;
    }
    
    private int graphHeight() {
        return getHeight() - 2 * MARGIN_Y;
    }
    
    private double lowerX() {
        if (points.size() == 0) {
            return 0;
        }
        return points.get(0).getX();
    }
    
    private double upperX() {
        if (points.size() == 0) {
            return 0;
        }
        return points.get(points.size() - 1).getX();
    }
    
    private double lowerY() {
        if (points.size() == 0) {
            return 0;
        }
        return points.stream().map(PointDouble::getY).reduce(points.get(0).getY(), Math::min);
    }
    
    private double upperY() {
        if (points.size() == 0) {
            return 0;
        }
        return points.stream().map(PointDouble::getY).reduce(points.get(0).getY(), Math::max);
    }
    
    private PointDouble valueToGraph(PointDouble point) {
        double valX = (point.getX() - lowerX()) / (upperX() - lowerX());
        double valY = (point.getY() - lowerY()) / (upperY() - lowerY());
        return new PointDouble(MARGIN_X + (int)((graphWidth() * EXTRA_AMOUNT) * (1 - valX) + (graphWidth() * (1 - EXTRA_AMOUNT)) * valX), getHeight() - MARGIN_Y - (int)((graphHeight() * EXTRA_AMOUNT) * (1 - valY) + (graphHeight() * (1 - EXTRA_AMOUNT)) * valY));
    }
    
    private PointDouble graphToValue(PointDouble point) {
        double valX = (point.getX() - (MARGIN_X + (graphWidth() * EXTRA_AMOUNT))) / ((MARGIN_X + (graphWidth() * (1 - EXTRA_AMOUNT))) - (MARGIN_X + (graphWidth() * EXTRA_AMOUNT)));
        double valY = (point.getY() - (MARGIN_Y + (graphHeight() * (1 - EXTRA_AMOUNT)))) / ((MARGIN_Y + (graphHeight() * EXTRA_AMOUNT)) - (MARGIN_Y + (graphHeight() * (1 - EXTRA_AMOUNT))));
        return new PointDouble(lowerX() * (1 - valX) + upperX() * valX, lowerY() * (1 - valY) + upperY() * valY);
    }
}
