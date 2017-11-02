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

    public final String ATM_START_SCENE = Main.ATM_START_SCENE;

    public Label newUserMsgLbl;
    public TextField userNameTxt;
    public TextField passwordTxt;


    public void initialize() {

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

        if (createNewUserResult.getSessionStatus() == SessionResult.ERROR_CODE){

            errorAlert(createNewUserResult.getSessionMessage(), APP_TITLE);

            return;

        }

        if (createNewUserResult.getStatus() <= Result.ERROR_CODE){

            errorAlert(createNewUserResult.getMessage(), APP_TITLE);

            return;

        }

        informationAlert("New User Created!!", APP_TITLE);

        //init ATMStartScene
        Parent root = FXMLLoader.load(getClass().getResource(ATM_START_SCENE));
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));

        //Show ATMStartScene
        PRIMARY_STAGE.show();

    }

    public void back(ActionEvent actionEvent) throws IOException {

        //init ATMStartScene
        Parent root = FXMLLoader.load(getClass().getResource(ATM_START_SCENE));
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));

        //Show ATMStartScene
        PRIMARY_STAGE.show();

    }
}