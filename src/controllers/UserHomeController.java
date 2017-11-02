package controllers;

import atmClient.*;
import atmClient.result.GetUserNameResult;
import atmClient.result.LogOutResult;
import atmClient.result.Result;
import atmClient.result.SessionResult;
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
    
    public final String DEPOSIT_SCENE = Main.DEPOSIT_SCENE;
    public final String ACCOUNT_LIST_SCENE = Main.ACCOUNT_LIST_SCENE;

    public Label userHomeHeaderLbl;

    public void initialize() throws IOException {

        //Get UserName
        GetUserNameResult getUserNameResult = atmClient.getUserName();

        //Check Session Status
        if (getUserNameResult.getSessionStatus() == SessionResult.INVALID_SESSION_CODE
                || getUserNameResult.getSessionStatus() == SessionResult.EXPIRED_SESSION_CODE){

            errorAlert(getUserNameResult.getSessionMessage(), APP_TITLE);

            //Go back to ATMStartScene
            ATMStartController.handleSceneShow();

            return;

        }

        if (getUserNameResult.getSessionStatus() <= SessionResult.ERROR_CODE){

            errorAlert(getUserNameResult.getSessionMessage(), APP_TITLE);

            setDefaultHeaderLbl();

            return;

        }

        //Check Result Status
        if (getUserNameResult.getStatus() <= Result.ERROR_CODE){

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

    public void runViewAccounts(ActionEvent actionEvent) throws IOException {

        //init ACCOUNT_LIST_SCENE
        Parent root = FXMLLoader.load(getClass().getResource(ACCOUNT_LIST_SCENE));
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));

        //Show ACCOUNT_LIST_SCENE
        PRIMARY_STAGE.show();

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

        if (logoutResult.getSessionStatus() <= SessionResult.ERROR_CODE){

            errorAlert(logoutResult.getSessionMessage(), APP_TITLE);

            return;

        }

        //Show ATMStartScene
        ATMStartController.handleSceneShow();

    }

}