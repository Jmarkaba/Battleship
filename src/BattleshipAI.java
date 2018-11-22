import javafx.scene.image.Image;
import java.awt.Point;
import java.util.ArrayList;

public class BattleshipAI {

    private static int turnNumber = 0;
    private static int[][] possibility_matrix = new int[10][10]; // ------> number of possible ship overlaps
    private static ArrayList<Point> potential_targets = new ArrayList<>(); // --> ArrayList of next target
    private static int[][] results = new int[10][10]; // -------> 0 is empty, 1 is miss/hit
    public static Point lastShot;
    private static boolean lastShotSucceeded = false;

    public BattleshipAI() {} //Unnecessary constructor

    public static void beginTurn() {
        shoot();
        turnNumber++;
        Game.playerShips.updateRemainingShips();
        if(Game.playerShips.allShipsSunk())
            Game.endGame();
        else
            Game.continueGame();

    }

    private static void shoot() {
        Point tar = chooseTarget();
        int tar_x = (int) tar.getX();
        int tar_y = (int) tar.getY();
        lastShot = tar;
        possibility_matrix[tar_x][tar_y] = 0;
        if (Game.playerShips.userBoatLocation[tar_x][tar_y] != null) {
            Game.player_grid[tar_x][tar_y].setImage(
                    new Image("resources/enemy_hit.png", 30, 30 , false, false));
            results[tar_x][tar_y] = 1;
            Game.playerShips.userBoatLocation[tar_x][tar_y] = null;
            setLastShotSucceeded(true);
        } else {
            Game.player_grid[tar_x][tar_y].setImage(
                    new Image("resources/enemy_miss.png", 30, 30, false, false));
            results[tar_x][tar_y] = 1;
            Game.playerShips.userBoatLocation[tar_x][tar_y] = null;
            setLastShotSucceeded(false);
        }
    }

    private static Point chooseTarget() {
        Point target;
        int column; // ----> A through J
        int row; // -------> 1 through 10

        if(turnNumber > 0) {
            if(getLastShotSucceeded() && potential_targets.size() != 0) {
                assignTargetBasedOnHit();
                int choice = (int) (Math.random()*(potential_targets.size()));
                int choiceNum = choice;
                System.out.println("Based on hit " + choice + " pot_tar_size " + potential_targets.size());
                target = potential_targets.get(choiceNum);
                System.out.println(potential_targets);
                System.out.println(target);
                potential_targets.remove(choiceNum);
                System.out.println(potential_targets);
            } else {
                if(potential_targets.size() == 0) {
                    assignTargetBasedOnProbability();
                    int choice = (int) (Math.random()*(potential_targets.size()));
                    int choiceNum = choice;
                    System.out.println("Based on prob " + choice + " pot_tar_size " + potential_targets.size());
                    target = potential_targets.get(choiceNum);
                    potential_targets.clear();
                    System.out.println("pot_tar_size' " + potential_targets.size());
                } else {
                    int choice = (int) (Math.random()*(potential_targets.size()));
                    int choiceNum = choice;
                    System.out.println("Based on leftovers " + choice + " pot_tar_size " + potential_targets.size());
                    target = potential_targets.get(choiceNum);
                    potential_targets.remove(choiceNum);
                    System.out.println(target);
                    System.out.println(potential_targets);
                }
            }

        } else {
            column = (int) (10 * (Math.random()));
            row = (int) (10 * (Math.random()));
            target = new Point(row, column);
        }
        return target;
    }

    /*
    Create target list and choose appropriate target
     */
    private static void assignTargetBasedOnProbability() {
        calculatePossibilities();
        int highestProb = 0;
        for (int i = 0; i < possibility_matrix.length; ++i) {
            for (int j = 0; j < possibility_matrix[i].length; ++j) {
                if(possibility_matrix[i][j] >= highestProb) {
                    highestProb = possibility_matrix[i][j];
                }
            }
        }
        System.out.println("Highest prob " + highestProb);
        for (int i = 0; i < possibility_matrix.length; ++i) {
            for (int j = 0; j < possibility_matrix[i].length; ++j) {
                if(possibility_matrix[i][j] == highestProb) {
                    potential_targets.add(new Point( i, j));
                }
            }
        }
    }

