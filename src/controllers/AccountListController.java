package controllers;

import atmClient.ATMClient;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import main.Main;

public class AccountListController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;

    public static ATMClient atmClient = Main.atmClient;
    public final String USER_HOME_SCENE = Main.USER_HOME_SCENE;

    public Label headerLbl;
    public ListView accountsListView;


    public void initialize() {

        headerLbl.setText("Accounts");

    }


    public void runViewAccount(ActionEvent actionEvent) {
    }

    public void back(ActionEvent actionEvent) {
    }
}