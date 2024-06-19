package h03;

// Subclass VersatileRobot, which inherits from the class HackingRobot
public class VersatileRobot extends HackingRobot {

    // Constructor of the VersatileRobot class with the parameters x, y, order, and exchange
    public VersatileRobot(int x, int y, boolean order, boolean exchange) {

        // Calling the constructor of the base class HackingRobot
        super(x, y, order);

        // Switching the values of x and y depending on exchange
        if (!exchange) {

            // Using a help variable to store the value of x
            int aux = x ;
            setX(y) ;
            setY(aux) ;
        }

        // Checking if the type of the robot is "diagonal" and setting the y-coordinate accordingly
        if (getType() == MovementType.DIAGONAL) {
            setY(getX());
        }
    }

    // Overriding the method of the superclass
    @Override
    public boolean shuffle(int itNr) {
        boolean changed = super.shuffle(itNr);
        if (getType() == MovementType.DIAGONAL) {
            setY(getX());
        }

        // Returning if at least one of the types has changed
        return changed;
    }

    // Overriding the method of the superclass
    @Override
    public void shuffle() {
        super.shuffle();
        if (getType() == MovementType.DIAGONAL) {
            setY(getX());
        }
    }
}
