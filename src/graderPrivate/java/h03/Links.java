package h03;

import com.google.common.base.Suppliers;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public final class Links {

    private Links() {}

    // TODO: Handle typos in package / class names

    // Packages
    public static final Supplier<PackageLink> BASE_PACKAGE_LINK = Suppliers.memoize(() -> BasicPackageLink.of("h03"));
    public static final Supplier<PackageLink> ROBOTS_PACKAGE_LINK = Suppliers.memoize(() -> BasicPackageLink.of("h03.robots"));

    // Enum h03.MovementType
    public static final Supplier<TypeLink> MOVEMENT_TYPE_LINK = getTypeLinkByName(ROBOTS_PACKAGE_LINK, "MovementType");
    public static final Supplier<EnumConstantLink> MOVEMENT_TYPE_DIAGONAL_LINK = getEnumConstantLinkByName(MOVEMENT_TYPE_LINK, "DIAGONAL");
    public static final Supplier<EnumConstantLink> MOVEMENT_TYPE_OVERSTEP_LINK = getEnumConstantLinkByName(MOVEMENT_TYPE_LINK, "OVERSTEP");
    public static final Supplier<EnumConstantLink> MOVEMENT_TYPE_TELEPORT_LINK = getEnumConstantLinkByName(MOVEMENT_TYPE_LINK, "TELEPORT");
    public static final Supplier<EnumConstantLink[]> MOVEMENT_TYPE_CONSTANTS = Suppliers.memoize(() -> new EnumConstantLink[] {
        MOVEMENT_TYPE_DIAGONAL_LINK.get(),
        MOVEMENT_TYPE_OVERSTEP_LINK.get(),
        MOVEMENT_TYPE_TELEPORT_LINK.get()
    });

    // Class h03.robots.HackingRobot
    public static final Supplier<TypeLink> HACKING_ROBOT_LINK = getTypeLinkByName(ROBOTS_PACKAGE_LINK, "HackingRobot");
    public static final Supplier<FieldLink> HACKING_ROBOT_TYPE_LINK = getFieldLinkByName(HACKING_ROBOT_LINK, "type");
    public static final Supplier<FieldLink> HACKING_ROBOT_ROBOT_TYPES_LINK = getFieldLinkByName(HACKING_ROBOT_LINK, "robotTypes");
    public static final Supplier<ConstructorLink> HACKING_ROBOT_CONSTRUCTOR_LINK = getConstructorLink(HACKING_ROBOT_LINK, int.class, int.class, boolean.class);
    public static final Supplier<MethodLink> HACKING_ROBOT_GET_TYPE_LINK = getMethodLink(HACKING_ROBOT_LINK, "getType");
    public static final Supplier<MethodLink> HACKING_ROBOT_GET_NEXT_TYPE_LINK = getMethodLink(HACKING_ROBOT_LINK, "getNextType");
    public static final Supplier<MethodLink> HACKING_ROBOT_GET_RANDOM_LINK = getMethodLink(HACKING_ROBOT_LINK, "getRandom", int.class);
    public static final Supplier<MethodLink> HACKING_ROBOT_SHUFFLE1_LINK = getMethodLink(HACKING_ROBOT_LINK, "shuffle", int.class);
    public static final Supplier<MethodLink> HACKING_ROBOT_SHUFFLE2_LINK = getMethodLink(HACKING_ROBOT_LINK, "shuffle");

    // Class h03.robots.DoublePowerRobot
    public static final Supplier<TypeLink> DOUBLE_POWER_ROBOT_LINK = getTypeLinkByName(ROBOTS_PACKAGE_LINK, "DoublePowerRobot");
    public static final Supplier<FieldLink> DOUBLE_POWER_ROBOT_DOUBLE_POWER_TYPES_LINK = getFieldLinkByName(DOUBLE_POWER_ROBOT_LINK, "doublePowerTypes");
    public static final Supplier<ConstructorLink> DOUBLE_POWER_ROBOT_CONSTRUCTOR_LINK = getConstructorLink(DOUBLE_POWER_ROBOT_LINK, int.class, int.class, boolean.class);
    public static final Supplier<MethodLink> DOUBLE_POWER_ROBOT_SHUFFLE1_LINK = getMethodLink(DOUBLE_POWER_ROBOT_LINK, "shuffle", int.class);
    public static final Supplier<MethodLink> DOUBLE_POWER_ROBOT_SHUFFLE2_LINK = getMethodLink(DOUBLE_POWER_ROBOT_LINK, "shuffle");

    private static Supplier<TypeLink> getTypeLinkByName(Supplier<PackageLink> packageLink, String name) {
        return Suppliers.memoize(() -> packageLink.get().getType(Matcher.of(typeLink -> typeLink.name().equals(name))));
    }

    private static Supplier<FieldLink> getFieldLinkByName(Supplier<TypeLink> owner, String name) {
        return Suppliers.memoize(() -> owner.get().getField(Matcher.of(fieldLink -> fieldLink.name().equals(name))));
    }

    private static Supplier<EnumConstantLink> getEnumConstantLinkByName(Supplier<TypeLink> owner, String name) {
        return Suppliers.memoize(() -> owner.get().getEnumConstant(Matcher.of(enumConstantLink -> enumConstantLink.name().equals(name))));
    }

    @SuppressWarnings("unchecked")
    private static Supplier<ConstructorLink> getConstructorLink(Supplier<TypeLink> owner) {
        return getConstructorLink(owner, new Supplier[0]);
    }

    @SuppressWarnings("unchecked")
    private static Supplier<ConstructorLink> getConstructorLink(Supplier<TypeLink> owner, Class<?>... parameterTypes) {
        return getConstructorLink(owner, Arrays.stream(parameterTypes)
            .map(parameterType -> (Supplier<TypeLink>) () -> BasicTypeLink.of(parameterType))
            .toArray(Supplier[]::new));
    }

    @SafeVarargs
    private static Supplier<ConstructorLink> getConstructorLink(Supplier<TypeLink> owner, Supplier<TypeLink>... parameterTypes) {
        return Suppliers.memoize(() ->
            owner.get().getConstructor(Matcher.of(constructorLink -> {
                List<? extends TypeLink> params = constructorLink.typeList();
                boolean found = params.size() == parameterTypes.length;
                for (int i = 0; found && i < parameterTypes.length; i++) {
                    found = parameterTypes[i].get().equals(params.get(i));
                }
                return found;
            })));
    }

    @SuppressWarnings("unchecked")
    private static Supplier<MethodLink> getMethodLink(Supplier<TypeLink> owner, String name) {
        return getMethodLink(owner, name, new Supplier[0]);
    }

    @SuppressWarnings("unchecked")
    private static Supplier<MethodLink> getMethodLink(Supplier<TypeLink> owner, String name, Class<?>... parameterTypes) {
        return getMethodLink(owner, name, Arrays.stream(parameterTypes)
            .map(parameterType -> (Supplier<TypeLink>) () -> BasicTypeLink.of(parameterType))
            .toArray(Supplier[]::new));
    }

    @SafeVarargs
    private static Supplier<MethodLink> getMethodLink(Supplier<TypeLink> owner, String name, Supplier<TypeLink>... parameterTypes) {
        return Suppliers.memoize(() ->
            owner.get().getMethod(Matcher.of(methodLink -> {
                List<? extends TypeLink> params = methodLink.typeList();
                boolean found = methodLink.name().equals(name) && params.size() == parameterTypes.length;
                for (int i = 0; found && i < parameterTypes.length; i++) {
                    found = parameterTypes[i].get().equals(params.get(i));
                }
                return found;
            })));
    }
}
