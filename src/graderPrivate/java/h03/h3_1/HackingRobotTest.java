package h03.h3_1;

import fopbot.Robot;
import fopbot.World;
import h03.robots.HackingRobot;
import h03.robots.MovementType;
import kotlin.Triple;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.objectweb.asm.Type;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.headers.FieldHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class HackingRobotTest {

    @BeforeAll
    public static void setup() {
        World.setSize(5, 5);
    }

    @AfterEach
    public void tearDown() {
        resetAll();
    }

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = getOriginalClassHeader(HackingRobot.class);

        assertTrue(Modifier.isPublic(originalClassHeader.access()), emptyContext(), result ->
            "Class HackingRobot is not declared public");
        assertEquals(Type.getInternalName(Robot.class), originalClassHeader.superName(), emptyContext(), result ->
            "Class HackingRobot does not have correct superclass");
    }

    @Test
    public void testFields() {
        FieldHeader type = assertNotNull(getOriginalFieldHeader(HackingRobot.class, "type"), emptyContext(),
            result -> "Field 'type' does not exist");
        assertTrue(Modifier.isPrivate(type.modifiers()), emptyContext(), result ->
            "Field 'type' in HackingRobot is not declared private");
        assertFalse(Modifier.isStatic(type.modifiers()), emptyContext(), result ->
            "Field 'type' in HackingRobot is declared static");
        assertEquals(Type.getDescriptor(MovementType.class), type.descriptor(), emptyContext(), result ->
            "Field 'type' in HackingRobot does not have correct type");

        FieldHeader robotTypes = assertNotNull(getOriginalFieldHeader(HackingRobot.class, "robotTypes"), emptyContext(),
            result -> "Field 'robotTypes' does not exist");
        assertTrue(Modifier.isPrivate(robotTypes.modifiers()), emptyContext(), result ->
            "Field robotTypes in HackingRobot is not declared private");
        assertFalse(Modifier.isStatic(robotTypes.modifiers()), emptyContext(), result ->
            "Field robotTypes in HackingRobot is declared static");
        assertEquals(Type.getDescriptor(MovementType[].class), robotTypes.descriptor(), emptyContext(), result ->
            "Field robotTypes in HackingRobot does not have correct type");

        // NOTE: it's impossible to test for default value when field is modified in constructor
    }

    @Test
    public void testConstructorHeader() {
        MethodHeader constructor = assertNotNull(getOriginalMethodHeader(HackingRobot.class, int.class, int.class, boolean.class),
            emptyContext(),
            result -> "Constructor 'HackingRobot(int, int, boolean)' does not exist");
        assertTrue(Modifier.isPublic(constructor.modifiers()), emptyContext(), result ->
            "Constructor 'HackingRobot(int, int, boolean)' is not declared public");
    }

    @Test
    public void testConstructorSuperCall() {
        Delegation.disable(MethodHeader.of(HackingRobot.class, int.class, int.class, boolean.class));

        int x = 2;
        int y = 2;
        Context.Builder<?> contextBuilder = contextBuilder()
            .add("x", x)
            .add("y", y);
        Robot hackingRobotInstance = getHackingRobotInstance(x, y, false, contextBuilder);
        Context context = contextBuilder.add("HackingRobot instance", hackingRobotInstance).build();

        assertEquals(x, hackingRobotInstance.getX(), context, result ->
            "Incorrect value for parameter x passed to super constructor");
        assertEquals(y, hackingRobotInstance.getY(), context, result ->
            "Incorrect value for parameter y passed to super constructor");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testConstructorSetsFields(boolean order) {
        Delegation.disable(MethodHeader.of(HackingRobot.class, int.class, int.class, boolean.class));

        List<String> expectedRobotTypes = order ?
            List.of("DIAGONAL", "TELEPORT", "OVERSTEP") :
            List.of("OVERSTEP", "DIAGONAL", "TELEPORT");
        int x = 2;
        int y = 2;
        Context.Builder<?> contextBuilder = contextBuilder()
            .add("x", x)
            .add("y", y);
        Robot hackingRobotInstance = getHackingRobotInstance(x, y, order, contextBuilder);
        Context context = contextBuilder.add("HackingRobot instance", hackingRobotInstance).build();

        assertEquals(expectedRobotTypes,
            Arrays.stream(FieldHeader.of(HackingRobot.class, "robotTypes").<MovementType[]>getValue(hackingRobotInstance))
                .map(Enum::name)
                .toList(),
            context,
            result -> "The values of array robotTypes in HackingRobot were not shifted correctly");
    }

    @Test
    public void testGetType() {
        Delegation.disable(MethodHeader.of(HackingRobot.class, "getType"));

        int x = 2;
        int y = 2;
        Context.Builder<?> contextBuilder = contextBuilder()
            .add("x", x)
            .add("y", y);
        HackingRobot hackingRobotInstance = getHackingRobotInstance(x, y, null, contextBuilder);
        Context baseContext = contextBuilder.add("HackingRobot instance", hackingRobotInstance).build();

        for (MovementType movementType : MovementType.values()) {
            FieldHeader.of(HackingRobot.class, "type").setValue(hackingRobotInstance, movementType);
            Context context = contextBuilder()
                .add(baseContext)
                .add("Field 'type'", movementType)
                .build();
            assertCallEquals(movementType, hackingRobotInstance::getType, context, result ->
                "The enum constant returned by 'getType()' is incorrect");
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testGetNextTypeNoMod(int offset) {
        testGetNextTypeMod(offset);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5, 6})
    public void testGetNextTypeMod(int offset) {
        testGetNextType(offset);
    }

/*
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
*/

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testShuffleWithParams_SetField(int index) {
        MovementType[] movementTypeConstants = MovementType.values();
        Triple<Context, HackingRobot, Boolean> invocationResult = testShuffleWithParams(index);

        assertEquals(movementTypeConstants[index],
            FieldHeader.of(HackingRobot.class, "type").getValue(invocationResult.getSecond()),
            invocationResult.getFirst(),
            result -> "Field 'type' in HackingRobot was not set to the correct value");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testShuffleWithParams_ReturnValue(int index) {
        Triple<Context, HackingRobot, Boolean> invocationResult = testShuffleWithParams(index);

        assertEquals(index != 0, invocationResult.getThird(), invocationResult.getFirst(), result ->
            "Method 'shuffle(int)' in HackingRobot did not return the expected value");
    }

    @Test
    public void testShuffleNoParams() {
        // Header
        MethodHeader shuffle = assertNotNull(getOriginalMethodHeader(HackingRobot.class, "shuffle"), emptyContext(),
            result -> "Method 'shuffle()' does not exist");

        assertTrue(Modifier.isPublic(shuffle.modifiers()), emptyContext(), result ->
            "Method 'shuffle()' in HackingRobot was not declared public");
        assertEquals(Type.VOID_TYPE, Type.getReturnType(shuffle.descriptor()), emptyContext(), result ->
            "Method 'shuffle()' has incorrect return type");

        // Body
        int limit = 5;
        AtomicInteger counter = new AtomicInteger(0);
        Delegation.disable(MethodHeader.of(HackingRobot.class, "shuffle"));
        Substitution.enable(MethodHeader.of(HackingRobot.class, "shuffle", int.class),
            invocation -> counter.incrementAndGet() >= limit);

        Context.Builder<?> contextBuilder = contextBuilder()
            .add("x", 0)
            .add("y", 0);
        HackingRobot hackingRobotInstance = getHackingRobotInstance(0, 0, null, contextBuilder);
        Context context = contextBuilder.add("HackingRobot instance", hackingRobotInstance).build();

        call(hackingRobotInstance::shuffle, context, result ->
            "An exception occurred while invoking 'shuffle()' in HackingRobot");
        assertEquals(limit, counter.get(), context, result ->
            "Method 'shuffle()' in HackingRobot did not return after 'shuffle(int)' returned true / was invoked %d times".formatted(limit));
    }

    /**
     * Create a new HackingRobot instance.
     *
     * @param x              the x coordinate
     * @param y              the y coordinate
     * @param order          the order parameter. May be {@code null}, in which case the constructor is called with
     *                       {@code false} first and then {@code true} if an exception was thrown
     * @param contextBuilder an optional context builder to append the {@code order} parameter to
     * @return a new HackingRobot instance
     */
    private HackingRobot getHackingRobotInstance(int x, int y, @Nullable Boolean order, @Nullable Context.Builder<?> contextBuilder) {
        Consumer<Boolean> appendContext = b -> {
            if (contextBuilder != null) {
                contextBuilder.add("order", b);
            }
        };
        HackingRobot hackingRobotInstance;

        if (order != null) {
            hackingRobotInstance = new HackingRobot(x, y, order);
            appendContext.accept(order);
        } else {
            try {
                hackingRobotInstance = new HackingRobot(x, y, false);
                appendContext.accept(false);
            } catch (Throwable t1) {
                System.err.printf("Could not invoke HackingRobot's constructor with params (%d, %d, false):%n%s%n", x, y, t1.getMessage());
                try {
                    hackingRobotInstance = new HackingRobot(x, y, true);
                    appendContext.accept(true);
                } catch (Throwable t2) {
                    System.err.printf("Could not invoke HackingRobot's constructor with params (%d, %d, true):%n%s%n", x, y, t2.getMessage());
                    throw new RuntimeException("Could not create an instance of HackingRobot");
                }
            }
        }

        return hackingRobotInstance;
    }

    private void testGetNextType(int offset) {
        Delegation.disable(MethodHeader.of(HackingRobot.class, "getNextType"));

        int x = 2;
        int y = 2;
        Context.Builder<?> contextBuilder = contextBuilder()
            .add("x", x)
            .add("y", y);
        HackingRobot hackingRobotInstance = getHackingRobotInstance(x, y, null, contextBuilder);
        MovementType[] movementTypeConstants = MovementType.values();
        Context context = contextBuilder
            .add("HackingRobot instance", hackingRobotInstance)
            .add("Field 'type'", movementTypeConstants[offset % movementTypeConstants.length])
            .add("Field 'robotTypes'", movementTypeConstants)
            .build();

        FieldHeader.of(HackingRobot.class, "type")
            .setValue(hackingRobotInstance, movementTypeConstants[offset % movementTypeConstants.length]);
        FieldHeader.of(HackingRobot.class, "robotTypes")
            .setValue(hackingRobotInstance, movementTypeConstants);
        assertCallEquals(movementTypeConstants[(offset + 1) % movementTypeConstants.length],
            hackingRobotInstance::getNextType,
            context,
            result -> "The value returned by 'getNextType()' is incorrect");
    }

    private Triple<Context, HackingRobot, Boolean> testShuffleWithParams(int index) {
        Substitution.enable(MethodHeader.of(HackingRobot.class, "getRandom", int.class), invocation -> index);
        Delegation.disable(MethodHeader.of(HackingRobot.class, "shuffle", int.class));

        MovementType[] movementTypeConstants = MovementType.values();
        Context.Builder<?> contextBuilder = contextBuilder()
            .add("x", 0)
            .add("y", 0);
        HackingRobot hackingRobotInstance = getHackingRobotInstance(0, 0, null, contextBuilder);
        contextBuilder.add("HackingRobot instance", hackingRobotInstance);

        FieldHeader.of(HackingRobot.class, "type").setValue(hackingRobotInstance, movementTypeConstants[0]);
        FieldHeader.of(HackingRobot.class, "robotTypes").setValue(hackingRobotInstance, movementTypeConstants);
        Context context = contextBuilder
            .add("Field 'type'", movementTypeConstants[0])
            .add("Field 'robotTypes'", movementTypeConstants)
            .add("getRandom(int) return value", index)
            .build();

        return new Triple<>(context, hackingRobotInstance, callObject(() -> hackingRobotInstance.shuffle(1), context, result ->
            "An exception occurred while invoking shuffle(int)"));
    }
}
