package controllers;

import atmClient.ATMClient;
import atmClient.result.CreateNewUserResult;
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
import static com.utils.Alerts.informationAlert;

public class NewUserController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;

    public static ATMClient atmClient = Main.atmClient;

    public static final String NEW_USER_SCENE = Main.NEW_USER_SCENE;

    public Label newUserMsgLbl;
    public TextField userNameTxt;
    public TextField passwordTxt;


    public void initialize() {

    }

    public void initData(){
        newUserMsgLbl.setText("Create new X.U.L.A User Account");
    }

    public void requestPasswordTxtFocus(ActionEvent actionEvent) {
        passwordTxt.requestFocus();
    }

    public void register(ActionEvent actionEvent) throws IOException {

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

        //Try to createNewUser
        CreateNewUserResult createNewUserResult = atmClient.createNewUser(userName, password);

        if (createNewUserResult.getSessionStatus() == SessionResult.INVALID_SESSION_CODE
                || createNewUserResult.getSessionStatus() == SessionResult.EXPIRED_SESSION_CODE){


            errorAlert(createNewUserResult.getSessionMessage(), APP_TITLE);

            return;

        }

        if (createNewUserResult.getSessionStatus() <= SessionResult.ERROR_CODE){

            errorAlert(createNewUserResult.getSessionMessage(), APP_TITLE);

            return;

        }

        if (createNewUserResult.getStatus() <= Result.ERROR_CODE){

            errorAlert(createNewUserResult.getMessage(), APP_TITLE);

            return;

        }

        informationAlert("New User Created!!", APP_TITLE);

        ATMStartController.handleSceneShow();

    }

    public void back(ActionEvent actionEvent) throws IOException {

        ATMStartController.handleSceneShow();

    }

    public static void handleSceneShow() throws IOException {

        //init NEW_USER_SCENE
        FXMLLoader fxmlLoader = new FXMLLoader(
                NewUserController.class.getClass().getResource(NEW_USER_SCENE)
        );
        Parent root = fxmlLoader.load();

        //Get AccountListController
        NewUserController newUserController =
                (NewUserController) fxmlLoader.getController();

        //init Controller Data
        newUserController.initData();

        //Show NEW_USER_SCENE
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));
        PRIMARY_STAGE.show();
    }

}