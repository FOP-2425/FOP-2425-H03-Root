package h03.h3_2;

import fopbot.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Modifier;

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
    public void testConstructor() {
        assertTrue((DOUBLE_POWER_ROBOT_CONSTRUCTOR_LINK.get().modifiers() & Modifier.PUBLIC) != 0, emptyContext(), result ->
            "Constructor in DoublePowerRobot is not declared public");

        // TODO: constructors == pain. Bytecode transformations; there is no other way to unit-test this
    }
}
