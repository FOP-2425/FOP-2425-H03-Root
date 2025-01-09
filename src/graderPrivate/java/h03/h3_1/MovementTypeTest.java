package h03.h3_1;

import h03.robots.MovementType;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.util.headers.ClassHeader;

import java.lang.reflect.Modifier;
import java.util.Set;

import static org.tudalgo.algoutils.transform.SubmissionExecutionHandler.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class MovementTypeTest {

    @Test
    public void testEnum() {
        ClassHeader orignalClassHeader = getOriginalClassHeader(MovementType.class);
        assertTrue(Modifier.isPublic(orignalClassHeader.access()), emptyContext(), result ->
            "Enum MovementType was not declared public");
    }

    @Test
    public void testEnumConstants() {
        Set<String> expectedConstants = Set.of("DIAGONAL", "OVERSTEP", "TELEPORT");
        assertEquals(expectedConstants.size(), getOriginalEnumConstants(MovementType.class).size(), emptyContext(),
            result -> "Enum MovementType does not have the correct number of constants");

        for (String expected : expectedConstants) {
            assertNotNull(getOriginalEnumConstant(MovementType.class, expected), emptyContext(),
                result -> "Enum constant %s not found in MovementType".formatted(expected));
        }
    }
}
