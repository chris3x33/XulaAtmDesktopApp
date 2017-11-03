package controllers;

import atmClient.ATMClient;
import atmClient.result.Result;
import atmClient.result.SessionResult;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

import static atmClient.handler.SessionHandler.isValidSession;
import static com.utils.Alerts.errorAlert;

public class DepositSelectorController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;
    public static ATMClient atmClient = Main.atmClient;

    public static String DEPOSIT_SELECTOR_SCENE = Main.DEPOSIT_SELECTOR_SCENE;

    public void initialize() {

    }

    public SessionResult initData() throws IOException {

        return new SessionResult(SessionResult.SUCCESS_CODE, Result.ERROR_CODE);

    }

    public static void handleSceneShow() throws IOException{

        //init DEPOSIT_SELECTOR_SCENE
        FXMLLoader fxmlLoader = new FXMLLoader(
                AccountListController.class.getResource(DEPOSIT_SELECTOR_SCENE)
        );
        Parent root = fxmlLoader.load();

        //Get AccountListController
        DepositSelectorController accountListController =
                (DepositSelectorController) fxmlLoader.getController();

        //Setup accountListController Data
        SessionResult sessionResult = accountListController.initData();

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

        //Show DEPOSIT_SELECTOR_SCENE
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));
        PRIMARY_STAGE.show();

    }

}