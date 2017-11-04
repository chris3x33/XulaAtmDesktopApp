package controllers;

import atmClient.ATMClient;
import atmClient.result.GetAccountBalanceResult;
import atmClient.result.Result;
import atmClient.result.SessionResult;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

import static atmClient.handler.SessionHandler.isValidSession;

public class DepositController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;
    
    public static ATMClient atmClient = Main.atmClient;
    
    public TextField depositAmountTxt;
    public Label accountIdLbl;
    public Label accountBalanceLbl;

    private long accountId = -1;
    private double accountBalance = -1;

    public void initialize() {

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

    private void setAccountIdLbl(long accountId) {
        accountIdLbl.setText("Account: "+accountId);
    }

    private void setAccountBalanceLbl(double balance) {
        accountBalanceLbl.setText("Balance: "+balance);
    }

    public void deposit(ActionEvent actionEvent) {

    }

    public void cancel(ActionEvent actionEvent) throws IOException {

        UserHomeController.handleSceneShow();

    }

    public void runAccountChange(ActionEvent actionEvent) throws IOException {
        DepositSelectorController.handleSceneShow();
    }
}