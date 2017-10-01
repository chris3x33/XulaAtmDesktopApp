package main;

import atmClient.ATMClient;
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
    public static ATMClient atmClient;
    public static final String ATM_START_SCENE = "/scenes/ATMStartScene.fxml";
    public static final String LOGIN_SCENE = "/scenes/LoginScene.fxml";
    public static final String NEW_USER_SCENE = "/scenes/NewUserScene.fxml";
    public static final String USER_HOME_SCENE = "/scenes/UserHomeScene.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource(ATM_START_SCENE));
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
