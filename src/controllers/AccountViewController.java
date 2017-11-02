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

import static com.utils.Alerts.errorAlert;


public class AccountViewController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;

    public static final Stage PRIMARY_STAGE = Main.primaryStage;

    public static ATMClient atmClient = Main.atmClient;

    public final String ACCOUNT_LIST_SCENE = Main.ACCOUNT_LIST_SCENE;
    public final String ATM_START_SCENE = Main.ATM_START_SCENE;

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


    private void setAccountBalance() throws IOException {
        //Get Account Balance
        GetAccountBalanceResult accountBalanceResult = atmClient.getAccountBalance(accountId);

        if(!isValidSession(accountBalanceResult)){

            errorAlert(
                    accountBalanceResult.getSessionMessage(),
                    APP_TITLE
            );

            atmClient.clearSession();

            goToATMStartScene();

            return;

        }

        int status = accountBalanceResult.getStatus();
        if (status <= Result.ERROR_CODE){

            errorAlert(
                    accountBalanceResult.getMessage(),
                    APP_TITLE
            );

            goToAccountList();

            return;
        }

        accountBalance = accountBalanceResult.getAccountBalance();

    }

    private boolean isValidSession(SessionResult sessionResult){

        int sessionStatus = sessionResult.getSessionStatus();
        return !((sessionStatus == SessionResult.EXPIRED_SESSION_CODE) ||
                (sessionStatus == SessionResult.INVALID_SESSION_CODE));
    }

    private void goToATMStartScene() throws IOException {

        //init ATMStartScene
        Parent root = FXMLLoader.load(getClass().getResource(ATM_START_SCENE));
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));

        //Show ATMStartScene
        PRIMARY_STAGE.show();

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
}