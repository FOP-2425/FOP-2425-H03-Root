package h03;

import java.util.concurrent.ThreadLocalRandom;

public class TestConstants {
    public static long RANDOM_SEED = ThreadLocalRandom.current().nextLong();

    public static final int TEST_TIMEOUT_IN_SECONDS = 5;

    public static final int TEST_ITERATIONS = 5;
}
