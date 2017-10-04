package controllers;

import atmClient.ATMClient;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.Main;

public class UserHomeController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;

    public static ATMClient atmClient = Main.atmClient;

    public final String DEPOSIT_SCENE = Main.DEPOSIT_SCENE;

    public Label userHomeMsgLbl;


    public void initialize() {

    }


    public void runViewAccounts(ActionEvent actionEvent) {
    }

    public void runTransfer(ActionEvent actionEvent) {
    }

    public void runDeposit(ActionEvent actionEvent) {
    }

    public void runWithdraw(ActionEvent actionEvent) {
    }
}