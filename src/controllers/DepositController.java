package controllers;

import atmClient.ATMClient;
import atmClient.result.Result;
import atmClient.result.SessionResult;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

public class DepositController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;
    
    public static ATMClient atmClient = Main.atmClient;
    
    public TextField depositAmountTxt;
    public Label accountIdLbl;
    public Label accountBalanceLbl;

    public void initialize() {

    }

    public void deposit(ActionEvent actionEvent) {

    }

    public void cancel(ActionEvent actionEvent) throws IOException {

        UserHomeController.handleSceneShow();

    }
}