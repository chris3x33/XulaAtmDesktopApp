package controllers;

import atmClient.ATMClient;
import atmClient.result.GetAccountBalanceResult;
import atmClient.result.Result;
import atmClient.result.SessionResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

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

    public void initialize() throws IOException {

    }

    public SessionResult initData(long accountId){

        this.accountId = accountId;

        //Get Account Balance
        GetAccountBalanceResult accountBalanceResult = atmClient.getAccountBalance(accountId);

        if (!isValidSession(accountBalanceResult) ||
                accountBalanceResult.getStatus() == Result.ERROR_CODE){
            return accountBalanceResult;
        }
        accountBalance = accountBalanceResult.getAccountBalance();

        //set Labels
        setAccountIdLbl(accountId);
        setAccountBalanceLbl(accountBalance);

        return accountBalanceResult;
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

    private void setAccountBalanceLbl(double balance) {
        accountBalanceLbl.setText("Balance: "+balance);
    }

    public void runStatementViewer(ActionEvent actionEvent) {
    }

    public void back(ActionEvent actionEvent) throws IOException {

        goToAccountList();

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