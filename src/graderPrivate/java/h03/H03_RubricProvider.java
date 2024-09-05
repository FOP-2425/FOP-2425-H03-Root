package h03;

import org.sourcegrade.jagr.api.rubric.*;

import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;

public class H03_RubricProvider implements RubricProvider {

    private static final Criterion H3_1_1 = Criterion.builder().
        shortDescription("H3.1.1 | Movement types")
        .maxPoints(1)
        .addChildCriteria(
            criterion(
                "Die Enumeration MovementType ist korrekt deklariert und umfasst DIAGONAL, OVERSTEP, TELEPORT."
            )
        )
        .build();

    private static final Criterion H3_1_2 = Criterion.builder().
        shortDescription("H3.1.2 | First class")
        .maxPoints(1)
        .addChildCriteria(
            criterion(
                "Die Klasse HackingRobot ist korrekt deklariert mit den Attributen type und robotTypes."
            )
        )
        .build();

    private static final Criterion H3_1_3 = Criterion.builder().
        shortDescription("H3.1.3 | Robot under construction")
        .maxPoints(3)
        .addChildCriteria(
            criterion(
                "Der Konstruktor von HackingRobot ist korrekt deklariert."
            ),
            criterion(
                "Der Konstruktor ruft den Konstruktor der Basisklasse Robot korrekt auf."
            ),
            criterion(
                "Das Attribut robotTypes ist korrekt initialisiert und die Elemente korrekt nach order verschoben."
            )
        )
        .build();

    private static final Criterion H3_1_4 = Criterion.builder().
        shortDescription("H3.1.4 | Access to robot types")
        .maxPoints(3)
        .addChildCriteria(
            criterion(
                "Die Methode getType gibt den aktuellen Robotertyp korrekt zurück."
            ),
            criterion(
                "Die Methode getNextType gibt den nächsten Typ korrekt zurück, wobei bei Bedarf zum Index 0 zurückgesprungen wird."
            ),
            criterion(
                "Die Methode getNextType berücksichtigt korrekt den Zyklus der Bewegungsmuster, wenn der letzte Typ erreicht wurde."
            )
        )
        .build();

    private static final Criterion H3_1_5 = Criterion.builder().
        shortDescription("H3.1.5 | Swap type")
        .maxPoints(3)
        .addChildCriteria(
            criterion(
                "Die Methode shuffle(int itNr) funktioniert korrekt und ändert den Robotertyp zufällig."
            ),
            criterion(
                "Die Methode gibt true zurück, wenn der Typ geändert wurde, sonst false."
            ),
            criterion(
                "Die Methode shuffle() ohne Parameter funktioniert korrekt und randomisiert den Typ bis er sich ändert."
            )
        )
        .build();

    private static final Criterion H3_1_6 = Criterion.builder().
        shortDescription("H3.1.6 | Are you sure of the swap?")
        .maxPoints(1).addChildCriteria(
            criterion(
                "Die Methode shuffle() ist korrekt überladen und garantiert, dass der Typ des Roboters geändert wird."
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
                "Die Klasse DoublePowerRobot ist korrekt deklariert mit den Attributen und Methoden."
            ),
            criterion(
                "Der Konstruktor initialisiert doublePowerTypes korrekt mit den aktuellen und nächsten Typen."
            ),
            criterion(
                "Die Methode shuffle() für DoublePowerRobot aktualisiert den zweiten Typ korrekt."
            ),
            criterion(
                "Die Methode shuffle(int itNr) für DoublePowerRobot funktioniert korrekt."
            )
        )
        .build();

    private static final Criterion H3_2_2 = Criterion.builder().
        shortDescription("H3.2.2 | VersatileRobot")
        .maxPoints(4).addChildCriteria(
            criterion(
                "Die Klasse VersatileRobot ist korrekt deklariert."
            ),
            criterion(
                "Der Konstruktor der Klasse VersatileRobot setzt y = x, wenn der Typ DIAGONAL ist."
            ),
            criterion(
                "Die Methoden shuffle() setzen korrekt die y-Koordinate, wenn der Typ DIAGONAL ist."
            ),
            criterion(
                "Die Methode shuffle(int itNr) funktioniert korrekt."
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
                "Die Klasse RobotsChallenge ist korrekt deklariert mit dem Attribut winThreshold = 2."
            )
        )
        .build();

    private static final Criterion H3_3_2 = Criterion.builder().
        shortDescription("H3.3.2 | Participators over here")
        .maxPoints(2)
        .addChildCriteria(
            criterion(
                "Der Konstruktor von RobotsChallenge weist korrekt die Parameter begin, goal, und robots zu."
            ),
            criterion(
                "Der Konstruktor sorgt dafür, dass begin durch 2 geteilt wird."
            )
        )
        .build();

    private static final Criterion H3_3_3 = Criterion.builder().
        shortDescription("H3.3.3 | Quick maths")
        .maxPoints(3)
        .addChildCriteria(
            criterion(
                "Die Methode calculateStepsDiagonal ist korrekt implementiert und berechnet die Schritte für den Typ DIAGONAL."
            ),
            criterion(
                "Die Methode calculateStepsOverstep ist korrekt implementiert und berechnet die Schritte für den Typ OVERSTEP."
            ),
            criterion(
                "Die Methode calculateStepsTeleport ist korrekt implementiert und berechnet die Schritte für den Typ TELEPORT."
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
}
