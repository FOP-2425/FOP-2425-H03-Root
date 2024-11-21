package h03.h3_1;

import h03.TestConstants;
import h03.robots.MovementType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.transform.SubmissionExecutionHandler;
import org.tudalgo.algoutils.transform.util.ClassHeader;
import org.tudalgo.algoutils.transform.util.FieldHeader;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
@Timeout(
    value = TestConstants.TEST_TIMEOUT_IN_SECONDS,
    unit = TimeUnit.SECONDS,
    threadMode = Timeout.ThreadMode.SEPARATE_THREAD
)
public class MovementTypeTest {

    @Test
    public void testEnum() {
        ClassHeader orignalClassHeader = SubmissionExecutionHandler.getOriginalClassHeader(MovementType.class);

        assertTrue(Modifier.isPublic(orignalClassHeader.access()), emptyContext(), result ->
            "Enum MovementType was not declared public");
    }

    @Test
    public void testEnumConstants() {
        Set<String> expectedConstants = Set.of("DIAGONAL", "OVERSTEP", "TELEPORT");
        Set<FieldHeader> originalFieldHeaders = SubmissionExecutionHandler.getOriginalFieldHeaders(MovementType.class);

        assertEquals(expectedConstants.size(), originalFieldHeaders.size(), emptyContext(), result ->
            "Enum MovementType does not have the correct number of constants");

        for (String expected : expectedConstants) {
            boolean exists = originalFieldHeaders.stream().anyMatch(fieldHeader -> fieldHeader.name().equals(expected));
            assertTrue(exists, emptyContext(), result -> "Enum constant %s not found in MovementType".formatted(expected));
        }
    }
}
