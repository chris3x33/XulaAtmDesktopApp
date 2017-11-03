package main;

import atmClient.ATMClient;
import controllers.ATMStartController;
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
    public static ATMClient atmClient = new ATMClient();

    public static final String ATM_START_SCENE = "/scenes/ATMStartScene.fxml";
    public static final String LOGIN_SCENE = "/scenes/LoginScene.fxml";
    public static final String NEW_USER_SCENE = "/scenes/NewUserScene.fxml";
    public static final String USER_HOME_SCENE = "/scenes/UserHomeScene.fxml";
    public static final String DEPOSIT_SCENE = "/scenes/DepositScene.fxml";
    public static final String SETUP_CONNECTION_SCENE = "/scenes/SetupConnectionScene.fxml";
    public static final String ACCOUNT_LIST_SCENE = "/scenes/AccountListScene.fxml";
    public static final String ACCOUNT_VIEW_SCENE = "/scenes/AccountViewScene.fxml";
    public static final String DEPOSIT_SELECTOR_SCENE = "/scenes/DepositSelectorScene.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Set title
        this.primaryStage = primaryStage;
        primaryStage.setTitle(APP_TITLE);

        //Show ATM_START_SCENE
        ATMStartController.handleSceneShow();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
