package h03.h3_3;

import fopbot.World;
import h03.RobotsChallenge;
import h03.MathMinMock;
import h03.robots.DoublePowerRobot;
import h03.robots.HackingRobot;
import h03.robots.MovementType;
import kotlin.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.Invocation;
import org.tudalgo.algoutils.transform.util.headers.FieldHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;

import java.lang.reflect.*;
import java.util.*;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class RobotsChallengeTest {

    @BeforeAll
    public static void setup() {
        World.setSize(5, 5);
        World.setDelay(0);
    }

    @AfterEach
    public void tearDown() {
        resetAll();
    }

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(RobotsChallenge.class);

        assertTrue(Modifier.isPublic(originalClassHeader.access()), emptyContext(), result ->
            "Class RobotsChallenge was not declared public");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 15})
    public void testConstructor(int begin) {
        Delegation.disable(MethodHeader.of(RobotsChallenge.class, int.class, int.class, DoublePowerRobot[].class));

        int goal = 5;
        DoublePowerRobot[] robots = new DoublePowerRobot[0];
        Context context = contextBuilder()
            .add("begin", begin)
            .add("goal", goal)
            .add("robots", robots)
            .build();

        Object instance = callObject(() -> new RobotsChallenge(begin, goal, robots), context, result ->
            "An exception occurred while invoking constructor of class RobotsChallenge");
        assertEquals(begin / 2,
            FieldHeader.of(RobotsChallenge.class, "begin").getValue(instance),
            context,
            result -> "Value of field 'begin' is incorrect");
        assertEquals(goal,
            FieldHeader.of(RobotsChallenge.class, "goal").getValue(instance),
            context,
            result -> "Value of field 'goal' is incorrect");
        assertSame(robots,
            FieldHeader.of(RobotsChallenge.class, "robots").getValue(instance),
            context,
            result -> "Value of field 'robots' is incorrect");
    }

    @Test
    public void testWinThreshold() {
        Delegation.disable(MethodHeader.of(RobotsChallenge.class, int.class, int.class, DoublePowerRobot[].class));

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
        assertEquals(2,
            FieldHeader.of(RobotsChallenge.class, "winThreshold").getValue(instance),
            context,
            result -> "Value of field 'winThreshold' is incorrect");
    }

    @ParameterizedTest
    @JsonParameterSetTest("/h03/CalculateStepsDiagonalDataSet.generated.json")
    public void testCalculateStepsDiagonal(JsonParameterSet params) throws NoSuchMethodException {
        testCalculateStepsAllTypes(params, RobotsChallenge.class.getDeclaredMethod("calculateStepsDiagonal"));
    }

    @ParameterizedTest
    @JsonParameterSetTest("/h03/CalculateStepsOverstepDataSet.generated.json")
    public void testCalculateStepsOverstep(JsonParameterSet params) throws NoSuchMethodException {
        testCalculateStepsAllTypes(params, RobotsChallenge.class.getDeclaredMethod("calculateStepsOverstep"));
    }

    @ParameterizedTest
    @JsonParameterSetTest("/h03/CalculateStepsTeleportDataSet.generated.json")
    public void testCalculateStepsTeleport(JsonParameterSet params) throws NoSuchMethodException {
        testCalculateStepsAllTypes(params, RobotsChallenge.class.getDeclaredMethod("calculateStepsTeleport"));
    }

    @Test
    public void testFindWinnersCalc() {
        MethodHeader calculateSteps = MethodHeader.of(RobotsChallenge.class, "calculateSteps", MovementType.class);
        Delegation.disable(MethodHeader.of(RobotsChallenge.class, "findWinners"));

        int begin = 2;
        int goal = 5;
        MovementType[] movementTypes = MovementType.values();
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            Substitution.enable(MethodHeader.of(HackingRobot.class, "getType"),
                invocation -> movementTypes[finalI % movementTypes.length]);
            Substitution.enable(MethodHeader.of(HackingRobot.class, "getNextType"),
                invocation -> movementTypes[(finalI + 1) % movementTypes.length]);
            Logging.reset();
            Logging.enable(calculateSteps);

            DoublePowerRobot[] robots = new DoublePowerRobot[] {new DoublePowerRobot(0, 0, false)};
            Context context = contextBuilder()
                .add("begin", begin)
                .add("goal", goal)
                .add("robots", robots)
                .build();

            RobotsChallenge robotsChallengeInstance = new RobotsChallenge(begin * 2, goal, robots);

            call(robotsChallengeInstance::findWinners, context, result -> "An exception occurred while invoking findWinners");
            List<Invocation> invocations = Logging.getInvocations(calculateSteps);
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
    public void testFindWinnersMin() {
        Delegation.disable(MethodHeader.of(RobotsChallenge.class, "findWinners"));
        Substitution.enable(MethodHeader.of(RobotsChallenge.class, "calculateSteps", MovementType.class),
            invocation -> invocation.getParameter(0, MovementType.class).ordinal());

        int begin = 2;
        int goal = 5;
        MovementType[] movementTypes = MovementType.values();
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            Substitution.enable(MethodHeader.of(HackingRobot.class, "getType"),
                invocation -> movementTypes[finalI % movementTypes.length]);
            Substitution.enable(MethodHeader.of(HackingRobot.class, "getNextType"),
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
    public void testFindWinnersReturn() {
        MovementType[] movementTypes = MovementType.values();
        DoublePowerRobot[] robots = new DoublePowerRobot[] {
            new DoublePowerRobot(0, 0, false),
            new DoublePowerRobot(0, 0, false),
            new DoublePowerRobot(0, 0, false)
        };
        Substitution.enable(MethodHeader.of(HackingRobot.class, "getType"), invocation -> {
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
        Substitution.enable(MethodHeader.of(HackingRobot.class, "getNextType"), invocation -> {
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
        Substitution.enable(MethodHeader.of(RobotsChallenge.class, "calculateSteps", MovementType.class),
            invocation -> invocation.getParameter(0, MovementType.class).ordinal() * 3);
        Delegation.disable(MethodHeader.of(RobotsChallenge.class, "findWinners"));

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
        Delegation.disable(method);

        Context context = params.toContext("expected");
        DoublePowerRobot[] robots = new DoublePowerRobot[0];
        RobotsChallenge instance = callObject(() -> new RobotsChallenge(params.getInt("begin"), params.getInt("goal"), robots),
            context, result -> "An exception occurred while invoking constructor of class RobotsChallenge");

        assertCallEquals(params.getInt("expected"), () -> method.invoke(instance), context, result ->
            result.cause() == null ? method.getName() + " returned an incorrect value" : result.cause().getCause().getMessage());
    }
}
