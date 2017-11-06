package controllers;

import atmClient.ATMClient;
import atmClient.result.DepositResult;
import atmClient.result.GetAccountBalanceResult;
import atmClient.result.Result;
import atmClient.result.SessionResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;
import sun.misc.FloatingDecimal;

import java.io.IOException;

import static atmClient.handler.SessionHandler.isValidSession;
import static com.utils.Alerts.errorAlert;
import static com.utils.Alerts.informationAlert;

public class DepositController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;
    
    public static ATMClient atmClient = Main.atmClient;

    public static final String DEPOSIT_SCENE = Main.DEPOSIT_SCENE;
    
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

    public void deposit(ActionEvent actionEvent) throws IOException {

        String amountStr = depositAmountTxt.getText();

        //Check amountStr
        if (!isDouble(amountStr)){

            errorAlert("Enter an amount to deposit", APP_TITLE);

            depositAmountTxt.clear();

            return;
        }

        //Get Amount
        double amount = Double.parseDouble(amountStr);

        //Run deposit
        DepositResult depositResult = atmClient.deposit(accountId, amount);

        int sessionStatus = depositResult.getSessionStatus();
        if (!isValidSession(depositResult) ||
                sessionStatus <= SessionResult.ERROR_CODE){

            errorAlert(depositResult.getSessionMessage(),APP_TITLE);

            ATMStartController.handleSceneShow();

            return;
        }

        int status = depositResult.getStatus();
        if (status <= Result.ERROR_CODE){

            errorAlert( depositResult.getMessage(),APP_TITLE);

            return;
        }

        informationAlert(depositResult.getDepositMsg(), APP_TITLE);

        UserHomeController.handleSceneShow();

    }

    public void cancel(ActionEvent actionEvent) throws IOException {

        UserHomeController.handleSceneShow();

    }

    public void runAccountChange(ActionEvent actionEvent) throws IOException {
        DepositSelectorController.handleSceneShow();
    }

    public static void handleSceneShow(long accountId) throws IOException {

        //init DEPOSIT_SCENE
        FXMLLoader fxmlLoader = new FXMLLoader(
                AccountViewController.class.getResource(DEPOSIT_SCENE)
        );
        Parent root = fxmlLoader.load();

        //Get DepositController
        DepositController depositController =
                (DepositController) fxmlLoader.getController();

        //Setup depositController Data
        SessionResult sessionResult = depositController.initData(accountId);

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

        //Show DEPOSIT_SCENE
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