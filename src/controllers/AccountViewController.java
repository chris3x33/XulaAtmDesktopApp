package controllers;

import atmClient.ATMClient;
import atmClient.XulaATMTransaction;
import atmClient.result.*;
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

import static atmClient.handler.SessionHandler.isValidSession;
import static com.utils.Alerts.errorAlert;


public class AccountViewController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;

    public static final Stage PRIMARY_STAGE = Main.primaryStage;

    public static ATMClient atmClient = Main.atmClient;

    public final String ACCOUNT_LIST_SCENE = Main.ACCOUNT_LIST_SCENE;
    public final String ATM_START_SCENE = Main.ATM_START_SCENE;
    public static final String ACCOUNT_VIEW_SCENE = Main.ACCOUNT_VIEW_SCENE;

    public Label accountIdLbl;
    public Label accountTypeLbl;
    public Label accountBalanceLbl;
    public ListView transactionsListView;

    private long accountId = -1;
    private double accountBalance = -1;
    private int accountType;
    private ArrayList<XulaATMTransaction> atmTransactions;

    public void initialize() throws IOException {

    }

    public SessionResult initData(long accountId){

        this.accountId = accountId;

        //Get Account Balance
        GetAccountBalanceResult accountBalanceResult = atmClient.getAccountBalance(accountId);

        if (!isValidSession(accountBalanceResult) ||
                accountBalanceResult.getSessionStatus() == SessionResult.ERROR_CODE ||
                accountBalanceResult.getStatus() == Result.ERROR_CODE){
            return accountBalanceResult;
        }
        accountBalance = accountBalanceResult.getAccountBalance();

        //Get TransactionIds
        GetTransactionIdsResult getTransactionIdsResult = atmClient.getTransactionIds(
                accountId
        );
        if (!isValidSession(getTransactionIdsResult) ||
                getTransactionIdsResult.getSessionStatus() == SessionResult.ERROR_CODE ||
        getTransactionIdsResult.getStatus() == Result.ERROR_CODE){
            return getTransactionIdsResult;
        }
        ArrayList<Long> transactionIds = getTransactionIdsResult.getTransactionIds();

        //Set Transactions
        setAtmTransactions(accountId, transactionIds);

        //set Labels
        setAccountIdLbl(accountId);
        setAccountBalanceLbl(accountBalance);
        setTransactionsListView(atmTransactions);

        return accountBalanceResult;
    }

    private SessionResult setAtmTransactions(long accountId, ArrayList<Long> transactionIds) {

         atmTransactions = new ArrayList<XulaATMTransaction>();

        for (long transactionId : transactionIds){

            GetTransactionResult getTransactionResult =
                    atmClient.getTransaction(accountId, transactionId);

            if (!isValidSession(getTransactionResult) ||
                    getTransactionResult.getSessionStatus() == SessionResult.ERROR_CODE ||
                    getTransactionResult.getStatus() == Result.ERROR_CODE){
                return getTransactionResult;
            }

            atmTransactions.add(getTransactionResult.getAtmTransaction());
        }

        return new SessionResult(SessionResult.SUCCESS_CODE,Result.SUCCESS_CODE);

    }

    private void setTransactionsListView(ArrayList<XulaATMTransaction> atmTransactions){

        ObservableList<String> formattedAccountLists = FXCollections.observableArrayList();

        for (XulaATMTransaction atmTransaction: atmTransactions){
            formattedAccountLists.add(
                    atmTransaction.getType()+ " " +
                    String.format("$%.2f",atmTransaction.getAmount())+ " " +
                    atmTransaction.getDateTime()+ " " +
                    String.format("$%.2f",atmTransaction.getPrevAmount())
            );
        }

        transactionsListView.setItems(formattedAccountLists);

        transactionsListView.getSelectionModel().select(0);

    }

    private void setAccountIdLbl(long accountId) {
        accountIdLbl.setText("Account: "+accountId);
    }

    private void setAccountBalanceLbl(double balance) {
        accountBalanceLbl.setText("Balance: "+balance);
    }

    public void runStatementViewer(ActionEvent actionEvent) {
    }

    public void back(ActionEvent actionEvent) throws IOException {

        AccountListController.handleSceneShow();

    }

    public static void handleSceneShow(long accountId) throws IOException {

        //init ACCOUNT_VIEW_SCENE
        FXMLLoader fxmlLoader = new FXMLLoader(AccountViewController.class.getResource(ACCOUNT_VIEW_SCENE));
        Parent root = fxmlLoader.load();


        //Get AccountViewController
        AccountViewController accountViewController =
                (AccountViewController) fxmlLoader.getController();

        //Setup accountViewController Data
        SessionResult sessionResult = accountViewController.initData(accountId);


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

        //Show ACCOUNT_VIEW_SCENE
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));
        PRIMARY_STAGE.show();

    }


}