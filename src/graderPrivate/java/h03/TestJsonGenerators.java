package h03;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import java.io.IOException;

import static h03.TestConstants.TEST_ITERATIONS;

@DisabledIf("org.tudalgo.algoutils.tutor.general.Utils#isJagrRun()")
public class TestJsonGenerators {

    @Test
    public void generateCalculateStepsDiagonalDataSet() throws IOException {
        TestUtils.generateJsonTestData(
            (mapper, index, rnd) -> {
                int beginOrig = rnd.nextInt(10);
                int begin = beginOrig / 2;
                int goal = rnd.nextInt(20);
                int expected = Math.abs(begin - goal);

                return mapper.createObjectNode()
                    .put("begin", beginOrig)
                    .put("goal", goal)
                    .put("expected", expected);
            },
            TEST_ITERATIONS,
            "CalculateStepsDiagonalDataSet"
        );
    }

    @Test
    public void generateCalculateStepsOverstepDataSet() throws IOException {
        TestUtils.generateJsonTestData(
            (mapper, index, rnd) -> {
                int beginOrig = rnd.nextInt(10);
                int begin = beginOrig / 2;
                int goal = rnd.nextInt(20);
                int expected = (Math.abs(begin - goal) % 2 == 0) ? Math.abs(begin - goal) : Math.abs(begin - goal) + 1;

                return mapper.createObjectNode()
                    .put("begin", beginOrig)
                    .put("goal", goal)
                    .put("expected", expected);
            },
            TEST_ITERATIONS,
            "CalculateStepsOverstepDataSet"
        );
    }

    @Test
    public void generateCalculateStepsTeleportDataSet() throws IOException {
        TestUtils.generateJsonTestData(
            (mapper, index, rnd) -> {
                int beginOrig = rnd.nextInt(10);
                int begin = beginOrig / 2;
                int goal = rnd.nextInt(20);
                int expected = (Math.abs(begin - goal) % 2 == 0) ? Math.abs(begin - goal) / 2 : (Math.abs(begin - goal) / 2) + 2;

                return mapper.createObjectNode()
                    .put("begin", beginOrig)
                    .put("goal", goal)
                    .put("expected", expected);
            },
            TEST_ITERATIONS,
            "CalculateStepsTeleportDataSet"
        );
    }
}