    private static boolean notContains(Point point) {
        boolean b = true;
        for(Point p : potential_targets) {
            if (p.equals(point))
                b = false;
        }
        return b;
    }

    private static void checkAndAdd(Point p) {
        if(results[(int)p.getX()][(int)p.getY()] == 0)
            if(notContains(p))
                potential_targets.add(p);
    }

    private static void assignTargetBasedOnHit() {
        if((int)lastShot.getX() > 0 && (int)lastShot.getX() < 9 && (int)lastShot.getY() > 0 && (int)lastShot.getY() < 9) {
            checkAndAdd(new Point((int)lastShot.getX() + 1,(int)lastShot.getY()));
            checkAndAdd(new Point((int)lastShot.getX() - 1,(int)lastShot.getY()));
            checkAndAdd(new Point((int)lastShot.getX(),(int)lastShot.getY() + 1));
            checkAndAdd(new Point((int)lastShot.getX(),(int)lastShot.getY() - 1));
        }
        else if ((int)lastShot.getY() == 0) {
            checkAndAdd(new Point((int)lastShot.getX(),(int)lastShot.getY() + 1));
            if ((int)lastShot.getX() != 0)
                checkAndAdd(new Point((int)lastShot.getX() - 1,(int)lastShot.getY()));
            if ((int)lastShot.getX() != 9)
                checkAndAdd(new Point((int)lastShot.getX() + 1,(int)lastShot.getY()));
        }
        else if ((int)lastShot.getX() == 0) {
            checkAndAdd(new Point((int)lastShot.getX() + 1,(int)lastShot.getY()));
            if ((int)lastShot.getY() != 0)
                checkAndAdd(new Point((int)lastShot.getX(),(int)lastShot.getY() - 1));
            if ((int)lastShot.getY() != 9)
                checkAndAdd(new Point((int)lastShot.getX(),(int)lastShot.getY() + 1));
        }
        else if ((int)lastShot.getY() == 9) {
            checkAndAdd(new Point((int)lastShot.getX(),(int)lastShot.getY() - 1));
            if ((int)lastShot.getX() != 0)
                checkAndAdd(new Point((int)lastShot.getX() - 1,(int)lastShot.getY()));
            if ((int)lastShot.getX() != 9)
                checkAndAdd(new Point((int)lastShot.getX() + 1,(int)lastShot.getY()));
        }
        else if ((int)lastShot.getX() == 9) {
            checkAndAdd(new Point((int)lastShot.getX() - 1,(int)lastShot.getY()));
            if ((int)lastShot.getY() != 0)
                checkAndAdd(new Point((int)lastShot.getX(),(int)lastShot.getY() - 1));
            if ((int)lastShot.getY() != 9)
                checkAndAdd(new Point((int)lastShot.getX(),(int)lastShot.getY() + 1));
        }
    }

    /*
    Update a probability matrix of where ships can be located
     */

    private static void calculatePossibilities() {
        for (int i = 0; i < results.length; ++i) {
            for (int j = 0; j < results[i].length; ++j) {
                if (results[i][j] == 0) {
                    int possibilities = 0;
                    if (Game.playerShips.userCarrier.isUnSunk())
                        possibilities += possibilitiesWithCarrierLeft(i, j);
                    if (Game.playerShips.userBattleship.isUnSunk())
                        possibilities += possibilitiesWithBattleshipLeft(i, j);
                    if (Game.playerShips.userCruiser.isUnSunk())
                        possibilities += possibilitiesWithCruiserOrSubLeft(i, j);
                    if (Game.playerShips.userSub.isUnSunk())
                        possibilities += possibilitiesWithCruiserOrSubLeft(i, j);
                    if (Game.playerShips.userDestroyer.isUnSunk())
                        possibilities += possibilitiesWithDestroyerLeft(i, j);
                    possibility_matrix[i][j] = possibilities;
                } else { possibility_matrix[i][j] = 0; }
            }
        }
    }

