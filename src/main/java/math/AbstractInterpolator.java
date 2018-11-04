package math;

import javafx.util.Pair;

import java.util.*;
import java.util.function.DoubleUnaryOperator;

public abstract class AbstractInterpolator implements Interpolator {
    private final List<Pair<Double, Double>> points;
    
    AbstractInterpolator(List<Pair<Double, Double>> points) {
        this.points = new ArrayList<>();
        points.forEach(p -> this.points.add(new Pair<>(p.getKey(), p.getValue())));
        this.points.sort(Comparator.comparing(Pair::getKey));
    }
    
    protected List<Pair<Double, Double>> getPoints() {
        return points;
    }
    
    
    @Override
    public abstract double evaluate(double value);
    
    @Override
    public double lower() {
        return points.get(0).getKey();
    }
    @Override
    public double upper() {
        return points.get(points.size() - 1).getKey();
    }
}
