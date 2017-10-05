package controllers;

import atmClient.ATMClient;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

public class SetupConnectionController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;
    public static ATMClient atmClient = Main.atmClient;
    
    public Label headerLbl;
    public TextField ipAddressTxt;
    public TextField portTxt;
    public TextField timeOutTxt;


    public void initialize() {

    }


    public void runSetConnection(ActionEvent actionEvent) {
    }

    public void runBack(ActionEvent actionEvent) {
    }
}