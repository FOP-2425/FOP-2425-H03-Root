package h03.h3_3;

import fopbot.World;
import h03.mock.RobotsChallengeAuxMock;
import kotlin.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.EnumConstantLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;

import static h03.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class RobotsChallengeTest {

    @BeforeAll
    public static void setup() {
        World.setSize(5, 5);
        World.setDelay(0);
    }

    @Test
    public void testClassHeader() {
        TypeLink robotsChallengeLink = ROBOTS_CHALLENGE_LINK.get();

        assertTrue((robotsChallengeLink.modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Class RobotsChallenge was not declared public");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 15})
    public void testConstructor(int begin) {
        int goal = 5;
        Object[] robots = (Object[]) Array.newInstance(DOUBLE_POWER_ROBOT_LINK.get().reflection(), 0);
        Context context = contextBuilder()
            .add("begin", begin)
            .add("goal", goal)
            .add("robots", robots)
            .build();

        Object instance = callObject(() -> ROBOTS_CHALLENGE_CONSTRUCTOR_LINK.get().invoke(begin, goal, robots), context, result ->
            "An exception occurred while invoking constructor of class RobotsChallenge");
        assertEquals(begin / 2, ROBOTS_CHALLENGE_BEGIN_LINK.get().get(instance), context, result ->
            "Value of field 'begin' is incorrect");
        assertEquals(goal, ROBOTS_CHALLENGE_GOAL_LINK.get().get(instance), context, result ->
            "Value of field 'goal' is incorrect");
        assertSame(robots, ROBOTS_CHALLENGE_ROBOTS_LINK.get().get(instance), context, result ->
            "Value of field 'robots' is incorrect");
    }

    @Test
    public void testWinThreshold() {
        int begin = 10;
        int goal = 5;
        Object[] robots = (Object[]) Array.newInstance(DOUBLE_POWER_ROBOT_LINK.get().reflection(), 0);
        Context context = contextBuilder()
            .add("begin", begin)
            .add("goal", goal)
            .add("robots", robots)
            .build();

        Object instance = callObject(() -> ROBOTS_CHALLENGE_CONSTRUCTOR_LINK.get().invoke(begin, goal, robots), context, result ->
            "An exception occurred while invoking constructor of class RobotsChallenge");
        assertEquals(2, ROBOTS_CHALLENGE_WIN_THRESHOLD_LINK.get().get(instance), context, result ->
            "Value of field 'winThreshold' is incorrect");
    }

    @ParameterizedTest
    @JsonParameterSetTest("/h03/CalculateStepsDiagonalDataSet.generated.json")
    public void testCalculateStepsDiagonal(JsonParameterSet params) {
        testCalculateStepsAllTypes(params, ROBOTS_CHALLENGE_CALCULATE_STEPS_DIAGONAL);
    }

    @ParameterizedTest
    @JsonParameterSetTest("/h03/CalculateStepsOverstepDataSet.generated.json")
    public void testCalculateStepsOverstep(JsonParameterSet params) {
        testCalculateStepsAllTypes(params, ROBOTS_CHALLENGE_CALCULATE_STEPS_OVERSTEP);
    }

    @ParameterizedTest
    @JsonParameterSetTest("/h03/CalculateStepsTeleportDataSet.generated.json")
    public void testCalculateStepsTeleport(JsonParameterSet params) {
        testCalculateStepsAllTypes(params, ROBOTS_CHALLENGE_CALCULATE_STEPS_TELEPORT);
    }

    @Test
    public void testFindWinnersCalc() {
        int begin = 2;
        int goal = 5;
        Enum<?>[] movementTypes = Arrays.stream(MOVEMENT_TYPE_CONSTANTS.get())
            .map(EnumConstantLink::constant)
            .toArray(Enum[]::new);
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            Object[] robots = (Object[]) Array.newInstance(DOUBLE_POWER_ROBOT_LINK.get().reflection(), 1);
            robots[0] = Mockito.mock(DOUBLE_POWER_ROBOT_LINK.get().reflection(), invocation -> {
                if (invocation.getMethod().equals(HACKING_ROBOT_GET_TYPE_LINK.get().reflection())) {
                    return movementTypes[finalI % movementTypes.length];
                } else if (invocation.getMethod().equals(HACKING_ROBOT_GET_NEXT_TYPE_LINK.get().reflection())) {
                    return movementTypes[(finalI + 1) % movementTypes.length];
                } else {
                    return invocation.callRealMethod();
                }
            });
            Context context = contextBuilder()
                .add("begin", begin)
                .add("goal", goal)
                .add("robots", robots)
                .build();

            Set<String> calculateStepsArgs = new HashSet<>();
            Object robotsChallengeInstance = Mockito.mock(ROBOTS_CHALLENGE_LINK.get().reflection(), invocation -> {
                if (invocation.getMethod().equals(ROBOTS_CHALLENGE_CALCULATE_STEPS.get().reflection())) {
                    calculateStepsArgs.add(invocation.<Enum<?>>getArgument(0).name());
                }
                return invocation.callRealMethod();
            });
            ROBOTS_CHALLENGE_BEGIN_LINK.get().set(robotsChallengeInstance, begin);
            ROBOTS_CHALLENGE_GOAL_LINK.get().set(robotsChallengeInstance, goal);
            ROBOTS_CHALLENGE_ROBOTS_LINK.get().set(robotsChallengeInstance, robots);
            ROBOTS_CHALLENGE_WIN_THRESHOLD_LINK.get().set(robotsChallengeInstance, 2);

            call(() -> ROBOTS_CHALLENGE_FIND_WINNERS.get().invoke(robotsChallengeInstance), context, result ->
                "An exception occurred while invoking findWinners");
            assertEquals(2, calculateStepsArgs.size(), context, result -> "calculateSteps was not called exactly twice");
            assertEquals(Set.of(movementTypes[i % movementTypes.length].name(), movementTypes[(i + 1) % movementTypes.length].name()),
                calculateStepsArgs,
                context,
                result -> "calculateSteps was not called with <robot>.getType() and <robot>.getNextType()");
        }
    }

    @Test
    @ExtendWith(JagrExecutionCondition.class)
    public void testFindWinnersMin() {
        int begin = 2;
        int goal = 5;
        Enum<?>[] movementTypes = Arrays.stream(MOVEMENT_TYPE_CONSTANTS.get())
            .map(EnumConstantLink::constant)
            .toArray(Enum[]::new);
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            Object[] robots = (Object[]) Array.newInstance(DOUBLE_POWER_ROBOT_LINK.get().reflection(), 1);
            robots[0] = Mockito.mock(DOUBLE_POWER_ROBOT_LINK.get().reflection(), invocation -> {
                if (invocation.getMethod().equals(HACKING_ROBOT_GET_TYPE_LINK.get().reflection())) {
                    return movementTypes[finalI % movementTypes.length];
                } else if (invocation.getMethod().equals(HACKING_ROBOT_GET_NEXT_TYPE_LINK.get().reflection())) {
                    return movementTypes[(finalI + 1) % movementTypes.length];
                } else {
                    return invocation.callRealMethod();
                }
            });
            Context context = contextBuilder()
                .add("begin", begin)
                .add("goal", goal)
                .add("robots", robots)
                .build();


            Object robotsChallengeInstance = Mockito.mock(ROBOTS_CHALLENGE_LINK.get().reflection(), invocation -> {
                if (invocation.getMethod().equals(ROBOTS_CHALLENGE_CALCULATE_STEPS.get().reflection())) {
                    return invocation.<Enum<?>>getArgument(0).ordinal();
                } else {
                    return invocation.callRealMethod();
                }
            });
            ROBOTS_CHALLENGE_BEGIN_LINK.get().set(robotsChallengeInstance, begin);
            ROBOTS_CHALLENGE_GOAL_LINK.get().set(robotsChallengeInstance, goal);
            ROBOTS_CHALLENGE_ROBOTS_LINK.get().set(robotsChallengeInstance, robots);
            ROBOTS_CHALLENGE_WIN_THRESHOLD_LINK.get().set(robotsChallengeInstance, 2);

            List<Pair<Integer, Integer>> minInvocations = RobotsChallengeAuxMock.MIN_INVOCATIONS;
            minInvocations.clear();
            call(() -> ROBOTS_CHALLENGE_FIND_WINNERS.get().invoke(robotsChallengeInstance), context, result ->
                "An exception occurred while invoking findWinners");
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
        int begin = 2;
        int goal = 5;
        Enum<?>[] movementTypes = Arrays.stream(MOVEMENT_TYPE_CONSTANTS.get())
            .map(EnumConstantLink::constant)
            .toArray(Enum[]::new);
        Object[] robots = (Object[]) Array.newInstance(DOUBLE_POWER_ROBOT_LINK.get().reflection(), 3);
        for (int i = 0; i < robots.length; i++) {
            final int finalI = i;
            robots[i] = Mockito.mock(DOUBLE_POWER_ROBOT_LINK.get().reflection(), invocation -> {
                if (invocation.getMethod().equals(HACKING_ROBOT_GET_TYPE_LINK.get().reflection())) {
                    return movementTypes[finalI % movementTypes.length];
                } else if (invocation.getMethod().equals(HACKING_ROBOT_GET_NEXT_TYPE_LINK.get().reflection())) {
                    return movementTypes[(finalI + 1) % movementTypes.length];
                } else {
                    return invocation.callRealMethod();
                }
            });
        }
        Context context = contextBuilder()
            .add("begin", begin)
            .add("goal", goal)
            .add("robots", robots)
            .build();

        Object robotsChallengeInstance = Mockito.mock(ROBOTS_CHALLENGE_LINK.get().reflection(), invocation -> {
            if (invocation.getMethod().equals(ROBOTS_CHALLENGE_CALCULATE_STEPS.get().reflection())) {
                return invocation.<Enum<?>>getArgument(0).ordinal() * 3;
            } else {
                return invocation.callRealMethod();
            }
        });
        ROBOTS_CHALLENGE_BEGIN_LINK.get().set(robotsChallengeInstance, begin);
        ROBOTS_CHALLENGE_GOAL_LINK.get().set(robotsChallengeInstance, goal);
        ROBOTS_CHALLENGE_ROBOTS_LINK.get().set(robotsChallengeInstance, robots);
        ROBOTS_CHALLENGE_WIN_THRESHOLD_LINK.get().set(robotsChallengeInstance, 2);

        Object[] returnValue = callObject(() -> ROBOTS_CHALLENGE_FIND_WINNERS.get().invoke(robotsChallengeInstance), context, result ->
            "An exception occurred while invoking findWinners");
        assertEquals(robots.length, returnValue.length, context, result -> "Returned array has incorrect length");
        int a = 0;
        for (int i = 0; i < robots.length; i++) {
            if (HACKING_ROBOT_GET_TYPE_LINK.get().invoke(robots[i]) == MOVEMENT_TYPE_DIAGONAL_LINK.get().constant() ||
                HACKING_ROBOT_GET_NEXT_TYPE_LINK.get().invoke(robots[i]) == MOVEMENT_TYPE_DIAGONAL_LINK.get().constant()) {
                assertSame(robots[i], returnValue[a++], context, result -> "Robot was not found in array / at wrong index");
            }
        }
        for (; a < robots.length; a++) {
            assertNull(returnValue[a], context, result -> "Found unexpected robots in array");
        }
    }

    private void testCalculateStepsAllTypes(JsonParameterSet params, Supplier<MethodLink> methodLinkSupplier) {
        Context context = params.toContext("expected");
        Object[] robots = (Object[]) Array.newInstance(DOUBLE_POWER_ROBOT_LINK.get().reflection(), 0);
        Object instance = callObject(() -> ROBOTS_CHALLENGE_CONSTRUCTOR_LINK.get().invoke(params.getInt("begin"), params.getInt("goal"), robots),
            context, result -> "An exception occurred while invoking constructor of class RobotsChallenge");

        assertCallEquals(params.getInt("expected"), () -> methodLinkSupplier.get().invoke(instance), context, result ->
            methodLinkSupplier.get().name() + " returned an incorrect value");
    }
}
