package math;

import java.util.List;

public interface InterpolatorFactory {
    Interpolator makeInterpolator(List<PointDouble> points) throws InterpolationException;
}
