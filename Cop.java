import java.util.ArrayList;
import java.util.Random;

public class Cop implements Occupant {

    private Grid grid;
    private Patch location;
    private boolean occupied;

    public Cop(Grid grid, Patch location) {
        this.grid = grid;
        this.location = location;
        this.occupied = false;
    }

    public void move() {
        ArrayList<Patch> targets = grid.getTargets(location);

        if (targets.size() > 0) {
            Random rand = new Random();
            Patch target = targets.get(rand.nextInt(targets.size()));

            location.remove(this);
            target.add(this);
            location = target;
        }
    }

    public boolean isOccupying() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public void enforce() {
        ArrayList<Occupant> neighbors = grid.getNeighbors(location);
        ArrayList<Agent> rebels = new ArrayList<Agent>();
        for (Occupant neighbor : neighbors) {
            if (neighbor instanceof Agent
                    && ((Agent) neighbor).isActive()) {
                rebels.add((Agent) neighbor);
            }
        }

        if (rebels.size() > 0) {
            Random rand = new Random();
            int term = rand.nextInt(30);
            rebels.get(rand.nextInt(rebels.size())).arrest(term);
            // move to rebel spot
        }
    }
}
