package h03.h3_1;

import fopbot.Robot;
import fopbot.World;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.EnumConstantLink;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
        assertTrue((HACKING_ROBOT_LINK.get().modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Class HackingRobot is not declared public");
        assertEquals(Robot.class, HACKING_ROBOT_LINK.get().superType().reflection(), emptyContext(), result ->
            "Class HackingRobot does not have correct superclass");
    }

    @Test
    public void testFields() {
        assertTrue((HACKING_ROBOT_TYPE_LINK.get().modifiers() & Modifier.PRIVATE) != 0, emptyContext(), result ->
            "Field type in HackingRobot is not declared private");
        assertFalse((HACKING_ROBOT_TYPE_LINK.get().modifiers() & Modifier.STATIC) != 0, emptyContext(), result ->
            "Field type in HackingRobot is declared static");
        assertEquals(MOVEMENT_TYPE_LINK.get().reflection(), HACKING_ROBOT_TYPE_LINK.get().type().reflection(), emptyContext(), result ->
            "Field type in HackingRobot does not have correct type");

        assertTrue((HACKING_ROBOT_ROBOT_TYPES_LINK.get().modifiers() & Modifier.PRIVATE) != 0, emptyContext(), result ->
            "Field robotTypes in HackingRobot is not declared private");
        assertFalse((HACKING_ROBOT_ROBOT_TYPES_LINK.get().modifiers() & Modifier.STATIC) != 0, emptyContext(), result ->
            "Field robotTypes in HackingRobot is declared static");
        assertEquals(MOVEMENT_TYPE_LINK.get().reflection().arrayType(), HACKING_ROBOT_ROBOT_TYPES_LINK.get().type().reflection(), emptyContext(), result ->
            "Field robotTypes in HackingRobot does not have correct type");

        Context.Builder<?> contextBuilder = contextBuilder()
            .add("x", 0)
            .add("y", 0);
        Object hackingRobotInstance = getHackingRobotInstance(0, 0, null, contextBuilder);
        List<String> expectedRobotTypes = List.of("TELEPORT", "OVERSTEP", "DIAGONAL");
        List<String> actualRobotTypes = Arrays.stream(HACKING_ROBOT_ROBOT_TYPES_LINK.get().<Enum<?>[]>get(hackingRobotInstance))
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
            Arrays.stream(HACKING_ROBOT_ROBOT_TYPES_LINK.get().<Enum<?>[]>get(hackingRobotInstance))
                .map(Enum::name)
                .toList(),
            context,
            result -> "The values of array robotTypes in HackingRobot were not shifted correctly");
        assertEquals(expectedRobotTypes.getFirst(),
            HACKING_ROBOT_TYPE_LINK.get().<Enum<?>>get(hackingRobotInstance).name(),
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

        for (EnumConstantLink movementTypeConstantLink : MOVEMENT_TYPE_CONSTANTS.get()) {
            Enum<?> movementTypeConstant = movementTypeConstantLink.constant();
            HACKING_ROBOT_TYPE_LINK.get().set(hackingRobotInstance, movementTypeConstant);
            Context context = contextBuilder()
                .add(baseContext)
                .add("Field 'type'", movementTypeConstant)
                .build();
            assertCallEquals(movementTypeConstant, () -> HACKING_ROBOT_GET_TYPE_LINK.get().invoke(hackingRobotInstance), context, result ->
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
        EnumConstantLink[] movementTypeConstantsLinks = MOVEMENT_TYPE_CONSTANTS.get();
        Enum<?>[] movementTypeConstants = Arrays.stream(movementTypeConstantsLinks)
            .map(EnumConstantLink::constant)
            .toArray(Enum[]::new);
        Context context = contextBuilder
            .add("HackingRobot instance", hackingRobotInstance)
            .add("Field 'type'", movementTypeConstants[offset % movementTypeConstants.length])
            .add("Field 'robotTypes'", movementTypeConstants)
            .build();

        // Everyone's tough 'til runtime type safety checks show up
        Object typesafeRobotTypes = Array.newInstance(MOVEMENT_TYPE_LINK.get().reflection(), movementTypeConstants.length);
        System.arraycopy(movementTypeConstants, 0, typesafeRobotTypes, 0, movementTypeConstants.length);

        HACKING_ROBOT_TYPE_LINK.get().set(hackingRobotInstance, movementTypeConstants[offset % movementTypeConstants.length]);
        HACKING_ROBOT_ROBOT_TYPES_LINK.get().set(hackingRobotInstance, typesafeRobotTypes);
        assertCallEquals(movementTypeConstants[(offset + 1) % movementTypeConstants.length],
            () -> HACKING_ROBOT_GET_NEXT_TYPE_LINK.get().invoke(hackingRobotInstance),
            context,
            result -> "The value returned by getNextType is incorrect");
    }

    @Test
    public void testGetRandom() throws Throwable {
        // Header
        assertTrue((HACKING_ROBOT_GET_RANDOM_LINK.get().modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Method getRandom(int) in HackingRobot was not declared public");
        assertEquals(int.class, HACKING_ROBOT_GET_RANDOM_LINK.get().returnType().reflection(), emptyContext(), result ->
            "Method getRandom(int) has incorrect return type");

        // Code
        Object hackingRobotInstance = Mockito.mock(HACKING_ROBOT_LINK.get().reflection(), Mockito.CALLS_REAL_METHODS);
        List<Integer> returnedInts = new LinkedList<>();
        for (int i = 50; i <= 100; i++) {
            int result = HACKING_ROBOT_GET_RANDOM_LINK.get().invoke(hackingRobotInstance, i);
            final int finalI = i;
            assertTrue(result >= 0 && result < i, contextBuilder().add("limit", i).build(), r ->
                "Result of getRandom(%d) is not within bounds [0, %d]".formatted(finalI, finalI - 1));
            returnedInts.add(result);
        }

        assertTrue(returnedInts.stream().anyMatch(i -> i >= 3), emptyContext(), result ->
            "50 invocations of getRandom(int) didn't return any number > 2, which is extremely unlikely");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testShuffle1(int index) {
        // Header
        assertTrue((HACKING_ROBOT_SHUFFLE1_LINK.get().modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Method shuffle(int) in HackingRobot was not declared public");
        assertEquals(boolean.class, HACKING_ROBOT_SHUFFLE1_LINK.get().returnType().reflection(), emptyContext(), result ->
            "Method shuffle(int) has incorrect return type");

        // Code
        Object hackingRobotInstance = Mockito.mock(HACKING_ROBOT_LINK.get().reflection(), invocation -> {
            if (invocation.getMethod().equals(HACKING_ROBOT_GET_RANDOM_LINK.get().reflection())) {
                return index;
            } else {
                return invocation.callRealMethod();
            }
        });
        EnumConstantLink[] movementTypeConstantLinks = MOVEMENT_TYPE_CONSTANTS.get();
        Enum<?>[] movementTypeConstants = Arrays.stream(movementTypeConstantLinks)
            .map(EnumConstantLink::constant)
            .toArray(Enum[]::new);
        Object typesafeRobotTypes = Array.newInstance(MOVEMENT_TYPE_LINK.get().reflection(), movementTypeConstants.length);
        System.arraycopy(movementTypeConstants, 0, typesafeRobotTypes, 0, movementTypeConstants.length);

        HACKING_ROBOT_TYPE_LINK.get().set(hackingRobotInstance, movementTypeConstants[0]);
        HACKING_ROBOT_ROBOT_TYPES_LINK.get().set(hackingRobotInstance, typesafeRobotTypes);
        Context context = contextBuilder()
            .add("Field 'type'", movementTypeConstants[0])
            .add("Field 'robotTypes'", movementTypeConstants)
            .add("getRandom(int) return value", index)
            .build();

        assertCallEquals(index != 0, () -> HACKING_ROBOT_SHUFFLE1_LINK.get().invoke(hackingRobotInstance, 1), context, result ->
            "Method 'shuffle(int)' in HackingRobot did not return the expected value");
        assertEquals(movementTypeConstants[index], HACKING_ROBOT_TYPE_LINK.get().get(hackingRobotInstance), context, result ->
            "Field 'type' in HackingRobot was not set to the correct value");
    }

    @Test
    public void testShuffle2() {
        // Header
        assertTrue((HACKING_ROBOT_SHUFFLE2_LINK.get().modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Method shuffle() in HackingRobot was not declared public");
        assertEquals(void.class, HACKING_ROBOT_SHUFFLE2_LINK.get().returnType().reflection(), emptyContext(), result ->
            "Method shuffle() has incorrect return type");

        // Code
        int limit = 5;
        AtomicInteger counter = new AtomicInteger(0);
        Object hackingRobotInstance = Mockito.mock(HACKING_ROBOT_LINK.get().reflection(), invocation -> {
            if (invocation.getMethod().equals(HACKING_ROBOT_SHUFFLE1_LINK.get().reflection())) {
                return counter.incrementAndGet() >= limit;
            } else {
                return invocation.callRealMethod();
            }
        });
        call(() -> HACKING_ROBOT_SHUFFLE2_LINK.get().invoke(hackingRobotInstance), emptyContext(), result ->
            "An exception occurred while invoking shuffle() in HackingRobot");
        assertEquals(limit, counter.get(), emptyContext(), result ->
            "Method shuffle() in HackingRobot did not return after shuffle(int) returned true / was invoked %d times".formatted(limit));
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
                hackingRobotInstance = HACKING_ROBOT_CONSTRUCTOR_LINK.get().invoke(x, y, order);
                appendContext.accept(order);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        } else {
            try {
                hackingRobotInstance = HACKING_ROBOT_CONSTRUCTOR_LINK.get().invoke(x, y, false);
                appendContext.accept(false);
            } catch (Throwable t1) {
                System.err.printf("Could not invoke HackingRobot's constructor with params (%d, %d, false):%n%s%n", x, y, t1.getMessage());
                try {
                    hackingRobotInstance = HACKING_ROBOT_CONSTRUCTOR_LINK.get().invoke(x, y, true);
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
