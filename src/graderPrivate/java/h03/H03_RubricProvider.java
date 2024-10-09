package h03;

import h03.h3_1.HackingRobotTest;
import h03.h3_1.MovementTypeTest;
import h03.h3_2.DoublePowerRobotTest;
import h03.h3_2.VersatileRobotTest;
import h03.h3_3.RobotsChallengeTest;
import h03.mock.HackingRobotClassTransformer;
import org.sourcegrade.jagr.api.rubric.*;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;
import org.tudalgo.algoutils.tutor.general.json.JsonParameterSet;

import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;

public class H03_RubricProvider implements RubricProvider {

    private static final Criterion H3_1_1 = Criterion.builder().
        shortDescription("H3.1.1 | Movement types")
        .maxPoints(1)
        .addChildCriteria(
            criterion(
                "Die Enumeration MovementType ist korrekt deklariert und umfasst DIAGONAL, OVERSTEP, TELEPORT.",
                JUnitTestRef.ofMethod(() -> MovementTypeTest.class.getDeclaredMethod("testEnumConstants"))
            )
        )
        .build();

    private static final Criterion H3_1_2 = Criterion.builder().
        shortDescription("H3.1.2 | First class")
        .maxPoints(1)
        .addChildCriteria(
            criterion(
                "Die Klasse HackingRobot ist korrekt deklariert mit den Attributen type und robotTypes.",
                JUnitTestRef.ofMethod(() -> HackingRobotTest.class.getDeclaredMethod("testClassHeader")),
                JUnitTestRef.ofMethod(() -> HackingRobotTest.class.getDeclaredMethod("testFields"))
            )
        )
        .build();

    private static final Criterion H3_1_3 = Criterion.builder().
        shortDescription("H3.1.3 | Robot under construction")
        .maxPoints(3)
        .addChildCriteria(
            criterion(
                "Der Konstruktor von HackingRobot ist korrekt deklariert.",
                JUnitTestRef.ofMethod(() -> HackingRobotTest.class.getDeclaredMethod("testConstructorHeader"))
            ),
            criterion(
                "Der Konstruktor ruft den Konstruktor der Basisklasse Robot korrekt auf.",
                JUnitTestRef.ofMethod(() -> HackingRobotTest.class.getDeclaredMethod("testConstructorSuperCall"))
            ),
            criterion(
                "Das Attribut robotTypes ist korrekt initialisiert und die Elemente korrekt nach order verschoben.",
                JUnitTestRef.ofMethod(() -> HackingRobotTest.class.getDeclaredMethod("testConstructorSetsFields", boolean.class))
            )
        )
        .build();

    private static final Criterion H3_1_4 = Criterion.builder().
        shortDescription("H3.1.4 | Access to robot types")
        .maxPoints(3)
        .addChildCriteria(
            criterion(
                "Die Methode getType gibt den aktuellen Robotertyp korrekt zurück.",
                JUnitTestRef.ofMethod(() -> HackingRobotTest.class.getDeclaredMethod("testGetType"))
            ),
            criterion(
                "Die Methode getNextType gibt den nächsten Typ korrekt zurück, wenn nicht zum Index 0 zurückgesprungen wird.",
                JUnitTestRef.ofMethod(() -> HackingRobotTest.class.getDeclaredMethod("testGetNextTypeNoMod", int.class))
            ),
            criterion(
                "Die Methode getNextType gibt den nächsten Typ korrekt zurück, wenn zum Index 0 zurückgesprungen werden muss.",
                JUnitTestRef.ofMethod(() -> HackingRobotTest.class.getDeclaredMethod("testGetNextTypeMod", int.class))
            )
        )
        .build();

