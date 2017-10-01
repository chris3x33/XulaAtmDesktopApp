package controllers;

import atmClient.ATMClient;
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

    public final String ATM_START_SCENE = Main.ATM_START_SCENE;
    public final String USER_HOME_SCENE = Main.USER_HOME_SCENE;

    public Label loginMsgLbl;
    public TextField userNameTxt;
    public TextField passwordTxt;


    public void initialize() {

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

        //Go to UserHomeScene if login success
        Parent root = FXMLLoader.load(getClass().getResource(USER_HOME_SCENE));
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));

        //Show USER_HOME_SCENE
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