package controllers;

import atmClient.ATMClient;
import atmClient.result.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

import static atmClient.handler.SessionHandler.isValidSession;
import static com.utils.Alerts.errorAlert;
import static com.utils.Alerts.informationAlert;

public class WithdrawController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;
    
    public static ATMClient atmClient = Main.atmClient;

    public static final String WITHDRAW_SCENE = Main.WITHDRAW_SCENE;
    
    public TextField withdrawAmountTxt;
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

    public void withdraw(ActionEvent actionEvent) throws IOException {

        String amountStr = withdrawAmountTxt.getText();

        //Check amountStr
        if (!isDouble(amountStr)){

            errorAlert("Enter an amount to deposit", APP_TITLE);

            withdrawAmountTxt.clear();

            return;
        }

        //Get Amount
        double amount = Double.parseDouble(amountStr);

        //Run Withdraw
        WithdrawResult withdrawResult = atmClient.withdraw(accountId, amount);

        int sessionStatus = withdrawResult.getSessionStatus();
        if (!isValidSession(withdrawResult) ||
                sessionStatus <= SessionResult.ERROR_CODE){

            errorAlert(withdrawResult.getSessionMessage(),APP_TITLE);

            ATMStartController.handleSceneShow();

            return;
        }

        int status = withdrawResult.getStatus();
        if (status <= Result.ERROR_CODE){

            errorAlert( withdrawResult.getMessage(),APP_TITLE);

            return;
        }

        informationAlert(withdrawResult.getWithdrawMsg(), APP_TITLE);

        UserHomeController.handleSceneShow();

    }

    public void cancel(ActionEvent actionEvent) throws IOException {

        UserHomeController.handleSceneShow();

    }

    public void runAccountChange(ActionEvent actionEvent) throws IOException {
        WithdrawSelectorController.handleSceneShow();
    }

    public static void handleSceneShow(long accountId) throws IOException {

        //init WITHDRAW_SCENE
        FXMLLoader fxmlLoader = new FXMLLoader(
                WithdrawController.class.getResource(WITHDRAW_SCENE)
        );
        Parent root = fxmlLoader.load();

        //Get WithdrawController
        WithdrawController withdrawController =
                (WithdrawController) fxmlLoader.getController();

        //Setup withdrawController Data
        SessionResult sessionResult = withdrawController.initData(accountId);

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

            UserHomeController.handleSceneShow();

            return;
        }

        //Show WITHDRAW_SCENE
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));
        PRIMARY_STAGE.show();

    }

    private boolean isDouble(String str){

        try {
            Double.parseDouble(str);
            return true;

        }catch (NumberFormatException e){
            return false;
        }

    }

}