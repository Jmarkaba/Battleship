import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;

public class Game {

    private static Scene game_scene;
    private static Group root;
    private static TextArea textArea, userIn1, userIn2;
    private static Button enter;
    private static ImageView[][] enemy_grid = new ImageView[10][10];
    public static ImageView[][] player_grid = new ImageView[10][10];
    private static boolean[][] wasHit = new boolean[10][10];

    protected static ShipOrientation playerShips = new ShipOrientation();
    private static ShipOrientation enemyShips = new ShipOrientation();

    private static String currentPrompt;
    private static int currentShipID = 1;
    private static int currentShipIDLength = 1;
    protected static boolean player1LastHitSucceeded;

    public Game(Stage stage) {
        enemyShips.createRandomOrientation();

        createScene(stage);
        stage.setScene(game_scene);
        stage.sizeToScene();
        stage.centerOnScreen();

        promptUserForShips();
    }

    public static void continueGame(){
        disableGridButtons(false);
        if(player1LastHitSucceeded)
            textArea.setText("Your last shot was a HIT");
        else
            textArea.setText("Your last shot was a MISS");
        textArea.appendText("\nThe enemy shot at " + convertNumberToLetter((int)BattleshipAI.lastShot.getY()) + (BattleshipAI.lastShot.getX()+1));
        if(BattleshipAI.getLastShotSucceeded())
            textArea.appendText("\nThe enemy's last shot was a HIT");
        else
            textArea.appendText("\nThe enemy's last shot was a MISS");
        textArea.appendText("\nTake your next shot!");
    }

    private static void startGame() {
        textArea.clear();
        userIn1.setVisible(false);
        userIn1.setDisable(true);
        userIn2.setVisible(false);
        userIn2.setDisable(true);
        enter.setVisible(false);
        enter.setDisable(true);
        textArea.clear();
        textArea.setText("It's your turn click on the enemy board to" +
                "\nfire at a spot.");
        disableGridButtons(false);
    }

    public static void endGame() {
        textArea.clear();
        if(enemyShips.allShipsSunk())
            textArea.appendText("YOU WIN!");
        else if(playerShips.allShipsSunk())
            textArea.appendText("YOU LOSE!");
        textArea.appendText("\nGame lasted: " + BattleshipAI.getTurnNumber() + " turns.");
    }

    private static void showBoatOnGrid(int col1, int row1, int col2, int row2){
        if (col1 == col2) {
            for (int i = row1; i <= row2; ++i) {
                player_grid[col1][i].setImage(new Image("resources/grid_box_w_ship.png", 30, 30,
                        false, false));
            }
        }
        else if (row1 == row2) {
            for (int i = col1; i <= col2; ++i) {
                player_grid[i][row1].setImage(new Image("resources/grid_box_w_ship.png", 30, 30,
                        false, false));
            }
        }
    }

    private static void createNextBoat(int col1, int row1, int col2, int row2){
        switch (currentShipID) {
            case 1:
                playerShips.addBoat(new Boat("Destroyer", 2), new Point(col1, row1), new Point(col2, row2));
                currentPrompt =
                        "Placing submarine..." +
                        "\nEnter the coordinates of the submarine " +
                        "\n(length 3)";
                textArea.setText(currentPrompt);
                currentShipIDLength = 2;
                showBoatOnGrid(col1, row1, col2, row2);
                break;
            case 2:
                playerShips.addBoat(new Boat("Submarine", 3), new Point(col1, row1), new Point(col2, row2));
                currentPrompt =
                        "Placing cruiser..." +
                        "\nEnter the coordinates of the cruiser" +
                        "\n(length 3)";
                textArea.setText(currentPrompt);
                showBoatOnGrid(col1, row1, col2, row2);
                break;
            case 3:
                playerShips.addBoat(new Boat("Cruiser", 3), new Point(col1, row1), new Point(col2, row2));
                currentPrompt =
                        "Placing battleship..." +
                        "\nEnter the coordinates of the battleship " +
                        "\n(length 4)";
                textArea.setText(currentPrompt);
                currentShipIDLength = 3;
                showBoatOnGrid(col1, row1, col2, row2);
                break;
            case 4:
                playerShips.addBoat(new Boat("Battleship", 2), new Point(col1, row1), new Point(col2, row2));
                currentPrompt =
                        "Placing carrier..." +
                        "\nEnter the coordinates of the aircraft carrier" +
                        "\n(length 5)";
                textArea.setText(currentPrompt);
                currentShipIDLength = 4;
                showBoatOnGrid(col1, row1, col2, row2);
                break;
            case 5:
                playerShips.addBoat(new Boat("Carrier", 2), new Point(col1, row1), new Point(col2, row2));
                showBoatOnGrid(col1, row1, col2, row2);
                startGame();
                break;
        }
    }

