/* Implementation of an Agent.
 *
 * Wanders around the simulation grid, rebels if sufficiently aggrieved
 * with the "central authority".
 * 
 */

import java.lang.Math;

public class Agent extends Person {

    // set of possible agent states
    public enum State {
        INACTIVE,
        ACTIVE,
        JAILED
    }

    // default initial values for private attributes
    private static final State INITIAL_STATE = Agent.State.INACTIVE;
    private static final int INITIAL_TERM = 0;
    
    // used to stabilise numeric calculations
    private static final double EPSILON = 0.1;

    private State state;
    //private Cell location;

    // agent's personal degree of risk aversion, and perceived hardship
    private double aversion;
    private double hardship;

    // time remaining to spend in jail, after being arrested
    private int remainingTerm;

    public Agent(double aversion, double hardship, Cell location) {
        super(location);
        this.aversion = aversion;
        this.hardship = hardship;
        //this.location = location;
        //this.location.enter(this);
        this.state = INITIAL_STATE;
        this.remainingTerm = INITIAL_TERM;
    }

    // @override
    // moves to a random cell in the neighborhood, provided that the
    //   agent is not currently in jail
    public void move() {
        if (this.state == Agent.State.JAILED) {
            return;
        }
        super.move();
    }

    // Checks whether the agent is sufficiently aggreived with the
    //   government that they wish to actively rebel. If this is the case
    //   puts them in ACTIVE state, otherwise puts them in INACTIVE state.
    //   Takes current level of government legitimacy as input.
    public void rebel(double legitimacy) {
        // if currently in jail ...
        if (this.state == Agent.State.JAILED) {
            // ... then reduce jail term by 1
            this.remainingTerm--;

            // if agent still has time to serve ...
            if (this.remainingTerm > 0) {
                // ... then can't do anything else
                return;
            }
        }

        // otherwise, if agent's grievance outweighs risk of rebelling ...
        if (this.computeGrievance(legitimacy) >= this.computeRisk()
                                                + Agent.EPSILON) {
            // ... then rebel
            this.state = Agent.State.ACTIVE;
        } else {
            // ... otherwise don't rebel
            this.state = Agent.State.INACTIVE;
        }
    }

    // compute an agent's grievance against the government, given their
    //   current level of legitimacy
    private double computeGrievance(double legitimacy) {
        return this.hardship * (1 - legitimacy);
    }

    // compute an agent's perceived risk of actively rebelling
    private double computeRisk() {
        return this.aversion * this.computePerceivedDanger();
    }

    // computes an agent's perceived danger of actively rebelling, based
    //   on their own risk aversion, and the ratio of cops to active
    //   agents in the neighborhood
    private double computePerceivedDanger() {
        // count the number of cops and active agents in neighborhood
        int cops = super.getLocation().countCopsInNeighborhood();
        int agents = super.getLocation().countActiveAgentsInNeighborhood();

        // denominator is at least 1, for numerical stability
        if (agents <= 0) {
            agents = 1;
        }

        // compute perceived danger
        return (1 - Math.exp(-2.3 * Math.floor(
            (double) cops / (double) agents)));
    }

    // arrest the agent - puts the agent in JAILED state for the given
    //  number of time-steps
    public void arrest(int term) {
        this.remainingTerm = term;
        this.state = Agent.State.JAILED;
    }

    // check whether the agent is actively rebelling
    public boolean isActive() {
        return this.state == Agent.State.ACTIVE;
    }

    // check whether the agent is in jail
    public boolean isJailed() {
        return this.state == Agent.State.JAILED;
    }

    // get the agent's current state
    public State getState(Agent agent){
        return this.state;
    }
}
