import java.awt.*;

public class ShipOrientation {

    private Point[][] points = new Point[10][10];
    public String[][] userBoatLocation = new String[10][10];
    public Boat userDestroyer, userCruiser, userSub, userBattleship, userCarrier;

    public ShipOrientation() {}

    public void createRandomOrientation() {
        for(int i = 0; i < points.length; ++i) {
            for(int j = 0; j < points[i].length; ++j) {
                points[i][j] = new Point( i+1, j+1);
            }
        }
        Point destroyerStart = points[0][0];
        Point destroyerEnd = new Point((int)destroyerStart.getX(),(int)destroyerStart.getY()+2);
        userDestroyer = new Boat("Destroyer", 2);
        this.addBoat(userDestroyer, new Point(0, 1), new Point(0, 2));
    }

    public boolean isOverlapping(int col1, int row1, int col2, int row2) {
        boolean bool = false;
        if (col1 == col2) {
            for (int i = row1; i <= row2; ++i) {
                if(userBoatLocation[col1][i] != null)
                    bool = true;
            }
        }
        else if (row1 == row2) {
            for (int i = col1; i <= col2; ++i) {
                if(userBoatLocation[i][row1] != null)
                    bool = true;
            }
        }
        return bool;
    }

    public void addBoat(Boat boat, Point start, Point end) {
        switch (boat.getBoatType()) {
            case "Destroyer":
                userDestroyer = boat;
            case "Submarine":
                userSub = boat;
            case "Cruiser":
                userCruiser = boat;
            case "Battleship":
                userBattleship = boat;
            case "Carrier":
                userCarrier = boat;
        }
        if ((int)start.getY() == (int)end.getY()) {
            for (int i = (int)start.getX(); i <= end.getX(); i++) {
                userBoatLocation[i][(int)end.getY()] = boat.getBoatType();
                System.out.println("i=" + i + " " + end.getY());
            }
        } else if ((int)start.getX() == (int)end.getX()) {
            for (int i = (int)start.getY(); i <= end.getY(); i++) {
                userBoatLocation[(int)end.getX()][i] = boat.getBoatType();
                System.out.println(end.getX() + " " + "i=" + i);
            }
        }
    }

    public void updateRemainingShips() {
        int destroyerSpots = 0, cruiserSpots = 0, subSpots = 0, battleshipSpots = 0, carrierSpots = 0;
        for(String[] sArr : userBoatLocation) {
            for(String s: sArr) {
                if(s == "Destroyer")
                    ++destroyerSpots;
                if(s == "Cruiser")
                    ++cruiserSpots;
                if(s == "Submarine")
                    ++subSpots;
                if(s == "Battleship")
                    ++battleshipSpots;
                if(s == "Carrier")
                    ++carrierSpots;
            }
        }
        if(destroyerSpots == 0)
            userDestroyer.setSunk(true);
        if(cruiserSpots == 0)
            userCruiser.setSunk(true);
        if(subSpots == 0)
            userSub.setSunk(true);
        if(battleshipSpots == 0)
            userBattleship.setSunk(true);
        if(carrierSpots == 0)
            userCarrier.setSunk(true);
    }

    public boolean allShipsSunk() {
        if (userDestroyer.isUnSunk() == false && userSub.isUnSunk() == false && userCruiser.isUnSunk() == false
        && userBattleship.isUnSunk() == false && userCarrier.isUnSunk() == false)
            return true;
        else
            return false;
    }
} //end of ShipOrientation class
