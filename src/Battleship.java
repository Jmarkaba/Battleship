import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Battleship extends Application{

    protected static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Group root = new Group();
        Scene main_menu = new Scene(root,600, 400);

        ImageView menu_image = new ImageView(new Image("battleship_images/battleship_image.jpg",main_menu.getWidth(),
                main_menu.getHeight(), false, false));
        ImageView title_text = new ImageView(new Image("battleship_images/title.png",200,
                50, false, true));
        title_text.setLayoutX(main_menu.getWidth()/2 - main_menu.getWidth()/10);
        title_text.setLayoutY(main_menu.getHeight()/2 - main_menu.getHeight()/4);

        VBox button_list = new VBox();
        Button playGame = new Button("  Play Game  ");
        playGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new Game(primaryStage);
            }
        });
        Button howToPlay = new Button("How To Play");
        howToPlay.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new Game(primaryStage);
            }
        });
        button_list.getChildren().addAll(playGame, howToPlay);
        button_list.setAlignment(Pos.CENTER);
        button_list.setSpacing(15);
        button_list.setLayoutX(main_menu.getWidth()/2);
        button_list.setLayoutY(main_menu.getHeight()/2 - main_menu.getHeight()/10);
        root.getChildren().addAll(menu_image, title_text, button_list);

        primaryStage.setTitle("Battleship 1.0");
        primaryStage.setScene(main_menu);
        primaryStage.getIcons().add(new Image("battleship_images/icon.png"));
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch();
    }

} //end of Battleship class
