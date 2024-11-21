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
import org.mockito.Mockito;
import org.objectweb.asm.Type;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.SubmissionExecutionHandler;
import org.tudalgo.algoutils.transform.util.ClassHeader;
import org.tudalgo.algoutils.transform.util.FieldHeader;
import org.tudalgo.algoutils.transform.util.MethodHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.EnumConstantLink;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static h03.Links.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class HackingRobotTest {

    private final SubmissionExecutionHandler executionHandler = SubmissionExecutionHandler.getInstance();

    @BeforeAll
    public static void setup() {
        World.setSize(5, 5);
    }

    @AfterEach
    public void tearDown() {
        executionHandler.resetMethodInvocationLogging();
        executionHandler.resetMethodSubstitution();
        executionHandler.resetMethodDelegation();
    }

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = SubmissionExecutionHandler.getOriginalClassHeader(HackingRobot.class);

        assertTrue(Modifier.isPublic(originalClassHeader.access()), emptyContext(), result ->
            "Class HackingRobot is not declared public");
        assertEquals(Type.getInternalName(Robot.class), originalClassHeader.superName(), emptyContext(), result ->
            "Class HackingRobot does not have correct superclass");
    }

    @Test
    public void testFields() throws NoSuchFieldException {
        Set<FieldHeader> originalFieldHeaders = SubmissionExecutionHandler.getOriginalFieldHeaders(HackingRobot.class);

        FieldHeader typeFieldHeader = originalFieldHeaders.stream()
            .filter(fieldHeader -> fieldHeader.name().equals("type"))
            .findFirst()
            .orElseThrow(() -> new NoSuchFieldException("Field 'type' does not exist"));
        assertTrue(Modifier.isPrivate(typeFieldHeader.access()), emptyContext(), result ->
            "Field type in HackingRobot is not declared private");
        assertFalse(Modifier.isStatic(typeFieldHeader.access()), emptyContext(), result ->
            "Field type in HackingRobot is declared static");
        assertEquals(Type.getDescriptor(MovementType.class), typeFieldHeader.descriptor(), emptyContext(), result ->
            "Field type in HackingRobot does not have correct type");

        FieldHeader robotTypes = originalFieldHeaders.stream()
            .filter(fieldHeader -> fieldHeader.name().equals("robotTypes"))
            .findFirst()
            .orElseThrow(() -> new NoSuchFieldException("Field 'robotTypes' does not exist"));
        assertTrue(Modifier.isPrivate(robotTypes.access()), emptyContext(), result ->
            "Field robotTypes in HackingRobot is not declared private");
        assertFalse(Modifier.isStatic(robotTypes.access()), emptyContext(), result ->
            "Field robotTypes in HackingRobot is declared static");
        assertEquals(Type.getDescriptor(MovementType[].class), robotTypes.descriptor(), emptyContext(), result ->
            "Field robotTypes in HackingRobot does not have correct type");

        // NOTE: it's impossible to test for default value when field is modified in constructor
    }

    @Test
    public void testConstructorHeader() throws NoSuchMethodException {
        Constructor<HackingRobot> constructor = HackingRobot.class.getDeclaredConstructor(int.class, int.class, boolean.class);
        MethodHeader constructorMethodHeader = new MethodHeader(constructor);
        MethodHeader originalConstructorHeader = SubmissionExecutionHandler.getOriginalMethodHeaders(HackingRobot.class)
            .stream()
            .filter(constructorMethodHeader::equals)
            .findFirst()
            .orElseThrow(() -> new NoSuchMethodException("Constructor 'HackingRobot(int, int, boolean)' does not exist"));

        assertTrue(Modifier.isPublic(originalConstructorHeader.access()), emptyContext(), result ->
            "Constructor 'HackingRobot(int, int, boolean)' is not declared public");
    }

    @Test
    public void testConstructorSuperCall() throws NoSuchMethodException {
        executionHandler.disableMethodDelegation(HackingRobot.class.getDeclaredConstructor(int.class, int.class, boolean.class));

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
    public void testConstructorSetsFields(boolean order) throws ReflectiveOperationException {
        executionHandler.disableMethodDelegation(HackingRobot.class.getDeclaredConstructor(int.class, int.class, boolean.class));
        Field robotTypesField = HackingRobot.class.getDeclaredField("robotTypes");
        robotTypesField.trySetAccessible();

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
            Arrays.stream((MovementType[]) robotTypesField.get(hackingRobotInstance))
                .map(Enum::name)
                .toList(),
            context,
            result -> "The values of array robotTypes in HackingRobot were not shifted correctly");
    }

    @Test
    public void testGetType() throws ReflectiveOperationException {
        executionHandler.disableMethodDelegation(HackingRobot.class.getDeclaredMethod("getType"));
        Field typeField = HackingRobot.class.getDeclaredField("type");
        typeField.trySetAccessible();

        int x = 2;
        int y = 2;
        Context.Builder<?> contextBuilder = contextBuilder()
            .add("x", x)
            .add("y", y);
        HackingRobot hackingRobotInstance = getHackingRobotInstance(x, y, null, contextBuilder);
        Context baseContext = contextBuilder.add("HackingRobot instance", hackingRobotInstance).build();

        for (MovementType movementType : MovementType.values()) {
            typeField.set(hackingRobotInstance, movementType);
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
    public void testGetNextTypeNoMod(int offset) throws ReflectiveOperationException {
        testGetNextTypeMod(offset);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5, 6})
    public void testGetNextTypeMod(int offset) throws ReflectiveOperationException {
        testGetNextType(offset);
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
    public void testShuffleWithParams_SetField(int index) {
        Enum<?>[] movementTypeConstants = Arrays.stream(MOVEMENT_TYPE_CONSTANTS.get())
            .map(EnumConstantLink::constant)
            .toArray(Enum[]::new);
        Triple<Context, Object, Boolean> invocationResult = testShuffleWithParams(index);

        assertEquals(movementTypeConstants[index], HACKING_ROBOT_TYPE_LINK.get().get(invocationResult.getSecond()), invocationResult.getFirst(), result ->
            "Field 'type' in HackingRobot was not set to the correct value");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testShuffleWithParams_ReturnValue(int index) {
        Triple<Context, Object, Boolean> invocationResult = testShuffleWithParams(index);

        assertEquals(index != 0, invocationResult.getThird(), invocationResult.getFirst(), result ->
            "Method 'shuffle(int)' in HackingRobot did not return the expected value");
    }

    @Test
    public void testShuffleNoParams() {
        // Header
        assertTrue((HACKING_ROBOT_SHUFFLE2_LINK.get().modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Method shuffle() in HackingRobot was not declared public");
        assertEquals(void.class, HACKING_ROBOT_SHUFFLE2_LINK.get().returnType().reflection(), emptyContext(), result ->
            "Method shuffle() has incorrect return type");

        // Body
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

    private void testGetNextType(int offset) throws ReflectiveOperationException {
        executionHandler.disableMethodDelegation(HackingRobot.class.getDeclaredMethod("getNextType"));
        Field typeField = HackingRobot.class.getDeclaredField("type");
        Field robotTypesField = HackingRobot.class.getDeclaredField("robotTypes");
        typeField.trySetAccessible();
        robotTypesField.trySetAccessible();

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

        typeField.set(hackingRobotInstance, movementTypeConstants[offset % movementTypeConstants.length]);
        robotTypesField.set(hackingRobotInstance, movementTypeConstants);
        assertCallEquals(movementTypeConstants[(offset + 1) % movementTypeConstants.length],
            hackingRobotInstance::getNextType,
            context,
            result -> "The value returned by 'getNextType()' is incorrect");
    }

    private Triple<Context, Object, Boolean> testShuffleWithParams(int index) {
        Object hackingRobotInstance = Mockito.mock(HACKING_ROBOT_LINK.get().reflection(), invocation -> {
            if (invocation.getMethod().equals(HACKING_ROBOT_GET_RANDOM_LINK.get().reflection())) {
                return index;
            } else {
                return invocation.callRealMethod();
            }
        });
        Enum<?>[] movementTypeConstants = Arrays.stream(MOVEMENT_TYPE_CONSTANTS.get())
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

        return new Triple<>(context, hackingRobotInstance, callObject(() -> HACKING_ROBOT_SHUFFLE1_LINK.get().invoke(hackingRobotInstance, 1), context, result ->
            "An exception occurred while invoking shuffle(int)"));
    }
}
