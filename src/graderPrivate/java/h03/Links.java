package h03;

import com.google.common.base.Suppliers;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.*;

import java.util.List;
import java.util.function.Supplier;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.emptyContext;

public final class Links {

    private Links() {}

    // TODO: Handle typos in package / class names

    public static final Supplier<PackageLink> BASE_PACKAGE = Suppliers.memoize(() -> BasicPackageLink.of("h03"));
    public static final Supplier<PackageLink> ROBOTS_PACKAGE = Suppliers.memoize(() -> BasicPackageLink.of("h03.robots"));

    public static final Supplier<TypeLink> MOVEMENT_TYPE_LINK = Suppliers.memoize(() ->
        ROBOTS_PACKAGE.get().getType(Matcher.of(typeLink -> typeLink.name().equals("MovementType"))));

    public static final Supplier<TypeLink> HACKING_ROBOT_LINK = Suppliers.memoize(() ->
        ROBOTS_PACKAGE.get().getType(Matcher.of(typeLink -> typeLink.name().equals("HackingRobot"))));
    public static final Supplier<FieldLink> HACKING_ROBOT_TYPE_LINK = Suppliers.memoize(() ->
        HACKING_ROBOT_LINK.get().getField(Matcher.of(fieldLink -> fieldLink.name().equals("type"))));
    public static final Supplier<FieldLink> HACKING_ROBOT_ROBOT_TYPES_LINK = Suppliers.memoize(() ->
        HACKING_ROBOT_LINK.get().getField(Matcher.of(fieldLink -> fieldLink.name().equals("robotTypes"))));
    public static final Supplier<ConstructorLink> HACKING_ROBOT_CONSTRUCTOR_LINK = Suppliers.memoize(() ->
        HACKING_ROBOT_LINK.get().getConstructor(Matcher.of(constructorLink -> constructorLink.typeList()
            .stream()
            .map(TypeLink::reflection)
            .toList()
            .equals(List.of(int.class, int.class, boolean.class)))));
    public static final Supplier<MethodLink> HACKING_ROBOT_GET_TYPE_LINK = Suppliers.memoize(() ->
        HACKING_ROBOT_LINK.get().getMethod(Matcher.of(methodLink -> methodLink.name().equals("getType") && methodLink.typeList().isEmpty())));
    public static final Supplier<MethodLink> HACKING_ROBOT_GET_NEXT_TYPE_LINK = Suppliers.memoize(() ->
        HACKING_ROBOT_LINK.get().getMethod(Matcher.of(methodLink -> methodLink.name().equals("getNextType") && methodLink.typeList().isEmpty())));
    public static final Supplier<MethodLink> HACKING_ROBOT_GET_RANDOM_LINK = Suppliers.memoize(() ->
        HACKING_ROBOT_LINK.get().getMethod(Matcher.of(methodLink -> methodLink.name().equals("getRandom") &&
            methodLink.typeList().size() == 1 &&
            methodLink.typeList().getFirst().reflection() == int.class)));
    public static final Supplier<MethodLink> HACKING_ROBOT_SHUFFLE1_LINK = Suppliers.memoize(() ->
        HACKING_ROBOT_LINK.get().getMethod(Matcher.of(methodLink -> methodLink.name().equals("shuffle") &&
            methodLink.typeList().size() == 1 &&
            methodLink.typeList().getFirst().reflection() == int.class)));
    public static final Supplier<MethodLink> HACKING_ROBOT_SHUFFLE2_LINK = Suppliers.memoize(() ->
        HACKING_ROBOT_LINK.get().getMethod(Matcher.of(methodLink -> methodLink.name().equals("shuffle") && methodLink.typeList().isEmpty())));

    public static final Supplier<TypeLink> DOUBLE_POWER_ROBOT_LINK = Suppliers.memoize(() ->
        ROBOTS_PACKAGE.get().getType(Matcher.of(typeLink -> typeLink.name().equals("DoublePowerRobot"))));
    public static final Supplier<FieldLink> DOUBLE_POWER_ROBOT_DOUBLE_POWER_TYPES_LINK = Suppliers.memoize(() ->
        DOUBLE_POWER_ROBOT_LINK.get().getField(Matcher.of(fieldLink -> fieldLink.name().equals("doublePowerTypes"))));
    public static final Supplier<ConstructorLink> DOUBLE_POWER_ROBOT_CONSTRUCTOR_LINK = Suppliers.memoize(() ->
        DOUBLE_POWER_ROBOT_LINK.get().getConstructor(Matcher.of(constructorLink -> constructorLink.typeList()
            .stream()
            .map(TypeLink::reflection)
            .toList()
            .equals(List.of(int.class, int.class, boolean.class)))));
    public static final Supplier<MethodLink> DOUBLE_POWER_ROBOT_SHUFFLE1_LINK = Suppliers.memoize(() ->
        DOUBLE_POWER_ROBOT_LINK.get().getMethod(Matcher.of(methodLink -> methodLink.name().equals("shuffle") &&
            methodLink.typeList().size() == 1 &&
            methodLink.typeList().getFirst().reflection() == int.class)));
    public static final Supplier<MethodLink> DOUBLE_POWER_ROBOT_SHUFFLE2_LINK = Suppliers.memoize(() ->
        DOUBLE_POWER_ROBOT_LINK.get().getMethod(Matcher.of(methodLink -> methodLink.name().equals("shuffle") && methodLink.typeList().isEmpty())));

    public static Enum<?>[] getMovementTypeEnums() {
        Enum<?>[] movementTypeConstants = MOVEMENT_TYPE_LINK.get().getEnumConstants()
            .stream()
            .map(EnumConstantLink::constant)
            .toArray(Enum[]::new);
        assertEquals(3, movementTypeConstants.length, emptyContext(), result ->
            "Precondition failed: Number of enum constants in MovementType is incorrect");
        return movementTypeConstants;
    }
}
