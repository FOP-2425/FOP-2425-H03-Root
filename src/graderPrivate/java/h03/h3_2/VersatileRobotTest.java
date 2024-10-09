package h03.h3_2;

import fopbot.Robot;
import fopbot.World;
import h03.mock.HackingRobotClassTransformer;
import h03.mock.HackingRobotMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.lang.reflect.Modifier;
import java.util.function.BiFunction;

import static h03.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class VersatileRobotTest {

    @BeforeAll
    public static void setup() {
        World.setSize(5, 5);
    }

    @Test
    public void testClassHeader() {
        TypeLink versatileRobotLink = VERSATILE_ROBOT_LINK.get();

        assertTrue((versatileRobotLink.modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Class VersatileRobot was not declared public");
        assertEquals(HACKING_ROBOT_LINK.get(), versatileRobotLink.superType(), emptyContext(), result ->
            "Class VersatileRobot does not extend HackingRobot");
    }

    @Test
    @ExtendWith(JagrExecutionCondition.class)
    public void testConstructor() {
        BiFunction<String, Object[], ?> methodAction = (methodName, args) -> switch (methodName) {
            case "getType" -> "DIAGONAL";
            case "getNextType" -> "OVERSTEP";
            case "getRandom" -> 1;
            case "shuffle" -> false;
            default -> throw new IllegalStateException("Unexpected method: " + methodName);
        };
        HackingRobotClassTransformer.useMock(true);
        HackingRobotMock.getInstance().setMethodAction(methodAction);

        int x = 0;
        int y = 4;
        boolean order = false;
        boolean exchange = false;
        Context context = contextBuilder()
            .add("x", x)
            .add("y", y)
            .add("order", order)
            .add("exchange", exchange)
            .add("super.getType() return value", methodAction.apply("getType", new Object[0]))
            .add("super.getNextType() return value", methodAction.apply("getNextType", new Object[0]))
            .add("super.getRandom(int) return value", methodAction.apply("getRandom", new Object[0]))
            .add("super.shuffle(int) return value", methodAction.apply("shuffle", new Object[0]))
            .build();
        try {
            Robot instance = callObject(() -> VERSATILE_ROBOT_CONSTRUCTOR_LINK.get().invoke(x, y, order, exchange), context, result ->
                "An exception occurred while invoking constructor of class VersatileRobot");

            assertEquals(x, instance.getX(), context, result -> "The x-coordinate of this VersatileRobot is incorrect");
            assertEquals(x, instance.getY(), context, result -> "The y-coordinate of this VersatileRobot is incorrect");
        } finally {
            HackingRobotClassTransformer.useMock(false);
        }
    }

    @Test
    @ExtendWith(JagrExecutionCondition.class)
    public void testShuffleWithParams() {
        BiFunction<String, Object[], ?> methodAction = (methodName, args) -> switch (methodName) {
            case "getType" -> "DIAGONAL";
            case "getNextType" -> "OVERSTEP";
            case "getRandom" -> 1;
            case "shuffle" -> false;
            default -> throw new IllegalStateException("Unexpected method: " + methodName);
        };
        HackingRobotClassTransformer.useMock(true);
        HackingRobotMock.getInstance().setMethodAction(methodAction);

        int x = 0;
        int y = 4;
        boolean order = false;
        boolean exchange = false;
        Context context = contextBuilder()
            .add("x", x)
            .add("y", y)
            .add("order", order)
            .add("exchange", exchange)
            .add("super.getType() return value", methodAction.apply("getType", new Object[0]))
            .add("super.getNextType() return value", methodAction.apply("getNextType", new Object[0]))
            .add("super.getRandom(int) return value", methodAction.apply("getRandom", new Object[0]))
            .add("super.shuffle(int) return value", methodAction.apply("shuffle", new Object[0]))
            .build();
        try {
            Robot instance = callObject(() -> VERSATILE_ROBOT_CONSTRUCTOR_LINK.get().invoke(x, y, order, exchange), context, result ->
                "An exception occurred while invoking constructor of class VersatileRobot");
            instance.setY(y);
            call(() -> VERSATILE_ROBOT_SHUFFLE1_LINK.get().invoke(instance, 1), context, result ->
                "An exception occurred while invoking shuffle(int)");

            assertEquals(x, instance.getX(), context, result -> "The x-coordinate of this VersatileRobot is incorrect");
            assertEquals(x, instance.getY(), context, result -> "The y-coordinate of this VersatileRobot is incorrect");
        } finally {
            HackingRobotClassTransformer.useMock(false);
        }
    }

    @Test
    @ExtendWith(JagrExecutionCondition.class)
    public void testShuffleNoParams() {
        BiFunction<String, Object[], ?> methodAction = (methodName, args) -> switch (methodName) {
            case "getType" -> "DIAGONAL";
            case "getNextType" -> "OVERSTEP";
            case "getRandom" -> 1;
            case "shuffle" -> false;
            default -> throw new IllegalStateException("Unexpected method: " + methodName);
        };
        HackingRobotClassTransformer.useMock(true);
        HackingRobotMock.getInstance().setMethodAction(methodAction);

        int x = 0;
        int y = 4;
        boolean order = false;
        boolean exchange = false;
        Context context = contextBuilder()
            .add("x", x)
            .add("y", y)
            .add("order", order)
            .add("exchange", exchange)
            .add("super.getType() return value", methodAction.apply("getType", new Object[0]))
            .add("super.getNextType() return value", methodAction.apply("getNextType", new Object[0]))
            .add("super.getRandom(int) return value", methodAction.apply("getRandom", new Object[0]))
            .add("super.shuffle(int) return value", methodAction.apply("shuffle", new Object[0]))
            .build();
        try {
            Robot instance = callObject(() -> VERSATILE_ROBOT_CONSTRUCTOR_LINK.get().invoke(x, y, order, exchange), context, result ->
                "An exception occurred while invoking constructor of class VersatileRobot");
            instance.setY(y);
            call(() -> VERSATILE_ROBOT_SHUFFLE2_LINK.get().invoke(instance), context, result ->
                "An exception occurred while invoking shuffle()");

            assertEquals(x, instance.getX(), context, result -> "The x-coordinate of this VersatileRobot is incorrect");
            assertEquals(x, instance.getY(), context, result -> "The y-coordinate of this VersatileRobot is incorrect");
        } finally {
            HackingRobotClassTransformer.useMock(false);
        }
    }
}
