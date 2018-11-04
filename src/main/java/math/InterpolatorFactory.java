package math;



import javafx.util.Pair;

import java.util.List;

public interface InterpolatorFactory {
    Interpolator makeInterpolator(List<Pair<Double, Double>> points) throws InterpolationException;
}
