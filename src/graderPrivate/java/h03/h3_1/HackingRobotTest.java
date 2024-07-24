package h03.h3_1;

import fopbot.Robot;
import fopbot.World;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static h03.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class HackingRobotTest {

    @BeforeAll
    public static void setup() {
        World.setSize(5, 5);
    }

    @Test
    public void testClassHeader() {
        assertTrue((HACKING_ROBOT_LINK.modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Class HackingRobot is not declared public");
        assertEquals(Robot.class, HACKING_ROBOT_LINK.superType().reflection(), emptyContext(), result ->
            "Class HackingRobot does not have correct superclass");
    }

    @Test
    public void testFields() {
        assertTrue((HACKING_ROBOT_TYPE_LINK.modifiers() & Modifier.PRIVATE) != 0, emptyContext(), result ->
            "Field type in HackingRobot is not declared private");
        assertFalse((HACKING_ROBOT_TYPE_LINK.modifiers() & Modifier.STATIC) != 0, emptyContext(), result ->
            "Field type in HackingRobot is declared static");
        assertEquals(MOVEMENT_TYPE_LINK.reflection(), HACKING_ROBOT_TYPE_LINK.type().reflection(), emptyContext(), result ->
            "Field type in HackingRobot does not have correct type");

        assertTrue((HACKING_ROBOT_ROBOT_TYPES_LINK.modifiers() & Modifier.PRIVATE) != 0, emptyContext(), result ->
            "Field robotTypes in HackingRobot is not declared private");
        assertFalse((HACKING_ROBOT_ROBOT_TYPES_LINK.modifiers() & Modifier.STATIC) != 0, emptyContext(), result ->
            "Field robotTypes in HackingRobot is declared static");
        assertEquals(MOVEMENT_TYPE_LINK.reflection().arrayType(), HACKING_ROBOT_ROBOT_TYPES_LINK.type().reflection(), emptyContext(), result ->
            "Field robotTypes in HackingRobot does not have correct type");

        Context.Builder<?> contextBuilder = contextBuilder()
            .add("x", 0)
            .add("y", 0);
        Object hackingRobotInstance = getHackingRobotInstance(0, 0, null, contextBuilder);
        List<String> expectedRobotTypes = List.of("TELEPORT", "OVERSTEP", "DIAGONAL");
        List<String> actualRobotTypes = Arrays.stream(HACKING_ROBOT_ROBOT_TYPES_LINK.<Enum<?>[]>get(hackingRobotInstance))
            .map(Enum::name)
            .toList();
        Context context = contextBuilder.add("HackingRobot instance", hackingRobotInstance).build();
        // FIXME: impossible to test default value when field is modified in constructor
        assertEquals(expectedRobotTypes, actualRobotTypes, context, result ->
            "Value of field robotTypes in HackingRobot does not equal expected one");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testConstructor(boolean order) {
        List<String> expectedRobotTypes = order ?
            List.of("DIAGONAL", "TELEPORT", "OVERSTEP") :
            List.of("OVERSTEP", "DIAGONAL", "TELEPORT");
        int x = 2;
        int y = 2;
        Context.Builder<?> contextBuilder = contextBuilder()
            .add("x", x)
            .add("y", y);
        Robot hackingRobotInstance = (Robot) getHackingRobotInstance(x, y, order, contextBuilder);
        Context context = contextBuilder.add("HackingRobot instance", hackingRobotInstance).build();

        assertEquals(x, hackingRobotInstance.getX(), context, result ->
            "Incorrect value for parameter x passed to super constructor");
        assertEquals(y, hackingRobotInstance.getY(), context, result ->
            "Incorrect value for parameter y passed to super constructor");
        assertEquals(expectedRobotTypes,
            Arrays.stream(HACKING_ROBOT_ROBOT_TYPES_LINK.<Enum<?>[]>get(hackingRobotInstance))
                .map(Enum::name)
                .toList(),
            context,
            result -> "The values of array robotTypes in HackingRobot were not shifted correctly");
        assertEquals(expectedRobotTypes.getFirst(),
            HACKING_ROBOT_TYPE_LINK.<Enum<?>>get(hackingRobotInstance).name(),
            context,
            result -> "Value of field type in HackingRobot is incorrect");
    }

    @Test
    public void testGetType() {
        int x = 2;
        int y = 2;
        Context.Builder<?> contextBuilder = contextBuilder()
            .add("x", x)
            .add("y", y);
        Object hackingRobotInstance = getHackingRobotInstance(x, y, null, contextBuilder);
        Context baseContext = contextBuilder.add("HackingRobot instance", hackingRobotInstance).build();

        for (Enum<?> movementTypeConstant : getMovementTypeEnums()) {
            HACKING_ROBOT_TYPE_LINK.set(hackingRobotInstance, movementTypeConstant);
            Context context = contextBuilder()
                .add(baseContext)
                .add("Field 'type'", movementTypeConstant)
                .build();
            assertCallEquals(movementTypeConstant, () -> HACKING_ROBOT_GET_TYPE_LINK.invoke(hackingRobotInstance), context, result ->
                "The enum constant returned by getType is incorrect");
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    public void testGetNextType(int offset) {
        int x = 2;
        int y = 2;
        Context.Builder<?> contextBuilder = contextBuilder()
            .add("x", x)
            .add("y", y);
        Object hackingRobotInstance = getHackingRobotInstance(x, y, null, contextBuilder);
        Enum<?>[] movementTypeConstants = getMovementTypeEnums();
        Context context = contextBuilder
            .add("HackingRobot instance", hackingRobotInstance)
            .add("Field 'type'", movementTypeConstants[offset % movementTypeConstants.length])
            .add("Field 'robotTypes'", movementTypeConstants)
            .build();

        // Everyone's tough 'til runtime type safety checks show up
        Object typesafeRobotTypes = Array.newInstance(MOVEMENT_TYPE_LINK.reflection(), movementTypeConstants.length);
        System.arraycopy(movementTypeConstants, 0, typesafeRobotTypes, 0, movementTypeConstants.length);

        HACKING_ROBOT_TYPE_LINK.set(hackingRobotInstance, movementTypeConstants[offset % movementTypeConstants.length]);
        HACKING_ROBOT_ROBOT_TYPES_LINK.set(hackingRobotInstance, typesafeRobotTypes);
        assertCallEquals(movementTypeConstants[(offset + 1) % movementTypeConstants.length],
            () -> HACKING_ROBOT_GET_NEXT_TYPE_LINK.invoke(hackingRobotInstance),
            context,
            result -> "The value returned by getNextType is incorrect");
    }

    /**
     * Create a new HackingRobot object using {@link h03.Links#HACKING_ROBOT_CONSTRUCTOR_LINK}.
     *
     * @param x              the x coordinate
     * @param y              the y coordinate
     * @param order          the order parameter. May be {@code null}, in which case the constructor is called with
     *                       {@code false} first and then {@code true} if an exception was thrown
     * @param contextBuilder an optional context builder to append the {@code order} parameter to
     * @return a new HackingRobot instance
     */
    private Object getHackingRobotInstance(int x, int y, @Nullable Boolean order, @Nullable Context.Builder<?> contextBuilder) {
        Consumer<Boolean> appendContext = b -> {
            if (contextBuilder != null) {
                contextBuilder.add("order", b);
            }
        };
        Object hackingRobotInstance;

        if (order != null) {
            try {
                hackingRobotInstance = HACKING_ROBOT_CONSTRUCTOR_LINK.invoke(x, y, order);
                appendContext.accept(order);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        } else {
            try {
                hackingRobotInstance = HACKING_ROBOT_CONSTRUCTOR_LINK.invoke(x, y, false);
                appendContext.accept(false);
            } catch (Throwable t1) {
                System.err.printf("Could not invoke HackingRobot's constructor with params (%d, %d, false):%n%s%n", x, y, t1.getMessage());
                try {
                    hackingRobotInstance = HACKING_ROBOT_CONSTRUCTOR_LINK.invoke(x, y, true);
                    appendContext.accept(true);
                } catch (Throwable t2) {
                    System.err.printf("Could not invoke HackingRobot's constructor with params (%d, %d, true):%n%s%n", x, y, t2.getMessage());
                    throw new RuntimeException("Could not create an instance of HackingRobot");
                }
            }
        }

        return hackingRobotInstance;
    }
}
