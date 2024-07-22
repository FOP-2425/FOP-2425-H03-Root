package h03;

import org.tudalgo.algoutils.tutor.general.match.Match;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

public class Links {

    // TODO: Handle typos in package / class names

    public static final PackageLink BASE_PACKAGE = BasicPackageLink.of("h03");
    public static final PackageLink ROBOTS_PACKAGE = BasicPackageLink.of("h03.robots");

    public static final TypeLink MOVEMENT_TYPE_LINK = ROBOTS_PACKAGE.getType(new Matcher<>() {
        @Override
        public <ST extends TypeLink> Match<ST> match(ST object) {
            return Match.match(object, object.name().equals("MovementType"));
        }
    });
}
