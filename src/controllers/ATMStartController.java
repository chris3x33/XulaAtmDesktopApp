package controllers;

import atmClient.ATMClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

public class ATMStartController {

    public static final int WINDOWHEIGHT = Main.WINDOWHEIGHT;
    public static final int WINDOWWIDTH = Main.WINDOWWIDTH;
    public static final String APP_TITLE = Main.APP_TITLE;
    public static final Stage PRIMARY_STAGE = Main.primaryStage;

    public static ATMClient atmClient = Main.atmClient;

    public final String LOGIN_SCENE = Main.LOGIN_SCENE;
    public final String NEW_USER_SCENE = Main.NEW_USER_SCENE;

    public Label welcomeMsgLbl;


    public void initialize() {

    }


    public void runLogin(ActionEvent actionEvent) throws IOException {

        //init LoginScene
        Parent root = FXMLLoader.load(getClass().getResource(LOGIN_SCENE));
        PRIMARY_STAGE.setScene(new Scene(root, WINDOWWIDTH, WINDOWHEIGHT));

        //Show LoginScene
        PRIMARY_STAGE.show();


    }

    public void runOpenAccount(ActionEvent actionEvent) throws IOException {

        //init NewUserScene

        //Show NewUserScene

    }
}