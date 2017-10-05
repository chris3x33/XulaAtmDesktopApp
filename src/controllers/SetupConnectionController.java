package controllers;

import atmClient.ATMClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

import static com.utils.Alerts.errorAlert;

public class SetupConnectionController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;

    public static ATMClient atmClient = Main.atmClient;

    public final String ATM_START_SCENE = Main.ATM_START_SCENE;

    public Label headerLbl;
    public TextField ipAddressTxt;
    public TextField portTxt;
    public TextField timeOutTxt;


    public void initialize() {

    }


    public void runSetConnection(ActionEvent actionEvent) {

        //get ipAddress, portStr, and timeOutStr
        String ipAddress = ipAddressTxt.getText();
        String portStr = portTxt.getText();
        String timeOutStr = timeOutTxt.getText();

        //check ipAddress
        if (ipAddress.isEmpty()){

            //Show Error
            String errMsg = "Enter IP Address";

            errorAlert(errMsg, APP_TITLE);

            return;

        }



    }

    public void runBack(ActionEvent actionEvent) throws IOException {

        //init ATMStartScene
        Parent root = FXMLLoader.load(getClass().getResource(ATM_START_SCENE));
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));

        //Show ATMStartScene
        PRIMARY_STAGE.show();

    }
}