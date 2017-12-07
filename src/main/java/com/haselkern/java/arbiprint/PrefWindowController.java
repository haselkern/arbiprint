package com.haselkern.java.arbiprint;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The controller for handling interactions in the {@link PrefWindow}
 */
public class PrefWindowController {

    @FXML
    private TextField printerCommand;
    @FXML
    private TextField printerHost;

    private Stage primaryStage;
    private IMainCallback callback;

    public PrefWindowController(Stage primaryStage, IMainCallback callback) {
        this.primaryStage = primaryStage;
        this.callback = callback;
    }

    @FXML
    public void initialize(){
        printerCommand.setText(Prefs.getPrintCommand());
        printerHost.setText(Prefs.getHost());
    }

    @FXML
    public void save(){
        Prefs.setPrintCommand(printerCommand.getText());
        Prefs.setHost(printerHost.getText());
        primaryStage.close();
    }

    @FXML
    public void resetLocal(){
        Prefs.revert();
        callback.clearFields();
        primaryStage.close();
    }

    @FXML
    public void resetServer(){
        callback.resetServer();
        primaryStage.close();
    }

    @FXML
    public void cancel(){
        primaryStage.close();
    }

}
