package controllers;

import atmClient.*;
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

    public Label userHomeHeaderLbl;

    public void initialize() throws IOException {

        //Get UserName
        GetUserNameResult getUserNameResult = atmClient.getUserName();

        //Check Session Status
        if (getUserNameResult.getSessionStatus() == SessionResult.INVALID_SESSION_CODE
                || getUserNameResult.getSessionStatus() == SessionResult.EXPIRED_SESSION_CODE){

            errorAlert(getUserNameResult.getSessionMessage(), APP_TITLE);

            //Go back to ATMStartScene
            goToATMStartScene();

            return;

        }

        if (getUserNameResult.getSessionStatus() == SessionResult.ERROR_CODE){

            errorAlert(getUserNameResult.getSessionMessage(), APP_TITLE);

            setDefaultHeaderLbl();

            return;

        }

        //Check Result Status
        if (getUserNameResult.getStatus() == Result.ERROR_CODE){

            errorAlert(getUserNameResult.getMessage(), APP_TITLE);

            setDefaultHeaderLbl();

            return;

        }

        //Get UserName
        String userName = getUserNameResult.getUserName();
        setHeaderLbl("Welcome, "+userName);

    }


    private void setHeaderLbl(String msg) {
        userHomeHeaderLbl.setText(msg);
    }

    private void setDefaultHeaderLbl(){
        userHomeHeaderLbl.setText("Welcome");
    }

    private void goToATMStartScene() throws IOException {

        //init ATMStartScene
        Parent root = FXMLLoader.load(getClass().getResource(ATM_START_SCENE));
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));

        //Show ATMStartScene
        PRIMARY_STAGE.show();

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