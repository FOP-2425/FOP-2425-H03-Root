package h03.h3_2;

import fopbot.World;
import h03.mock.HackingRobotClassTransformer;
import h03.mock.HackingRobotMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.api.testing.extension.JagrExecutionCondition;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.EnumConstantLink;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static h03.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class DoublePowerRobotTest {

    @BeforeAll
    public static void setup() {
        World.setSize(5, 5);
    }

    @Test
    public void testClassHeader() {
        assertTrue((DOUBLE_POWER_ROBOT_LINK.get().modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Class DoublePowerRobot is not declared public");
        assertEquals(HACKING_ROBOT_LINK.get().reflection(), DOUBLE_POWER_ROBOT_LINK.get().superType().reflection(), emptyContext(), result ->
            "Class DoublePowerRobot does not have correct superclass");
    }

    @Test
    public void testFields() {
        assertEquals(MOVEMENT_TYPE_LINK.get().reflection().arrayType(), DOUBLE_POWER_ROBOT_DOUBLE_POWER_TYPES_LINK.get().type().reflection(), emptyContext(), result ->
            "Field doublePowerTypes in DoublePowerRobot does not have correct type");
    }

    @Test
    public void testMethodHeaders() {
        call(DOUBLE_POWER_ROBOT_SHUFFLE1_LINK::get, emptyContext(), result -> "Method shuffle(int) does not exist in DoublePowerRobot");
        call(DOUBLE_POWER_ROBOT_SHUFFLE2_LINK::get, emptyContext(), result -> "Method shuffle() does not exist in DoublePowerRobot");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testConstructorSetsField(boolean order) {
        List<String> expectedDoublePowerTypes = order ?
            List.of(MOVEMENT_TYPE_DIAGONAL_LINK.get().name(), MOVEMENT_TYPE_TELEPORT_LINK.get().name()) :
            List.of(MOVEMENT_TYPE_OVERSTEP_LINK.get().name(), MOVEMENT_TYPE_DIAGONAL_LINK.get().name());
        int x = 2;
        int y = 2;
        Context context = contextBuilder()
            .add("x", x)
            .add("y", y)
            .add("order", order)
            .build();

        Object instance = callObject(() -> DOUBLE_POWER_ROBOT_CONSTRUCTOR_LINK.get().invoke(x, y, order), context, result ->
            "An exception occurred while invoking constructor of class DoublePowerRobot");
        List<String> actualDoublePowerTypes = Arrays.stream(DOUBLE_POWER_ROBOT_DOUBLE_POWER_TYPES_LINK.get().<Enum<?>[]>get(instance))
            .map(Enum::name)
            .toList();
        assertEquals(expectedDoublePowerTypes, actualDoublePowerTypes, context, result ->
            "Array doublePowerTypes does not contain the correct values");
    }

    @ParameterizedTest
    @ExtendWith(JagrExecutionCondition.class)
    @ValueSource(ints = {0, 1, 2, 3})
    public void testShuffleWithParams(int offset) {
        EnumConstantLink[] movementTypeLinks = MOVEMENT_TYPE_CONSTANTS.get();
        BiFunction<String, Object[], ?> methodAction = (methodName, args) -> switch (methodName) {
            case "getType" -> movementTypeLinks[offset % movementTypeLinks.length].name();
            case "getNextType" -> movementTypeLinks[(offset + 1) % movementTypeLinks.length].name();
            case "getRandom" -> 1;
            case "shuffle" -> false;
            default -> throw new IllegalStateException("Unexpected method: " + methodName);
        };
        HackingRobotClassTransformer.useMock(true);
        HackingRobotMock.getInstance().setMethodAction(methodAction);

        int x = 2;
        int y = 2;
        boolean order = false;
        Context context = contextBuilder()
            .add("x", x)
            .add("y", y)
            .add("order", order)
            .add("super.getType() return value", methodAction.apply("getType", new Object[0]))
            .add("super.getNextType() return value", methodAction.apply("getNextType", new Object[0]))
            .add("super.getRandom(int) return value", methodAction.apply("getRandom", new Object[0]))
            .add("super.shuffle(int) return value", methodAction.apply("shuffle", new Object[0]))
            .build();
        try {
            Object instance = callObject(() -> DOUBLE_POWER_ROBOT_CONSTRUCTOR_LINK.get().invoke(x, y, order), context, result ->
                "An exception occurred while invoking constructor of class DoublePowerRobot");
            boolean returnValue = callObject(() -> DOUBLE_POWER_ROBOT_SHUFFLE1_LINK.get().invoke(instance, 1), context, result ->
                "An exception occurred while invoking shuffle(int)");

            assertEquals(methodAction.apply("shuffle", new Object[0]), returnValue, context, result ->
                "Return value of shuffle(int) is incorrect");
            assertEquals(methodAction.apply("getType", new Object[0]),
                DOUBLE_POWER_ROBOT_DOUBLE_POWER_TYPES_LINK.get().<Enum<?>[]>get(instance)[0].name(),
                context,
                result -> "Value of doublePowerTypes[0] is incorrect");
            assertEquals(methodAction.apply("getNextType", new Object[0]),
                DOUBLE_POWER_ROBOT_DOUBLE_POWER_TYPES_LINK.get().<Enum<?>[]>get(instance)[1].name(),
                context,
                result -> "Value of doublePowerTypes[1] is incorrect");
        } finally {
            HackingRobotClassTransformer.useMock(false);
        }
    }

    @ParameterizedTest
    @ExtendWith(JagrExecutionCondition.class)
    @ValueSource(ints = {0, 1, 2, 3})
    public void testShuffleNoParams(int offset) {
        EnumConstantLink[] movementTypeLinks = MOVEMENT_TYPE_CONSTANTS.get();
        BiFunction<String, Object[], ?> methodAction = (methodName, args) -> switch (methodName) {
            case "getType" -> movementTypeLinks[offset % movementTypeLinks.length].name();
            case "getNextType" -> movementTypeLinks[(offset + 1) % movementTypeLinks.length].name();
            case "getRandom" -> 1;
            case "shuffle" -> false;
            default -> throw new IllegalStateException("Unexpected method: " + methodName);
        };
        HackingRobotClassTransformer.useMock(true);
        HackingRobotMock.getInstance().setMethodAction(methodAction);

        int x = 2;
        int y = 2;
        boolean order = false;
        Context context = contextBuilder()
            .add("x", x)
            .add("y", y)
            .add("order", order)
            .add("super.getType() return value", methodAction.apply("getType", new Object[0]))
            .add("super.getNextType() return value", methodAction.apply("getNextType", new Object[0]))
            .add("super.getRandom(int) return value", methodAction.apply("getRandom", new Object[0]))
            .add("super.shuffle(int) return value", methodAction.apply("shuffle", new Object[0]))
            .build();
        try {
            Object instance = callObject(() -> DOUBLE_POWER_ROBOT_CONSTRUCTOR_LINK.get().invoke(x, y, order), context, result ->
                "An exception occurred while invoking constructor of class DoublePowerRobot");
            call(() -> DOUBLE_POWER_ROBOT_SHUFFLE2_LINK.get().invoke(instance), context, result ->
                "An exception occurred while invoking shuffle()");

            assertEquals(methodAction.apply("getType", new Object[0]),
                DOUBLE_POWER_ROBOT_DOUBLE_POWER_TYPES_LINK.get().<Enum<?>[]>get(instance)[0].name(),
                context,
                result -> "Value of doublePowerTypes[0] is incorrect");
            assertEquals(methodAction.apply("getNextType", new Object[0]),
                DOUBLE_POWER_ROBOT_DOUBLE_POWER_TYPES_LINK.get().<Enum<?>[]>get(instance)[1].name(),
                context,
                result -> "Value of doublePowerTypes[1] is incorrect");
        } finally {
            HackingRobotClassTransformer.useMock(false);
        }
    }
}
