package math;


import javafx.util.Pair;

import java.util.*;
import java.util.function.DoubleUnaryOperator;

public class NewtonBackInterpolator implements InterpolatorFactory {
    @Override
    public Interpolator makeInterpolator(List<Pair<Double, Double>> points) throws InterpolationException {
        points = new ArrayList<>(points);
        
        if (points.size() < 1) {
            throw new InterpolationException("Points list must not be empty");
        }
        
        points.sort(Comparator.comparing(Pair::getKey));
        
        double step;
        if (points.size() == 1) {
            step = 0;
        }
        else {
            step = points.get(1).getKey() - points.get(0).getKey();
        }
        
        for (int i = 0; i < points.size() - 1; i++) {
            if (points.get(i + 1).getKey() - points.get(i).getKey() != step) {
                throw new InterpolationException("Distance between points on X axis must be equal");
            }
        }
        
        return new AbstractInterpolator(points){
            @Override
            public double evaluate(double value) {
                double result = 0;
                double q = (value - upper()) / step;
                
                for (int i = 0; i < getPoints().size(); i++) {
                    
                    double qMulti = 1;
                    for (int j = 0; j < i; j++) {
                        qMulti *= q - j;
                    }
                    
                    double fact = 1;
                    for (int j = i; j > 1; j--) {
                        fact *= j;
                    }
                    
                    result += difference(getPoints().size() - 1 - i, getPoints(), i) * qMulti / fact;
                }
                
                return result;
            }
        };
    }
    
    private double difference(int value, List<Pair<Double, Double>> points, int order) {
        if (order <= 0) {
            if (value < 0 || value > points.size() - 1) {
                throw new IllegalArgumentException("Point at " + value + " does not exist in the list");
            }
            return points.get(value).getValue();
        }
        return difference(value + 1, points, order - 1) - difference(value, points, order - 1);
    }
}
