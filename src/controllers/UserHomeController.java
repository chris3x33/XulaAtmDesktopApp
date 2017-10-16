package controllers;

import atmClient.ATMClient;
import atmClient.LogOutResult;
import atmClient.SessionResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

import static com.utils.Alerts.errorAlert;

public class UserHomeController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;

    public static ATMClient atmClient = Main.atmClient;

    public final String ATM_START_SCENE = Main.ATM_START_SCENE;
    public final String DEPOSIT_SCENE = Main.DEPOSIT_SCENE;

    public Label userHomeMsgLbl;


    public void initialize() {

    }


    public void runViewAccounts(ActionEvent actionEvent) {
    }

    public void runTransfer(ActionEvent actionEvent) {
    }

    public void runDeposit(ActionEvent actionEvent) throws IOException {

        //init DEPOSIT_SCENE
        Parent root = FXMLLoader.load(getClass().getResource(DEPOSIT_SCENE));
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));

        //Show DEPOSIT_SCENE
        PRIMARY_STAGE.show();

    }

    public void runWithdraw(ActionEvent actionEvent) {
    }

    public void runLogOut(ActionEvent actionEvent) throws IOException {

        //LogOut
        LogOutResult logoutResult = atmClient.logout();

        //Check Session Status
        if (logoutResult.getSessionStatus() == SessionResult.INVALID_SESSION_CODE
                || logoutResult.getSessionStatus() == SessionResult.EXPIRED_SESSION_CODE){


            errorAlert(logoutResult.getSessionMessage(), APP_TITLE);

            return;

        }

        if (logoutResult.getSessionStatus() == SessionResult.ERROR_CODE){

            errorAlert(logoutResult.getSessionMessage(), APP_TITLE);

            return;

        }

        //init ATMStartScene
        Parent root = FXMLLoader.load(getClass().getResource(ATM_START_SCENE));
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));

        //Show ATMStartScene
        PRIMARY_STAGE.show();

    }
}