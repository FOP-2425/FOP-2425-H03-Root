package h03;

// Subclass DoublePowerRobot, which inherits from the `HackingRobot` class and allows the robot to have two types simultaneously
public class DoublePowerRobot extends HackingRobot {

    // Private array doublePowerTypes containing the two types for the DoublePowerRobot.
    private MovementType[] doublePowerTypes = new MovementType[2];

    // Constructor of the DoublePowerRobot class with parameters x, y, and order
    public DoublePowerRobot(int x, int y, boolean order) {

        // Calling the constructor of the base class HackingRobot
        super(x, y, order);

        // Assigning the two types to doublePowerTypes
        doublePowerTypes[0] = getType();
        doublePowerTypes[1] = getNextType();
    }

    // Overriding the method of the super class
    @Override
    public boolean shuffle(int itNr) {
        // Calling the shuffle method of the superclass to change the robot's type
        boolean changed = super.shuffle(itNr);

        // Updating the types in doublePowerTypes based on the new value of type
        doublePowerTypes[0] = getType();
        doublePowerTypes[1] = getNextType();

        // Returning whether the types have changed
        return changed;
    }

    // Overriding the method of the superclass
    @Override
    public void shuffle() {
        // Calling the shuffle method of the superclass to change the robot's type
        super.shuffle();
        doublePowerTypes[0] = getType();
        doublePowerTypes[1] = getNextType();
    }
}
