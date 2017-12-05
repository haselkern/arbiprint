package com.haselkern.java.arbiprint;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PrefWindowController {

    @FXML
    private TextField printerCommand;
    @FXML
    private TextField printerHost;

    private Stage primaryStage;

    public PrefWindowController(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
    public void reset(){
        Prefs.revert();
        primaryStage.close();
    }

    @FXML
    public void cancel(){
        primaryStage.close();
    }

}
