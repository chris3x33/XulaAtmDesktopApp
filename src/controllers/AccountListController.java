package controllers;

import atmClient.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public ListView<String> accountsListView;
    private ArrayList<Long> accountIds;
    private ArrayList<Double> accountBalances;


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

        accountBalances = getAccountBalances(accountIds);

        setAccountsListView(accountIds, accountBalances);

    }

    private void setAccountsListView(
            ArrayList<Long> accountIds, ArrayList<Double> accountBalances) {

        ArrayList<String> formattedAccounts = getFormattedAccounts(accountIds, accountBalances);

        ObservableList<String> formattedAccountLists = FXCollections.observableArrayList();

        formattedAccountLists.addAll(formattedAccounts);

        accountsListView.setItems(formattedAccountLists);

        accountsListView.getSelectionModel().select(0);

    }

    private ArrayList<String> getFormattedAccounts(ArrayList<Long> accountIds, ArrayList<Double> accountBalances) {

        ArrayList<String> formattedAccounts = new ArrayList<String>();

        for (int i = 0; i < accountIds.size(); i++) {

            String formattedAccount = String.format(
                    "Account: %d, Balance: %.2f",
                    accountIds.get(i),
                    accountBalances.get(i)
            );

            formattedAccounts.add(formattedAccount);

        }

        return formattedAccounts;

    }

    private ArrayList<Double> getAccountBalances(ArrayList<Long> accountIds) {

        ArrayList<Double> accountBalances = new ArrayList<Double>();

        for (long accountId: accountIds){
            GetAccountBalanceResult getAccountBalanceResult = atmClient.getAccountBalance(accountId);

            double accountBalance = getAccountBalanceResult.getAccountBalance();

            accountBalances.add(accountBalance);

        }

        return accountBalances;
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