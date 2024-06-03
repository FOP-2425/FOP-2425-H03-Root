package h03;
import fopbot.Robot;

import java.util.Random;

public class HackingRobot extends Robot{
    // Privates Array "roboterTypen", das die Elemente der Enumeration MovementType in alphabetischer Reihenfolge enthält.
    private MovementType[] roboterTypen = {MovementType.DIAGONAL, MovementType.OVERSTEP, MovementType.TELEPORT};

    // Privates String, das den Typ des jeweiligen Roboters enthält.
    private MovementType type;

    // Konstruktor der HackingRobot-Klasse mit den Parametern x, y und order
    public HackingRobot(int x, int y, boolean order) {
        // Aufruf des Konstruktors der Basisklasse Robot
        super(x, y);

        // Verschieben der Elemente von roboterTypen je nach Wert von order
        if (order) {
            // Elemente um 1 Index nach rechts verschieben
            MovementType lastElement = roboterTypen[roboterTypen.length - 1];
            for (int i = roboterTypen.length - 1; i > 0; i--) {
                roboterTypen[i] = roboterTypen[i - 1];
            }
            roboterTypen[0] = lastElement;
        } else {
            // Elemente um 1 Index nach links verschieben
            MovementType firstElement = roboterTypen[0];
            for (int i = 0; i < roboterTypen.length - 1; i++) {
                roboterTypen[i] = roboterTypen[i + 1];
            }
            roboterTypen[roboterTypen.length - 1] = firstElement;
        }

        // Zuweisen des ersten Wertes von roboterTypen an type
        this.type = roboterTypen[0];
    }

    // Getter-Methode für die Variable "type"
    public MovementType getType() {
        return type;
    }

    // Methode, die den Typ zurückgibt, der sich um 1 Index rechts des aktuellen Typs des Roboters befindet
    public MovementType getNextType() {
        int currentIndex = -1;
        for (int i = 0; i < roboterTypen.length; i++) {
            if (roboterTypen[i] == type) {
                currentIndex = i;
                break;
            }
        }
        return roboterTypen[(currentIndex + 1) % roboterTypen.length];
    }

    // Methode "shuffle", die den Typ "type" des Roboters zufällig ändert
    public boolean shuffle() {
        // Hilfsvariable, um den vorherigen Typ zu speichern
        MovementType previousType = this.type;

        // Zufällig generierten Index für den neuen Typ
        Random random = new Random();
        int randomIndex = random.nextInt(roboterTypen.length);

        // Neuen Typ zuweisen
        this.type = roboterTypen[randomIndex];

        // Überprüfen, ob sich der Typ geändert hat
        return this.type != previousType;
    }

    // Überladene Methode "shuffle", die den Typ des Roboters auf jeden Fall ändert
    public void forceShuffle() {
        Random random = new Random();
        MovementType newType = this.type;
        while (newType == this.type) {
            int randomIndex = random.nextInt(roboterTypen.length);
            newType = roboterTypen[randomIndex];
        }
        this.type = newType;
    }

}
