package h03;

import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;

public class MathMinMock {

    public static final List<Pair<Integer, Integer>> MIN_INVOCATIONS = new ArrayList<>();

    public static int min(int a, int b) {
        MIN_INVOCATIONS.add(new Pair<>(a, b));
        return Math.min(a, b);
    }

    public static long min(long a, long b) {
        MIN_INVOCATIONS.add(new Pair<>((int) a, (int) b));
        return Math.min(a, b);
    }

    public static float min(float a, float b) {
        MIN_INVOCATIONS.add(new Pair<>((int) a, (int) b));
        return Math.min(a, b);
    }

    public static double min(double a, double b) {
        MIN_INVOCATIONS.add(new Pair<>((int) a, (int) b));
        return Math.min(a, b);
    }
}