    private static void promptUserForShips() {
        currentPrompt = "Welcome to Battleship!" +
                "\nPlace your ships on your board." +
                "\nPlacing destroyer..." +
                "\nEnter the coordinates of the ship" +
                "\n(length 2)" +
                "\ne.g., 'A8' and 'A9'";
        textArea = new TextArea(currentPrompt);
        textArea.setLayoutX(game_scene.getWidth()/9);
        textArea.setLayoutY(game_scene.getHeight()*7/10);
        textArea.setPrefSize(260, 115);
        textArea.setDisable(true);
        userIn1 = new TextArea();
        userIn1.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 2 ? change : null));
        userIn1.setLayoutX(textArea.getLayoutX());
        userIn1.setLayoutY(textArea.getLayoutY() + 125);
        userIn1.setMaxSize(40, 15);
        userIn2 = new TextArea();
        userIn2.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 2 ? change : null));
        userIn2.setLayoutX(userIn1.getLayoutX() + 50);
        userIn2.setLayoutY(textArea.getLayoutY() + 125);
        userIn2.setMaxSize(40, 15);
        enter = new Button("Enter");
        enter.setLayoutX(userIn2.getLayoutX() + 50);
        enter.setLayoutY(userIn2.getLayoutY() + 2.5);
        /*
        Enter button
         */
        enter.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String input1 = "", input2 = "";
                try {
                    input1 = userIn1.getText();
                    input2 = userIn2.getText();
                    userIn1.clear(); userIn2.clear();
                } catch (Exception e) { reportError("Please enter both inputs.");}
                char letter1, letter2;
                int row1 = 10, row2 = 10, column1 = 10, column2 = 10, thirdDig1, thirdDig2;
                try {
                    letter1 = input1.toUpperCase().charAt(0);
                    letter2 = input2.toUpperCase().charAt(0);
                    row1 = convertLetterToNumber(letter1);
                    row2 = convertLetterToNumber(letter2);
                    column1 = Character.getNumericValue(input1.charAt(1)) - 1;
                    column2 = Character.getNumericValue(input2.charAt(1)) - 1;
                } catch (Exception e) {}
                try {
                    thirdDig1 = Character.getNumericValue(input1.charAt(2));
                    if(thirdDig1 == 0 && column1 == 0)
                        column1 = 9;
                } catch (Exception e) {}
                try {
                    thirdDig2 = Character.getNumericValue(input2.charAt(2));
                    if(thirdDig2 == 0 && column2 == 0)
                        column2 = 9;
                } catch (Exception e) {}
                if(column1 >= 0 && column1 <= 9 && row1 >= 0 && row1 <= 9
                && column2 >= 0 && column2 <= 9 && row2 >= 0 && row2 <= 9) {
                    if (column1 == column2) {
                        if(row2 == row1 + currentShipIDLength) {
                            if(!playerShips.isOverlapping(column1, row1, column2, row2)) {
                                createNextBoat(column1, row1, column2, row2);
                                ++currentShipID;
                            } else {
                                reportError("SHIPS OVERLAP. TRY AGAIN");
                            }
                        } else {
                            reportError("IMPOSSIBLE SHIP POSITIONING. TRY AGAIN");
                        }
                    }
                    else if (row1 == row2) {
                        if(column2 == column1 + currentShipIDLength) {
                            if(!playerShips.isOverlapping(column1, row1, column2, row2)) {
                                createNextBoat(column1, row1, column2, row2);
                                ++currentShipID;
                            } else {
                                reportError("SHIPS OVERLAP. TRY AGAIN");
                            }
                        } else {
                            reportError("IMPOSSIBLE SHIP POSITIONING. TRY AGAIN");
                        }
                    } else {
                        reportError("INVALID INPUT. TRY AGAIN");
                    }
                } else {
                    reportError("INVALID INPUT. TRY AGAIN");
                }
            }
        });
        /*
        End of enter button
         */
        root.getChildren().addAll(textArea, userIn1, userIn2, enter);
    }

    public static void disableGridButtons(boolean bool) {
        for (int i = 0; i < enemy_grid.length; ++i) {
            for (int j = 0; j < enemy_grid[i].length; ++j) {
                if (!wasHit[i][j])
                    enemy_grid[i][j].setDisable(bool);
            }
        }
    }

    private static void createScene(Stage stage) {
        /*
        Scene Design of game
         */
        root = new Group();
        game_scene = new Scene(root, 900, 600);
        //game_scene.setFill(Color.DARKBLUE);

        for (int i = 0; i < enemy_grid.length; ++i) {
            for (int j = 0; j < enemy_grid[i].length; ++j) {
                enemy_grid[i][j] = new ImageView(new Image("resources/grid_box.png", 40, 40,
                        false, false));
                enemy_grid[i][j].setLayoutX(game_scene.getWidth()/2 + j*40);
                enemy_grid[i][j].setLayoutY(game_scene.getHeight()/2 - game_scene.getHeight()/5 + i*40);
                enemy_grid[i][j].setCursor(Cursor.CROSSHAIR);
                enemy_grid[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        disableGridButtons(true);
                        double x = event.getSceneX();
                        double y = event.getSceneY();
                        int x_index = (int)(x-450)/40;
                        int y_index = (int)(y-180)/40;
                        if(enemyShips.userBoatLocation[y_index][x_index] != null){
                            enemy_grid[y_index][x_index].setImage(new Image("resources/hit.png", 40, 40,
                            false, false));
                            enemyShips.userBoatLocation[y_index][x_index] = null;
                            wasHit[y_index][x_index] = true;

                        } else {
                            enemy_grid[y_index][x_index].setImage(new Image("resources/miss.png", 40, 40,
                                    false, false));
                            wasHit[y_index][x_index] = true;
                        }
                        playerShips.updateRemainingShips();
                        if(enemyShips.allShipsSunk())
                            endGame();
                        else
                            BattleshipAI.beginTurn();
                    }
                });
                enemy_grid[i][j].setDisable(true);
                root.getChildren().add(enemy_grid[i][j]);
            }
        }
        for (int i = 0; i < player_grid.length; ++i) {
            for (int j = 0; j < player_grid[i].length; ++j) {
                player_grid[i][j] = new ImageView(new Image("resources/grid_box.png", 30, 30,
                        false, false));
                player_grid[i][j].setLayoutX(enemy_grid[0][0].getLayoutX() - 30*10 - 40
                        + j*30);
                player_grid[i][j].setLayoutY(enemy_grid[0][0].getLayoutY() - game_scene.getHeight()/6
                        + i*30);
                root.getChildren().add(player_grid[i][j]);
            }
        }

        HBox player_columns = new HBox(33, new Label("A"), new Label("B"), new Label("C"), new Label("D"), new Label("E"),
                new Label("F"), new Label("G"), new Label("H"), new Label("I"), new Label("J"));
        player_columns.setLayoutX(enemy_grid[0][0].getLayoutX());
        player_columns.setLayoutY(enemy_grid[0][0].getLayoutY() - 25);
        player_columns.setPadding(new Insets(0, 0, 0 ,15));

        HBox enemy_grid_columns = new HBox(23, new Label("A"), new Label("B"), new Label("C"), new Label("D"), new Label("E"),
                new Label("F"), new Label("G"), new Label("H"), new Label("I"), new Label("J"));
        enemy_grid_columns.setLayoutX(player_grid[0][0].getLayoutX());
        enemy_grid_columns.setLayoutY(player_grid[0][0].getLayoutY() - 25);
        enemy_grid_columns.setPadding(new Insets(0, 0, 0 ,7));

        VBox enemy_rows = new VBox(22.5, new Label("1"), new Label("2"), new Label("3"), new Label("4"), new Label("5"),
                new Label("6"), new Label("7"), new Label("8"), new Label("9"), new Label("10"));
        enemy_rows.setLayoutX(enemy_grid[0][0].getLayoutX() - 25);
        enemy_rows.setLayoutY(enemy_grid[0][0].getLayoutY());
        enemy_rows.setPadding(new Insets( 14, 0, 0, 0));
        enemy_rows.setAlignment(Pos.CENTER);

        VBox player_rows = new VBox(13, new Label("1"), new Label("2"), new Label("3"), new Label("4"), new Label("5"),
                new Label("6"), new Label("7"), new Label("8"), new Label("9"), new Label("10"));
        player_rows.setLayoutX(player_grid[0][0].getLayoutX() - 25);
        player_rows.setLayoutY(player_grid[0][0].getLayoutY());
        player_rows.setPadding(new Insets( 6, 0, 0, 0));
        player_rows.setAlignment(Pos.CENTER);
        root.getChildren().addAll(player_columns, player_rows, enemy_grid_columns, enemy_rows);

        Label opponent = new Label("YOUR BOARD");
        opponent.setFont(Font.font("Times New Roman"));
        opponent.setFont(Font.font(15));
        opponent.setLayoutX(enemy_grid_columns.getLayoutX() + 100);
        opponent.setLayoutY(enemy_grid_columns.getLayoutY() - 30);
        Label player = new Label("ENEMY BOARD");
        player.setFont(Font.font("Times New Roman"));
        player.setFont(Font.font(15));
        player.setLayoutX(player_columns.getLayoutX() + 160);
        player.setLayoutY(player_columns.getLayoutY() - 30);
        root.getChildren().addAll(player, opponent);

        Image title = new Image("resources/title.png",200,
                50, false, true);
        ImageView title_text = new ImageView(title);
        title_text.setLayoutX(3*game_scene.getWidth()/5);
        title_text.setLayoutY(game_scene.getHeight()/12);
        root.getChildren().add(title_text);
        /*
        End of Scene Design
         */
    }

    private static void reportError(String s) {
        textArea.clear();
        textArea.setText(currentPrompt);
        textArea.appendText("\n" + s);
    }

    private static int convertLetterToNumber(char letter){
        int col = 10;
        switch (letter) {
            case 'A': col = 0; break;
            case 'B': col = 1; break;
            case 'C': col = 2; break;
            case 'D': col = 3; break;
            case 'E': col = 4; break;
            case 'F': col = 5; break;
            case 'G': col = 6; break;
            case 'H': col = 7; break;
            case 'I': col = 8; break;
            case 'J': col = 9; break;
        }
        return col;
    }

    private static String convertNumberToLetter(int i){
        String col = "";
        switch (i) {
            case 0: col = "A"; break;
            case 1: col = "B"; break;
            case 2: col = "C"; break;
            case 3: col = "D"; break;
            case 4: col = "E"; break;
            case 5: col = "F"; break;
            case 6: col = "G"; break;
            case 7: col = "H"; break;
            case 8: col = "I"; break;
            case 9: col = "J"; break;
        }
        return col;
    }
} //end of Game class
