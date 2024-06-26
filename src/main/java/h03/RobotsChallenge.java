package h03;

/**
 * The {@code RobotsChallenge} class performs a challenge between robots of the {@code DoublePowerRobot} class.
 */
public class RobotsChallenge {

    private final DoublePowerRobot[] robots;
    private final int goal;
    private final int begin;
    private final int winThreshold = 2;

    /**
     * Constructs a new {@code RobotsChallenge} with the specified starting position, goal, and array of robots.
     *
     * @param begin  The starting position of the robots.
     * @param goal   The target coordinates.
     * @param robots The array of {@code DoublePowerRobot} objects participating in the challenge.
     */
    public RobotsChallenge(int begin, int goal, final DoublePowerRobot[] robots) {
        this.begin = begin / 2;
        this.goal = goal;
        this.robots = robots;
    }

    /**
     * Calculates the number of steps needed for a robot to reach the goal based on its movement type.
     *
     * @param type The {@code MovementType} of the robot.
     * @return The number of steps required to reach the goal.
     */
    public int calculateSteps(MovementType type) {
        int steps = 0;
        if (type == MovementType.DIAGONAL) {
            steps = Math.abs(begin - goal);
        } else if (type == MovementType.OVERSTEP) {
            steps = (Math.abs(begin - goal) % 2 == 0) ? Math.abs(begin - goal) : Math.abs(begin - goal) + 1;
        } else if (type == MovementType.TELEPORT) {
            steps = (Math.abs(begin - goal) % 2 == 0) ? Math.abs(begin - goal) / 2 : (Math.abs(begin - goal) / 2) + 2;
        }
        return steps;
    }

    /**
     * Finds the winning robots in the challenge based on their movement types and the number of steps required to reach the goal.
     *
     * @return An array of {@code DoublePowerRobot} objects that are the winners of the challenge.
     */
    public DoublePowerRobot[] findWinners() {
        int winnerCount = 0;
        DoublePowerRobot[] winners = new DoublePowerRobot[robots.length];

        for (DoublePowerRobot robot : robots) {
            int stepsFirstType = calculateSteps(robot.getType());
            int stepsSecondType = calculateSteps(robot.getNextType());
            int steps = Math.min(stepsFirstType, stepsSecondType);

            if (steps <= winThreshold) {
                winners[winnerCount] = robot;
                winnerCount++;
            }
        }

        return winners;
    }
}
