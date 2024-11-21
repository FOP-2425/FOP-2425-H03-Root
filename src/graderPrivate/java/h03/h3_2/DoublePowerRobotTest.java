package h03.h3_2;

import fopbot.World;
import h03.TestConstants;
import h03.robots.DoublePowerRobot;
import h03.robots.HackingRobot;
import h03.robots.MovementType;
import net.bytebuddy.jar.asm.Type;
import org.apache.commons.lang3.function.TriConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.SubmissionExecutionHandler;
import org.tudalgo.algoutils.transform.util.ClassHeader;
import org.tudalgo.algoutils.transform.util.FieldHeader;
import org.tudalgo.algoutils.transform.util.MethodHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
public class DoublePowerRobotTest {

    private final SubmissionExecutionHandler executionHandler = SubmissionExecutionHandler.getInstance();

    private static Field doublePowerTypesField;
    private static Constructor<?> versatileRobotConstructor;
    private static Method shuffleMethod;
    private static Method shuffleWithParamMethod;

    @BeforeAll
    public static void setup() {
        World.setSize(5, 5);

        try {
            doublePowerTypesField = DoublePowerRobot.class.getDeclaredField("doublePowerTypes");
            versatileRobotConstructor = DoublePowerRobot.class.getDeclaredConstructor(int.class, int.class, boolean.class);
            shuffleMethod = DoublePowerRobot.class.getDeclaredMethod("shuffle");
            shuffleWithParamMethod = DoublePowerRobot.class.getDeclaredMethod("shuffle", int.class);

            doublePowerTypesField.trySetAccessible();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        executionHandler.resetMethodInvocationLogging();
        executionHandler.resetMethodSubstitution();
        executionHandler.resetMethodDelegation();
    }

    @Test
    public void testClassHeader() {
        ClassHeader originalClassHeader = SubmissionExecutionHandler.getOriginalClassHeader(DoublePowerRobot.class);

        assertTrue(Modifier.isPublic(originalClassHeader.access()), emptyContext(), result ->
            "Class DoublePowerRobot is not declared public");
        assertEquals(Type.getInternalName(HackingRobot.class), originalClassHeader.superName(), emptyContext(), result ->
            "Class DoublePowerRobot does not have correct superclass");
    }

    @Test
    public void testFields() throws NoSuchFieldException {
        FieldHeader doublePowerTypesFieldHeader = new FieldHeader(doublePowerTypesField);
        FieldHeader originalFieldHeader = SubmissionExecutionHandler.getOriginalFieldHeaders(DoublePowerRobot.class)
            .stream()
            .filter(doublePowerTypesFieldHeader::equals)
            .findFirst()
            .orElseThrow(() -> new NoSuchFieldException("Field 'doublePowerTypes' does not exist"));

        assertEquals(Type.getDescriptor(MovementType[].class), originalFieldHeader.descriptor(), emptyContext(), result ->
            "Field 'doublePowerTypes' in DoublePowerRobot does not have correct type");
    }

    @Test
    public void testMethodHeaders() throws NoSuchMethodException {
        Set<MethodHeader> originalMethodHeaders = SubmissionExecutionHandler.getOriginalMethodHeaders(DoublePowerRobot.class);

        MethodHeader shuffleMethodHeader = new MethodHeader(shuffleMethod);
        originalMethodHeaders.stream()
            .filter(shuffleMethodHeader::equals)
            .findFirst()
            .orElseThrow(() -> new NoSuchMethodException("Method 'shuffle()' does not exist"));

        MethodHeader shuffleWithParamMethodHeader = new MethodHeader(shuffleWithParamMethod);
        originalMethodHeaders.stream()
            .filter(shuffleWithParamMethodHeader::equals)
            .findFirst()
            .orElseThrow(() -> new NoSuchMethodException("Method 'shuffle(int)' does not exist"));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testConstructorSetsField(boolean order) throws ReflectiveOperationException {
        executionHandler.disableMethodDelegation(versatileRobotConstructor);

        List<String> expectedDoublePowerTypes = order ?
            List.of("DIAGONAL", "TELEPORT") :
            List.of("OVERSTEP", "DIAGONAL");
        int x = 2;
        int y = 2;
        Context context = contextBuilder()
            .add("x", x)
            .add("y", y)
            .add("order", order)
            .build();

        DoublePowerRobot instance = callObject(() -> new DoublePowerRobot(x, y, order), context, result ->
            "An exception occurred while invoking constructor of class DoublePowerRobot");
        List<String> actualDoublePowerTypes = Arrays.stream((MovementType[]) doublePowerTypesField.get(instance))
            .map(Enum::name)
            .toList();
        assertEquals(expectedDoublePowerTypes, actualDoublePowerTypes, context, result ->
            "Array doublePowerTypes does not contain the correct values");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    public void testShuffleWithParams(int offset) throws ReflectiveOperationException {
        testShuffle(offset, shuffleWithParamMethod, (instance, context, shuffleReturnValue) -> {
            boolean returnValue = callObject(() -> instance.shuffle(1), context, result ->
                "An exception occurred while invoking 'shuffle(int)'");

            assertEquals(shuffleReturnValue, returnValue, context, result -> "Return value of 'shuffle(int)' is incorrect");
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    public void testShuffleNoParams(int offset) throws ReflectiveOperationException {
        testShuffle(offset, shuffleMethod, (instance, context, ignored) -> {
            call(instance::shuffle, context, result -> "An exception occurred while invoking 'shuffle()'");
        });
    }

    private void testShuffle(int offset, Method shuffleMethod, TriConsumer<DoublePowerRobot, Context, Boolean> instanceConsumer) throws ReflectiveOperationException {
        MovementType[] movementTypes = MovementType.values();
        MovementType getTypeReturnValue = movementTypes[offset % movementTypes.length];
        MovementType getNextTypeReturnValue = movementTypes[(offset + 1) % movementTypes.length];
        int getRandomReturnValue = 1;
        boolean shuffleReturnValue = false;
        executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("getType"),
            invocation -> getTypeReturnValue);
        executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("getNextType"),
            invocation -> getNextTypeReturnValue);
        executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("getRandom", int.class),
            invocation -> getRandomReturnValue);
        executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("shuffle", int.class),
            invocation -> shuffleReturnValue);
        executionHandler.substituteMethod(HackingRobot.class.getDeclaredMethod("shuffle"), invocation -> null);
        executionHandler.disableMethodDelegation(shuffleMethod);

        int x = 2;
        int y = 2;
        boolean order = false;
        Context context = contextBuilder()
            .add("x", x)
            .add("y", y)
            .add("order", order)
            .add("super.getType() return value", getTypeReturnValue)
            .add("super.getNextType() return value", getNextTypeReturnValue)
            .add("super.getRandom(int) return value", getRandomReturnValue)
            .add("super.shuffle(int) return value", shuffleReturnValue)
            .build();

        DoublePowerRobot instance = callObject(() -> new DoublePowerRobot(x, y, order), context, result ->
            "An exception occurred while invoking constructor of class DoublePowerRobot");
        instanceConsumer.accept(instance, context, shuffleReturnValue);

        assertEquals(getTypeReturnValue,
            ((MovementType[]) doublePowerTypesField.get(instance))[0],
            context,
            result -> "Value of doublePowerTypes[0] is incorrect");
        assertEquals(getNextTypeReturnValue,
            ((MovementType[]) doublePowerTypesField.get(instance))[1],
            context,
            result -> "Value of doublePowerTypes[1] is incorrect");
    }
}
