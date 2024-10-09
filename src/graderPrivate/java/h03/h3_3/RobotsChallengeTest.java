package h03.h3_3;

import fopbot.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSetTest;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import static h03.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class RobotsChallengeTest {

    @BeforeAll
    public static void setup() {
        World.setSize(5, 5);
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

    private void testCalculateStepsAllTypes(JsonParameterSet params, Supplier<MethodLink> methodLinkSupplier) {
        Context context = params.toContext("expected");
        Object[] robots = (Object[]) Array.newInstance(DOUBLE_POWER_ROBOT_LINK.get().reflection(), 0);
        Object instance = callObject(() -> ROBOTS_CHALLENGE_CONSTRUCTOR_LINK.get().invoke(params.getInt("begin"), params.getInt("goal"), robots),
            context, result -> "An exception occurred while invoking constructor of class RobotsChallenge");

        assertCallEquals(params.getInt("expected"), () -> methodLinkSupplier.get().invoke(instance), context, result ->
            methodLinkSupplier.get().name() + " returned an incorrect value");
    }
}
