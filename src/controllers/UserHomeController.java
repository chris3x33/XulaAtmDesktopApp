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

import static atmClient.handler.SessionHandler.isValidSession;
import static com.utils.Alerts.errorAlert;

public class UserHomeController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;

    public static ATMClient atmClient = Main.atmClient;

    public final String ACCOUNT_LIST_SCENE = Main.ACCOUNT_LIST_SCENE;
    public static final String USER_HOME_SCENE = Main.USER_HOME_SCENE;

    public Label userHomeHeaderLbl;

    public void initialize() throws IOException {

    }

    public SessionResult initData() throws IOException {

        //Get UserName
        GetUserNameResult getUserNameResult = atmClient.getUserName();

        //Check Session Status
        if (!isValidSession(getUserNameResult)){

            return getUserNameResult;

        }

        if (getUserNameResult.getSessionStatus() <= SessionResult.ERROR_CODE){

            return getUserNameResult;

        }

        //Check Result Status
        if (getUserNameResult.getStatus() <= Result.ERROR_CODE){

            return getUserNameResult;

        }

        //Get UserName
        String userName = getUserNameResult.getUserName();
        setHeaderLbl("Welcome, "+userName);

        return getUserNameResult;

    }

    public static void handleSceneShow() throws IOException {

        //init USER_HOME_SCENE
        FXMLLoader fxmlLoader = new FXMLLoader(
                UserHomeController.class.getResource(USER_HOME_SCENE)
        );
        Parent root = fxmlLoader.load();

        //Get UserHomeController
        UserHomeController userHomeController =
                (UserHomeController) fxmlLoader.getController();

        //Setup userHomeController Data
        SessionResult sessionResult = userHomeController.initData();

        int sessionStatus = sessionResult.getSessionStatus();
        if (!isValidSession(sessionResult)){

            errorAlert(sessionResult.getSessionMessage(),APP_TITLE);

            ATMStartController.handleSceneShow();

            return;
        }

        if (sessionStatus <= SessionResult.ERROR_CODE){

            errorAlert(sessionResult.getSessionMessage(),APP_TITLE);

            ATMStartController.handleSceneShow();

            return;
        }

        //Check Result Status
        if (sessionResult.getStatus() <= Result.ERROR_CODE){

            errorAlert(sessionResult.getMessage(), APP_TITLE);

            return;

        }

        //Show ACCOUNT_VIEW_SCENE
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));
        PRIMARY_STAGE.show();
    }

    private void setHeaderLbl(String msg) {
        userHomeHeaderLbl.setText(msg);
    }

    private void setDefaultHeaderLbl(){
        userHomeHeaderLbl.setText("Welcome");
    }

    public void runViewAccounts(ActionEvent actionEvent) throws IOException {

        AccountListController.handleSceneShow();
    }

    public void runTransfer(ActionEvent actionEvent) {
    }

    public void runDeposit(ActionEvent actionEvent) throws IOException {

        DepositSelectorController.handleSceneShow();

    }

    public void runWithdraw(ActionEvent actionEvent) throws IOException {
        WithdrawSelectorController.handleSceneShow();
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