    public static boolean getLastShotSucceeded() {
        return lastShotSucceeded;
    }

    public static int getTurnNumber() { return turnNumber; }

    public static void setLastShotSucceeded(boolean bool) {
        lastShotSucceeded = bool;
    }

    //length 5 ship
    private static int possibilitiesWithCarrierLeft(int i, int j) {
        /*
        Horizontal tests
         */
        int xlength = 1;
        try {
            if (BattleshipAI.results[i-1][j] == 0) {
                xlength++;
                try {
                    if (BattleshipAI.results[i-2][j] == 0) {
                        xlength++;
                        try {
                            if (BattleshipAI.results[i-3][j] == 0) {
                                xlength++;
                                try {
                                    if (BattleshipAI.results[i-4][j] == 0)
                                        xlength++;
                                } catch (IndexOutOfBoundsException e) {}
                            }
                        } catch (IndexOutOfBoundsException e) {}
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        } catch (IndexOutOfBoundsException e) {}
        try {
            if (BattleshipAI.results[i+1][j] == 0) {
                xlength++;
                try {
                    if (BattleshipAI.results[i+2][j] == 0) {
                        xlength++;
                        try {
                            if (BattleshipAI.results[i+3][j] == 0) {
                                xlength++;
                                try {
                                    if (BattleshipAI.results[i+4][j] == 0)
                                        xlength++;
                                } catch (IndexOutOfBoundsException e) {}
                            }
                        } catch (IndexOutOfBoundsException e) {}
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        } catch (IndexOutOfBoundsException e) {}

        /*
        Vertical tests
         */
        int ylength = 1;
        try {
            if (BattleshipAI.results[i][j-1] == 0) {
                ylength++;
                try {
                    if (BattleshipAI.results[i][j-2] == 0) {
                        ylength++;
                        try {
                            if (BattleshipAI.results[i][j-3] == 0) {
                                ylength++;
                                try {
                                    if (BattleshipAI.results[i][j-4] == 0)
                                        ylength++;
                                } catch (IndexOutOfBoundsException e) {}
                            }
                        } catch (IndexOutOfBoundsException e) {}
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        } catch (IndexOutOfBoundsException e) {}
        try {
            if (BattleshipAI.results[i][j+1] == 0) {
                ylength++;
                try {
                    if (BattleshipAI.results[i][j+2] == 0) {
                        ylength++;
                        try {
                            if (BattleshipAI.results[i][j+3] == 0) {
                                ylength++;
                                try {
                                    if (BattleshipAI.results[i][j+4] == 0)
                                        ylength++;
                                } catch (IndexOutOfBoundsException e) {}
                            }
                        } catch (IndexOutOfBoundsException e) {}
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        } catch (IndexOutOfBoundsException e) {}

        int possibilities = 0;
        if (xlength >=5)
            possibilities += (xlength - 4);
        if (ylength >=5)
            possibilities += (ylength - 4);

        return possibilities;
    }

    //length 4 ship
    private static int possibilitiesWithBattleshipLeft(int i, int j) {
        /*
        Horizontal tests
         */
        int xlength = 1;
        try {
            if (BattleshipAI.results[i-1][j] == 0) {
                xlength++;
                try {
                    if (BattleshipAI.results[i-2][j] == 0) {
                        xlength++;
                        try {
                            if (BattleshipAI.results[i-3][j] == 0) {
                                xlength++;
                            }
                        } catch (IndexOutOfBoundsException e) {}
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        } catch (IndexOutOfBoundsException e) {}
        try {
            if (BattleshipAI.results[i+1][j] == 0) {
                xlength++;
                try {
                    if (BattleshipAI.results[i+2][j] == 0) {
                        xlength++;
                        try {
                            if (BattleshipAI.results[i+3][j] == 0) {
                                xlength++;
                            }
                        } catch (IndexOutOfBoundsException e) {}
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        } catch (IndexOutOfBoundsException e) {}

        /*
        Vertical tests
         */
        int ylength = 1;
        try {
            if (BattleshipAI.results[i][j-1] == 0) {
                ylength++;
                try {
                    if (BattleshipAI.results[i][j-2] == 0) {
                        ylength++;
                        try {
                            if (BattleshipAI.results[i][j-3] == 0) {
                                ylength++;
                            }
                        } catch (IndexOutOfBoundsException e) {}
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        } catch (IndexOutOfBoundsException e) {}
        try {
            if (BattleshipAI.results[i][j+1] == 0) {
                ylength++;
                try {
                    if (BattleshipAI.results[i][j+2] == 0) {
                        ylength++;
                        try {
                            if (BattleshipAI.results[i][j+3] == 0) {
                                ylength++;
                            }
                        } catch (IndexOutOfBoundsException e) {}
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        } catch (IndexOutOfBoundsException e) {}

        int possibilities = 0;
        if (xlength >=4)
            possibilities += (xlength - 3);
        if (ylength >=4)
            possibilities += (ylength - 3);

        return possibilities;
    }

    //length 3 ship
    private static int possibilitiesWithCruiserOrSubLeft(int i, int j) {
        /*
        Horizontal tests
         */
        int xlength = 1;
        try {
            if (BattleshipAI.results[i-1][j] == 0) {
                xlength++;
                try {
                    if (BattleshipAI.results[i-2][j] == 0) {
                        xlength++;
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        } catch (IndexOutOfBoundsException e) {}
        try {
            if (BattleshipAI.results[i+1][j] == 0) {
                xlength++;
                try {
                    if (BattleshipAI.results[i+2][j] == 0) {
                        xlength++;
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        } catch (IndexOutOfBoundsException e) {}

        /*
        Vertical tests
         */
        int ylength = 1;
        try {
            if (BattleshipAI.results[i][j-1] == 0) {
                ylength++;
                try {
                    if (BattleshipAI.results[i][j-2] == 0) {
                        ylength++;
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        } catch (IndexOutOfBoundsException e) {}
        try {
            if (BattleshipAI.results[i][j+1] == 0) {
                ylength++;
                try {
                    if (BattleshipAI.results[i][j+2] == 0) {
                        ylength++;
                    }
                } catch (IndexOutOfBoundsException e) {}
            }
        } catch (IndexOutOfBoundsException e) {}

        int possibilities = 0;
        if (xlength >=3)
            possibilities += (xlength - 2);
        if (ylength >=3)
            possibilities += (ylength - 2);

        return possibilities;
    }

    //length 2 ship
    private static int possibilitiesWithDestroyerLeft(int i, int j) {
        /*
        Horizontal tests
         */
        int xlength = 1;
        try {
            if (BattleshipAI.results[i-1][j] == 0) {
                xlength++;
            }
        } catch (IndexOutOfBoundsException e) {}
        try {
            if (BattleshipAI.results[i+1][j] == 0) {
                xlength++;
            }
        } catch (IndexOutOfBoundsException e) {}

        /*
        Vertical tests
         */
        int ylength = 1;
        try {
            if (BattleshipAI.results[i][j-1] == 0) {
                ylength++;
            }
        } catch (IndexOutOfBoundsException e) {}
        try {
            if (BattleshipAI.results[i][j+1] == 0) {
                ylength++;
            }
        } catch (IndexOutOfBoundsException e) {}

        int possibilities = 0;
        if (xlength >=2)
            possibilities += (xlength - 1);
        if (ylength >=2)
            possibilities += (ylength - 1);

        return possibilities;
    }
} //end of BattleshipAI class