    private static final Criterion H3_1_5 = Criterion.builder().
        shortDescription("H3.1.5 | Swap type")
        .addChildCriteria(
            criterion(
                "Die Methode shuffle(int itNr) funktioniert korrekt und ändert den Robotertyp zufällig.",
                JUnitTestRef.ofMethod(() -> HackingRobotTest.class.getDeclaredMethod("testShuffleWithParams_SetField", int.class))
            ),
            criterion(
                "Die Methode gibt true zurück, wenn der Typ geändert wurde, sonst false.",
                JUnitTestRef.ofMethod(() -> HackingRobotTest.class.getDeclaredMethod("testShuffleWithParams_ReturnValue", int.class))
            )
        )
        .build();

    private static final Criterion H3_1_6 = Criterion.builder().
        shortDescription("H3.1.6 | Are you sure of the swap?")
        .addChildCriteria(
            criterion(
                "Die Methode shuffle() ist korrekt überladen und garantiert, dass der Typ des Roboters geändert wird.",
                2,
                JUnitTestRef.ofMethod(() -> HackingRobotTest.class.getDeclaredMethod("testShuffleNoParams"))
            )
        )
        .build();

    private static final Criterion H3_1 = Criterion.builder().
        shortDescription("H3.1 | HackingRobot")
        .maxPoints(12).addChildCriteria(
            H3_1_1,
            H3_1_2,
            H3_1_3,
            H3_1_4,
            H3_1_5,
            H3_1_6
        )
        .build();

    private static final Criterion H3_2_1 = Criterion.builder().
        shortDescription("H3.2.1 | DoublePowerRobot")
        .maxPoints(4).addChildCriteria(
            criterion(
                "Die Klasse DoublePowerRobot ist korrekt deklariert mit den Attributen und Methoden.",
                JUnitTestRef.ofMethod(() -> DoublePowerRobotTest.class.getDeclaredMethod("testClassHeader")),
                JUnitTestRef.ofMethod(() -> DoublePowerRobotTest.class.getDeclaredMethod("testFields")),
                JUnitTestRef.ofMethod(() -> DoublePowerRobotTest.class.getDeclaredMethod("testMethodHeaders"))
            ),
            criterion(
                "Der Konstruktor initialisiert doublePowerTypes korrekt mit den aktuellen und nächsten Typen.",
                JUnitTestRef.ofMethod(() -> DoublePowerRobotTest.class.getDeclaredMethod("testConstructorSetsField", boolean.class))
            ),
            criterion(
                "Die Methode shuffle(int itNr) für DoublePowerRobot funktioniert korrekt.",
                JUnitTestRef.ofMethod(() -> DoublePowerRobotTest.class.getDeclaredMethod("testShuffleWithParams", int.class))
            ),
            criterion(
                "Die Methode shuffle() für DoublePowerRobot aktualisiert den zweiten Typ korrekt.",
                JUnitTestRef.ofMethod(() -> DoublePowerRobotTest.class.getDeclaredMethod("testShuffleNoParams", int.class))
            )
        )
        .build();

    private static final Criterion H3_2_2 = Criterion.builder().
        shortDescription("H3.2.2 | VersatileRobot")
        .maxPoints(4).addChildCriteria(
            criterion(
                "Die Klasse VersatileRobot ist korrekt deklariert.",
                JUnitTestRef.ofMethod(() -> VersatileRobotTest.class.getDeclaredMethod("testClassHeader"))
            ),
            criterion(
                "Der Konstruktor der Klasse VersatileRobot setzt y = x, wenn der Typ DIAGONAL ist.",
                JUnitTestRef.ofMethod(() -> VersatileRobotTest.class.getDeclaredMethod("testConstructor"))
            ),
            criterion(
                "Die Methode shuffle(int itNr) funktioniert korrekt.",
                JUnitTestRef.ofMethod(() -> VersatileRobotTest.class.getDeclaredMethod("testShuffleWithParams"))
            ),
            criterion(
                "Die Methode shuffle() setzt korrekt die y-Koordinate, wenn der Typ DIAGONAL ist.",
                JUnitTestRef.ofMethod(() -> VersatileRobotTest.class.getDeclaredMethod("testShuffleNoParams"))
            )
        )
        .build();

    private static final Criterion H3_2 = Criterion.builder().
        shortDescription("H3.2 | Special Hacking Robots")
        .maxPoints(8).addChildCriteria(H3_2_1, H3_2_2)
        .build();

