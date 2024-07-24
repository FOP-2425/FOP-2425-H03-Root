package h03;

import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.*;

import java.util.List;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.emptyContext;

public final class Links {

    private Links() {}

    // TODO: Handle typos in package / class names
    // TODO: Convert to memoized suppliers

    public static final PackageLink BASE_PACKAGE = BasicPackageLink.of("h03");
    public static final PackageLink ROBOTS_PACKAGE = BasicPackageLink.of("h03.robots");

    public static final TypeLink MOVEMENT_TYPE_LINK = ROBOTS_PACKAGE.getType(Matcher.of(typeLink -> typeLink.name().equals("MovementType")));

    public static final TypeLink HACKING_ROBOT_LINK = ROBOTS_PACKAGE.getType(Matcher.of(typeLink -> typeLink.name().equals("HackingRobot")));
    public static final FieldLink HACKING_ROBOT_TYPE_LINK = HACKING_ROBOT_LINK.getField(Matcher.of(fieldLink -> fieldLink.name().equals("type")));
    public static final FieldLink HACKING_ROBOT_ROBOT_TYPES_LINK = HACKING_ROBOT_LINK.getField(Matcher.of(fieldLink -> fieldLink.name().equals("robotTypes")));
    public static final ConstructorLink HACKING_ROBOT_CONSTRUCTOR_LINK = HACKING_ROBOT_LINK.getConstructor(Matcher.of(constructorLink ->
        constructorLink.typeList()
            .stream()
            .map(TypeLink::reflection)
            .toList()
            .equals(List.of(int.class, int.class, boolean.class))));
    public static final MethodLink HACKING_ROBOT_GET_TYPE_LINK = HACKING_ROBOT_LINK.getMethod(Matcher.of(methodLink ->
        methodLink.name().equals("getType") &&
        methodLink.typeList().isEmpty()));
    public static final MethodLink HACKING_ROBOT_GET_NEXT_TYPE_LINK = HACKING_ROBOT_LINK.getMethod(Matcher.of(methodLink ->
        methodLink.name().equals("getNextType") &&
        methodLink.typeList().isEmpty()));

    public static Enum<?>[] getMovementTypeEnums() {
        Enum<?>[] movementTypeConstants = MOVEMENT_TYPE_LINK.getEnumConstants()
            .stream()
            .map(EnumConstantLink::constant)
            .toArray(Enum[]::new);
        assertEquals(3, movementTypeConstants.length, emptyContext(), result ->
            "Precondition failed: Number of enum constants in MovementType is incorrect");
        return movementTypeConstants;
    }
}
