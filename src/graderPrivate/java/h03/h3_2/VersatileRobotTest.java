package h03.h3_2;

import fopbot.World;
import h03.robots.HackingRobot;
import h03.robots.MovementType;
import h03.robots.VersatileRobot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Type;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.SubmissionExecutionHandler;
import org.tudalgo.algoutils.transform.util.ClassHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class VersatileRobotTest {

    private final SubmissionExecutionHandler executionHandler = SubmissionExecutionHandler.getInstance();

    private static Constructor<?> versatileRobotConstructor;
    private static Method shuffleMethod;
    private static Method shuffleWithParamMethod;

    private final MovementType getTypeReturnValue = MovementType.DIAGONAL;
    private final MovementType getNextTypeReturnValue = MovementType.OVERSTEP;
    private final int getRandomReturnValue = 1;
    private final boolean shuffleReturnValue = false;

    private final int x = 0;
    private final int y = 4;
    private final boolean order = false;
    private final boolean exchange = false;
    private final Context context = contextBuilder()
        .add("x", x)
        .add("y", y)
        .add("order", order)
        .add("exchange", exchange)
        .add("super.getType() return value", getTypeReturnValue)
        .add("super.getNextType() return value", getNextTypeReturnValue)
        .add("super.getRandom(int) return value", getRandomReturnValue)
        .add("super.shuffle(int) return value", shuffleReturnValue)
        .build();

    @BeforeAll
    public static void setup() {
        World.setSize(5, 5);

        try {
            versatileRobotConstructor = VersatileRobot.class.getDeclaredConstructor(int.class, int.class, boolean.class, boolean.class);
            shuffleMethod = VersatileRobot.class.getDeclaredMethod("shuffle");
            shuffleWithParamMethod = VersatileRobot.class.getDeclaredMethod("shuffle", int.class);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    private void setupEnvironment(Executable executable) throws ReflectiveOperationException {
        executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("getType"),
            invocation -> getTypeReturnValue);
        executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("getNextType"),
            invocation -> getNextTypeReturnValue);
        executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("getRandom", int.class),
            invocation -> getRandomReturnValue);
        executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("shuffle", int.class),
            invocation -> shuffleReturnValue);
        executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("shuffle"), invocation -> null);
        executionHandler.disableMethodDelegation(executable);
    }

    @AfterEach
    public void tearDown() {
        executionHandler.resetMethodInvocationLogging();
        executionHandler.resetMethodSubstitution();
        executionHandler.resetMethodDelegation();
    }

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = SubmissionExecutionHandler.getOriginalClassHeader(VersatileRobot.class);

        assertTrue(Modifier.isPublic(originalClassHeader.access()), emptyContext(), result ->
            "Class VersatileRobot was not declared public");
        assertEquals(Type.getInternalName(HackingRobot.class), originalClassHeader.superName(), emptyContext(), result ->
            "Class VersatileRobot does not extend HackingRobot");
    }

    @Test
    public void testConstructor() throws ReflectiveOperationException {
        setupEnvironment(versatileRobotConstructor);

        VersatileRobot instance = callObject(() -> new VersatileRobot(x, y, order, exchange), context, result ->
            "An exception occurred while invoking constructor of class VersatileRobot");

        assertEquals(x, instance.getX(), context, result -> "The x-coordinate of this VersatileRobot is incorrect");
        assertEquals(x, instance.getY(), context, result -> "The y-coordinate of this VersatileRobot is incorrect");
    }

    @Test
    public void testShuffleWithParams() throws ReflectiveOperationException {
        setupEnvironment(shuffleWithParamMethod);

        VersatileRobot instance = callObject(() -> new VersatileRobot(x, y, order, exchange), context, result ->
            "An exception occurred while invoking constructor of class VersatileRobot");
        instance.setY(y);
        call(() -> instance.shuffle(1), context, result -> "An exception occurred while invoking shuffle(int)");

        assertEquals(x, instance.getX(), context, result -> "The x-coordinate of this VersatileRobot is incorrect");
        assertEquals(x, instance.getY(), context, result -> "The y-coordinate of this VersatileRobot is incorrect");
    }

    @Test
    public void testShuffleNoParams() throws ReflectiveOperationException {
        setupEnvironment(shuffleMethod);

        VersatileRobot instance = callObject(() -> new VersatileRobot(x, y, order, exchange), context, result ->
            "An exception occurred while invoking constructor of class VersatileRobot");
        instance.setY(y);
        call(instance::shuffle, context, result -> "An exception occurred while invoking shuffle()");

        assertEquals(x, instance.getX(), context, result -> "The x-coordinate of this VersatileRobot is incorrect");
        assertEquals(x, instance.getY(), context, result -> "The y-coordinate of this VersatileRobot is incorrect");
    }
}
