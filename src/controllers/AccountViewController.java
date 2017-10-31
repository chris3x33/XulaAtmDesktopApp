package controllers;

import atmClient.ATMClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

import static com.utils.Alerts.errorAlert;


public class AccountViewController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;

    public static final Stage PRIMARY_STAGE = Main.primaryStage;

    public static ATMClient atmClient = Main.atmClient;

    public final String ACCOUNT_LIST_SCENE = Main.ACCOUNT_LIST_SCENE;

    public Label accountIdLbl;
    public Label accountTypeLbl;
    public Label accountBalanceLbl;
    public ListView transactionsListView;

    public void initialize() throws IOException {


        long accountId = atmClient.getSelectedAccountId();

        //Check if Invalid AccountId
        if (accountId <0){
            errorAlert("Invalid accountId cannot view account!!", APP_TITLE);
            goToAccountList();
        }

    }

    private void goToAccountList() throws IOException {

        //init ACCOUNT_LIST_SCENE
        Parent root = FXMLLoader.load(getClass().getResource(ACCOUNT_LIST_SCENE));
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));

        //Show ACCOUNT_LIST_SCENE
        PRIMARY_STAGE.show();
    }

    private void setAccountIdLbl(long accountId) {
        accountIdLbl.setText("Account: "+accountId);
    }


    public void runStatementViewer(ActionEvent actionEvent) {
    }

    public void back(ActionEvent actionEvent) throws IOException {

        goToAccountList();

    }
}