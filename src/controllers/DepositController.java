package controllers;

import atmClient.ATMClient;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

public class DepositController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;
    public static ATMClient atmClient = Main.atmClient;
    public TextField balanceTxt;
    public TextField depositAmountTxt;


    public void initialize() {

    }


}