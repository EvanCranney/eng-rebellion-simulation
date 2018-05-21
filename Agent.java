import java.lang.Math;

public class Agent implements Person {

    public enum State {
        INACTIVE,
        ACTIVE,
        JAILED
    }

    private static final State INITIAL_STATE = Agent.State.INACTIVE;
    private static final int INITIAL_TERM = 0;
    private static final double EPSILON = 0.1;

    private State state;
    private Cell location;

    private double aversion;
    private double hardship;
    private int remainingTerm;

    public Agent(double aversion, double hardship, Cell location) {
        this.aversion = aversion;
        this.hardship = hardship;
        this.location = location;
        this.location.enter(this);
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
        // check: not jailed
        if (this.state == Agent.State.JAILED) {
            return;
        }

        // get random cell in vicinity
        Cell target = this.location.getRandomNeighbor();

        // change location if there exists an unoccupied neighbor
        if (target != null) {
            this.moveTo(target);
        }
    }

    private void moveTo(Cell target) {
        this.location.leave(this);
        target.enter(this);
        this.location = target;
    }

    public Cell getLocation() {
        return this.location;
    }

    public void rebel() {
        // if jailed, then can't do anything. just pass time
        if (this.state == Agent.State.JAILED) {
            // reduce jail-term
            this.remainingTerm--;
            if (this.remainingTerm < 0) {
                this.state = Agent.State.INACTIVE;
            }
            return;
        }

        // otherwise check if agreived
        if (this.computeGreivance() >= this.computeRisk() + Agent.EPSILON) {
            this.state = Agent.State.ACTIVE;
        } else {
            this.state = Agent.State.INACTIVE;
        }
    }

    private double computeGreivance() {
        return this.hardship * (1 - Rebellion.GOVERNMENT_LEGITIMACY);
    }

    private double computeRisk() {
        return this.aversion * this.computePerceivedDanger();
    }

    private double computePerceivedDanger() {
        int cops = this.location.countCopsInNeighborhood();
        int agents = this.location.countActiveAgentsInNeighborhood();
        if (agents <= 0) {
            agents = 1; // for numerical stability
        }
        return (1 - Math.exp(-2.3 * Math.floor(
            (double) cops / (double) agents)));
    }

    public void arrest(int term) {
        this.remainingTerm = term;
        this.state = Agent.State.JAILED;
    }

}
