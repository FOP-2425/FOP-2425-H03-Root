package h03;

// RobotsChallenge class, which performs a challenge between robots of the DoublePowerRobot class
public class RobotsChallenge {

    private final DoublePowerRobot[] robots;
    private final int goal;
    private final int begin;
    private final int winThreshold=2;

    public RobotsChallenge(int begin, int goal, final DoublePowerRobot[] robots) {

        // Setting the starting position of the robots
        this.begin=begin/2;

        // Setting the target coordinates
        this.goal = goal;

        // Setting the array of robots
        this.robots = robots;
    }

    // Method that calculates the number of steps needed to reach the goal
    public int calculateSteps(MovementType type) {
        int steps = 0;
        if (type == MovementType.DIAGONAL) {
            // Calculating the number of steps required for the "diagonal" type
            steps = Math.abs(begin - goal);
        } else if (type == MovementType.OVERSTEP) {
            // Calculating the number of steps required for the "overStep" type
            steps = (Math.abs(begin - goal) % 2 == 0) ? Math.abs(begin - goal) : Math.abs(begin - goal) + 1;
        } else if (type == MovementType.TELEPORT) {
            // Calculating the number of steps required for the "teleport" type
            steps = (Math.abs(begin - goal) % 2 == 0) ? Math.abs(begin - goal) / 2 : (Math.abs(begin - goal) / 2) + 2;
        }
        return steps;
    }

    // Method that returns the winning robots in the challenge
    public DoublePowerRobot[] findWinners() {

        // Initializing the iterator to count the number of winning robots
        int winnerCount = 0;

        // Initializing the array in which the winning robots will be stored
        DoublePowerRobot[] winners = new DoublePowerRobot[robots.length];

        // Iterating over all the robots that participate in the challenge
        for (DoublePowerRobot robot : robots) {

            // Calculating the number of steps with the first type
            int stepsFirstType = calculateSteps(robot.getType());

            // Calculating the number of steps with the second type
            int stepsSecondType = calculateSteps(robot.getNextType());

            // Calculating the minimal number of steps
            int steps = Math.min(stepsFirstType, stepsSecondType);

            // Verifying if the number of steps allows to the roboter to win
            if (steps <= winThreshold) {
                winners[winnerCount]=robot;
                winnerCount=winnerCount+1;
            }
        }

        // Returning the array of the winning robots
        return winners;
    }
}
