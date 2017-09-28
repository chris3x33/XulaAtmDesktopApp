package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String APP_TITLE = "Sample";
    public static final int WINDOWHEIGHT = 275;
    public static final int WINDOWWIDTH = 400;
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/scenes/ATMStartScene.fxml"));
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
