package h03.mock;

import java.util.function.BiFunction;

public class HackingRobotMock {

    private BiFunction<String, Object[], ?> methodAction;

    private static HackingRobotMock instance;

    private HackingRobotMock() {}

    public static HackingRobotMock getInstance() {
        if (instance == null) {
            instance = new HackingRobotMock();
        }
        return instance;
    }

    public void setMethodAction(BiFunction<String, Object[], ?> methodAction) {
        this.methodAction = methodAction;
    }

    public String getType() {  // transform back to MovementType
        return (String) methodAction.apply("getType", new Object[0]);
    }

    public String getNextType() {  // transform back to MovementType
        return (String) methodAction.apply("getNextType", new Object[0]);
    }

    public int getRandom(int limit) {
        return (int) methodAction.apply("getRandom", new Object[] {limit});
    }

    public boolean shuffle(int itNr) {
        return (boolean) methodAction.apply("shuffle", new Object[] {itNr});
    }

    public void shuffle() {
        methodAction.apply("shuffle", new Object[0]);
    }
}
