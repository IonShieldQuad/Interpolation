package core;

import javafx.util.Pair;
import math.InterpolationException;
import math.Interpolator;
import math.InterpolatorFactory;
import math.NewtonBackInterpolator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainWindow {
    private JPanel graph;
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
    
    private Interpolator interpolator;
    
    private MainWindow() {
        initComponents();
    }
    
    private void initComponents() {
        initTableModel();
        addPointButton.addActionListener(e -> addPoint(addX.getText(), addY.getText()));
        clearButton.addActionListener(e -> initTableModel());
        buildButton.addActionListener(e -> makeInterpolator());
    }
    
    private void makeInterpolator() {
        InterpolatorFactory factory = getInterpolatorFactory();
        
        try {
            interpolator = factory.makeInterpolator(getPointList());
            log.append("\nInterpolator generated");
            updateGraph();
        }
        catch (NumberFormatException | InterpolationException e) {
            log.append("\n" + e.getMessage());
        }
    }
    
    private void updateGraph() {
        //TODO
    }
    
    private List<Pair<Double, Double>> getPointList() {
        List<Pair<Double, Double>> list = new ArrayList<>();
        for (int i = 0; i < points.getModel().getRowCount(); i++) {
        
            if (points.getModel().getValueAt(i, 0) == null || points.getModel().getValueAt(i, 1) == null) {
                continue;
            }
        
            list.add(new Pair<>(Double.parseDouble((String)points.getModel().getValueAt(i, 0)), Double.parseDouble((String)points.getModel().getValueAt(i, 1))));
        }
        list.sort(Comparator.comparing(Pair::getKey));
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
