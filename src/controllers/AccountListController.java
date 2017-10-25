package controllers;

import atmClient.ATMClient;
import atmClient.GetAccountIdsResult;
import atmClient.Result;
import atmClient.SessionResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.util.ArrayList;

import static com.utils.Alerts.errorAlert;

public class AccountListController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;

    public static ATMClient atmClient = Main.atmClient;
    public final String USER_HOME_SCENE = Main.USER_HOME_SCENE;

    public Label headerLbl;
    public ListView accountsListView;
    private ArrayList<Long> accountIds;


    public void initialize() {

        headerLbl.setText("Accounts");

        GetAccountIdsResult getAccountIdsResult = atmClient.getAccountIds();

        //Check Session Status
        if (getAccountIdsResult.getSessionStatus() == SessionResult.INVALID_SESSION_CODE
                || getAccountIdsResult.getSessionStatus() == SessionResult.EXPIRED_SESSION_CODE){

            errorAlert(getAccountIdsResult.getSessionMessage(), APP_TITLE);

            return;

        }

        if (getAccountIdsResult.getSessionStatus() == SessionResult.ERROR_CODE){

            errorAlert(getAccountIdsResult.getSessionMessage(), APP_TITLE);

            return;

        }

        //Check Result Status
        if (getAccountIdsResult.getStatus() == Result.ERROR_CODE){

            errorAlert(getAccountIdsResult.getMessage(), APP_TITLE);

            return;

        }
        accountIds = getAccountIdsResult.getAccountIds();


    }


    public void runViewAccount(ActionEvent actionEvent) {
    }

    public void back(ActionEvent actionEvent) throws IOException {

        //init UserHomeScene
        Parent root = FXMLLoader.load(getClass().getResource(USER_HOME_SCENE));
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));

        //Show USER_HOME_SCENE
        PRIMARY_STAGE.show();

    }
}