package h03.h3_2;

import fopbot.World;
import h03.robots.DoublePowerRobot;
import h03.robots.HackingRobot;
import h03.robots.MovementType;
import net.bytebuddy.jar.asm.Type;
import org.apache.commons.lang3.function.TriConsumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;
import org.tudalgo.algoutils.transform.util.headers.FieldHeader;
import org.tudalgo.algoutils.transform.util.headers.MethodHeader;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class DoublePowerRobotTest {

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
        ClassHeader originalClassHeader = getOriginalClassHeader(DoublePowerRobot.class);
        assertTrue(Modifier.isPublic(originalClassHeader.access()), emptyContext(), result ->
            "Class DoublePowerRobot is not declared public");
        assertEquals(Type.getInternalName(HackingRobot.class), originalClassHeader.superName(), emptyContext(), result ->
            "Class DoublePowerRobot does not have correct superclass");
    }

    @Test
    public void testFields() {
        FieldHeader doublePowerTypes = assertNotNull(getOriginalFieldHeader(DoublePowerRobot.class, "doublePowerTypes"), emptyContext(),
            result -> "Field 'doublePowerTypes' does not exist");

        assertEquals(Type.getDescriptor(MovementType[].class), doublePowerTypes.descriptor(), emptyContext(),
            result -> "Field 'doublePowerTypes' in DoublePowerRobot does not have correct type");
    }

    @Test
    public void testMethodHeaders() {
        assertNotNull(getOriginalMethodHeader(DoublePowerRobot.class, "shuffle"), emptyContext(),
            result -> "Method 'shuffle()' does not exist");

        assertNotNull(getOriginalMethodHeader(DoublePowerRobot.class, "shuffle", int.class), emptyContext(),
            result -> "Method 'shuffle(int)' does not exist");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testConstructorSetsField(boolean order) {
        Delegation.disable(MethodHeader.of(DoublePowerRobot.class, int.class, int.class, boolean.class));

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
        List<String> actualDoublePowerTypes = Arrays.stream(FieldHeader.of(DoublePowerRobot.class, "doublePowerTypes")
                .<MovementType[]>getValue(instance))
            .map(Enum::name)
            .toList();
        assertEquals(expectedDoublePowerTypes, actualDoublePowerTypes, context, result ->
            "Array doublePowerTypes does not contain the correct values");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    public void testShuffleWithParams(int offset) {
        testShuffle(offset, MethodHeader.of(DoublePowerRobot.class, "shuffle", int.class),
            (instance, context, shuffleReturnValue) -> {
                boolean returnValue = callObject(() -> instance.shuffle(1), context, result ->
                    "An exception occurred while invoking 'shuffle(int)'");

                assertEquals(shuffleReturnValue, returnValue, context, result -> "Return value of 'shuffle(int)' is incorrect");
            });
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    public void testShuffleNoParams(int offset) {
        testShuffle(offset, MethodHeader.of(DoublePowerRobot.class, "shuffle"),
            (instance, context, ignored) -> {
                call(instance::shuffle, context, result -> "An exception occurred while invoking 'shuffle()'");
            });
    }

    private void testShuffle(int offset, MethodHeader shuffleMethod, TriConsumer<DoublePowerRobot, Context, Boolean> instanceConsumer) {
        MovementType[] movementTypes = MovementType.values();
        MovementType getTypeReturnValue = movementTypes[offset % movementTypes.length];
        MovementType getNextTypeReturnValue = movementTypes[(offset + 1) % movementTypes.length];
        int getRandomReturnValue = 1;
        boolean shuffleReturnValue = false;

        Substitution.enable(MethodHeader.of(HackingRobot.class, "getType"), invocation -> getTypeReturnValue);
        Substitution.enable(MethodHeader.of(HackingRobot.class, "getNextType"), invocation -> getNextTypeReturnValue);
        Substitution.enable(MethodHeader.of(HackingRobot.class, "getRandom", int.class), invocation -> getRandomReturnValue);
        Substitution.enable(MethodHeader.of(HackingRobot.class, "shuffle", int.class), invocation -> shuffleReturnValue);
        Substitution.enable(MethodHeader.of(HackingRobot.class, "shuffle"), invocation -> null);
        Delegation.disable(shuffleMethod);

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

        FieldHeader doublePowerTypes = FieldHeader.of(DoublePowerRobot.class, "doublePowerTypes");
        assertEquals(getTypeReturnValue,
            doublePowerTypes.<MovementType[]>getValue(instance)[0],
            context,
            result -> "Value of doublePowerTypes[0] is incorrect");
        assertEquals(getNextTypeReturnValue,
            doublePowerTypes.<MovementType[]>getValue(instance)[1],
            context,
            result -> "Value of doublePowerTypes[1] is incorrect");
    }
}
