package h03.h3_1;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.WithName;

import java.util.Set;
import java.util.stream.Collectors;

import static h03.Links.MOVEMENT_TYPE_LINK;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class MovementTypeTest {

    @Test
    public void testEnumConstants() {
        Set<String> expected = Set.of("DIAGONAL", "OVERSTEP", "TELEPORT");
        Set<String> actual = MOVEMENT_TYPE_LINK.get().getEnumConstants()
            .stream()
            .map(WithName::name)
            .collect(Collectors.toSet());
        Context context = emptyContext();

        assertEquals(expected.size(), actual.size(), context, result ->
            "Enum MovementType does not have the correct number of constants");
        for (String expectedConstant : expected) {
            assertTrue(actual.contains(expectedConstant), context, result ->
                "Enum constant %s not found in MovementType".formatted(expectedConstant));
        }
    }
}
