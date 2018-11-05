package core;

import graphics.GraphDisplay;
import math.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainWindow {
    private JTable points;
    private JPanel rootPanel;
    private JTextArea log;
    private JButton addPointButton;
    private JTextField addX;
    private JTextField addY;
    private JButton buildButton;
    private JTextField calcX;
    private JButton calculateButton;
    private JButton clearButton;
    private GraphDisplay graph;
    
    private Interpolator interpolator;
    
    private MainWindow() {
        initComponents();
    }
    
    private void initComponents() {
        initTableModel();
        addPointButton.addActionListener(e -> addPoint(addX.getText(), addY.getText()));
        clearButton.addActionListener(e -> initTableModel());
        buildButton.addActionListener(e -> makeInterpolator());
        calculateButton.addActionListener(e -> interpolateValue(calcX.getText()));
    }
    
    private void interpolateValue(String text) {
        if (interpolator == null) {
            log.append("\nError: Interpolator is null");
        }
        try {
            double value = Double.parseDouble(text);
            double result = interpolator.evaluate(value);
            log.append("\nValue at x = " + value + " is " + result);
        }
        catch (NumberFormatException e) {
            log.append("\nInvalid input value");
        } catch (InterpolationException e) {
            log.append("\nError: " + e.getMessage());
        }
    }
    
    private void makeInterpolator() {
        InterpolatorFactory factory = getInterpolatorFactory();
        
        try {
            log.setText("");
            interpolator = factory.makeInterpolator(getPointList());
            log.append("\nInterpolator generated");
            updateGraph();
        }
        catch (InterpolationException e) {
            log.append("\n" + e.getMessage());
        }
        catch (NumberFormatException e) {
            log.append("\nInvalid input format");
        }
    }
    
    private void updateGraph() {
        graph.setInterpolator(interpolator);
        graph.setPoints(getPointList());
        graph.repaint();
    }
    
    private List<PointDouble> getPointList() {
        List<PointDouble> list = new ArrayList<>();
        for (int i = 0; i < points.getModel().getRowCount(); i++) {
        
            if (points.getModel().getValueAt(i, 0) == null || points.getModel().getValueAt(i, 1) == null) {
                continue;
            }
        
            list.add(new PointDouble(Double.parseDouble((String)points.getModel().getValueAt(i, 0)), Double.parseDouble((String)points.getModel().getValueAt(i, 1))));
        }
        list.sort(Comparator.comparing(PointDouble::getX));
        return list;
    }
    
    private InterpolatorFactory getInterpolatorFactory() {
        return new NewtonBackInterpolator();
    }
    
    private void addPoint(Object x, Object y) {
        ((DefaultTableModel)points.getModel()).addRow(new Object[]{x, y});
    }
    
    private void initTableModel() {
        points.setModel(new DefaultTableModel(new Object[]{"X", "Y"}, 0));
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Interpolation");
        MainWindow gui = new MainWindow();
        frame.setContentPane(gui.rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
