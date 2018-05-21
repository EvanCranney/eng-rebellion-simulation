import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Cop {

    private Cell location;

    private Random rand;

    public Cop(Cell location) {
        this.location = location;
        this.location.enter(this);
        this.rand = new Random();
    }

    public void move() {
        // get ranodm cell in vicinity
        Cell target = this.location.getRandomNeighbor();
        // change location if possible
        if (target != null) {
            this.moveTo(target);
        }
    }

    private void moveTo(Cell target) {
        this.location.leave(this);
        target.enter(this);
        this.location = target;
    }

    public void enforce() {
        ArrayList<Agent> active = this.location.getActiveAgentsInNeighborhood();

        // if active agent visible, then arrest
        if (active.size() > 0) {
            Collections.shuffle(active);
            Agent agent = active.get(0);
            Cell target = agent.getLocation();
            this.moveTo(target);
            agent.arrest(this.rand.nextInt(Rebellion.MAX_JAIL_TERM));
        }
    }

    public Cell getLocation(){
        return this.location;
    }

}
