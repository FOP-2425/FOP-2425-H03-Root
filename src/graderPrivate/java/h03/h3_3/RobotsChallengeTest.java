package h03.h3_3;

import fopbot.World;
import h03.RobotsChallenge;
import h03.MathMinMock;
import h03.TestConstants;
import h03.robots.DoublePowerRobot;
import h03.robots.HackingRobot;
import h03.robots.MovementType;
import kotlin.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.SubmissionExecutionHandler;
import org.tudalgo.algoutils.transform.util.ClassHeader;
import org.tudalgo.algoutils.transform.util.Invocation;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
public class RobotsChallengeTest {

    private final SubmissionExecutionHandler executionHandler = SubmissionExecutionHandler.getInstance();

    private static Field robotsField;
    private static Field beginField;
    private static Field goalField;
    private static Field winThresholdField;
    private static Constructor<?> robotsChallengeConstructor;
    private static Method calculateStepsDiagonalMethod;
    private static Method calculateStepsOverstepMethod;
    private static Method calculateStepsTeleportMethod;
    private static Method calculateStepsMethod;
    private static Method findWinnersMethod;

    @BeforeAll
    public static void setup() {
        World.setSize(5, 5);
        World.setDelay(0);

        try {
            robotsField = RobotsChallenge.class.getDeclaredField("robots");
            beginField = RobotsChallenge.class.getDeclaredField("begin");
            goalField = RobotsChallenge.class.getDeclaredField("goal");
            winThresholdField = RobotsChallenge.class.getDeclaredField("winThreshold");
            robotsChallengeConstructor = RobotsChallenge.class.getDeclaredConstructor(int.class, int.class, DoublePowerRobot[].class);
            calculateStepsDiagonalMethod = RobotsChallenge.class.getDeclaredMethod("calculateStepsDiagonal");
            calculateStepsOverstepMethod = RobotsChallenge.class.getDeclaredMethod("calculateStepsOverstep");
            calculateStepsTeleportMethod = RobotsChallenge.class.getDeclaredMethod("calculateStepsTeleport");
            calculateStepsMethod = RobotsChallenge.class.getDeclaredMethod("calculateSteps", MovementType.class);
            findWinnersMethod = RobotsChallenge.class.getDeclaredMethod("findWinners");

            robotsField.trySetAccessible();
            beginField.trySetAccessible();
            goalField.trySetAccessible();
            winThresholdField.trySetAccessible();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        executionHandler.resetMethodInvocationLogging();
        executionHandler.resetMethodSubstitution();
        executionHandler.resetMethodDelegation();
    }

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = SubmissionExecutionHandler.getOriginalClassHeader(RobotsChallenge.class);

        assertTrue(Modifier.isPublic(originalClassHeader.access()), emptyContext(), result ->
            "Class RobotsChallenge was not declared public");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 15})
    public void testConstructor(int begin) throws ReflectiveOperationException {
        executionHandler.disableMethodDelegation(robotsChallengeConstructor);

        int goal = 5;
        DoublePowerRobot[] robots = new DoublePowerRobot[0];
        Context context = contextBuilder()
            .add("begin", begin)
            .add("goal", goal)
            .add("robots", robots)
            .build();

        Object instance = callObject(() -> new RobotsChallenge(begin, goal, robots), context, result ->
            "An exception occurred while invoking constructor of class RobotsChallenge");
        assertEquals(begin / 2, beginField.get(instance), context, result ->
            "Value of field 'begin' is incorrect");
        assertEquals(goal, goalField.get(instance), context, result ->
            "Value of field 'goal' is incorrect");
        assertSame(robots, robotsField.get(instance), context, result ->
            "Value of field 'robots' is incorrect");
    }

    @Test
    public void testWinThreshold() throws ReflectiveOperationException {
        executionHandler.disableMethodDelegation(robotsChallengeConstructor);

        int begin = 10;
        int goal = 5;
        DoublePowerRobot[] robots = new DoublePowerRobot[0];
        Context context = contextBuilder()
            .add("begin", begin)
            .add("goal", goal)
            .add("robots", robots)
            .build();

        Object instance = callObject(() -> new RobotsChallenge(begin, goal, robots), context, result ->
            "An exception occurred while invoking constructor of class RobotsChallenge");
        assertEquals(2, winThresholdField.get(instance), context, result ->
            "Value of field 'winThreshold' is incorrect");
    }

    @ParameterizedTest
    @JsonParameterSetTest("/h03/CalculateStepsDiagonalDataSet.generated.json")
    public void testCalculateStepsDiagonal(JsonParameterSet params) {
        testCalculateStepsAllTypes(params, calculateStepsDiagonalMethod);
    }

    @ParameterizedTest
    @JsonParameterSetTest("/h03/CalculateStepsOverstepDataSet.generated.json")
    public void testCalculateStepsOverstep(JsonParameterSet params) {
        testCalculateStepsAllTypes(params, calculateStepsOverstepMethod);
    }

    @ParameterizedTest
    @JsonParameterSetTest("/h03/CalculateStepsTeleportDataSet.generated.json")
    public void testCalculateStepsTeleport(JsonParameterSet params) {
        testCalculateStepsAllTypes(params, calculateStepsTeleportMethod);
    }

    @Test
    public void testFindWinnersCalc() throws ReflectiveOperationException {
        executionHandler.disableMethodDelegation(findWinnersMethod);

        int begin = 2;
        int goal = 5;
        MovementType[] movementTypes = MovementType.values();
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("getType"),
                invocation -> movementTypes[finalI % movementTypes.length]);
            executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("getNextType"),
                invocation -> movementTypes[(finalI + 1) % movementTypes.length]);
            executionHandler.resetMethodInvocationLogging();
            executionHandler.enableMethodInvocationLogging(calculateStepsMethod);

            DoublePowerRobot[] robots = new DoublePowerRobot[] {new DoublePowerRobot(0, 0, false)};
            Context context = contextBuilder()
                .add("begin", begin)
                .add("goal", goal)
                .add("robots", robots)
                .build();

            RobotsChallenge robotsChallengeInstance = new RobotsChallenge(begin * 2, goal, robots);

            call(robotsChallengeInstance::findWinners, context, result -> "An exception occurred while invoking findWinners");
            List<Invocation> invocations = executionHandler.getInvocationsForMethod(calculateStepsMethod);
            assertEquals(2, invocations.size(), context, result -> "calculateSteps was not called exactly twice");
            assertEquals(List.of(movementTypes[i % movementTypes.length], movementTypes[(i + 1) % movementTypes.length]),
                invocations.stream()
                    .map(invocation -> invocation.getParameter(0, MovementType.class))
                    .toList(),
                context,
                result -> "calculateSteps was not called with <robot>.getType() and <robot>.getNextType()");
        }
    }

    @Test
    public void testFindWinnersMin() throws ReflectiveOperationException {
        executionHandler.disableMethodDelegation(findWinnersMethod);
        executionHandler.substituteMethod(calculateStepsMethod,
            invocation -> invocation.getParameter(0, MovementType.class).ordinal());

        int begin = 2;
        int goal = 5;
        MovementType[] movementTypes = MovementType.values();
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("getType"),
                invocation -> movementTypes[finalI % movementTypes.length]);
            executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("getNextType"),
                invocation -> movementTypes[(finalI + 1) % movementTypes.length]);

            DoublePowerRobot[] robots = new DoublePowerRobot[] {new DoublePowerRobot(0, 0, false)};
            Context context = contextBuilder()
                .add("begin", begin)
                .add("goal", goal)
                .add("robots", robots)
                .build();
            RobotsChallenge robotsChallengeInstance = new RobotsChallenge(begin * 2, goal, robots);

            MathMinMock.MIN_INVOCATIONS.clear();
            call(robotsChallengeInstance::findWinners, context, result -> "An exception occurred while invoking findWinners");
            List<Pair<Integer, Integer>> minInvocations = new ArrayList<>(MathMinMock.MIN_INVOCATIONS);
            assertTrue(!minInvocations.isEmpty(), context, result -> "Math.min was not called at least once");
            Pair<Integer, Integer> expectedArgs = new Pair<>(finalI % movementTypes.length, (finalI + 1) % movementTypes.length);
            assertTrue(
                minInvocations.stream()
                    .anyMatch(pair -> pair.getFirst().equals(expectedArgs.getFirst()) && pair.getSecond().equals(expectedArgs.getSecond()) ||
                        pair.getFirst().equals(expectedArgs.getSecond()) && pair.getSecond().equals(expectedArgs.getFirst())),
                contextBuilder()
                    .add(context)
                    .add("expected", "Math.min(%d, %d) or Math.min(%d, %d)".formatted(expectedArgs.getFirst(), expectedArgs.getSecond(), expectedArgs.getSecond(), expectedArgs.getFirst()))
                    .build(),
                result -> "Math.min was not called with the expected arguments"
            );
        }
    }

    @Test
    public void testFindWinnersReturn() throws Throwable {
        MovementType[] movementTypes = MovementType.values();
        DoublePowerRobot[] robots = new DoublePowerRobot[] {
            new DoublePowerRobot(0, 0, false),
            new DoublePowerRobot(0, 0, false),
            new DoublePowerRobot(0, 0, false)
        };
        executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("getType"),
            invocation -> {
                if (invocation.getInstance() == robots[0]) {
                    return movementTypes[0];
                } else if (invocation.getInstance() == robots[1]) {
                    return movementTypes[1];
                } else if (invocation.getInstance() == robots[2]) {
                    return movementTypes[2];
                } else {
                    return null;
                }
            });
        executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("getNextType"),
            invocation -> {
                if (invocation.getInstance() == robots[0]) {
                    return movementTypes[1];
                } else if (invocation.getInstance() == robots[1]) {
                    return movementTypes[2];
                } else if (invocation.getInstance() == robots[2]) {
                    return movementTypes[0];
                } else {
                    return null;
                }
            });
        executionHandler.substituteMethod(calculateStepsMethod,
            invocation -> invocation.getParameter(0, MovementType.class).ordinal() * 3);
        executionHandler.disableMethodDelegation(findWinnersMethod);

        int begin = 2;
        int goal = 5;
        Context context = contextBuilder()
            .add("begin", begin)
            .add("goal", goal)
            .add("robots", robots)
            .build();
        RobotsChallenge robotsChallengeInstance = new RobotsChallenge(begin * 2, goal, robots);

        DoublePowerRobot[] returnValue = callObject(robotsChallengeInstance::findWinners, context, result ->
            "An exception occurred while invoking findWinners");
        assertEquals(robots.length, returnValue.length, context, result -> "Returned array has incorrect length");
        int a = 0;
        for (DoublePowerRobot robot : robots) {
            if (robot.getType() == MovementType.DIAGONAL || robot.getNextType() == MovementType.DIAGONAL) {
                assertSame(robot, returnValue[a++], context, result -> "Robot was not found in array / at wrong index");
            }
        }
        for (; a < robots.length; a++) {
            assertNull(returnValue[a], context, result -> "Found unexpected robots in array");
        }
    }

    private void testCalculateStepsAllTypes(JsonParameterSet params, Method method) {
        executionHandler.disableMethodDelegation(method);

        Context context = params.toContext("expected");
        DoublePowerRobot[] robots = new DoublePowerRobot[0];
        RobotsChallenge instance = callObject(() -> new RobotsChallenge(params.getInt("begin"), params.getInt("goal"), robots),
            context, result -> "An exception occurred while invoking constructor of class RobotsChallenge");

        assertCallEquals(params.getInt("expected"), () -> method.invoke(instance), context, result ->
            method.getName() + " returned an incorrect value");
    }
}
