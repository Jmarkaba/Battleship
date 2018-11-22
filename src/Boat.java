import java.awt.*;

public class Boat {

    private String boatType;
    private Point start;
    private Point end;
    private int boatLength;
    private boolean sunk = false;

    public Boat(String boatType, int boatLength) {
        this.boatType = boatType;
        this.boatLength = boatLength;
    }

    public int getBoatLength() {
        return boatLength;
    }

    public String getBoatType() {
        return boatType;
    }

    public boolean isUnSunk() {
        return !sunk;
    }

    public void setSunk(boolean b) {
        sunk = b;
    }
} //end of Boat class

