package h03;
import fopbot.Robot;

import java.util.Random;

// Derived class HackingRobot, whose attributes acquire hacking methods for movement in the grid
public class HackingRobot extends Robot {

    // Private array "roboterTypes" containing the elements of the enumeration MovementType in alphabetical order
    private MovementType[] roboterTypes = {MovementType.DIAGONAL, MovementType.OVERSTEP, MovementType.TELEPORT};

    // Privates String, das den Typ des jeweiligen Roboters enthält
    private MovementType type;

    // Private string containing the type of the robot
    public HackingRobot(int x, int y, boolean order) {

        // Calling the constructor of the base class Robot
        super(x, y);

        // Moving the elements of roboterTypes depending on the value of order
        if (order) {

            // Move elements to the right by 1 index
            MovementType lastElement = roboterTypes[roboterTypes.length - 1];
            for (int i = roboterTypes.length - 1; i > 0; i--) {
                roboterTypes[i] = roboterTypes[i - 1];
            }

            // Correction of the last element of the array
            roboterTypes[0] = lastElement;
        } else {

            // Move elements to the left by 1 index
            MovementType firstElement = roboterTypes[0];
            for (int i = 0; i < roboterTypes.length - 1; i++) {
                roboterTypes[i] = roboterTypes[i + 1];
            }

            // Correction of the first element of the array
            roboterTypes[roboterTypes.length - 1] = firstElement;
        }

        // Assigning the first value of roboterTypes to type
        this.type = roboterTypes[0];
    }

    // Getter method for type
    public MovementType getType() {
        return type;
    }

    // Method that returns the type located 1 index to the right of the current type of the robot
    public MovementType getNextType() {

        // Initializing the index that iterates over the array of the robot types
        int currentIndex = -1;

        // Iterating over the array of the robot types to find the index of the actual type
        for (int i = 0; i < roboterTypes.length; i++) {
            if (roboterTypes[i] == type) {
                currentIndex = i;
                break;
            }
        }

        // Returning the next type to the right
        return roboterTypes[(currentIndex + 1) % roboterTypes.length];
    }

    // Method "shuffle" that can randomly change the type of the robot
    public boolean shuffle(int itNr) {

        // Initializing of the random function
        Random random = new Random();

        // Helper variable to store the previous type
        MovementType previousType = this.type;

        // Iterating itNr times and changing the type of the robot depending of the random generated index
        for (int i = 0; i < itNr; i++) {

            // Randomly generated index for the new type
            int randomIndex = random.nextInt(roboterTypes.length);

            // Assigning the new type to the robot's type
            this.type = roboterTypes[randomIndex];
        }

        // Check if the type has changed
        return this.type != previousType;
    }

    // Overloaded method "shuffle" that definitely changes the robot's type
    public void shuffle() {

        // Repeating the randomisation until the robot has a new type
        while (!shuffle(1)) {
        }
    }
}
