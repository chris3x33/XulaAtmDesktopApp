package controllers;

import atmClient.ATMClient;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

public class LoginController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;

    public static ATMClient atmClient = Main.atmClient;

    public Label loginMsgLbl;
    public TextField userNameTxt;
    public TextField passwordTxt;


    public void initialize() {

    }


    public void login(ActionEvent actionEvent) {
    }

    public void back(ActionEvent actionEvent) {
    }
}