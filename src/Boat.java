public class Boat {

    private int boatType;
    private int boatLength;
    private boolean sunk;

    public Boat(int boatType, int boatLength) {
        this.boatType = boatType;
        this.boatLength = boatLength;
        sunk = false;
    }

    public int getBoatLength() {
        return boatLength;
    }

    public int getBoatType() {
        return boatType;
    }

    public boolean isSunk() {
        return sunk;
    }

    public void setSunk(boolean b) {
        sunk = b;
    }
} //end of Boat class

