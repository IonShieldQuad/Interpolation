package math;

public interface Interpolator {
    double lower();
    double upper();
    double evaluate(double value) throws InterpolationException;
}
