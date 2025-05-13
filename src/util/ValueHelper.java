package util;

public class ValueHelper {
    // Clamps value
    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }
}
