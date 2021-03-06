package controllers;

import atmClient.ATMClient;
import atmClient.result.LoginResult;
import atmClient.result.Result;
import atmClient.result.SessionResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

import static com.utils.Alerts.errorAlert;

public class LoginController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;

    public static ATMClient atmClient = Main.atmClient;

    public static final String LOGIN_SCENE = Main.LOGIN_SCENE;

    public Label loginMsgLbl;
    public TextField userNameTxt;
    public TextField passwordTxt;


    public void initialize() {
    }

    public void initData(){
        loginMsgLbl.setText("X.U.L.A ATM Login");
    }

    public static void handleSceneShow() throws IOException {

        //init LOGIN_SCENE
        FXMLLoader fxmlLoader = new FXMLLoader(
                LoginController.class.getClass().getResource(LOGIN_SCENE)
        );
        Parent root = fxmlLoader.load();

        //Get LoginController
        LoginController loginController =
                (LoginController) fxmlLoader.getController();

        //init Controller Data
        loginController.initData();

        //Show LOGIN_SCENE
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));
        PRIMARY_STAGE.show();
    }

    public void login(ActionEvent actionEvent) throws IOException {

        //get password and userName
        String userName = userNameTxt.getText();
        String password = passwordTxt.getText();

        //check userName
        if (userName.isEmpty()){

            //Show Error
            String errMsg = "Enter Your UserName";

            errorAlert(errMsg, APP_TITLE);

            return;

        }

        //check password
        if (password.isEmpty()){

            //Show Error
            String errMsg = "Enter Your Password";

            errorAlert(errMsg, APP_TITLE);

            return;

        }

        //Try to login
        LoginResult loginResult = atmClient.login(userName, password);

        //Check Session Status
        if (loginResult.getSessionStatus() == SessionResult.INVALID_SESSION_CODE
                || loginResult.getSessionStatus() == SessionResult.EXPIRED_SESSION_CODE){


            errorAlert(loginResult.getSessionMessage(), APP_TITLE);

            ATMStartController.handleSceneShow();

            return;

        }

        if (loginResult.getSessionStatus() <= SessionResult.ERROR_CODE){

            errorAlert(loginResult.getSessionMessage(), APP_TITLE);

            ATMStartController.handleSceneShow();

            return;

        }

        //Check Result Status
        if (loginResult.getStatus() <= Result.ERROR_CODE){

            errorAlert(loginResult.getMessage(), APP_TITLE);

            return;

        }

        //Go to UserHomeScene if login success
        UserHomeController.handleSceneShow();

    }

    public void back(ActionEvent actionEvent) throws IOException {

        ATMStartController.handleSceneShow();

    }

    public void requestPasswordTxtFocus(ActionEvent actionEvent) {
        passwordTxt.requestFocus();
    }
}