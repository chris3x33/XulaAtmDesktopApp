package controllers;

import atmClient.ATMClient;
import atmClient.result.Result;
import atmClient.result.SessionResult;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

public class DepositSelectorController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;
    public static ATMClient atmClient = Main.atmClient;

    public void initialize() {

    }

    public SessionResult initData() throws IOException {

        return new SessionResult(SessionResult.SUCCESS_CODE, Result.ERROR_CODE);

    }



}