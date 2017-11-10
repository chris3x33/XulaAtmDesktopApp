package controllers;

import atmClient.ATMClient;
import atmClient.result.GetAccountBalanceResult;
import atmClient.result.GetAccountIdsResult;
import atmClient.result.Result;
import atmClient.result.SessionResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.util.ArrayList;

import static atmClient.handler.SessionHandler.isValidSession;
import static com.utils.Alerts.errorAlert;

public class WithdrawSelectorController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;
    public static ATMClient atmClient = Main.atmClient;

    public static String WITHDRAW_SELECTOR_SCENE = Main.WITHDRAW_SELECTOR_SCENE;
    
    public ListView accountsListView;
    private ArrayList<Long> accountIds;
    private ArrayList<Double> accountBalances;

    public void initialize() {

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

    public SessionResult initData() throws IOException {

        //Get Account Ids
        GetAccountIdsResult getAccountIdsResult = atmClient.getAccountIds();
        if (getAccountIdsResult.getSessionStatus() <= SessionResult.ERROR_CODE
                ||getAccountIdsResult.getStatus() <= Result.ERROR_CODE){
            return getAccountIdsResult;
        }
        accountIds = getAccountIdsResult.getAccountIds();

        //set Account Balances
        SessionResult sessionResult = setAccountBalances(accountIds);
        if (sessionResult.getSessionStatus() <= SessionResult.ERROR_CODE
                ||sessionResult.getStatus() <= Result.ERROR_CODE){
            return sessionResult;
        }

        //Setup scene data
        setAccountsListView(accountIds, accountBalances);

        return new SessionResult(SessionResult.SUCCESS_CODE, Result.SUCCESS_CODE);

    }

    private SessionResult setAccountBalances(ArrayList<Long> accountIds) {

        accountBalances = new ArrayList<Double>();

        for (long accountId: accountIds){

            GetAccountBalanceResult getAccountBalanceResult =
                    atmClient.getAccountBalance(accountId);

            if (getAccountBalanceResult.getSessionStatus() <= SessionResult.ERROR_CODE
                    ||getAccountBalanceResult.getStatus() <= Result.ERROR_CODE){
                return getAccountBalanceResult;
            }

            double accountBalance = getAccountBalanceResult.getAccountBalance();

            accountBalances.add(accountBalance);

        }

        return new SessionResult(
                SessionResult.SUCCESS_CODE,
                Result.SUCCESS_CODE
        );

    }

    public static void handleSceneShow() throws IOException{

        //init WITHDRAW_SELECTOR_SCENE
        FXMLLoader fxmlLoader = new FXMLLoader(
                AccountListController.class.getResource(WITHDRAW_SELECTOR_SCENE)
        );
        Parent root = fxmlLoader.load();

        //Get WithdrawSelectorController
        WithdrawSelectorController withdrawSelectorController =
                (WithdrawSelectorController) fxmlLoader.getController();

        //Setup withdrawSelectorController Data
        SessionResult sessionResult = withdrawSelectorController.initData();

        int sessionStatus = sessionResult.getSessionStatus();
        if (!isValidSession(sessionResult)){

            errorAlert(sessionResult.getSessionMessage(),APP_TITLE);

            ATMStartController.handleSceneShow();

            return;
        }

        if (sessionStatus <= SessionResult.ERROR_CODE){

            errorAlert(sessionResult.getSessionMessage(),APP_TITLE);

            ATMStartController.handleSceneShow();

            return;
        }

        int status = sessionResult.getStatus();
        if (status <= Result.ERROR_CODE){

            errorAlert( sessionResult.getMessage(),APP_TITLE);

            return;
        }

        //Show WITHDRAW_SELECTOR_SCENE
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));
        PRIMARY_STAGE.show();

    }

    public void runWithdraw(ActionEvent actionEvent) throws IOException {

        int selectedAccountIndex = accountsListView.getSelectionModel().getSelectedIndex();

        long accountId = accountIds.get(selectedAccountIndex);

        WithdrawController.handleSceneShow(accountId);
    }

    public void back(ActionEvent actionEvent) throws IOException {

        UserHomeController.handleSceneShow();

    }
}