    private static final Criterion H3_3_1 = Criterion.builder().
        shortDescription("H3.3.1 | First things first")
        .maxPoints(1)
        .addChildCriteria(
            criterion(
                "Die Klasse RobotsChallenge ist korrekt deklariert.",
                JUnitTestRef.ofMethod(() -> RobotsChallengeTest.class.getDeclaredMethod("testClassHeader"))
            )
        )
        .build();

    private static final Criterion H3_3_2 = Criterion.builder().
        shortDescription("H3.3.2 | Participators over here")
        .maxPoints(2)
        .addChildCriteria(
            criterion(
                "Der Konstruktor von RobotsChallenge weist korrekt die Parameter begin, goal, und robots zu.",
                JUnitTestRef.ofMethod(() -> RobotsChallengeTest.class.getDeclaredMethod("testConstructor", int.class))
            ),
            criterion(
                "Der Konstruktor sorgt dafür, dass winThreshold auf 2 gesetzt wird (direkt oder indirekt).",
                JUnitTestRef.ofMethod(() -> RobotsChallengeTest.class.getDeclaredMethod("testWinThreshold"))
            )
        )
        .build();

    private static final Criterion H3_3_3 = Criterion.builder().
        shortDescription("H3.3.3 | Quick maths")
        .maxPoints(3)
        .addChildCriteria(
            criterion(
                "Die Methode calculateStepsDiagonal ist korrekt implementiert und berechnet die Schritte für den Typ DIAGONAL.",
                JUnitTestRef.ofMethod(() -> RobotsChallengeTest.class.getDeclaredMethod("testCalculateStepsDiagonal", JsonParameterSet.class))
            ),
            criterion(
                "Die Methode calculateStepsOverstep ist korrekt implementiert und berechnet die Schritte für den Typ OVERSTEP.",
                JUnitTestRef.ofMethod(() -> RobotsChallengeTest.class.getDeclaredMethod("testCalculateStepsOverstep", JsonParameterSet.class))
            ),
            criterion(
                "Die Methode calculateStepsTeleport ist korrekt implementiert und berechnet die Schritte für den Typ TELEPORT.",
                JUnitTestRef.ofMethod(() -> RobotsChallengeTest.class.getDeclaredMethod("testCalculateStepsTeleport", JsonParameterSet.class))
            )
        )
        .build();

    private static final Criterion H3_3_4 = Criterion.builder().
        shortDescription("H3.3.4 | Let the show begin")
        .maxPoints(3)
        .addChildCriteria(
            criterion(
                "Die Methode findWinners berechnet korrekt die Schritte für jeden Roboter und gibt die Gewinner zurück."
            ),
            criterion(
                "Die Methode verwendet Math.min korrekt, um die minimalen Schritte zu berechnen."
            ),
            criterion(
                "Gewinner werden korrekt in der Liste winners gespeichert."
            )
        )
        .build();

    private static final Criterion H3_3 = Criterion.builder().
        shortDescription("H3.3 | Let Robots Compete!")
        .maxPoints(9)
        .addChildCriteria(
            H3_3_1,
            H3_3_2,
            H3_3_3,
            H3_3_4
        )
        .build();

    private static final Criterion H3_4 = Criterion.builder().
        shortDescription("H3.4 | Documentation")
        .maxPoints(3)
        .addChildCriteria(
            criterion(
                "Alle öffentlichen Klassen, Methoden und Konstruktoren sind mit JavaDoc korrekt dokumentiert."
                , 3
            )
        )
        .build();

    private static final Rubric RUBRIC = Rubric.builder()
        .title("H03 | Mission Robots: The Ultimate Grid Race")
        .addChildCriteria(
            H3_1,
            H3_2,
            H3_3,
            H3_4
        )
        .build();


    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    @Override
    public void configure(RubricConfiguration configuration) {
        configuration.addTransformer(new HackingRobotClassTransformer());
    }
}
