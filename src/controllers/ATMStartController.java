package controllers;

import atmClient.ATMClient;
import atmClient.result.Result;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

import static com.utils.Alerts.errorAlert;

public class ATMStartController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;

    public static ATMClient atmClient = Main.atmClient;

    public static final String ATM_START_SCENE = Main.ATM_START_SCENE;
    public final String SETUP_CONNECTION_SCENE = Main.SETUP_CONNECTION_SCENE;

    public Label welcomeMsgLbl;


    public void initialize() {
        welcomeMsgLbl.setText("Welcome to X.U.L.A ATM");
    }


    public void runLogin(ActionEvent actionEvent) throws IOException {

        Result connectResult = atmClient.connect();

        if(connectResult.getStatus() <= Result.ERROR_CODE){
            errorAlert(connectResult.getMessage(), APP_TITLE);
            return;
        }

        LoginController.handleSceneShow();

    }

    public void runOpenAccount(ActionEvent actionEvent) throws IOException {

        Result connectResult = atmClient.connect();

        if(connectResult.getStatus() <= Result.ERROR_CODE){
            errorAlert(connectResult.getMessage(), APP_TITLE);
            return;
        }

        NewUserController.handleSceneShow();

    }

    public void exit(ActionEvent actionEvent) {

        System.exit(0);

    }

    public void runSetupConnection(ActionEvent actionEvent) throws IOException {


        SetupConnectionController.handleSceneShow();

    }

    public static void handleSceneShow() throws IOException {

        //init ATMStartScene
        Parent root = FXMLLoader.load(ATMStartController.class.getClass().getResource(ATM_START_SCENE));
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));

        //Show ATMStartScene
        PRIMARY_STAGE.show();

    }

}