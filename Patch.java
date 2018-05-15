/* Implementation of a Patch (derived from the NetLogo concept of a Patch).
 * This can be thought of as a single "cell" in the simulation world,
 * which entities can occupy.
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

public class Patch {

    private int row; 
    private int col;
    private ArrayList<Occupant> occupants;

    // constructor
    public Patch(int row, int col) {
        this.row = row;
        this.col = col;
        occupants = new ArrayList<Occupant>();
    }

    // add occupant to patch
    public void add(Occupant occupant) {
        occupants.add(occupant);
    }

    // remove occupant from patch
    public void remove(Occupant occupant) {
        occupants.remove(occupant);
    }

    // checks whether the patch is occupied
    public boolean isOccupied() {
        for (Occupant occupant : occupants) {
            if (occupant.isOccupying()) {
                return true;
            }
        }
        return false;
    }

    // gets all occupants
    public ArrayList<Occupant> getOccupants() {
        return occupants;
    }

    // get the row
    public int getRow() {
        return row;
    }

    // get the col
    public int getCol() {
        return col;
    }

    /*
    public float distanceTo(Gridd other, int height, int width) {
        int rowDistance;
        int colDistance;

        // if rows in same half ... 
        int rowOther = other.getRow();
        if (Math.abs(row - rowOther) > height / 2) {
            // ... then just take absolute difference
            rowDistance = Math.abs(row - rowOther);

        // but if rows in opposite halfs ...
        } else {
            // ... if other row is in upper half
            if (rowOther >= height / 2) {
                // ... then transpose it to beneath the lower half
                rowOther -= height;
            }
            // ... and if the other row is in lower half
            else {
                // ... then transpose it to above the upper half
                rowOther += height;
            }
            rowDistance = Math.abs(row - rowOther);
        }

        // repeat for cols ...
        int colOther = other.getCol();
        if (Math.abs(col - colOther) >= width / 2) {
            colDistance = Math.abs(col - colOther);
        } else {
            if (colOther >= width / 2) {
                colOther -= height;
            } else {
                colOther += height;
            }
            colDistance = Math.abs(col - colOther);
        }

        return (float) Math.hypot(rowDistance, colDistance);
    }
    */
}
