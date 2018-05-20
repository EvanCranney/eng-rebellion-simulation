public class Agent implements Person {

    public enum State {
        INACTIVE,
        ACTIVE,
        JAILED
    }

    private static final State INITIAL_STATE = Agent.State.INACTIVE;
    private static final int INITIAL_TERM = 0;

    private State state;
    private Cell location;

    private double aversion;
    private double hardship;
    private int remainingTerm;

    public Agent(double aversion, double hardship, Cell location) {
        this.aversion = aversion;
        this.hardship = hardship;
        this.location = location;
        this.state = INITIAL_STATE;
        this.remainingTerm = INITIAL_TERM;
    }

    public boolean isActive() {
        return this.state == Agent.State.ACTIVE;
    }

    public boolean isJailed() {
        return this.state == Agent.State.JAILED;
    }

    public void move() {

    }

    public void rebel() {

    }

    public void arrest() {

    }

}
